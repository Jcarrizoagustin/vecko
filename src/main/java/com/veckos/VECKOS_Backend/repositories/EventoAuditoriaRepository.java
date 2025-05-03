package com.veckos.VECKOS_Backend.repositories;

import com.veckos.VECKOS_Backend.entities.EventoAuditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventoAuditoriaRepository extends JpaRepository<EventoAuditoria,Long> {

    List<EventoAuditoria> findAllByAccion(String accion);

    List<EventoAuditoria> findAllByOrderByIdDesc();
}
