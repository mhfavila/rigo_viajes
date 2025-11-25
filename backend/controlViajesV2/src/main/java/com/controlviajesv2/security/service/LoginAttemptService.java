package com.controlviajesv2.security.service;

import com.controlviajesv2.controller.AuthController;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {
    private static final Logger logger = LoggerFactory.getLogger(LoginAttemptService.class);
    private final int MAX_ATTEMPT = 5; // Máximo de intentos
    private final LoadingCache<String, Integer> attemptsCache;

    public LoginAttemptService() {
        super();
        // Configura la caché para que borre los datos pasados 15 minutos
        attemptsCache = CacheBuilder.newBuilder()
                .expireAfterWrite(15, TimeUnit.MINUTES)
                .build(new CacheLoader<String, Integer>() {
                    public Integer load(String key) {
                        return 0;
                    }
                });
    }

    /**
     * Se llama cuando el usuario escribe mal la contraseña.
     * Suma +1 al contador.
     */
    public void loginFailed(String key) {
        logger.info("Fallo contrasena");
        int attempts = 0;
        try {
            attempts = attemptsCache.get(key);
        } catch (ExecutionException e) {
            attempts = 0;
        }
        attempts++;
        attemptsCache.put(key, attempts);
    }

    /**
     * Se llama cuando el usuario acierta.
     * Resetea el contador a 0.
     */
    public void loginSucceeded(String key) {
        attemptsCache.invalidate(key);
    }

    /**
     * Verifica si el usuario está bloqueado actualmente.
     */
    public boolean isBlocked(String key) {
        try {
            return attemptsCache.get(key) >= MAX_ATTEMPT;
        } catch (ExecutionException e) {
            return false;
        }
    }
}