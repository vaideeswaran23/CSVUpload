package com.csv.upload.repository;

import com.csv.upload.model.CsvFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CsvFileRepository  extends JpaRepository<CsvFile, Long> {

    Optional<CsvFile> findByCode(String code);

}
