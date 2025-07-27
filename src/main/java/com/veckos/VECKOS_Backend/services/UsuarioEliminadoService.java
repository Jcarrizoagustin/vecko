package com.veckos.VECKOS_Backend.services;

import com.veckos.VECKOS_Backend.entities.Usuario;
import com.veckos.VECKOS_Backend.entities.UsuarioEliminado;
import com.veckos.VECKOS_Backend.repositories.UsuarioEliminadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UsuarioEliminadoService {

    @Autowired
    private UsuarioEliminadoRepository usuarioEliminadoRepository;

    public boolean existUsuarioEliminado(Usuario usuario){
        return usuarioEliminadoRepository.existsByUsuario(usuario);
    }

    @Transactional
    public UsuarioEliminado eliminarUsuario(Usuario usuario){
        UsuarioEliminado usuarioEliminado = new UsuarioEliminado();
        usuarioEliminado.setUsuario(usuario);
        usuarioEliminado.setFechaEliminacion(LocalDate.now());
        return usuarioEliminadoRepository.save(usuarioEliminado);
    }

    @Transactional
    public boolean eliminarUsuarioEliminado(Usuario usuario){
        Optional<UsuarioEliminado> usuarioEliminadoOpt = usuarioEliminadoRepository.getByUsuario(usuario);
        if(usuarioEliminadoOpt.isPresent()){
            UsuarioEliminado usuarioEliminado = usuarioEliminadoOpt.get();
            usuarioEliminado.setUsuario(null);
            usuarioEliminadoRepository.save(usuarioEliminado);
            return true;
        }
        return false;
    }
}
