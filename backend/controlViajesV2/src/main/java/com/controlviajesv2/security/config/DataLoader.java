package com.controlviajesv2.security.config;

import com.controlviajesv2.controller.AuthController;
import com.controlviajesv2.dto.EmpresaDTO;
import com.controlviajesv2.dto.UsuarioDTO;
import com.controlviajesv2.dto.ViajeDTO;
import com.controlviajesv2.entity.Empresa;
import com.controlviajesv2.entity.Usuario;
import com.controlviajesv2.entity.Viaje;
import com.controlviajesv2.exception.ResourceNotFoundException;
import com.controlviajesv2.mapper.EmpresaMapper;
import com.controlviajesv2.mapper.ViajeMapper;
import com.controlviajesv2.repository.EmpresaRepository;
import com.controlviajesv2.repository.UsuarioRepository;
import com.controlviajesv2.repository.ViajeRepository;
import com.controlviajesv2.serviceImpl.EmpresaServiceImpl;
import com.controlviajesv2.serviceImpl.UsuarioServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

/**
 * clase para cargar datos de prueba la primera vez que se crean laas tablas en la bbdd
 */
@Component
public class DataLoader implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final UsuarioRepository usuarioRepository;
    private final UsuarioServiceImpl usuarioServiceImpl;
    private final PasswordEncoder passwordEncoder;
    private final EmpresaRepository empresaRepository;
    private final EmpresaServiceImpl empresaServiceImpl;
    private final ViajeRepository viajeRepository;

    public DataLoader(UsuarioRepository usuarioRepository, UsuarioServiceImpl usuarioServiceImpl, PasswordEncoder passwordEncoder, EmpresaRepository empresaRepository, EmpresaServiceImpl empresaServiceImpl, ViajeRepository viajeRepository) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioServiceImpl = usuarioServiceImpl;
        this.passwordEncoder = passwordEncoder;
        this.empresaRepository = empresaRepository;
        this.empresaServiceImpl = empresaServiceImpl;
        this.viajeRepository = viajeRepository;
    }

    //revisar por ver como creo yo el registro de los nuevos usuaruiso
    @Override
    public void run(String... args) {
        if (usuarioRepository.count() == 0) { // Solo si está vacío

            for (int i = 0; i < 12; i++) {
                // Crear nuevo usuario con password cifrada
                Usuario nuevoUsuario = new Usuario();
                nuevoUsuario.setNombre(Nombre());
                // nuevoUsuario.setEmail(request.getNombre() + "@tudominio.com"); // Si quieres, puedes pedir email en otro DTO
                String email = nuevoUsuario.getNombre().replace(" ", "");
                nuevoUsuario.setEmail(email + "@gmail.com");
                String contrasena =Contrasena();
                nuevoUsuario.setPassword(passwordEncoder.encode(contrasena));

                // Asignar rol por defecto
                Set<String> roles = new HashSet<>();
                //roles.add("USER");
                roles.add(ROl());
                nuevoUsuario.setRoles(roles);

                // Verificar si ya existe usuario con ese nombre
                if (usuarioRepository.findByNombre(nuevoUsuario.getNombre()).isPresent()) {
                    i = i - 1;
                } else {
                    guardarEnTxt(nuevoUsuario.getNombre(), contrasena);
                    usuarioRepository.save(nuevoUsuario);
                }


                logger.info("Intento de registro para el usuario: {}", nuevoUsuario.getNombre());
            }
            crearEmpresas();
            crearViajes();

        }
    }

    private void guardarEnTxt(String nombre, String contrasena) {
        String rutaArchivo = "C:/Users\\Favila\\Desktop/rigo_viajes/usuarios.txt";// ruta del archivo
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo, true))) {
            bw.write("Nombre: " + nombre + ", Contraseña: " + contrasena);
            bw.newLine(); // salto de línea
            System.out.println("Usuario guardado en archivo txt correctamente.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void crearViajes() {


        for (int i = 1; i < 40; i++) {
            Random random = new Random();
            int numero = random.nextInt(10); // genera de 0 a 9
            if (numero == 0) {
                numero = 12;
            }
            System.out.println(numero);
            long numeroLong = numero;

            EmpresaDTO empresaDTO = empresaServiceImpl.obtenerEmpresaPorId(numeroLong);


            ViajeDTO nuevoViaje = new ViajeDTO();


            nuevoViaje.setFecha(fechaViaje());
            nuevoViaje.setPrecioKm(precioKM());
            nuevoViaje.setDestino(destino());
            nuevoViaje.setDistancia(distancia());

            nuevoViaje.setEmpresaId(empresaDTO.getId());


            Optional<Empresa> optionalEmpresa = empresaRepository.findById(nuevoViaje.getEmpresaId());
            if (optionalEmpresa.isPresent()) {
                Empresa empresa = optionalEmpresa.get();
                Viaje viaje = ViajeMapper.toEntity(nuevoViaje, empresa);


                viajeRepository.save(viaje);


            } else {

            }

        }


    }

    private Double distancia() {
        List<Double> distancias = new ArrayList<>();
        int cantidadDistancias = 50;
        Random random = new Random();

        // Generar lista de 50 distancias aleatorias entre 50.0 km y 500.0 km
        for (int i = 0; i < cantidadDistancias; i++) {
            double distancia = 50.0 + (500.0 - 50.0) * random.nextDouble(); // rango 50.0 - 500.0
            distancia = Math.round(distancia * 10.0) / 10.0; // redondear a 1 decimal
            distancias.add(distancia);
        }

        // Elegir una distancia aleatoria de la lista
        int indiceAleatorio = random.nextInt(distancias.size());
        return distancias.get(indiceAleatorio);

    }

    private String destino() {
        List<String> destinos = new ArrayList<>();

        // Lista de 50 destinos de ejemplo
        destinos.add("Madrid");
        destinos.add("Barcelona");
        destinos.add("Valencia");
        destinos.add("Sevilla");
        destinos.add("Bilbao");
        destinos.add("Granada");
        destinos.add("Málaga");
        destinos.add("Córdoba");
        destinos.add("Zaragoza");
        destinos.add("Toledo");
        destinos.add("Salamanca");
        destinos.add("Valladolid");
        destinos.add("Alicante");
        destinos.add("San Sebastián");
        destinos.add("Gijón");
        destinos.add("Oviedo");
        destinos.add("Pamplona");
        destinos.add("Santander");
        destinos.add("Murcia");
        destinos.add("Almería");
        destinos.add("Burgos");
        destinos.add("Vigo");
        destinos.add("Lugo");
        destinos.add("Ourense");
        destinos.add("Huelva");
        destinos.add("Jaén");
        destinos.add("León");
        destinos.add("Badajoz");
        destinos.add("Cáceres");
        destinos.add("Logroño");
        destinos.add("Gerona");
        destinos.add("Tarragona");
        destinos.add("Lérida");
        destinos.add("Palma de Mallorca");
        destinos.add("Ibiza");
        destinos.add("Menorca");
        destinos.add("La Coruña");
        destinos.add("Santiago de Compostela");
        destinos.add("Santander");
        destinos.add("Ciudad Real");
        destinos.add("Segovia");
        destinos.add("Ávila");
        destinos.add("Huesca");
        destinos.add("Teruel");
        destinos.add("Castellón");
        destinos.add("Albacete");
        destinos.add("Badajoz");
        destinos.add("Melilla");

        // Elegir un destino aleatorio
        Random random = new Random();
        int indiceAleatorio = random.nextInt(destinos.size());
        return destinos.get(indiceAleatorio);
    }

    private Double precioKM() {
        List<Double> precios = new ArrayList<>();
        int cantidadPrecios = 50;
        Random random = new Random();

        // Generar lista de 50 precios aleatorios entre 0.5 y 3.0
        for (int i = 0; i < cantidadPrecios; i++) {
            double precio = 0.5 + (100.0 - 10.5) * random.nextDouble(); // rango 10.5 - 100.0
            precio = Math.round(precio * 100.0) / 100.0; // redondear a 2 decimales
            precios.add(precio);
        }

        // Elegir un precio aleatorio de la lista
        int indiceAleatorio = random.nextInt(precios.size());
        return precios.get(indiceAleatorio);

    }

    private LocalDate fechaViaje() {

        List<LocalDate> fechas = new ArrayList<>();
        LocalDate fechaInicio = LocalDate.of(2018, 8, 1); // fecha inicial
        int cantidadFechas = 50;

        // Generar lista de 50 fechas consecutivas
        for (int i = 0; i < cantidadFechas; i++) {
            fechas.add(fechaInicio.plusDays(i));
        }

        // Elegir una fecha aleatoria
        Random random = new Random();
        int indiceAleatorio = random.nextInt(fechas.size());
        return fechas.get(indiceAleatorio);
    }

    public String Nombre() {


        List<String> nombres = Arrays.asList(
                "Juan Pérez", "Ana López", "Carlos García", "María Fernández", "Pedro Sánchez",
                "Lucía Torres", "Javier Ruiz", "Elena Gómez", "Miguel Martín", "Sara Romero",
                "David Ortega", "Laura Jiménez", "Francisco Castro", "Isabel Morales", "Alberto Navarro",
                "Patricia Herrera", "Raúl Domínguez", "Carmen Soto", "Antonio Iglesias", "Rosa Delgado",
                "Manuel Lozano", "Teresa Méndez", "Andrés Fuentes", "Claudia Núñez", "Felipe León",
                "Nuria Vázquez", "Oscar Molina", "Beatriz Campos", "Sergio Peña", "Marta Cabrera"
        );

        Random random = new Random();

        return nombres.get(random.nextInt(nombres.size()));
    }


    public String Contrasena() {


        List<String> contraseñas = Arrays.asList(
                "pass1", "pass2", "pass3", "pass4", "pass5",
                "pass6", "pass7", "pass8", "pass9", "pass10",
                "pass11", "pass12", "pass13", "pass14", "pass15",
                "pass16", "pass17", "pass18", "pass19", "pass20",
                "pass21", "pass22", "pass23", "pass24", "pass25",
                "pass26", "pass27", "pass28", "pass29", "pass30"
        );

        Random random = new Random();

        return contraseñas.get(random.nextInt(contraseñas.size()));
    }

    public String ROl() {
        List<String> roles = Arrays.asList(
                "ROLE_USER", "ROLE_ADMIN", "ROLE_PRUEBA", "ROLE_USER"
        );
        Random random = new Random();
        return roles.get(random.nextInt(roles.size()));
    }

    private void crearEmpresas() {
        for (int i = 1; i < 20; i++) {
            Random random = new Random();
            int numero = random.nextInt(10); // genera de 0 a 9
            if (numero == 0) {
                numero = 12;
            }
            System.out.println(numero);
            long numeroLong = numero;

            UsuarioDTO newUsuario = usuarioServiceImpl.obtenerPorId(numeroLong);


            EmpresaDTO nuevaEmpresa = new EmpresaDTO();

            //nuevaEmpresa.setUsuarioId(1L);
            nuevaEmpresa.setNombre(nombreEmpresa());
            nuevaEmpresa.setDireccion(obtenerDireccionAleatoria());
            nuevaEmpresa.setCif(obtenerCifAleatorio());
            nuevaEmpresa.setTelefono(obtenerTelefonoAleatorio());
            nuevaEmpresa.setEmail(nuevaEmpresa.getNombre() + "@gmail.com");
            nuevaEmpresa.setUsuarioId(newUsuario.getId());


            Optional<Usuario> optionalUsuario = usuarioRepository.findById(nuevaEmpresa.getUsuarioId());
            if (optionalUsuario.isPresent()) {
                Usuario usuario = optionalUsuario.get();
                Empresa empresa = EmpresaMapper.toEntity(nuevaEmpresa, usuario);

                // Verificar si ya existe empres o cif con ese nombre
                if (empresaRepository.existsByNombre(empresa.getNombre()) ||
                        empresaRepository.existsByCif(empresa.getCif())) {
                    i = i - 1;
                } else {
                    empresaRepository.save(empresa);
                }


            } else {
                // manejar el caso en que no existe
            }

        }
    }


    public static String obtenerTelefonoAleatorio() {
        List<String> telefonos = Arrays.asList(
                "910123456",
                "911234567",
                "912345678",
                "913456789",
                "914567890",
                "915678901",
                "916789012",
                "917890123",
                "918901234",
                "919012345"
        );

        Random random = new Random();
        return telefonos.get(random.nextInt(telefonos.size()));
    }

    public static String obtenerDireccionAleatoria() {
        List<String> direcciones = Arrays.asList(
                "Calle Mayor 12, Madrid",
                "Avenida de la Constitución 45, Sevilla",
                "Paseo de la Castellana 101, Madrid",
                "Calle Gran Vía 23, Barcelona",
                "Plaza de España 7, Valencia",
                "Calle de Alcalá 56, Madrid",
                "Avenida Diagonal 89, Barcelona",
                "Calle San Fernando 15, Málaga",
                "Camino Real 42, Zaragoza",
                "Rambla Catalunya 18, Barcelona"
        );

        Random random = new Random();
        return direcciones.get(random.nextInt(direcciones.size()));
    }

    public static String obtenerCifAleatorio() {
        List<String> cifs = Arrays.asList(
                "A12345678",
                "B87654321",
                "C11223344",
                "D44332211",
                "E56789012",
                "F21098765",
                "G99887766",
                "H66778899",
                "J33445566",
                "K55667788",
                "L12345678",
                "M87654321",
                "N11223344",
                "P44332211",
                "Q56789012",
                "R21098765",
                "S99887766",
                "T66778899",
                "V33445566",
                "W55667788",
                "X99881122",
                "Y22334455",
                "Z66770088",
                "A98765432",
                "B12349876",
                "C56781234",
                "D43218765",
                "E87651234",
                "F34567812",
                "G78901234"
        );

        Random random = new Random();
        return cifs.get(random.nextInt(cifs.size()));
    }


    private String nombreEmpresa() {

        List<String> empresas = Arrays.asList(
                "TechSolutions",
                "GlobalCorp",
                "Innovatek",
                "GreenEnergy",
                "FastLogistics",
                "BlueWave",
                "SmartBuild",
                "FutureSoft",
                "EcoWorld",
                "RedLine", "AlphaTech",
                "BetaSolutions",
                "GammaSoft",
                "DeltaLogistics",
                "EpsilonEnergy",
                "ZetaConsulting",
                "EtaIndustries",
                "ThetaMedia",
                "IotaFoods",
                "KappaFinance",
                "LambdaDesign",
                "MuNetworks",
                "NuRetail",
                "XiAutomotive",
                "OmicronHealth",
                "PiConstruction",
                "RhoSystems",
                "SigmaMarketing",
                "TauEnterprises",
                "UpsilonServices"
        );

        Random random = new Random();
        return empresas.get(random.nextInt(empresas.size()));
    }


}
