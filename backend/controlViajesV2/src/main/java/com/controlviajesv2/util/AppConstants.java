package com.controlviajesv2.util;

public final class AppConstants {

    private AppConstants() {
        // Evita que se instancie
    }

    //Seguridad
    public static final String API_AUTH_PATH = "/api/auth/**";

    //AuthController
    public static final String REQUEST_AUTHCONTROLLER = "/api/auth";
    public static final String REQUEST_AUTHCONTROLLER_LOGIN =  "/login";
    public static final String REQUEST_AUTHCONTROLLER_REGISTER = "/register";

    //EmpresaController
    public static final String REQUEST_EMPRESACONTROLLER = "/api/empresas" ;
    public static final String REQUEST_EMPRESACONTROLLER_ID = "/{id}" ;
    public static final String REQUEST_EMPRESACONTROLLER_CONVIAJES = "/con-viajes" ;





    //UsuarioController
    public static final String REQUEST_USUARIOCONTROLLER = "/api/usuarios" ;
    public static final String REQUEST_USUARIOCONTROLLER_ID = "/{id}" ;
   // public static final String REQUEST_USUARIOCONTROLLER =  ;

    public static final String REQUEST_USUARIOCONTROLLER_empresas ="/empresas/{id}";


    //ViajeController
    public static final String REQUEST_VIAJECONTROLLER = "/api/viajes" ;
    public static final String REQUEST_VIAJECONTROLLER_ID ="/{id}"  ;
   // public static final String REQUEST_VIAJECONTROLLER =  ;

}
