package com.veckos.VECKOS_Backend.exceptions;

public class ConflictException extends RuntimeException{

    public ConflictException(String detail){
        super(detail);
    }
}
