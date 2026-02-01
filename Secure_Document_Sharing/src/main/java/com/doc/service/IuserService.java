package com.doc.service;

import com.doc.dto.UserDTO;


public interface IuserService
{

	
	// register the user 
	public String registerUser(UserDTO dto);
	
	// find user by username
	public UserDTO getByUsername(String username);
}
