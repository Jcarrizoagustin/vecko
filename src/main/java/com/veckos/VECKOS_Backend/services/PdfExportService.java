package com.veckos.VECKOS_Backend.services;

import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.veckos.VECKOS_Backend.dtos.asistencia.AsistenciaInfoDto;
import com.veckos.VECKOS_Backend.dtos.reporte.ReporteAsistenciaGeneralDto;
import com.veckos.VECKOS_Backend.dtos.reporte.ReporteAsistenciaIndividualDto;
import org.springframework.stereotype.Service;
import com.lowagie.text.*;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class PdfExportService {

    public byte[] generarReporteAsistenciaIndividual(ReporteAsistenciaIndividualDto dto) throws Exception {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();

        // Título
        Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
        Paragraph title = new Paragraph("Reporte de Asistencia Individual - Veckos Centro de entrenamientos", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        // Datos generales
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        document.add(new Paragraph("Nombre completo: " + dto.getNombreCompleto()));
        document.add(new Paragraph("Dni: " + dto.getDni()));
        document.add(new Paragraph("Fecha inicio: " + formatter.format(dto.getFechaInicio())));
        document.add(new Paragraph("Fecha fin: " + formatter.format(dto.getFechaFin())));
        document.add(new Paragraph("Total clases programadas: " + dto.getTotalClasesProgramadas()));
        document.add(new Paragraph("Total asistencias: " + dto.getTotalAsistencias()));
        document.add(new Paragraph("Porcentaje de asistencia: " + String.format("%.1f", dto.getPorcentajeAsistencia())  + "%"));
        document.add(Chunk.NEWLINE);

        // Tabla de asistencias (si querés mostrar una lista)
        if (dto.getAsistencias() != null && !dto.getAsistencias().isEmpty()) {
            PdfPTable table = new PdfPTable(2); // Ajustá según los campos de AsistenciaInfoDto
            table.setWidthPercentage(100);
            table.addCell("Fecha");
            table.addCell("Presente");

            for (AsistenciaInfoDto asistencia : dto.getAsistencias()) {
                table.addCell(asistencia.getFechaClase().format(formatter));
                table.addCell(asistencia.getPresente() ? "Sí" : "No");
            }

            document.add(table);
        }

        document.close();
        return out.toByteArray();
    }

    public byte[] generarReporteAsistenciaGeneral(ReporteAsistenciaGeneralDto dto) throws Exception {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();

        Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
        Font tableHeaderFont = new Font(Font.HELVETICA, 12, Font.BOLD);
        Font cellFont = new Font(Font.HELVETICA, 11);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Paragraph title = new Paragraph("Reporte General de Asistencias", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        document.add(new Paragraph("Periodo: " + formatter.format(dto.getFechaInicio()) + " - " + formatter.format(dto.getFechaFin())));
        document.add(new Paragraph("Total usuarios: " + dto.getTotalUsuarios()));
        document.add(new Paragraph("Total clases programadas: " + dto.getTotalClasesProgramadas()));
        document.add(new Paragraph("Total asistencias: " + dto.getTotalAsistencias()));
        document.add(Chunk.NEWLINE);

        // Tabla de ranking
        if (dto.getRankingUsuarios() != null && !dto.getRankingUsuarios().isEmpty()) {
            Paragraph subTitle = new Paragraph("Ranking de Usuarios", tableHeaderFont);
            document.add(subTitle);
            document.add(Chunk.NEWLINE);

            PdfPTable rankingTable = new PdfPTable(3);
            rankingTable.setWidthPercentage(100);
            rankingTable.addCell(new Phrase("Posicion", tableHeaderFont));
            rankingTable.addCell(new Phrase("Usuario", tableHeaderFont));
            rankingTable.addCell(new Phrase("Asistencias", tableHeaderFont));
            int index = 1;
            for (Object[] fila : dto.getRankingUsuarios()) {
                String posicion = String.valueOf(index);
                String nombre = String.valueOf(fila[1]);
                String asistencias = String.valueOf(fila[2]);
                rankingTable.addCell(new Phrase(posicion, cellFont));
                rankingTable.addCell(new Phrase(nombre, cellFont));
                rankingTable.addCell(new Phrase(asistencias, cellFont));
                index++;
            }
            document.add(rankingTable);
            document.add(Chunk.NEWLINE);
        }

        // Tabla de asistencias por día
        if (dto.getAsistenciasPorDia() != null && !dto.getAsistenciasPorDia().isEmpty()) {
            Paragraph subTitle = new Paragraph("Asistencias por Día", tableHeaderFont);
            document.add(subTitle);
            document.add(Chunk.NEWLINE);

            PdfPTable asistenciaTable = new PdfPTable(2);
            asistenciaTable.setWidthPercentage(100);
            asistenciaTable.addCell(new Phrase("Fecha", tableHeaderFont));
            asistenciaTable.addCell(new Phrase("Cantidad de Asistencias", tableHeaderFont));

            dto.getAsistenciasPorDia().entrySet().stream()
                    .sorted(Map.Entry.comparingByKey()) // opcional: orden por fecha
                    .forEach(entry -> {
                        asistenciaTable.addCell(new Phrase(entry.getKey().format(formatter), cellFont));
                        asistenciaTable.addCell(new Phrase(String.valueOf(entry.getValue()), cellFont));
                    });

            document.add(asistenciaTable);
        }

        document.close();
        return out.toByteArray();
    }
}
