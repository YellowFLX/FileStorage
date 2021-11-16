package com.example.FileStorage.controller;

import com.example.FileStorage.entity.FileEntity;
import com.example.FileStorage.exception.FileNameAlreadyExistException;
import com.example.FileStorage.model.File;
import com.example.FileStorage.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
    @PostMapping("/")
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

    @GetMapping("/download/{id}")
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

    @GetMapping("/downloadzip")
    public ResponseEntity downfileZip(@RequestParam ("id") List<Long> id, HttpServletResponse response) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(baos);
        for (Long Id : id) {
            FileEntity file = fileService.getFileById(Id);
            ZipEntry zipEntry = new ZipEntry(file.getFileName());
            zipEntry.setSize(file.getContent().length);
            zipOut.putNextEntry(zipEntry);
            StreamUtils.copy(file.getContent(), zipOut);
            zipOut.closeEntry();
        }
        zipOut.finish();
        zipOut.close();

        response.setHeader("Content-Disposition", "inline; filename=\""+ "zipFile.zip" +"\"");
        response.setContentLength(baos.toByteArray().length);

        FileCopyUtils.copy(baos.toByteArray(), response.getOutputStream());

        try {
            return ResponseEntity.ok("Скачивание завершено");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка");
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

