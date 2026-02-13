package com.doc.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doc.dto.AdminUserManageDTO;
import com.doc.service.IAdminUserManagementService;

@RestController
public class AdminUserManagementController
{
	
	
	@Autowired
	private IAdminUserManagementService adminUserService;
	
	
	
	// get disabled user count
	@GetMapping("/disabledUserCount")
	public ResponseEntity<Long> fetchDisabledUserCount()
	{
		// get loggedIn user
		
		// get service class method
		Long disableUserCount = adminUserService.getDisabledUserCount();
		
		return ResponseEntity.ok(disableUserCount);
	}
	
	
	// fetch all user Data
	@GetMapping("/fetchManageUserData")
	public ResponseEntity<?> fetchAllUserData(@PageableDefault(page=0, size=10, sort="insertedOn", direction=Direction.DESC) Pageable pageable)
	{
		// get the logged In Admin
		
		
		// get the service class method
		
		Page<AdminUserManageDTO> userData = adminUserService.getAllUsersData(pageable);
		
		
		Map<String, Object> response = new HashMap<>();
		response.put("content", userData.getContent());
	    response.put("currentPage", userData.getNumber());
	    response.put("pageSize", userData.getSize());
	    response.put("totalElements", userData.getTotalElements());
	    response.put("totalPages", userData.getTotalPages());
	    response.put("first", userData.isFirst());
	    response.put("last", userData.isLast());
		
		return ResponseEntity.ok(response);
		
	}
	
	

}
