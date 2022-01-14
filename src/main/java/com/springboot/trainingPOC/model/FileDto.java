package com.springboot.trainingPOC.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileDto implements Serializable {
        private static final long serialVersionUID = 1L;
        private UUID id;
        private String fileName;
        private String userName;
        private Date uploadTime;
        private String content;
}
