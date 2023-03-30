package com.csv.upload.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "csvfile")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CsvFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "source")
    private String source;

    @Column(name = "code_list_code")
    private String codeListCode;

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "display_value")
    private String displayValue;

    @Column(name = "long_description")
    private String longDescription;

    @Column(name = "from_date")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date fromDate;

    @Column(name = "to_date")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date toDate;

    @Column(name = "sorting_priority")
    private Integer sortingPriority;

}
