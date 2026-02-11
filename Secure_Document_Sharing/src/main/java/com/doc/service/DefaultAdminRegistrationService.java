package com.doc.service;

import java.util.Optional;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.doc.entity.Admin;
import com.doc.entity.AuthandAutho;
import com.doc.repository.AdminRepository;
import com.doc.repository.AuthRepository;


@Service
public class DefaultAdminRegistrationService
{
	
	
	@Autowired
	private AdminRepository adminRepo;
	
	@Value("${ADMIN_DEFAULT_USERNAME}")
    private String defaultUsername;
	
	@Value("${ADMIN_DEFAULT_NAME}")
	private String defaultName;

    @Value("${ADMIN_DEFAULT_PASSWORD}")
    private String defaultPassword;

    @Value("${ADMIN_DEFAULT_EMAIL}")
    private String defaultEmail;
    
    @Value("${ADMIN_DEFAULT_PHNO} ")
    private Long defaultPhno;
    
    
    @Autowired
	private AuthRepository authRepo;
    
    @Autowired
	private BCryptPasswordEncoder passwordEncoder;
    
    
    
    
 // save the admin details
 	@CacheEvict(value = "adminCache", allEntries = true)
 	public void saveAdminData() {
 		
 	    if (adminRepo.count() == 0) {
 	        
 	        Admin admin = new Admin();
 	        admin.setUsername(defaultUsername);
 	        admin.setName(defaultName);
 	        admin.setEmail(defaultEmail);
 	        admin.setPhno(defaultPhno);

 	        String rawPassword = defaultPassword; 
 	        if (rawPassword == null || rawPassword.isBlank()) {
 	        	throw new IllegalStateException("Default admin password is missing! Set it via environment variable ADMIN_DEFAULT_PASSWORD.");
 	        }

 	        String encodedPassword = passwordEncoder.encode(rawPassword);
 	        admin.setPassword(encodedPassword);

 	        AuthandAutho auth = new AuthandAutho();
 	        auth.setUsername(admin.getUsername());
 	        auth.setPassword(encodedPassword);
 	        auth.setRole("ADMIN");

 	        Optional<AuthandAutho> existingAuth = authRepo.findByUsername(auth.getUsername());
 	        if (existingAuth.isPresent()) {
 	          
 	            throw new RuntimeException("Admin username already registered!");
 	        }

 	        
 	        admin.setAuth(auth);

 	        adminRepo.save(admin);
 	       

 	    } 
 	}



}
