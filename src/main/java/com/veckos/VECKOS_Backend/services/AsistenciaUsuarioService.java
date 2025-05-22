package com.veckos.VECKOS_Backend.services;

import com.veckos.VECKOS_Backend.entities.Asistencia;
import com.veckos.VECKOS_Backend.entities.Usuario;
import com.veckos.VECKOS_Backend.repositories.AsistenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AsistenciaUsuarioService {

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    public List<Asistencia> findAllByUsuario(Usuario usuario){
        return asistenciaRepository.findAllByUsuario(usuario);
    }

    public void guardarAsistencia(Asistencia asistencia){
        asistenciaRepository.save(asistencia);
    }
}
