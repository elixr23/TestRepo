package com.springboot.trainingPOC.service;

import com.springboot.trainingPOC.advice.EmptyOutputException;
import com.springboot.trainingPOC.advice.NoFileFoundException;
import com.springboot.trainingPOC.model.FileDto;
import com.springboot.trainingPOC.model.MetaFile;
import com.springboot.trainingPOC.repository.FileManagerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.nio.file.NoSuchFileException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class FileManagerServiceImplTest {

    @Autowired
    @InjectMocks
    private FileManagerServiceImpl fileManagerService;

    @MockBean
    private FileManagerRepository fileManagerRepository;

    MetaFile file;
    FileDto fileDto;
    List<MetaFile> files;
    @BeforeEach
    public void setUp() {

        file = new MetaFile();
        file.setFileName("4d4fa23e-9b88-4eac-8118-0f3c316f0603_test.txt");
        file.setUserName("Sanjeet");
        file.setId(UUID.fromString("4d4fa23e-9b88-4eac-8118-0f3c316f0603"));
        file.setUploadTime(new Date());
        fileDto = new FileDto(UUID.fromString("f17cc291-01f5-4962-aa68-1cfa3208bffd"), "f17cc291-01f5-4962-aa68-1cfa3208bffd_test.txt", "Sanjeet", new Date(), "My Test POC");

        when(fileManagerRepository.findById(any())).thenReturn(Optional.ofNullable(file));
        files = new ArrayList<>();
        files.add(file);
        when(fileManagerRepository.findByUserName("Sanjeet")).thenReturn(files);
    }

    @Test
    public void testGetFileDetailsById_NoFilesPresent() throws Exception{
        Exception exception = assertThrows(NoSuchFileException.class, () -> {
            fileManagerService.getFileDetailsById(UUID.fromString("4d4fa23e-9b88-4eac-8118-0f3c316f0603"));
        });
        String expectedMessage = "There is no file available in the given directory for the id " + "4d4fa23e-9b88-4eac-8118-0f3c316f0603";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        //fileManagerService.getFileDetailsById(UUID.fromString("f17cc291-01f5-4962-aa68-1cfa3208bffd"));
        //verify(fileManagerRepository, times(1)).findById(UUID.fromString("f17cc291-01f5-4962-aa68-1cfa3208bffd"));
        //assertThat(fileManagerService.getFileDetailsById(file.getId())).isEqualTo(fileDto);
    }

    @Test
    public void testGetFileDetailsById_InvalidUUID() throws Exception{
        Exception exception = assertThrows(RuntimeException.class, () -> {
            fileManagerService.getFileDetailsById(UUID.fromString("23498"));
        });

        String expectedMessage = "Invalid UUID string: 23498";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testGetFileDetailsByUserName() throws Exception{
        fileManagerService.getFileDetailsByUserName("Sanjeet");
        verify(fileManagerRepository, times(1)).findByUserName("Sanjeet");
        assertThat(fileManagerService.getFileDetailsByUserName(file.getUserName()).get(0).getFileName().equals("test.txt"));
        assertThat(fileManagerService.getFileDetailsByUserName(file.getUserName()).get(0).getUserName().equals("Sanjeet"));
        assertThat(fileManagerService.getFileDetailsByUserName(file.getUserName()).get(0).getId().equals("b0be1942-d29f-4e68-a0e6-916c883d10b9"));
    }

    @Test
    public void testGetFileDetailsByUserName_NoFilesPresent() throws Exception{
        Exception exception = assertThrows(NoFileFoundException.class, () -> {
            fileManagerService.getFileDetailsByUserName("XYZ");
        });
        String expectedMessage = "No File found with the given userName : XYZ. Please provide correct userName.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

    }
}
