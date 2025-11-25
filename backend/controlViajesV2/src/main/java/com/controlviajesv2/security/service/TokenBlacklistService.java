package com.controlviajesv2.security.service;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlacklistService {

    // Usamos ConcurrentHashMap porque es seguro para hilos (varios usuarios a la vez)
    // Es como una lista de Strings, pero muy rápida para buscar
    private final Set<String> blacklist = ConcurrentHashMap.newKeySet();

    /**
     * Mete un token en la lista negra (normalmente al hacer Logout)
     */
    public void revokeToken(String token) {
        blacklist.add(token);
    }

    /**
     * Comprueba si un token está prohibido
     */
    public boolean isRevoked(String token) {
        return blacklist.contains(token);
    }
}