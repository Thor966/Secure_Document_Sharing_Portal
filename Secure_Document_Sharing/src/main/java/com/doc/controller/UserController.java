package com.doc.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doc.dto.UserDTO;
import com.doc.entity.User;
import com.doc.service.IuserService;


@RestController
@RequestMapping("/userOperations")
public class UserController
{
	
	@Autowired
	private IuserService userService;
	
	
	// get username
	public UserDTO getLoggedInUser()
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		
		UserDTO user = userService.getByUsername(username);
		
		return user;
	}
//******************************************************************************************
	
	
	
	
	// register user
	@PostMapping("/register")
	public ResponseEntity<String> userRegister(@RequestBody UserDTO dto)
	{
		// get the service class method 
		String msg = userService.registerUser(dto);
		
		return new ResponseEntity<String>(msg, HttpStatus.CREATED);
	}
	
	// get user
	@GetMapping("/get-username")
	public ResponseEntity<UserDTO> getusername()
	{
		
		UserDTO user = getLoggedInUser();
		
		return ResponseEntity.ok(user);
		
	}

}
