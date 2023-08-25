package com.akzam.paymentservice.configuration;

import com.akzam.paymentservice.DTO.ApiRuntimeExceptionResponse;
import com.akzam.paymentservice.exception.ApiExceptionResponseFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ApiExceptionResponseFactory apiExceptionResponseFactory;
    private final ObjectMapper objectMapper;

    @Autowired
    public JwtExceptionFilter(ApiExceptionResponseFactory apiExceptionResponseFactory, ObjectMapper objectMapper) {
        this.apiExceptionResponseFactory = apiExceptionResponseFactory;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            ApiRuntimeExceptionResponse errorResponse = apiExceptionResponseFactory.createApiRuntimeExceptionResponse(
                    HttpStatus.BAD_REQUEST,
                    "Invalid JWT token: " + e.getMessage()
            );

            String jsonResponse = objectMapper.writeValueAsString(errorResponse);

            try (PrintWriter writer = response.getWriter()) {
                writer.write(jsonResponse);
                writer.flush();
            }
        }
    }
}
