package com.example.FileStorage.controller;

import com.example.FileStorage.entity.FileEntity;
import com.example.FileStorage.exception.FileNameAlreadyExistException;
import com.example.FileStorage.model.File;
import com.example.FileStorage.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping
    public ResponseEntity getAllFiles() {
        try {
            Iterable<File> files = fileService.getAll();
            return ResponseEntity.ok(files);
        } catch (Exception e){
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

    @GetMapping("/upload")
    public ResponseEntity provideUploadInfo() {
        return ResponseEntity.ok("Вы можете загружать файл с использованием того же URL.");
    }
    @PostMapping("/upload")
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile multipartFile) {
        try {
            fileService.uploadFile(multipartFile);
            return ResponseEntity.ok("Файл "+multipartFile.getOriginalFilename()+" успешно загружен.");
        } catch (FileNameAlreadyExistException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.badRequest().body("Произошла ошибка2");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getFileById(@PathVariable("id") Long id) {
        try {
            FileEntity file = fileService.getFileById(id);
            return ResponseEntity.ok(fileService.toModel(file));
        } catch (Exception e){
            return ResponseEntity.badRequest().body("Такого файла не существует");
        }
    }

    @GetMapping("/{id}/download")
    public ResponseEntity downloadFile(@PathVariable("id") Long id, HttpServletResponse response) {
        try {
            FileEntity file = fileService.getFileById(id);

            response.setHeader("Content-Disposition", "inline; filename=\""+ file.getFileName() +"\"");
            response.setContentLength(file.getContent().length);

            FileCopyUtils.copy(file.getContent(), response.getOutputStream());

            return ResponseEntity.ok("Скачивание завершено");
        } catch (Exception e){
            return ResponseEntity.badRequest().body("Такого файла не существует");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteFile(@PathVariable("id") Long id) {
        try {
            FileEntity file = fileService.getFileById(id);
            String message = fileService.deleteFile(file);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Такого файла не существует");
        }
    }

}

