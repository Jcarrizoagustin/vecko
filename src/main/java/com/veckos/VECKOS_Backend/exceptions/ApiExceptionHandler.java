package com.veckos.VECKOS_Backend.exceptions;

import com.veckos.VECKOS_Backend.entities.EventoAuditoria;
import com.veckos.VECKOS_Backend.factories.EventoAuditoriaFactory;
import com.veckos.VECKOS_Backend.services.EventoAuditoriaService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ApiExceptionHandler {

    @Autowired
    private EventoAuditoriaService eventoAuditoriaService;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            BadRequestException.class,
            MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class
    })
    @ResponseBody
    public ErrorMessage badRequest(HttpServletRequest request, Exception exception){
        return new ErrorMessage(HttpStatus.BAD_REQUEST.value(),exception.getMessage());
    }



    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
            NotFoundException.class

    })
    @ResponseBody
    public ErrorMessage notFound(HttpServletRequest request, Exception exception){
        return new ErrorMessage(HttpStatus.NOT_FOUND.value(),exception.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({
            ConflictException.class,
            DataIntegrityViolationException.class
    })
    @ResponseBody
    public ErrorMessage conflict(HttpServletRequest request, Exception exception){
        return new ErrorMessage(HttpStatus.CONFLICT.value(),exception.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({
            UnauthorizedException.class,
            AuthenticationException.class,
            BadCredentialsException.class
    })
    @ResponseBody
    public ErrorMessage unauthorized(HttpServletRequest request, Exception exception){
        return new ErrorMessage(HttpStatus.UNAUTHORIZED.value(),exception.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({
            ForbiddenException.class
    })
    @ResponseBody
    public ErrorMessage forbidden(HttpServletRequest request, Exception exception){
        return new ErrorMessage(HttpStatus.FORBIDDEN.value(),exception.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    @ResponseBody
    public ErrorMessage fatalErrorUnexpectedException(HttpServletRequest request,Exception exception){
        EventoAuditoria eventoAuditoria = EventoAuditoriaFactory.crearEventoAuditoriaSystem("Ocurrio un ERROR en el sistema", exception.getMessage());
        eventoAuditoriaService.guardarEventoAuditoria(eventoAuditoria);
        return new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),exception.getMessage());
    }
}
