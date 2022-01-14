package com.springboot.trainingPOC.advice;

import com.springboot.trainingPOC.model.MetaFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SuccessResponse {
    private String success;
    private String message;
    private Object files;
}
