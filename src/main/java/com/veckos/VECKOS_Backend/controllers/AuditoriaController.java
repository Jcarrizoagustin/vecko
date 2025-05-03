package com.veckos.VECKOS_Backend.controllers;

import com.veckos.VECKOS_Backend.entities.EventoAuditoria;
import com.veckos.VECKOS_Backend.services.EventoAuditoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
public class AuditoriaController {

    @Autowired
    private EventoAuditoriaService eventoAuditoriaService;

    @GetMapping
    public ResponseEntity<List<EventoAuditoria>> getAll(){
        return ResponseEntity.ok(eventoAuditoriaService.obtenerTodosLosEventos());
    }

    @GetMapping("/tipo")
    public ResponseEntity<List<EventoAuditoria>> getAllByTipoEvento(@RequestParam String tipo){
        return ResponseEntity.ok(eventoAuditoriaService.obtenerEventosPorTipoAccion(tipo));
    }
}
