package com.example.FileStorage.model;

import com.example.FileStorage.entity.FileEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Date;

public class File {
    private Long id;
    private String fileName;
    private String format;
    private String url;
    private Long size;
    private Date uploadDate;

    final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

    public static File toModel(FileEntity entity){
        File model = new File();
        model.setId(entity.getId());
        model.setFileName(entity.getFileName());
        model.setFormat(entity.getFormat());
        model.setUrl(model.baseUrl+"/files/download?id="+entity.getId());
        model.setSize(entity.getSize());
        model.setUploadDate(entity.getUploadDate());
        return model;
    }

    public File() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }
}
