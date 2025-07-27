package com.veckos.VECKOS_Backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "usuarios_eliminados")
public class UsuarioEliminado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id",referencedColumnName = "id")
    private Usuario usuario;

    private LocalDate fechaEliminacion;

}
