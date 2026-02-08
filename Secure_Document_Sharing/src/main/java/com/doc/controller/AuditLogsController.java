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

import com.doc.dto.AuditLogsDTO;
import com.doc.dto.UserDTO;
import com.doc.service.IAuditLogsService;
import com.doc.service.IuserService;

@RestController
public class AuditLogsController 
{
	
	@Autowired
	private IAuditLogsService auditService;
	
	@Autowired
	private IuserService userService;
	
	
	
	// get the loggedIn user
	public UserDTO getLoggedInUser()
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		
		UserDTO user = userService.getByUsername(username);
		
		return user;
	}
	
	
	
	// get the security activity logs
	@GetMapping("/securityLogs")
	public ResponseEntity<?> fetchSecurityAuditLogs(@PageableDefault(page=0, size=10, sort="insertedOn", direction=Direction.DESC) Pageable pageable)
	{
		
		// get the logged in user
		UserDTO user = getLoggedInUser();
		
		// get the service class method
		Page<AuditLogsDTO> logs = auditService.getUserAuditLogs(user.getEmail(), pageable);
		
		
		return ResponseEntity.ok(logs);
		
	}
	
	
	
	// get the recent audit logs
	@GetMapping("/recentAuditLogs")
	public ResponseEntity<?> fetchRecentAuditLogs(@PageableDefault(page=0, size=5, sort="insertedOn", direction=Direction.DESC) Pageable pageable)
	{
		// get the loggedIn user
		UserDTO user = getLoggedInUser();
		
		// get the service class method
		Page<AuditLogsDTO> recentLogs = auditService.getUserAuditLogs(user.getEmail(), pageable);
		
		return ResponseEntity.ok(recentLogs);
	}

}
