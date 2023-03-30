package com.csv.upload.controller;

import com.csv.upload.dto.CsvDto;
import com.csv.upload.service.CsvFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/csv")
public class CsvController {

    @Autowired
    private CsvFileService csvFileService;

    @PostMapping
    public ResponseEntity<String> upload(@ModelAttribute CsvDto csvDto) {
        String status = csvFileService.uploadCsv(csvDto);
        return ResponseEntity.ok(status);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadCsv(@RequestParam("code") Optional<String> codeOptional) {
        String code = codeOptional.orElse("");
        Resource resource = csvFileService.downloadCsv(code);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=mydata.csv")
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(resource);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAll() {
        csvFileService.deleteAll();
        return ResponseEntity.ok("All csv data deleted");
    }

}
