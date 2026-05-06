package com.example.order.config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Security integration tests.
 *
 * <p>These tests validate profile-specific behavior:
 *
 * <ul>
 *   <li>prod profile enforces authentication
 *   <li>test profile bypasses authentication
 * </ul>
 */
class SecurityConfigTest {

    @Nested
    @SpringBootTest
    @AutoConfigureMockMvc
    @ActiveProfiles("prod")
    class ProdSecurityTests {

        @Autowired
        private MockMvc mockMvc;

        /**
         * Anonymous users must be redirected to login.
         */
        @Test
        void shouldRedirectAnonymousUserToLogin() throws Exception {
            mockMvc.perform(get("/view/orders"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrlPattern("**/login"));
        }

        /**
         * Login page must remain public.
         */
        @Test
        void shouldAllowAccessToLoginPage() throws Exception {
            mockMvc.perform(get("/login"))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @SpringBootTest
    @AutoConfigureMockMvc
    @ActiveProfiles("test")
    class TestProfileSecurityTests {

        @Autowired
        private MockMvc mockMvc;

        /**
         * Test profile disables authentication barriers.
         */
        @Test
        void shouldAllowAnonymousAccessInTestProfile() throws Exception {
            mockMvc.perform(get("/view/orders"))
                    .andExpect(status().isOk());
        }
    }
}