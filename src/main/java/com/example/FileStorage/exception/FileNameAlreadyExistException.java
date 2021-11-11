package com.example.FileStorage.exception;

public class FileNameAlreadyExistException extends Exception{
    public FileNameAlreadyExistException(String message){
        super(message);
    }
}
