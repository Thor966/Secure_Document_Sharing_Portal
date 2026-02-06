package com.doc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doc.dto.UserDTO;
import com.doc.service.IStatsService;
import com.doc.service.IuserService;

@RestController
public class StatsController 
{
	
	@Autowired
	private IStatsService statsService;
	
	@Autowired
	private IuserService userService;
	
	
	
	// get the logged in user
	public UserDTO getLoggedInUser()
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		String username = authentication.getName();
		
		UserDTO user = userService.getByUsername(username);
		
		return user;
	}
	
	
	
	// get the total Document count 
	@GetMapping("/documentCount")
	public ResponseEntity<Long> totalDocumentCount()
	{
		// get the logged in user
		UserDTO user = getLoggedInUser();
		
		// get the Service class method
		Long count = statsService.totalDocumentCount(user.getEmail());
		
		return ResponseEntity.ok(count);
	}
	
	
	// get the shared document count
	@GetMapping("/sharedDocCount")
	public ResponseEntity<Long> sharedDocumentCount()
	{
		// get the loggedin user
		UserDTO user = getLoggedInUser();
		
		// get the service class method
		Long sharedCount = statsService.sharedDocumentCount(user.getEmail());
		
		return ResponseEntity.ok(sharedCount);
	}
	
	
	
	// get the active link count
	@GetMapping("/activeLinkCount")
	public ResponseEntity<Long> getActiveLinkCount()
	{
		// get the loggedin user
		UserDTO user = getLoggedInUser();
		
		// get the service class method
		Long linkCount = statsService.getActiveLinkCount(user.getEmail());
		
		return ResponseEntity.ok(linkCount);
	}
	
	
	
	
	
	

}
