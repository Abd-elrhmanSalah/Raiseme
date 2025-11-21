package com.eprogs.raiseme.dto.authDTOS;

import com.eprogs.raiseme.entity.SystemUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@AllArgsConstructor
@Builder
public class SystemUserDetails implements UserDetails {
    private final SystemUser systemUser;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public String getPassword() {
        return this.systemUser.getPassword();
    }

    @Override
    public String getUsername() {
        return this.systemUser.getEmail();
    }


    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public Long getUserId() {
        return this.systemUser.getId();
    }

    public String getToken() {
        return this.systemUser.getToken();
    }

    public String getFirstName() {
        return this.systemUser.getFirstName();
    }

    public String getLastName() {
        return this.systemUser.getLastName();
    }

    public Boolean isActive() {
        return !this.systemUser.getIsFirstLogin();
    }
}
