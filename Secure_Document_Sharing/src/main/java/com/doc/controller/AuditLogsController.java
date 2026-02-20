package com.doc.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doc.dto.AdminDTO;
import com.doc.dto.AuditLogsDTO;
import com.doc.dto.GlobalAuditLogsDTO;
import com.doc.dto.UserDTO;
import com.doc.service.IAdminDashboardService;
import com.doc.service.IAuditLogsService;
import com.doc.service.IuserService;

@RestController
public class AuditLogsController 
{
	
	@Autowired
	private IAuditLogsService auditService;
	
	@Autowired
	private IuserService userService;
	
	@Autowired
	private IAdminDashboardService dashboardService;
	
	
	
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

	
	
	// get the global audit logs for admin
	@GetMapping("/globalAuditLogs")
	public ResponseEntity<?> getAllGlobalAuditLogs(@RequestParam(required = false) String keyword,
													@RequestParam(required = false) String status,
													@PageableDefault(page=0, size=20, sort="insertedOn", direction=Direction.DESC) Pageable pageable)
	{
		
		// get the logged in Admin
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		
		AdminDTO admin = dashboardService.getAdminByUsername(username);
		
		Page<GlobalAuditLogsDTO> auditPage;
		
		
		if(keyword != null && !keyword.trim().isEmpty() &&
				status != null && !status.trim().isEmpty())
		{
			auditPage = auditService.filterByKeywordAndStatus(keyword, status, pageable);
		}
		else if(keyword != null && !keyword.trim().isEmpty())
		{
			auditPage = auditService.filterByKeyword(keyword, pageable);
		}
		else if(status != null && !status.trim().isEmpty())
		{
			auditPage = auditService.filterByStatus(status, pageable);
		}
		else
		{
			auditPage = auditService.getGlobalAuditLogs(pageable);
		}
		
		
		// get the service class method
		
		Map<String, Object> response = new HashMap<>();
		
		response.put("content", auditPage.getContent());
		response.put("currentPage", auditPage.getNumber());
	    response.put("pageSize", auditPage.getSize());
	    response.put("totalElements", auditPage.getTotalElements());
	    response.put("totalPages", auditPage.getTotalPages());
	    response.put("first", auditPage.isFirst());
	    response.put("last", auditPage.isLast());
		
		
	    return ResponseEntity.ok(response);
	    
	}
}
