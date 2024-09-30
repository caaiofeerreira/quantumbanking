package com.banking.quantum.common.service;

import com.banking.quantum.client.domain.client.Client;
import com.banking.quantum.client.domain.repository.ClientRepository;
import com.banking.quantum.manager.domain.manager.Manager;
import com.banking.quantum.manager.domain.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Client client = clientRepository.findByCpf(username);

        if (client != null) {
            return client;
        }

        Manager manager = managerRepository.findByCpf(username);

        if (manager != null) {
            return manager;
        }

        throw new UsernameNotFoundException("Usuario nao encontrado.");
    }
}