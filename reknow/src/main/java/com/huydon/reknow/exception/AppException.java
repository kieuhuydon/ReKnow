package com.huydon.reknow.exception;

import lombok.Data;

@Data
public class AppException extends RuntimeException{
    private int statusCode;

    public AppException(int statusCode, String message){
        super(message);
        this.statusCode  = statusCode;
    }

}
