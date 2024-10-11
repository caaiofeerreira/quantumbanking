package com.banking.quantum.client.controller;

import com.banking.quantum.client.domain.client.Client;
import com.banking.quantum.client.domain.client.ClientAuthentication;
import com.banking.quantum.client.domain.repository.ClientRepository;
import com.banking.quantum.common.infra.exception.InvalidCredentialsException;
import com.banking.quantum.common.infra.exception.UnauthorizedAccessException;
import com.banking.quantum.common.infra.security.DadosTokenJWT;
import com.banking.quantum.common.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/quantumbanking/account")
public class ClientAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<DadosTokenJWT> login(@RequestBody @Valid ClientAuthentication dados) {

        Client client = clientRepository.findByCpf(dados.cpf());

        if (client != null && !client.isAccountActive()) {
            throw new UnauthorizedAccessException("Conta encerrada. Acesso negado.");
        }

        try {
            var authenticationToken = new UsernamePasswordAuthenticationToken(dados.cpf(), dados.password());
            var authentication = authenticationManager.authenticate(authenticationToken);
            var tokenJWT = tokenService.generateTokenClient((Client) authentication.getPrincipal());

            return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException("Credenciais inválidas fornecidas. Verifique seu login e senha e tente novamente.");
        }
    }
}