package com.banking.quantum.common.infra.security;

import com.banking.quantum.client.domain.client.Client;
import com.banking.quantum.client.domain.repository.ClientRepository;
import com.banking.quantum.common.infra.exception.UnauthorizedAccessException;
import com.banking.quantum.manager.domain.manager.Manager;
import com.banking.quantum.manager.domain.repository.ManagerRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = recoverToken(request);

        if (token != null) {
            try {

                try {
                    Client client = tokenService.getAccountFromToken(token).getClient();
                    SecurityContextHolder.getContext().setAuthentication(
                            new UsernamePasswordAuthenticationToken(client, null, client.getAuthorities()));
                } catch (UnauthorizedAccessException e) {

                    try {
                        Manager manager = tokenService.getManagerFromToken(token);
                        SecurityContextHolder.getContext().setAuthentication(
                                new UsernamePasswordAuthenticationToken(manager, null, manager.getAuthorities()));
                    } catch (UnauthorizedAccessException ex) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("Token inválido ou expirado.");
                        return;
                    }
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token inválido ou expirado.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }


    private String recoverToken(HttpServletRequest request) {

        var authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }
}