package com.springboot.trainingPOC.service;

import com.springboot.trainingPOC.advice.EmptyInputException;
import com.springboot.trainingPOC.advice.EmptyOutputException;
import com.springboot.trainingPOC.advice.InvalidFileFormatException;
import com.springboot.trainingPOC.advice.NoFileFoundException;
import com.springboot.trainingPOC.model.FileDto;
import com.springboot.trainingPOC.model.MetaFile;
import com.springboot.trainingPOC.repository.FileManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class FileManagerServiceImpl implements FileManagerService{
    @Value("${file.upload.dir}")
    private String FILE_DIR;

    @Value("${file.invalid.format}")
    private String INVALID_FILE;

    @Value("${file.empty.userName}")
    private String EMPTY_USERNAME;

    @Autowired
    FileManagerRepository fileManagerRepository;

    @Override
    public MetaFile uploadFile(MultipartFile file, String userName) throws IOException {
        if(!file.getContentType().equals("text/plain")){
            throw new InvalidFileFormatException(INVALID_FILE);
        }
        if (userName.isEmpty()) {
            throw new EmptyInputException(EMPTY_USERNAME);
        }
        String fileName = file.getOriginalFilename();
        UUID id = UUID.randomUUID();
        MetaFile metaFile = new MetaFile();
        metaFile.setUserName(userName);
        metaFile.setId(id);
        metaFile.setFileName(id +"_"+ fileName);
        metaFile.setUploadTime(new Date());
        File myFile = new File(FILE_DIR+ id+"_"+fileName);
        myFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(myFile);
        fos.write(file.getBytes());
        fos.close();
        return fileManagerRepository.save(metaFile);
    }

   /* @Override
    public MetaFile saveMetaFile(String fileName, String userName) {
        MetaFile metaFile = new MetaFile();
        metaFile.setFileName(userName+"_"+fileName);
        metaFile.setUserName(userName);
        metaFile.setId(UUID.randomUUID());
        metaFile.setUploadTime(new Date());
        return fileManagerRepository.save(metaFile);
    }*/

    @Override
    public FileDto getFileDetailsById(UUID id) throws IOException {
        Optional<MetaFile> fileOptional = fileManagerRepository.findById(id);
        if (!fileOptional.isPresent()) {
            throw new EmptyOutputException("No File Found with the given ID : " + id + ". Please provide correct ID");
        }
        String fileName = fileOptional.get().getFileName();
        String userName = fileOptional.get().getUserName();
        Date uploadTime = fileOptional.get().getUploadTime();
        UUID fileId = fileOptional.get().getId();
        String fileContent = getFileContent(fileName, fileId);
        FileDto fileDto = new FileDto(fileId, fileName, userName, uploadTime, fileContent);
        return fileDto;
        }

    @Override
    public List<MetaFile> getFileDetailsByUserName(String userName) {
        List<MetaFile> files = fileManagerRepository.findByUserName(userName);
        if(files.size()<=0){
            throw new NoFileFoundException("No File found with the given userName : "+userName+ ". Please provide correct userName.");
        }
        return files;
    }

    private String getFileContent(String fileName, UUID id) throws IOException {
            Path path = Paths.get(FILE_DIR+fileName);
            if (Files.notExists(path)) {
                throw new NoSuchFileException("There is no file available in the given directory for the id "+ id);
            }
            BufferedReader reader = Files.newBufferedReader(path);
            String data = reader.readLine();
        return data;
    }
   /* @Override
    public List<MetaFile> findAll() {
        return fileManagerRepository.findAll();
    }*/
}
