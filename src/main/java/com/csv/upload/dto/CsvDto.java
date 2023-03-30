package com.csv.upload.dto;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CsvDto {

    @NotNull(message = "File must not be empty")
    MultipartFile file;

}
