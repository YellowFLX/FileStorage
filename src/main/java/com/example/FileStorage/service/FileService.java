package com.example.FileStorage.service;

import com.example.FileStorage.entity.FileEntity;
import com.example.FileStorage.exception.FileNameAlreadyExistException;
import com.example.FileStorage.model.File;
import com.example.FileStorage.repository.FileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class FileService {
    @Autowired
    private FileRepo fileRepo;

    public FileEntity getFileByFileName(String fileName){
        return fileRepo.findByFileName(fileName);
    }

    public FileEntity getFileById(Long id){
        return fileRepo.findFileById(id);
    }
    public File toModel(FileEntity file){
        return File.toModel(file);
    }

    public void uploadFile(MultipartFile multipartFile) throws FileNameAlreadyExistException, IOException {
        String fileName = multipartFile.getOriginalFilename();
        if(getFileByFileName(fileName) != null) {
            throw new FileNameAlreadyExistException("Файл "+ fileName +" уже существует.");
        }

        FileEntity file = new FileEntity();
        file.setFileName(fileName);
        file.setContent(multipartFile.getBytes());
        file.setFormat(fileName.substring(fileName.lastIndexOf('.') + 1));
        file.setSize(multipartFile.getSize());
        file.setUploadDate(new Date());

        fileRepo.save(file);
    }
    public String deleteFile(FileEntity file){
        String fileName = file.getFileName();
        fileRepo.deleteById(file.getId());
        return ("Файл "+ fileName +" удален");
    }
    public Iterable<File> getAll(){
        List<File> filesList = new ArrayList<>();
        for (FileEntity file:
                fileRepo.findAll() ) {
            filesList.add(File.toModel(file));
        }
        return filesList;
    }
}
