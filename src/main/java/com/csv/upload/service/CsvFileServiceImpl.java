package com.csv.upload.service;

import com.csv.upload.dto.CsvDto;
import com.csv.upload.model.CsvFile;
import com.csv.upload.repository.CsvFileRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import com.opencsv.CSVWriter;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
public class CsvFileServiceImpl implements CsvFileService {

    @Autowired
    private CsvFileRepository csvFileRepository;

    private static final String[] expectedHeaders = {"source", "codeListCode", "code", "displayValue", "longDescription", "fromDate", "toDate", "sortingPriority"};

    public String uploadCsv(CsvDto csvDto) {
        try {
            Reader filReader = new InputStreamReader(csvDto.getFile().getInputStream());
            List<CsvFile> csvFiles = new ArrayList<>();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            try (CSVReader reader = new CSVReader(filReader)) {
                String[] headers = reader.readNext();
                if(!validateHeaders(headers, expectedHeaders)) {
                    return "Invalid Headers";
                }
                String[] line;
                while ((line = reader.readNext()) != null) {
                    CsvFile csvModel = new CsvFile();
                    csvModel.setSource(line[0]);
                    csvModel.setCodeListCode(line[1]);
                    csvModel.setCode(line[2]);
                    csvModel.setDisplayValue(line[3]);
                    csvModel.setLongDescription(line[4]);
                    if(line[5] != null && !line[5].isEmpty()) {
                        csvModel.setFromDate(dateFormat.parse(line[5]));
                    }
                    if(line[6] != null && !line[6].isEmpty()) {
                        csvModel.setToDate(dateFormat.parse(line[6]));
                    }
                    if(line[7] != null && !line[7].isEmpty()) {
                        csvModel.setSortingPriority(Integer.parseInt(line[7]));
                    }
                    csvFiles.add(csvModel);
                }
                csvFileRepository.saveAll(csvFiles);
                return "Uploaded";
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CsvValidationException e) {
                throw new RuntimeException(e);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed";
        }
        return "Failed";
    }

    private boolean validateHeaders(String[] headers, String[] expectedHeaders) {
        if(headers.length != expectedHeaders.length) {
            return false;
        }
        for(int i = 0; i < headers.length; i++) {
            if(!headers[i].equals(expectedHeaders[i])) {
                return false;
            }
        }
        return true;
    }

    public Resource downloadCsv(String code) {
        List<CsvFile> csvFiles =new ArrayList<>();
        if(code != null && !code.isEmpty()) {
            Optional<CsvFile> csvFileOptional = csvFileRepository.findByCode(code);
            if(csvFileOptional.isPresent()) {
                csvFiles.add(csvFileOptional.get());
            }
        } else {
            csvFiles = csvFileRepository.findAll();
        }
        StringWriter stringWriter = new StringWriter();
        CSVWriter writer = new CSVWriter(stringWriter);
        writer.writeNext(expectedHeaders);
        for(CsvFile csvFile : csvFiles) {
            String[] line = new String[expectedHeaders.length];
            line[0] = csvFile.getSource();
            line[1] = csvFile.getCodeListCode();
            line[2] = csvFile.getCode();
            line[3] = csvFile.getDisplayValue();
            line[4] = csvFile.getLongDescription();
            if(csvFile.getFromDate() != null) {
                line[5] = new SimpleDateFormat("dd-MM-yyyy").format(csvFile.getFromDate());
            }
            if(csvFile.getToDate() != null) {
                line[6] = new SimpleDateFormat("dd-MM-yyyy").format(csvFile.getToDate());
            }
            if(csvFile.getSortingPriority() != null) {
                line[7] = csvFile.getSortingPriority().toString();
            }
            writer.writeNext(line);
        }
        return new InputStreamResource(new ByteArrayInputStream(stringWriter.toString().getBytes(StandardCharsets.UTF_8)));
    }

    public void deleteAll() {
        csvFileRepository.deleteAll();
    }

}
