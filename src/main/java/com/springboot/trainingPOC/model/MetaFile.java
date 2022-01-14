package com.springboot.trainingPOC.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "files")
public class MetaFile implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    private String fileName;
    private String userName;
    private Date uploadTime;
}
