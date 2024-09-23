package com.banking.quantum.client.domain.client;

import com.banking.quantum.client.domain.account.Account;
import com.banking.quantum.client.domain.account.AccountStatus;
import com.banking.quantum.common.domain.user.User;
import com.banking.quantum.common.domain.user.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity(name = "Client")
@Table(name = "tb_client")
@Getter
@Setter
public class Client extends User implements UserDetails {

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ClientType type;

    @OneToOne
    @JsonManagedReference
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "pix_key", nullable = true)
    private String pixKey;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private UserRole userRole = UserRole.CLIENT;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + userRole.name()));
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return super.getEmail();
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean isAccountActive() {
        return getAccount() != null && getAccount().getStatus() != AccountStatus.ENCERRADA;
    }
}