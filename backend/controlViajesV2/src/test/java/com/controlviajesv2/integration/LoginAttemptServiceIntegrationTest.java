package com.controlviajesv2.integration;

import com.controlviajesv2.entity.LoginAttempt;
import com.controlviajesv2.repository.LoginAttemptRepository;
import com.controlviajesv2.security.service.LoginAttemptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class LoginAttemptServiceIntegrationTest {

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Autowired
    private LoginAttemptRepository loginAttemptRepository;

    @BeforeEach
    void cleanDb() {
        loginAttemptRepository.deleteAll();
    }

    @Test
    void loginFailed_bloqueaDespuesDeCincoIntentos() {
        String clave = "admin@test.com";

        for (int i = 0; i < 5; i++) {
            loginAttemptService.loginFailed(clave);
        }

        assertThat(loginAttemptService.isBlocked(clave)).isTrue();
        assertThat(loginAttemptRepository.findByClave(clave)).isPresent();
        assertThat(loginAttemptRepository.findByClave(clave).orElseThrow().getIntentos()).isEqualTo(5);
    }

    @Test
    void loginSucceeded_eliminaRegistroDeIntentos() {
        String clave = "admin@test.com";
        loginAttemptService.loginFailed(clave);

        loginAttemptService.loginSucceeded(clave);

        assertThat(loginAttemptRepository.findByClave(clave)).isEmpty();
        assertThat(loginAttemptService.isBlocked(clave)).isFalse();
    }

    @Test
    void cleanUpOldAttempts_borraRegistrosAntiguos() {
        LoginAttempt antiguo = new LoginAttempt("old-key", 6, LocalDateTime.now().minusHours(30));
        LoginAttempt reciente = new LoginAttempt("new-key", 1, LocalDateTime.now().minusHours(1));

        loginAttemptRepository.save(antiguo);
        loginAttemptRepository.save(reciente);

        loginAttemptService.cleanUpOldAttempts();

        assertThat(loginAttemptRepository.findByClave("old-key")).isEmpty();
        assertThat(loginAttemptRepository.findByClave("new-key")).isPresent();
    }
}
