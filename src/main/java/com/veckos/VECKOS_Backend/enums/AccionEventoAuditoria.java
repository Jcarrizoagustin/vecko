package com.veckos.VECKOS_Backend.enums;

public enum AccionEventoAuditoria {
    REGISTRAR_USUARIO("Registro nuevo usuario"),
    REGISTRAR_PAGO("Registro nuevo pago"),
    REGISTRAR_TURNO("Registro nuevo turno"),
    REGISTRAR_NUEVA_INSCRIPCION("Registro nueva inscripcion"),
    REGISTRAR_VENCIMIENTO_INSCRIPCION("Registro vencimiento inscripcion"),
    REGISTRAR_RENOVACION_INSCRIPCION("Registro renovacion automatica de inscripcion"),
    GENERAR_INFORME_FINANCIERO("Genero informe financiero");

    private AccionEventoAuditoria(String descripcion){
        this.descripcion = descripcion;
    }

    private String descripcion;

    public String getDescripcion(){
        return this.descripcion;
    }
}
