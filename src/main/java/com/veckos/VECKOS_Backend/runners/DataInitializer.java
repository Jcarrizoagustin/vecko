package com.veckos.VECKOS_Backend.runners;

import com.veckos.VECKOS_Backend.entities.*;
import com.veckos.VECKOS_Backend.enums.DescripcionTurno;
import com.veckos.VECKOS_Backend.repositories.CuentaRepository;
import com.veckos.VECKOS_Backend.repositories.PlanRepository;
import com.veckos.VECKOS_Backend.repositories.TurnoRepository;
import com.veckos.VECKOS_Backend.repositories.UsuarioRepository;
import com.veckos.VECKOS_Backend.security.repositories.RolRepository;
import com.veckos.VECKOS_Backend.security.repositories.UsuarioSistemaRepository;
import com.veckos.VECKOS_Backend.services.InscripcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioSistemaRepository usuarioSistemaRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private TurnoRepository turnoRepository;

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private InscripcionService inscripcionService;

    @Override
    public void run(String... args) throws Exception {
        try{
            // Crear roles si no existen
            inicializarRoles();

            // Crear usuario por defecto si no existe
            crearUsuarioAdmin();

            crearUsuarioProf();

            iniciarlizarZonaHoraria();

            //ajustarFechaFin();

            //crearUsuario();

            //crearPlan();

           //crearTurnos();

           //crearCuenta();

            inscripcionService.completarInscripciones();
        }catch (Exception ex){
            System.out.print(ex.getMessage());
        }
    }

    private void ajustarFechaFin() {
        inscripcionService.ajustarFechaInscripciones();
    }

    public void inicializarRoles() {
        // Comprobar si hay roles de forma más directa
        List<Rol> roles = rolRepository.findAll();
        if (roles.isEmpty()) {
            try {
                // Crear rol de administrador
                Rol rolAdmin = new Rol();
                rolAdmin.setNombre(Rol.RolNombre.ROLE_ADMIN);
                rolRepository.save(rolAdmin);

                // Crear rol de operador
                Rol rolOperador = new Rol();
                rolOperador.setNombre(Rol.RolNombre.ROLE_OPERADOR);
                rolRepository.save(rolOperador);

                System.out.println("Roles inicializados correctamente.");
            } catch (Exception e) {
                System.err.println("Error al inicializar roles: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Los roles ya existen en la base de datos.");
        }
    }

    public void crearUsuarioAdmin() {
        // Verificar si ya existe el usuario admin de forma más directa
        if (!usuarioSistemaRepository.existsByUsername("jmartinez")) {
            try {
                // Crear usuario administrador
                UsuarioSistema admin = new UsuarioSistema();
                admin.setNombre("Javier");
                admin.setApellido("Martinez");
                admin.setUsername("jmartinez");
                admin.setEmail("jmartinez@veckos-gym.com");
                admin.setPassword(passwordEncoder.encode("Veckos2025!"));
                admin.setActivo(true);

                // Asignar rol de administrador
                Set<Rol> roles = new HashSet<>();
                rolRepository.findByNombre(Rol.RolNombre.ROLE_ADMIN)
                        .ifPresent(roles::add);
                admin.setRoles(roles);

                // Guardar usuario
                usuarioSistemaRepository.save(admin);

                System.out.println("Usuario administrador " + admin.getUsername() +  " creado correctamente.");
            } catch (Exception e) {
                System.err.println("Error al crear usuario administrador: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("El usuario administrador ya existe en la base de datos.");
        }
    }

    public void crearUsuarioProf() {
        // Verificar si ya existe el usuario admin de forma más directa
        if (!usuarioSistemaRepository.existsByUsername("profeveckos")) {
            try {
                // Crear usuario administrador
                UsuarioSistema admin = new UsuarioSistema();
                admin.setNombre("Profesor");
                admin.setApellido("Veckos");
                admin.setUsername("profeveckos");
                admin.setEmail("profeveckos@veckos-gym.com");
                admin.setPassword(passwordEncoder.encode("Pr1657on!"));
                admin.setActivo(true);

                // Asignar rol de administrador
                Set<Rol> roles = new HashSet<>();
                rolRepository.findByNombre(Rol.RolNombre.ROLE_OPERADOR)
                        .ifPresent(roles::add);
                admin.setRoles(roles);

                // Guardar usuario
                usuarioSistemaRepository.save(admin);

                System.out.println("Usuario administrador " + admin.getUsername() +  " creado correctamente.");
            } catch (Exception e) {
                System.err.println("Error al crear usuario administrador: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("El usuario administrador ya existe en la base de datos.");
        }
    }

    public void crearUsuario(){
        List<Usuario> usuariosList = usuarioRepository.findAll();
        if(usuariosList.size() == 0){
            try{
                Usuario usuario1 = crearUsuario("Luis","Rodriguez","lrodriguez@tests.com","5678901234","56789012","20567890120",LocalDate.of(1981,Month.JUNE,10));
                Usuario usuario2 = crearUsuario("Sofia","Martinez","smartinez@tests.com","9012345678","12345678","20123456780",LocalDate.of(1990,Month.APRIL,15));
                Usuario usuario3 = crearUsuario("Pedro","Gonzalez","pgonzalez@tests.com","3456789012","98765432","2098765432",LocalDate.of(1975,Month.AUGUST,22));
                Usuario usuario4 = crearUsuario("Carla","Perez","cperez@tests.com","7890123456","87654321","2087654321",LocalDate.of(2002,Month.JANUARY,5));
                Usuario usuario5 = crearUsuario("Roberto","Sanchez","rsanchez@tests.com","2345678901","23456789","20234567890",LocalDate.of(1988,Month.MARCH,18));
                Usuario usuario6 = crearUsuario("Maria","Lopez","mlopez@tests.com","6789012345","34567890","2034567890",LocalDate.of(1992,Month.NOVEMBER,27));
                Usuario usuario7 = crearUsuario("Juan","Fernandez","jfernandez@tests.com","0123456789","45678901","2045678901",LocalDate.of(1972,Month.JULY,8));
                Usuario usuario8 = crearUsuario("Ana","Garcia","agarcia@tests.com","4567890123","56789013","2056789013",LocalDate.of(2006,Month.MAY,20));
                /*Usuario usuario9 = crearUsuario("Javier","Torres","jtorres@tests.com","8901234567","67890123","2067890123",LocalDate.of(1985,Month.OCTOBER,14));
                Usuario usuario10 = crearUsuario("Laura","Diaz","ldiaz@tests.com","1234567890","78901234","2078901234",LocalDate.of(1995,Month.DECEMBER,3));
                Usuario usuario11 = crearUsuario("Carlos","Romero","cromero@tests.com","5432109876","89012345","2089012345",LocalDate.of(1980,Month.FEBRUARY,25));
                Usuario usuario12 = crearUsuario("Patricia","Vasquez","pvasquez@tests.com","9876543210","90123456","2090123456",LocalDate.of(2007,Month.SEPTEMBER,11));
                Usuario usuario13 = crearUsuario("Miguel","Castro","mcastro@tests.com","3210987654","01234567","2001234567",LocalDate.of(1977,Month.APRIL,7));
                Usuario usuario14 = crearUsuario("Valeria","Ramirez","vramirez@tests.com","7654321098","12345866","20123456784",LocalDate.of(1998,Month.JUNE,19));
                Usuario usuario15 = crearUsuario("Fernando","Suarez","fsuarez@tests.com","2109876543","23456780","20234567800",LocalDate.of(1983,Month.AUGUST,28));
                Usuario usuario16 = crearUsuario("Lucia","Ortega","lortega@tests.com","6543210987","34567891","2034567891",LocalDate.of(2004,Month.JANUARY,12));
                Usuario usuario17 = crearUsuario("Antonio","Jimenez","ajimenez@tests.com","0987654321","45678909","2045678990",LocalDate.of(1970,Month.MARCH,23));
                Usuario usuario18 = crearUsuario("Mariana","Silva","msilva@tests.com","4321098765","56789014","2056789014",LocalDate.of(1989,Month.MAY,6));
                Usuario usuario19 = crearUsuario("Diego","Vargas","dvargas@tests.com","8765432109","67890124","2067890124",LocalDate.of(1993,Month.NOVEMBER,15));
                Usuario usuario20 = crearUsuario("Gabriela","Mendoza","gmendoza@tests.com","1098765432","78901235","2078901235",LocalDate.of(1974,Month.JULY,21));
                Usuario usuario21 = crearUsuario("Juan","Garcia","jgarcia@tests.com","5432167890","81234567","2081234567",LocalDate.of(1986,Month.OCTOBER,9));
                Usuario usuario22 = crearUsuario("Sofia","Perez","sperez@tests.com","9876512340","92345678","2092345678",LocalDate.of(2001,Month.DECEMBER,4));
                Usuario usuario23 = crearUsuario("Pedro","Torres","ptorres@tests.com","3219876540","03456789","2003456789",LocalDate.of(1979,Month.FEBRUARY,17));
                Usuario usuario24 = crearUsuario("Maria","Sanchez","msanchez@tests.com","7651234890","14567890","2014567890",LocalDate.of(1997,Month.APRIL,26));
                Usuario usuario25 = crearUsuario("Carlos","Fernandez","cfernandez@tests.com","2167893450","25678901","2025678901",LocalDate.of(1984,Month.JUNE,2));
                Usuario usuario26 = crearUsuario("Ana","Rodriguez","arodriguez@tests.com","6547891230","36789012","2036789012",LocalDate.of(2005,Month.SEPTEMBER,16));
                Usuario usuario27 = crearUsuario("Roberto","Lopez","rlopez@tests.com","0981234567","47890123","2047890123",LocalDate.of(1971,Month.NOVEMBER,30));
                Usuario usuario28 = crearUsuario("Laura","Gonzalez","lgonzalez@tests.com","4329876510","58901234","2058901234",LocalDate.of(1994,Month.JANUARY,13));
                Usuario usuario29 = crearUsuario("Javier","Martinez","jmartinez@tests.com","8761234590","69012345","2069012345",LocalDate.of(1976,Month.MARCH,27));
                Usuario usuario30 = crearUsuario("Carla","Diaz","cdiaz@tests.com","1987654320","70123456","2070123456",LocalDate.of(2003,Month.AUGUST,8));
                Usuario usuario31 = crearUsuario("Gimena","Carrizo","gcarrizo@test.com","3834242424","12345672","20123456720",LocalDate.of(1998,Month.APRIL, 2));*/
                this.usuarioRepository.save(usuario1);
                this.usuarioRepository.save(usuario2);
                this.usuarioRepository.save(usuario3);
                this.usuarioRepository.save(usuario4);
                this.usuarioRepository.save(usuario5);
                this.usuarioRepository.save(usuario6);
                this.usuarioRepository.save(usuario7);
                this.usuarioRepository.save(usuario8);
                /*this.usuarioRepository.save(usuario9);
                this.usuarioRepository.save(usuario10);
                this.usuarioRepository.save(usuario11);
                this.usuarioRepository.save(usuario12);
                this.usuarioRepository.save(usuario13);
                this.usuarioRepository.save(usuario14);
                this.usuarioRepository.save(usuario15);
                this.usuarioRepository.save(usuario16);
                this.usuarioRepository.save(usuario17);
                this.usuarioRepository.save(usuario18);
                this.usuarioRepository.save(usuario19);
                this.usuarioRepository.save(usuario20);
                this.usuarioRepository.save(usuario21);
                this.usuarioRepository.save(usuario21);
                this.usuarioRepository.save(usuario23);
                this.usuarioRepository.save(usuario23);
                this.usuarioRepository.save(usuario25);
                this.usuarioRepository.save(usuario26);
                this.usuarioRepository.save(usuario27);
                this.usuarioRepository.save(usuario28);
                this.usuarioRepository.save(usuario29);
                this.usuarioRepository.save(usuario30);
                this.usuarioRepository.save(usuario31);*/
                System.err.println("Usuarios creado correctamente");
            }catch(Exception ex){
                System.err.println("Error al crear usuario: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    public void crearPlan(){
        if(this.planRepository.findAll().size() == 0){
            try{
                Plan plan = new Plan();
                plan.setDescripcion("Descripcion plan 1");
                plan.setPrecio(BigDecimal.valueOf(29000));
                plan.setNombre("Wellness");
                Plan plan2 = new Plan();
                plan2.setDescripcion("Descripcion plan 2");
                plan2.setPrecio(BigDecimal.valueOf(34000));
                plan2.setNombre("Fitness");
                this.planRepository.save(plan);
                this.planRepository.save(plan2);
                System.out.println("Plan " + plan.getNombre() +  " creado correctamente.");
                System.out.println("Plan " + plan2.getNombre() +  " creado correctamente.");
            }catch (Exception ex){
                System.err.println("Error al crear plan: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private void crearCuenta(){
        if(cuentaRepository.findAll().size() == 0){
            Cuenta cuenta = new Cuenta();
            cuenta.setCbu("056282822885884");
            cuenta.setDescripcion("Naranja X");
            this.cuentaRepository.save(cuenta);
            System.out.println("Cuenta: " + cuenta.getDescripcion() + " creada correctamente");
            Cuenta cuenta2 = new Cuenta();
            cuenta2.setCbu("05628287588282");
            cuenta2.setDescripcion("Santander");
            this.cuentaRepository.save(cuenta2);
            System.out.println("Cuenta: " + cuenta2.getDescripcion() + " creada correctamente");

        }
    }

    private void iniciarlizarZonaHoraria(){
        TimeZone.setDefault(TimeZone.getTimeZone("America/Argentina/Buenos_Aires"));
    }

    public void crearTurnos(){
        if(this.turnoRepository.findAll().size() == 0){
            try{
                List<Turno> turnosList1 = generarTurnos(LocalTime.of(01,01), DescripcionTurno.AUXILIAR.name());
                List<Turno> turnosList2 = generarTurnos(LocalTime.of(10,00),"sistema");
                List<Turno> turnosList3 = generarTurnos(LocalTime.of(20,00),"sistema");
                List<Turno> turnosList4 = generarTurnos(LocalTime.of(21,00),"sistema");

                this.turnoRepository.saveAll(turnosList1);
                this.turnoRepository.saveAll(turnosList2);
                this.turnoRepository.saveAll(turnosList3);
                this.turnoRepository.saveAll(turnosList4);

                System.out.println("Turnos creados correctamente.");
            }catch (Exception ex){
                System.err.println("Error al crear turnos auxiliares: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private Usuario crearUsuario(String nombre, String apellido, String correo, String telefono, String dni, String cuil, LocalDate fechaNacimiento){
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setCorreo(correo);
        usuario.setTelefono(telefono);
        usuario.setDni(dni);
        usuario.setCuil(cuil);
        usuario.setFechaNacimiento(fechaNacimiento);
        usuario.setFechaAlta(LocalDateTime.now());
        return usuario;
    }

    private List<Turno> generarTurnos(LocalTime hora, String descripcion){
        DayOfWeek[] diasSemana = {DayOfWeek.MONDAY,DayOfWeek.TUESDAY,DayOfWeek.WEDNESDAY,DayOfWeek.THURSDAY,DayOfWeek.FRIDAY};
        List<Turno> turnosList = new ArrayList<>();
        for(DayOfWeek day : diasSemana){
            Turno turno = new Turno();
            turno.setHora(hora);
            turno.setDiaSemana(day);
            turno.setDescripcion(descripcion);
            turnosList.add(turno);
        }
        return turnosList;
    }
}
