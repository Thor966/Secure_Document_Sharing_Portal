package com.doc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.doc.entity.AuthandAutho;
import com.doc.repository.AuthRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    

    @Autowired
    private AuthRepository authRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

       

        AuthandAutho auth = authRepo.findByUsername(username)
            .orElseThrow(() -> {
             
                return new UsernameNotFoundException("User not found");
            });

      

        return org.springframework.security.core.userdetails.User
                .withUsername(auth.getUsername())
                .password(auth.getPassword())
                .authorities(auth.getRole())
                .build();
    }
}
