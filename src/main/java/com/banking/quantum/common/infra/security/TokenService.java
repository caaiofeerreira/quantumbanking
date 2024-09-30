package com.banking.quantum.common.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.banking.quantum.client.domain.account.Account;
import com.banking.quantum.client.domain.client.Client;
import com.banking.quantum.client.domain.repository.AccountRepository;
import com.banking.quantum.common.infra.exception.AccountNotFoundException;
import com.banking.quantum.common.infra.exception.ManagerNotFoundException;
import com.banking.quantum.common.infra.exception.UnauthorizedAccessException;
import com.banking.quantum.manager.domain.manager.Manager;
import com.banking.quantum.manager.domain.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private AccountRepository accountRepository;

    public String generateTokenClient(Client client) {

        try {
            var algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer("API quantum.banking")
                    .withSubject(client.getId().toString())
                    .withClaim("role", "CLIENT")
                    .withExpiresAt(dataExpiration())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    public String generateTokenManager(Manager manager) {

        try {
            var algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer("API quantum.banking")
                    .withSubject(manager.getId().toString())
                    .withClaim("role", "MANAGER")
                    .withExpiresAt(dataExpiration())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    public String getSubject(String tokenJWT) {

        try {
            var algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("API quantum.banking")
                    .build()
                    .verify(tokenJWT.replace("Bearer ", ""))
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new UnauthorizedAccessException("Token JWT inválido ou expirado!");
        }
    }

    public Account getAccountFromToken(String tokenJWT) {

        try {
            String cleanedToken = tokenJWT.replace("Bearer ", "");
            DecodedJWT decodedJWT = JWT.decode(cleanedToken);
            String role = decodedJWT.getClaim("role").asString();

            if (!"CLIENT".equals(role)) {
                throw new UnauthorizedAccessException("Token JWT não pertence a um cliente.");
            }

            String clientId = getSubject(cleanedToken);
            return accountRepository.findById(Long.parseLong(clientId))
                    .orElseThrow(() -> new AccountNotFoundException("Usuário não encontrado"));

        } catch (JWTVerificationException exception) {
            throw new UnauthorizedAccessException("Token JWT inválido ou expirado!");
        }
    }

    public Manager getManagerFromToken(String tokenJWT) {

        try {
            String cleanedToken = tokenJWT.replace("Bearer ", "");
            DecodedJWT decodedJWT = JWT.decode(cleanedToken);
            String role = decodedJWT.getClaim("role").asString();

            if (!"MANAGER".equals(role)) {
                throw new UnauthorizedAccessException("Token JWT não pertence a um gerente.");
            }

            String managerId = getSubject(cleanedToken);
            return managerRepository.findById(Long.parseLong(managerId))
                    .orElseThrow(() -> new ManagerNotFoundException("Gerente não encontrado"));

        } catch (JWTVerificationException exception) {
            throw new UnauthorizedAccessException("Token JWT inválido ou expirado!");
        }
    }

    private Instant dataExpiration() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}