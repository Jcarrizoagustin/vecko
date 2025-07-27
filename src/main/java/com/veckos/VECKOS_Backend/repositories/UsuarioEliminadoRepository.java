package com.veckos.VECKOS_Backend.repositories;

import com.veckos.VECKOS_Backend.entities.Usuario;
import com.veckos.VECKOS_Backend.entities.UsuarioEliminado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioEliminadoRepository extends JpaRepository<UsuarioEliminado,Long> {

    boolean existsByUsuario(Usuario usuario);

    Optional<UsuarioEliminado> getByUsuario(Usuario usuario);
}
