package com.veckos.VECKOS_Backend.dtos.asistencia;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsistenciaPorClaseRegistrarDto {

    @NotNull(message = "La lista de IDs de usuarios presentes es obligatoria")
    private List<Long> presentes;
    @NotNull(message = "La lista de IDs de usuarios ausentes es obligatoria")
    private List<Long> ausentes;
}
