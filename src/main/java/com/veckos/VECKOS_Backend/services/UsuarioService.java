package com.veckos.VECKOS_Backend.services;

import com.veckos.VECKOS_Backend.dtos.usuario.UsuarioListItemDto;
import com.veckos.VECKOS_Backend.entities.*;
import com.veckos.VECKOS_Backend.enums.AccionEventoAuditoria;
import com.veckos.VECKOS_Backend.enums.EstadoUsuario;
import com.veckos.VECKOS_Backend.exceptions.NotFoundException;
import com.veckos.VECKOS_Backend.factories.EventoAuditoriaFactory;
import com.veckos.VECKOS_Backend.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private EventoAuditoriaService eventoAuditoriaService;
    @Autowired
    private AsistenciaUsuarioService asistenciaUsuarioService;
    @Autowired
    private ClaseService claseService;
    @Autowired
    private UsuarioEliminadoService usuarioEliminadoService;
    @Autowired
    private InscripcionService inscripcionService;


    @Transactional
    public List<UsuarioListItemDto> obtenerTodosLosUsuarios(){
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream()
                .filter(usuario -> !usuarioEliminadoService.existUsuarioEliminado(usuario))
                .map(UsuarioListItemDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Usuario findById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> findByDni(String dni) {
        return usuarioRepository.findByDni(dni);
    }

    @Transactional
    public Usuario save(Usuario usuario) {
        if (usuario.getId() == null) {
            usuario.setFechaAlta(LocalDateTime.now());
        }
        EventoAuditoria eventoAuditoria = EventoAuditoriaFactory.crearEvento(AccionEventoAuditoria.REGISTRAR_USUARIO.getDescripcion(),usuario.toString());
        this.eventoAuditoriaService.guardarEventoAuditoria(eventoAuditoria);
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario update(Long id, Usuario usuarioDetails) {
        Usuario usuario = findById(id);

        usuario.setNombre(usuarioDetails.getNombre());
        usuario.setApellido(usuarioDetails.getApellido());
        usuario.setFechaNacimiento(usuarioDetails.getFechaNacimiento());
        usuario.setTelefono(usuarioDetails.getTelefono());
        usuario.setCorreo(usuarioDetails.getCorreo());
        //usuario.setEstado(usuarioDetails.getEstado());
        usuario.setDni(usuarioDetails.getDni());
        usuario.setCuil(usuarioDetails.getCuil());
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void deleteById(Long id) {
        try{
            Usuario usuario = findById(id);
            if(usuario.obtenerInscripcionActiva() != null){
                inscripcionService.completarInscripcion(usuario.obtenerInscripcionActiva());
            }
            UsuarioEliminado usuarioEliminado = usuarioEliminadoService.eliminarUsuario(usuario);
        }catch (Exception ex){
            EventoAuditoria evento = EventoAuditoriaFactory.crearEvento(AccionEventoAuditoria.ELIMINAR_USUARIO.getDescripcion(),
                    "Ocurrio un error al eliminar el usuario con ID: " + id);
        }
    }

    /*@Transactional(readOnly = true)
    public List<Usuario> findByEstado(Usuario.EstadoUsuario estado) {
        return usuarioRepository.findByEstado(estado);
    }*/

    @Transactional(readOnly = true)
    public List<Usuario> buscarUsuariosActivos(){
        return usuarioRepository.findUsuariosActivos();
    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarUsuariosPendientes(){
        return usuarioRepository.findAll().stream()
                .filter(usuario -> Objects.isNull(usuario.obtenerInscripcionActiva()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarPorTermino(String termino) {
        return usuarioRepository.buscarPorTermino(termino);
    }

    @Transactional
    public Usuario ingresoPorDni(String dni) {
        LocalDate hoy = LocalDate.now();
        Usuario usuario = usuarioRepository.findByDni(dni).orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        if(usuarioEliminadoService.existUsuarioEliminado(usuario)){
            throw new NotFoundException("Usuario no encontrado");
        }
        Inscripcion inscripcionActiva = usuario.obtenerInscripcionActiva();
        if(inscripcionActiva != null){
            //TODO registrar asistencia del dia
            Optional<Asistencia> asistenciaOpt = asistenciaUsuarioService.findAllByUsuario(usuario)
                    .stream().filter(asistencias -> asistencias.getClase().getFecha().equals(hoy))
                    .findFirst();
            if(asistenciaOpt.isPresent()){
                Asistencia asistencia = asistenciaOpt.get();
                asistencia.setPresente(true);
                asistencia.setFechaRegistro(LocalDateTime.of(hoy, LocalTime.now()));
                asistenciaUsuarioService.guardarAsistencia(asistencia);
                EventoAuditoria eventoAuditoria = EventoAuditoriaFactory.crearEventoAuditoriaSystem(AccionEventoAuditoria.REGISTRO_ASISTENCIA.getDescripcion(), "Se registro la asistencia por ingreso del usuario " + usuario.getNombre() + " " + usuario.getApellido());
                eventoAuditoriaService.guardarEventoAuditoria(eventoAuditoria);
            }else{
                //TODO crear asistencia si tiene clase este dia
                List<DetalleInscripcion> detalles = inscripcionActiva.getDetalles();
                Optional<DetalleInscripcion> detalle = detalles.stream().filter(detalleF -> hoy.getDayOfWeek().equals(detalleF.getDiaSemana())).findFirst();
                if(detalle.isPresent()){
                    Optional<Clase> claseOptional = detalle.get().getTurno().getClases().stream().filter(claseF -> claseF.getFecha().equals(hoy)).findFirst();
                    if(claseOptional.isPresent()){
                        Clase clase = claseOptional.get();
                        Asistencia asistencia = new Asistencia();
                        asistencia.setUsuario(usuario);
                        asistencia.setPresente(true);
                        asistencia.setFechaRegistro(LocalDateTime.of(hoy,LocalTime.now()));

                        clase.addAsistencia(asistencia);
                        claseService.guardarClase(clase);
                        EventoAuditoria eventoAuditoria = EventoAuditoriaFactory.crearEventoAuditoriaSystem(AccionEventoAuditoria.REGISTRO_ASISTENCIA.getDescripcion(), "Se registro la asistencia por ingreso del usuario " + usuario.getNombre() + " " + usuario.getApellido());
                        eventoAuditoriaService.guardarEventoAuditoria(eventoAuditoria);
                    }
                }
            }
        }
        return usuario;
    }

    @Transactional(readOnly = true)
    public List<Usuario> findConInscripcionActiva() {
        return usuarioRepository.findConInscripcionActivaEnFecha(LocalDate.now());
    }

    @Transactional(readOnly = true)
    public List<Usuario> findConPagoProximoAVencer() {
        LocalDate hoy = LocalDate.now();
        LocalDate enUnaSemana = hoy.plusDays(7);
        return usuarioRepository.findConPagoProximoAVencer(hoy, enUnaSemana);
    }

    @Transactional(readOnly = true)
    public boolean existsByDni(String dni) {
        return usuarioRepository.existsByDni(dni);
    }

    public long cantidadUsuariosActivos(){
        List<Usuario> usuarios = this.usuarioRepository.findAll();
        return usuarios.stream()
                .filter(usuario -> determinarEstadoUsuario(usuario).equals(EstadoUsuario.ACTIVO )).count();
    }

    public EstadoUsuario determinarEstadoUsuario(Usuario usuario){
        if(usuario.getInscripciones().size() == 0){
            return EstadoUsuario.PENDIENTE;
        }
        boolean esActivo = usuario.getInscripciones().stream()
                .anyMatch(inscripcion -> inscripcion.getEstadoInscripcion()
                        .equals(Inscripcion.EstadoInscripcion.EN_CURSO) && inscripcion.getEstadoPago().equals(Inscripcion.EstadoPago.PAGA));

        return esActivo ? EstadoUsuario.ACTIVO : EstadoUsuario.INACTIVO;
    }
}
