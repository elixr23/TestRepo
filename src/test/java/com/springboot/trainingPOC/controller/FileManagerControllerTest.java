package com.springboot.trainingPOC.controller;

import com.springboot.trainingPOC.model.FileDto;
import com.springboot.trainingPOC.model.MetaFile;
import com.springboot.trainingPOC.service.FileManagerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
//@WebMvcTest(FileManagerController.class)
public class FileManagerControllerTest {

    @InjectMocks
    private FileManagerController fileManagerController;
    @MockBean
    private FileManagerServiceImpl fileManagerService;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;
    MetaFile file;
    FileDto fileDto;
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        file = new MetaFile();
        file.setFileName("test.txt");
        file.setUserName("Sanjeet");
        file.setId(UUID.fromString("b0be1942-d29f-4e68-a0e6-916c883d10b9"));
        file.setUploadTime(new Date());
        fileDto = new FileDto(UUID.fromString("b0be1942-d29f-4e68-a0e6-916c883d10b9"), "test.txt", "Sanjeet", new Date(), "My Test POC");
    }

    //@Disabled
    @Test
    public void testUploadFile() throws Exception {

        String fileName = "sample-file-mock.txt";
        MockMultipartFile sampleFile = new MockMultipartFile(
                "File", fileName, "text/plain", "This is the file content".getBytes());
        MockMultipartHttpServletRequestBuilder multipartRequest =
                MockMvcRequestBuilders.multipart("/file/upload");
        mockMvc.perform(multipartRequest.file(sampleFile))
                .andExpect(status().isOk());

    }
    @Test
    public void test_handleFileUpload_NoFileProvided() throws Exception{
        MockMultipartHttpServletRequestBuilder multipartRequest =
                MockMvcRequestBuilders.multipart("/file/upload");

        mockMvc.perform(multipartRequest)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetFileDetailsById() throws Exception {

        when(fileManagerService.getFileDetailsById(UUID.fromString("b0be1942-d29f-4e68-a0e6-916c883d10b9"))).thenReturn(fileDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/file/b0be1942-d29f-4e68-a0e6-916c883d10b9"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.files.fileName").value("test.txt"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.files.userName").value("Sanjeet"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.files.id").value("b0be1942-d29f-4e68-a0e6-916c883d10b9"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetFileDetailsByUserName() throws Exception {

        List<MetaFile> files = new ArrayList<>();
        files.add(file);
        when(fileManagerService.getFileDetailsByUserName(anyString())).thenReturn(files);

        mockMvc.perform(MockMvcRequestBuilders.get("/file/user/sanjeet"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.files[0].fileName").value("test.txt"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.files[0].userName").value("Sanjeet"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.files[0].id").value("b0be1942-d29f-4e68-a0e6-916c883d10b9"))
                .andExpect(status().isOk());

    }
}