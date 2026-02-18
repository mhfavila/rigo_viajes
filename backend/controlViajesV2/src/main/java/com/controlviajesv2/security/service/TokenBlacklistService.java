package com.controlviajesv2.security.service;


import com.controlviajesv2.entity.TokenRevocado;
import com.controlviajesv2.repository.TokenRevocadoRepository;
import com.controlviajesv2.security.JwtTokenParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class TokenBlacklistService {
    @Autowired
    private TokenRevocadoRepository tokenRevocadoRepository;
    @Autowired
    private JwtTokenParser jwtTokenParser; // Para leer la fecha de expiración del token

    // Guardamos el token en la BD con su fecha de muerte
    public void blacklistToken(String token) {
        // Extraemos la fecha de expiración real del token
        Date expiracionDate = jwtTokenParser.extractExpiration(token);

        // Convertimos Date a LocalDateTime
        LocalDateTime expiracion = expiracionDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        TokenRevocado revocado = new TokenRevocado(token, expiracion);
        tokenRevocadoRepository.save(revocado);
    }

    // Consultamos a la BD si el token está revocado
    public boolean isBlacklisted(String token) {
        return tokenRevocadoRepository.findByToken(token).isPresent();
    }

    // 🧹 EL LIMPIADOR: Se ejecuta cada hora para borrar lo viejo
    @Scheduled(cron = "0 0 * * * *") // Cada hora en punto
    @Transactional
    public void limpiarTokensExpirados() {
        tokenRevocadoRepository.deleteByFechaExpiracionBefore(LocalDateTime.now());
    }


    /**
     * SE MODIFICA PARA USAR BASE DE DATOS EN VEZ DE ConcurrentHashMap ya que el servidor se reinicia cada x tiempo

     -- Usamos ConcurrentHashMap porque es seguro para hilos (varios usuarios a la vez)
     -- Es como una lista de Strings, pero muy rápida para buscar
     private final Set<String> blacklist = ConcurrentHashMap.newKeySet();
     * Mete un token en la lista negra (normalmente al hacer Logout)

     public void revokeToken(String token) {
     blacklist.add(token);
     }
     * Comprueba si un token está prohibido

     public boolean isRevoked(String token) {
     return blacklist.contains(token);
     }

     */
}