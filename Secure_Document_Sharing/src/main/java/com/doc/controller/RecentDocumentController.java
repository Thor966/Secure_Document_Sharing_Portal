package com.doc.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doc.dto.DocumentPermissionsDTO;
import com.doc.dto.UserDTO;
import com.doc.service.IRecentDocument;
import com.doc.service.IuserService;

@RestController
public class RecentDocumentController
{

	
	@Autowired
	private IRecentDocument recentDocService;
	
	@Autowired
	private IuserService userService;
	
	
	
	// get the loggedIn User 
	public UserDTO getLoggedInUser()
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		String username = authentication.getName();
		
		UserDTO user = userService.getByUsername(username);
		
		return user;
	}
	
	
	
	
	// fetch the recent documents
	@GetMapping("/recentDocuments")
	public ResponseEntity<?> fetchRecentDocuments(@PageableDefault(page=0, size=5, sort="insertedOn", direction= Direction.DESC) Pageable pageable)
	{
		// get the logged in user
		
		UserDTO user = getLoggedInUser();
		
		// get the service class method
		Page<DocumentPermissionsDTO> recentDoc = recentDocService.getRecentDocuments(user.getEmail(), pageable);
		
		
		return ResponseEntity.ok(recentDoc);
	}
}
