package com.veckos.VECKOS_Backend.exceptions;

public class BadRequestException extends RuntimeException{

    public BadRequestException(String detail){
        super(detail);
    }
}
