package com.veckos.VECKOS_Backend.exceptions;

public class ForbiddenException extends RuntimeException{

    public ForbiddenException(String detail){
        super(detail);
    }
}
