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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doc.dto.AdminUserManageDTO;
import com.doc.entity.User;
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
	
	

	
	
	
	// filter the user Data 
	@GetMapping("/fetchManageUserData")
	public ResponseEntity<?> filterUserData(@RequestParam(required = false) String keyword,
											@RequestParam(required = false) String status,
											@PageableDefault(page=0, size=10, sort="insertedOn", direction=Direction.DESC) Pageable pageable)
	{
		
		// get the logged in Admin
		
		
		Page<AdminUserManageDTO> userPage;
		
		if(keyword != null && !keyword.trim().isEmpty() &&
			status != null && !status.trim().isEmpty())
		{
			
			userPage = adminUserService.filterByKeywordAndStatus(keyword, status, pageable);
			
			System.out.println("both filter data "+userPage);
			
		}
		else if(keyword != null && !keyword.trim().isEmpty())
		{
			userPage = adminUserService.filterByKeyword(keyword, pageable);
			
			System.out.println("Only keyword Data "+userPage);
			
		}
		else if(status != null && !status.trim().isEmpty())
		{
			userPage = adminUserService.filterByStatus(status, pageable);
			
			System.out.println("Only status Data "+ userPage);
		}
		else
		{
			userPage = adminUserService.getAllUsersData(pageable);
		}
		
		
		Map<String, Object> response = new HashMap<>();
		
		
		response.put("content", userPage.getContent());
	    response.put("currentPage", userPage.getNumber());
	    response.put("pageSize", userPage.getSize());
	    response.put("totalElements", userPage.getTotalElements());
	    response.put("totalPages", userPage.getTotalPages());
	    response.put("first", userPage.isFirst());
	    response.put("last", userPage.isLast());
	    
	    
	    return ResponseEntity.ok(response);
	
		
 		
	}
	
	
	// update the user status
	@PostMapping("/toggleUserStatus/{uid}")
	public ResponseEntity<?> updateUserStatus(@PathVariable("uid") Long uid, @RequestParam("action") String action)
	{
		
		// get the logged In Admin
		
		
		// get the service class method
		
		try
		{
			adminUserService.updateUserStatus(uid, action);
			
			return ResponseEntity.ok("User Status updated Successfully");
		}
		catch(Exception e)
		{
			  return ResponseEntity
		                .badRequest()
		                .body("Failed to update user status");
			
		}
	}
 	
	
	

}
