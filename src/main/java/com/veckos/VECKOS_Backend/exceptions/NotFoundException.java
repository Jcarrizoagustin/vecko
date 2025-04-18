package com.veckos.VECKOS_Backend.exceptions;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String detail){
        super(detail);
    }
}
