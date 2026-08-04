package com.prima.factory.security;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class ZES_DevelopmentPasswordHashTest
{
    private static final String ZES_DEVELOPMENT_HASH =
        "$2a$10$wyzaUYXo4NJTynqgVHn.su/gmZAqPF08oHlEeypKegf3CVr4iTPva";

    @Test
    void ZES_developmentHashMatchesDocumentedPassword()
    {
        assertTrue(new BCryptPasswordEncoder().matches("password", ZES_DEVELOPMENT_HASH));
    }
}
