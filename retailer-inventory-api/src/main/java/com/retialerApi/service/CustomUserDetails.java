package com.retialerApi.service;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.retialerApi.entity.Retailer;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private Retailer retailer;

    public CustomUserDetails(Retailer retailer) {
        this.retailer = retailer;
    }

    @Override
    public String getUsername() {
        return retailer.getUsername();
    }

    @Override
    public String getPassword() {
        return retailer.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}

