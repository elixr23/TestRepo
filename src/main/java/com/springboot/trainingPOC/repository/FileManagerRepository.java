package com.springboot.trainingPOC.repository;

import com.springboot.trainingPOC.model.MetaFile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FileManagerRepository extends MongoRepository<MetaFile, UUID> {
    List<MetaFile> findByUserName(String userName);
}
