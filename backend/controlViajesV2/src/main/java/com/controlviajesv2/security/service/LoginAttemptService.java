package com.controlviajesv2.security.service;

import com.controlviajesv2.entity.LoginAttempt;
import com.controlviajesv2.repository.LoginAttemptRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LoginAttemptService {
    private static final Logger logger = LoggerFactory.getLogger(LoginAttemptService.class);


    private static final int MAX_ATTEMPTS = 5; // Bloquear tras 5 intentos
    private static final int BLOCK_DURATION_HOURS = 24; // Duración del bloqueo


    @Autowired
    private LoginAttemptRepository loginAttemptRepository;

    /**
     * Se llama cuando el usuario falla el login (contraseña incorrecta).
     * Incrementa el contador en la Base de Datos.
     */
    public void loginFailed(String key) {
        Optional<LoginAttempt> attemptOpt = loginAttemptRepository.findByClave(key);
        LoginAttempt attempt;

        if (attemptOpt.isPresent()) {
            attempt = attemptOpt.get();
            // Si el registro es muy viejo (más de 24h), reseteamos el contador
            if (attempt.getUltimaModificacion().isBefore(LocalDateTime.now().minusHours(BLOCK_DURATION_HOURS))) {
                attempt.setIntentos(1);
            } else {
                attempt.setIntentos(attempt.getIntentos() + 1);
            }
            attempt.setUltimaModificacion(LocalDateTime.now());
        } else {
            // Primer fallo
            attempt = new LoginAttempt(key, 1, LocalDateTime.now());
        }

        loginAttemptRepository.save(attempt);
    }

    /**
     * Se llama cuando el usuario acierta la contraseña.
     * Borra el registro de fallos (lo perdona).
     */
    @Transactional
    public void loginSucceeded(String key) {
        loginAttemptRepository.deleteByClave(key);
    }

    /**
     * Verifica si la IP/Usuario está bloqueado actualmente.
     */
    public boolean isBlocked(String key) {
        Optional<LoginAttempt> attemptOpt = loginAttemptRepository.findByClave(key);

        if (attemptOpt.isPresent()) {
            LoginAttempt attempt = attemptOpt.get();
            // Está bloqueado SI: (Intentos >= MAX) Y (Aún no han pasado 24 horas)
            boolean intentosSuperados = attempt.getIntentos() >= MAX_ATTEMPTS;
            boolean bloqueoVigente = attempt.getUltimaModificacion().isAfter(LocalDateTime.now().minusHours(BLOCK_DURATION_HOURS));

            return intentosSuperados && bloqueoVigente;
        }

        return false;
    }

    /**
     * LIMPIEZA AUTOMÁTICA
     * Cada día a las 4 AM borra los registros antiguos para no llenar la BD de basura.
     */
    @Scheduled(cron = "0 0 4 * * ?")
    @Transactional
    public void cleanUpOldAttempts() {
        // Borramos todo lo que tenga más de 24 horas de antigüedad
        loginAttemptRepository.deleteByUltimaModificacionBefore(LocalDateTime.now().minusHours(BLOCK_DURATION_HOURS));
    }










    /**
     * esto se ha cambiado y ahora se hace por base de datos
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



    --**
     * Se llama cuando el usuario escribe mal la contraseña.
     * Suma +1 al contador.
     *
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

    --**
     * Se llama cuando el usuario acierta.
     * Resetea el contador a 0.
     *-
    public void loginSucceeded(String key) {
        attemptsCache.invalidate(key);
    }

    --**
     * Verifica si el usuario está bloqueado actualmente.
     *-
    public boolean isBlocked(String key) {
        try {
            return attemptsCache.get(key) >= MAX_ATTEMPT;
        } catch (ExecutionException e) {
            return false;
        }
    }
     */
}