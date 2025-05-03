package com.veckos.VECKOS_Backend.services;

import com.veckos.VECKOS_Backend.entities.EventoAuditoria;
import com.veckos.VECKOS_Backend.repositories.EventoAuditoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventoAuditoriaService {

    @Autowired
    private EventoAuditoriaRepository eventoAuditoriaRepository;

    public void guardarEventoAuditoria(EventoAuditoria eventoAuditoria){
        this.eventoAuditoriaRepository.save(eventoAuditoria);
    }

    public List<EventoAuditoria> obtenerTodosLosEventos(){
        return this.eventoAuditoriaRepository.findAllByOrderByIdDesc();
    }

    public List<EventoAuditoria> obtenerEventosPorTipoAccion(String tipo){
        return this.eventoAuditoriaRepository.findAllByAccion(tipo);
    }
}
