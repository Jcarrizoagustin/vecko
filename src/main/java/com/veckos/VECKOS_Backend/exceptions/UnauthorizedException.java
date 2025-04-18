package com.veckos.VECKOS_Backend.exceptions;

public class UnauthorizedException extends RuntimeException{

    public UnauthorizedException(String detail){
        super(detail);
    }
}
