package com.banking.quantum.manager.controller;

import com.banking.quantum.common.infra.exception.InvalidCredentialsException;
import com.banking.quantum.common.infra.security.DadosTokenJWT;
import com.banking.quantum.common.infra.security.TokenService;
import com.banking.quantum.manager.domain.manager.Manager;
import com.banking.quantum.manager.domain.manager.ManagerAuthentication;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/quantumbanking/manager")
public class ManagerAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<DadosTokenJWT> login(@RequestBody @Valid ManagerAuthentication dados) {

        try {
            var authenticationToken = new UsernamePasswordAuthenticationToken(dados.cpf(), dados.password());
            var authentication = authenticationManager.authenticate(authenticationToken);
            var tokenJWT = tokenService.generateTokenManager((Manager) authentication.getPrincipal());

            return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException("Credenciais inválidas fornecidas. Verifique seu login e senha e tente novamente.");
        }
    }
}