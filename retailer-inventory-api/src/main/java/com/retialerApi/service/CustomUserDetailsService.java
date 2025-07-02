package com.retialerApi.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.retialerApi.entity.Retailer;
import com.retialerApi.repository.RetailerRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private RetailerRepository retailerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Retailer retailer = retailerRepository.findByUsername(username);
        if (retailer == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(retailer);
    }
}
