package com.csv.upload.service;

import com.csv.upload.dto.CsvDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface CsvFileService {

    public String uploadCsv(CsvDto csvDto);

    public Resource downloadCsv(String code);

    public void deleteAll();

}
