package com.doc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doc.dto.UserDTO;
import com.doc.service.IAccessSummaryService;
import com.doc.service.IuserService;

@RestController
public class AccessSummaryController 
{
	
	
	@Autowired
	private IAccessSummaryService accessService;
	
	@Autowired
	private IuserService userService;
	
	
	
	// get the loggedIn user
	@PreAuthorize("hasAuthority('USER')")
	public UserDTO getLoggedInUser()
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		
		UserDTO user = userService.getByUsername(username);
		
		return user;
	}
	
	
	
	// get the OTP protected Documents
	@GetMapping("/docOtpCount")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<Long> fetchOTPProtectedDocuments()
	{
		// get the logged in user
		UserDTO user = getLoggedInUser();
		
		// get the service class method
		Long documentOtpCount = accessService.getOTPBasedDocuments(user.getEmail());
		
		return ResponseEntity.ok(documentOtpCount);
		
		
	}
	
	
	// get the Password Protected Documents
	@GetMapping("/docPassCount")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<Long> fetchPassProtectedDocuments()
	{
		// get the loggedIn User
		UserDTO user = getLoggedInUser();
		
		// get the service class method
		Long documentPassCount = accessService.getPassBasedDocuments(user.getEmail());
		
		return ResponseEntity.ok(documentPassCount);
	}
	
	
	
	// get the Expired Document Count
	@GetMapping("/docExpiredCount")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<Long> fetchExpiredDocumentCount()
	{
		// get the loggedIn user
		UserDTO user = getLoggedInUser();
		
		// get the service class method
		Long documentExpiredCount = accessService.getExpiredDocumentCount(user.getEmail());
		
		return ResponseEntity.ok(documentExpiredCount);
	}
	
	
	
	
	
	
	
	
	

}
