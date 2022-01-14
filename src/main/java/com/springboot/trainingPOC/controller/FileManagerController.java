package com.springboot.trainingPOC.controller;
import com.springboot.trainingPOC.advice.Response;
import com.springboot.trainingPOC.advice.SuccessResponse;
import com.springboot.trainingPOC.model.FileDto;
import com.springboot.trainingPOC.model.MetaFile;
import com.springboot.trainingPOC.service.FileManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/file")
public class FileManagerController {

    @Autowired
    private FileManagerService fileManagerService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("File") MultipartFile file, @RequestParam("userName") String userName) {
        try {
            MetaFile metaFile = fileManagerService.uploadFile(file, userName);
            //MetaFile metaFile = fileManagerService.saveMetaFile(file.getOriginalFilename(), userName);
            SuccessResponse response = new SuccessResponse("True", "File uploaded successfully", metaFile);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(new Response("False", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFileDetailsById(@PathVariable("id") UUID id) throws IOException {
        FileDto file = fileManagerService.getFileDetailsById(id);
        SuccessResponse response = new SuccessResponse("True","File Details for the given ID", file);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/user/{userName}")
    public ResponseEntity<?> getFileDetailsByUserName(@PathVariable("userName") String userName){
        List<MetaFile> files = fileManagerService.getFileDetailsByUserName(userName);
        SuccessResponse response = new SuccessResponse("True","File Details for the given userName", files);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

}
