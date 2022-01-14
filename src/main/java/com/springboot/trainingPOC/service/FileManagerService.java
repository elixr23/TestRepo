package com.springboot.trainingPOC.service;

import com.springboot.trainingPOC.model.FileDto;
import com.springboot.trainingPOC.model.MetaFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface FileManagerService {
    public MetaFile uploadFile(MultipartFile file, String userName) throws IOException;
    //public MetaFile saveMetaFile(String fileName, String userName) ;
    public FileDto getFileDetailsById(UUID id) throws IOException;
    List<MetaFile> getFileDetailsByUserName(String userName);
    //List<MetaFile> findAll();


}
