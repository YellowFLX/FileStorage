package com.example.FileStorage.repository;

import com.example.FileStorage.entity.FileEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepo extends CrudRepository<FileEntity, Long> {
    FileEntity findByFileName(String fileName);

    FileEntity findFileById(Long id);

}
