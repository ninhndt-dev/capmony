package com.capmoney.api.manager.bootstrap.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import com.capmoney.api.manager.bootstrap.repository.BootstrapStore;
import java.time.*;
import org.junit.jupiter.api.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

class BootstrapIssuerTest {
    @Test
    void disabledIssuerDoesNotTouchDatabaseOrMail() {
        BootstrapStore store = mock(BootstrapStore.class);
        JavaMailSender mail = mock(JavaMailSender.class);
        BootstrapIssuer issuer = new BootstrapIssuer(store, mail, Clock.systemUTC(), false,
                "manager@example.test", "http://127.0.0.1:28080");
        assertEquals("disabled", issuer.issue().code());
        verifyNoInteractions(store, mail);
    }
}
