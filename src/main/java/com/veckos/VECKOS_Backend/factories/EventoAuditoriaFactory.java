package com.veckos.VECKOS_Backend.factories;

import com.veckos.VECKOS_Backend.entities.EventoAuditoria;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

public class EventoAuditoriaFactory {

    public static EventoAuditoria crearEvento(String accion, String nuevoValor){
        EventoAuditoria eventoAuditoria = new EventoAuditoria();
        eventoAuditoria.setAccion(accion);
        eventoAuditoria.setUsuario(SecurityContextHolder.getContext().getAuthentication().getName());
        eventoAuditoria.setFecha(LocalDateTime.now());
        eventoAuditoria.setNuevoValor(nuevoValor);
        return eventoAuditoria;
    }

    public static EventoAuditoria crearEventoAuditoriaSystem(String accion, String nuevoValor){
        EventoAuditoria eventoAuditoria = new EventoAuditoria();
        eventoAuditoria.setAccion(accion);
        eventoAuditoria.setUsuario("Sistema");
        eventoAuditoria.setFecha(LocalDateTime.now());
        eventoAuditoria.setNuevoValor(nuevoValor);
        return eventoAuditoria;
    }
}
