package com.veckos.VECKOS_Backend.mappers;

import com.veckos.VECKOS_Backend.dtos.asistencia.AsistenciaInfoDto;
import com.veckos.VECKOS_Backend.entities.Asistencia;

import java.util.List;

public class AsistenciaMapper {
    private AsistenciaMapper(){}

    public static List<AsistenciaInfoDto> asistenciasListToAsistenciaInfoDto(List<Asistencia> asistencias){
        return asistencias.stream()
                .map(AsistenciaInfoDto::new).toList();
    }

    public static AsistenciaInfoDto asistenciaToAsistenciaInfoDto(Asistencia asistencia){
        return new AsistenciaInfoDto(asistencia);
    }
}
