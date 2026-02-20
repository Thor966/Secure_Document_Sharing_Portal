package com.doc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.doc.entity.AuthandAutho;
import com.doc.entity.User;
import com.doc.repository.AuthRepository;
import com.doc.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    

    @Autowired
    private AuthRepository authRepo;
    
    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

       

		
    	  AuthandAutho auth = authRepo.findByUsername(username)
    	            .orElseThrow(() ->
    	                    new UsernameNotFoundException("User not found"));

    	    
    	    User user = userRepo.findByemail(username).orElse(null);

    	    boolean isDisabled = false;
    	    boolean isLocked = false;

    	    if (user != null) {
    	        isDisabled = "DISABLED".equalsIgnoreCase(user.getStatus());
    	        isLocked = "LOCKED".equalsIgnoreCase(user.getStatus());
    	    }

    	    return org.springframework.security.core.userdetails.User
    	            .withUsername(auth.getUsername())
    	            .password(auth.getPassword())
    	            .authorities(auth.getRole())  
    	            .accountLocked(isLocked)
    	            .disabled(isDisabled)
    	            .build();

        

        }
}
