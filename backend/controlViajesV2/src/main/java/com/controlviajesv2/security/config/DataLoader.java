package com.controlviajesv2.security.config;

import com.controlviajesv2.controller.AuthController;
import com.controlviajesv2.dto.EmpresaDTO;
import com.controlviajesv2.dto.UsuarioDTO;
import com.controlviajesv2.dto.ViajeDTO;
import com.controlviajesv2.entity.*;
import com.controlviajesv2.exception.ResourceNotFoundException;
import com.controlviajesv2.mapper.EmpresaMapper;
import com.controlviajesv2.mapper.ViajeMapper;
import com.controlviajesv2.repository.*;
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
import java.math.BigDecimal;
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
    private final FacturaRepository facturaRepository;
    private final ServicioRepository servicioRepository;

    public DataLoader(UsuarioRepository usuarioRepository, UsuarioServiceImpl usuarioServiceImpl, PasswordEncoder passwordEncoder, EmpresaRepository empresaRepository, EmpresaServiceImpl empresaServiceImpl, ViajeRepository viajeRepository, FacturaRepository facturaRepository, ServicioRepository servicioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioServiceImpl = usuarioServiceImpl;
        this.passwordEncoder = passwordEncoder;
        this.empresaRepository = empresaRepository;
        this.empresaServiceImpl = empresaServiceImpl;
        this.viajeRepository = viajeRepository;
        this.facturaRepository = facturaRepository;
        this.servicioRepository = servicioRepository;
    }

    //revisar por ver como creo yo el registro de los nuevos usuaruiso
    @Override
    public void run(String... args) {
        if (usuarioRepository.count() == 0) { // Solo si está vacío
            Map<String, String> usuarios = new HashMap<>();
            for (int i = 0; i < 12; i++) {

                // Crear nuevo usuario con password cifrada
                Usuario nuevoUsuario = new Usuario();
                nuevoUsuario.setNombre(Nombre());
                // nuevoUsuario.setEmail(request.getNombre() + "@tudominio.com"); // Si quieres, puedes pedir email en otro DTO
                String email = nuevoUsuario.getNombre().replace(" ", "");
                nuevoUsuario.setEmail(email + "@gmail.com");
                String contrasena = Contrasena();
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
                    usuarios.put(nuevoUsuario.getNombre(), contrasena);
                    //guardarEnTxt(nuevoUsuario);
                    usuarioRepository.save(nuevoUsuario);
                }




                logger.info("Intento de registro para el usuario: {}", nuevoUsuario.getNombre());
            }
            guardarEnTxt(usuarios);
            crearEmpresas();
            crearViajes();
            cargarFacturasYServicios();

        }
    }

    private void guardarEnTxt(Map<String, String> usuarios) {
        String rutaArchivo = "C:/Users\\Favila\\Desktop/rigo_viajes/usuarios.txt";// ruta del archivo
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {//si quiero que mantenga lo que habia antes puedo a;adir true-->rutaArchivo,true


            // 🔹 Recorres cada par (clave, valor)
            for (Map.Entry<String, String> entry : usuarios.entrySet()) {
                String nombre = entry.getKey();
                String contrasena = entry.getValue();
                bw.write("Nombre: " + nombre + ", Contraseña: " + contrasena);
                bw.newLine(); // salto de línea
            }


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
            nuevoViaje.setOrigen("Valladolid");
            nuevoViaje.setDestino(destino());
            nuevoViaje.setDistancia(distancia());
            //nuevoViaje.setOrigen();

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

    private BigDecimal distancia() {
        List<BigDecimal> distancias = new ArrayList<>();
        int cantidadDistancias = 50;
        Random random = new Random();

        // Generar lista de 50 distancias aleatorias entre 50.0 km y 500.0 km
        for (int i = 0; i < cantidadDistancias; i++) {
            BigDecimal distancia = BigDecimal.valueOf(50.0 + (500.0 - 50.0) * random.nextDouble()); // rango 50.0 - 500.0
            // distancia = Math.round(distancia.multiply(10.0)  ) / 10.0; // redondear a 1 decimal
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

    private BigDecimal precioKM() {
        List<BigDecimal> precios = new ArrayList<>();
        int cantidadPrecios = 50;
        Random random = new Random();

        // Generar lista de 50 precios aleatorios entre 0.5 y 3.0
        for (int i = 0; i < cantidadPrecios; i++) {
            double precio = 0.5 + (100.0 - 10.5) * random.nextDouble(); // rango 10.5 - 100.0
            precio = Math.round(precio * 100.0) / 100.0; // redondear a 2 decimales
            precios.add(BigDecimal.valueOf(precio));
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



//datos de prueba de servicios y facturas

    private void cargarFacturasYServicios() {
        List<Empresa> empresas = empresaRepository.findAll();
        List<Usuario> usuarios = usuarioRepository.findAll();

        if (empresas.isEmpty() || usuarios.isEmpty()) {
            System.out.println(" No hay empresas o usuarios en la base de datos. No se pueden crear facturas.");
            return;
        }

        Random random = new Random();

        for (int i = 1; i <= 10; i++) { // crear 5 facturas de ejemplo
            Empresa empresa = empresas.get(random.nextInt(empresas.size()));
            Usuario usuario = usuarios.get(random.nextInt(usuarios.size()));

            Factura factura = Factura.builder()
                    .numeroFactura("F-" + String.format("%04d", i))
                    .fechaEmision(LocalDate.now().minusDays(random.nextInt(120)))
                    .empresa(empresa)
                    .usuario(usuario)
                    .totalBruto(BigDecimal.ZERO) // se calculará más abajo
                    .porcentajeIva(new BigDecimal("21.00"))
                    .porcentajeIrpf(new BigDecimal("15.00"))
                    .cuentaBancaria("ES7620770024003102575766")
                    .formaPago("Transferencia")
                    .estado("BORRADOR")
                    .observaciones("Factura generada automáticamente para pruebas.")
                    .build();

            List<Servicio> servicios = new ArrayList<>();
            BigDecimal totalBruto = BigDecimal.ZERO;
            int aleatorioFact = random.nextInt(7) + 1;
            for (int j = 1; j <= aleatorioFact; j++) { // número aleatorio de servicios entre uno y siete por factura
                BigDecimal precioKm = new BigDecimal("0.60");
                int km = 50 + random.nextInt(1000); // 50 a 1000 km
                BigDecimal importeServicio = precioKm.multiply(BigDecimal.valueOf(km));

                Servicio servicio = Servicio.builder()
                        .factura(factura)
                        .tipoServicio("Transporte")
                        .fechaServicio(factura.getFechaEmision().minusDays(random.nextInt(10)))
                        .origen(destino())
                        .destino(destino())
                        .km(km)
                        .precioKm(precioKm)
                        .importeServicio(importeServicio)
                        .clienteFinal("Cliente " + i)
                        .observaciones("Servicio de prueba número " + j)
                        .build();

                servicios.add(servicio);
                totalBruto = totalBruto.add(importeServicio);
            }

            factura.setServicios(servicios);

            BigDecimal importeIva = totalBruto.multiply(factura.getPorcentajeIva().divide(new BigDecimal("100")));
            BigDecimal importeIrpf = totalBruto.multiply(factura.getPorcentajeIrpf().divide(new BigDecimal("100")));
            BigDecimal totalFactura = totalBruto.add(importeIva).subtract(importeIrpf);

            factura.setTotalBruto(totalBruto);
            factura.setImporteIva(importeIva);
            factura.setImporteIrpf(importeIrpf);
            factura.setTotalFactura(totalFactura);

            facturaRepository.save(factura);
        }

        System.out.println("✅ Facturas y servicios de prueba creados correctamente.");
    }

}




