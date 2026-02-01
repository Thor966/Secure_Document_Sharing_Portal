package com.doc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.doc.dto.UserDTO;
import com.doc.entity.AuthandAutho;
import com.doc.entity.User;
import com.doc.repository.UserRepository;

@Service
public class UserService  implements IuserService
{
	
	@Autowired
	private  UserRepository userRepo;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	
	
	
	// register user 
	@Override
	@CacheEvict(value = "userCache", allEntries = true)
	public String registerUser(UserDTO dto) {
		
		
		// create user Entity
		User user = new User();
		
		user.setFirstName(dto.getFirstName());
		user.setLastName(dto.getLastName());
		user.setEmail(dto.getEmail());
		user.setPassword(dto.getPassword());
		
		// Encode the password
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		
		// create auth object and set the data
		AuthandAutho auth = new AuthandAutho();
		auth.setUsername(String.valueOf(user.getEmail()));
		auth.setPassword(user.getPassword());
		auth.setRole("USER");
		
		// Link User --> Auth
		user.setAuth(auth);
		
		userRepo.save(user);
		
		return "User Registered Successfully... "+user.getPublicId();
	}
	
	// get by user username
	@Override
	@Cacheable(value="userCache", key="#username")
	public UserDTO getByUsername(String username)
	{
		User user = userRepo.findByemail(username).orElseThrow(()-> new IllegalArgumentException("User Not found..."));
		
		// copy data in dto
		UserDTO dto = new UserDTO();
		dto.setEmail(user.getEmail());
		dto.setFirstName(user.getFirstName());
		
		return dto;
	}
	
	
	

}
