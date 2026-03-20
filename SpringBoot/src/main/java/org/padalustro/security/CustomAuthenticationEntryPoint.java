package org.padalustro.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("text/plain;charset=UTF-8");

        String message = authException.getMessage();

        // Distinguir entre caducado e invalido
        if (message != null && (message.contains("expired") || message.contains("caducado"))) {
            response.getWriter().write("Error: El token ha caducado.");
        } else {
            response.getWriter().write("Error: Token invalido.");
        }
    }
}
