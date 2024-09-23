package com.banking.quantum.manager.controller;

import com.banking.quantum.common.domain.dto.UserUpdateDto;
import com.banking.quantum.manager.domain.dto.ManagerDto;
import com.banking.quantum.manager.service.ManagerUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quantumbanking/dashboard")
public class ManagerUpdateController {

    @Autowired
    private ManagerUpdateService managerUpdateService;

    @PutMapping("/update-profile")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ManagerDto> updateProfileManager(@RequestHeader("Authorization") String token,
                                                           @RequestBody UserUpdateDto updateDto) {

        ManagerDto updateManager = managerUpdateService.managerUpdate(token, updateDto);
        return ResponseEntity.ok(updateManager);
    }
}