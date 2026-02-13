package com.doc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doc.dto.StorageUsageDTO;
import com.doc.service.IAdminDashboardService;

@RestController
public class AdminDashboardController
{
	
	@Autowired
	private IAdminDashboardService dashboardService;
	
	
	// get the logged in admin
	
	
	
	// get the total register count and daywise user count
	@GetMapping("/registerUserCount")
	public ResponseEntity<?> fetchTotalUserCount()
	{
		// get looged in admin
		
		// get the service class method
		Long userCount = dashboardService.getTotalUsersCount();
		
		// get the todays register count
		Long todaysUserCount = dashboardService.getTodaysRegisterCount();
		
		Map<String, Object> response = new HashMap<>();
		
		response.put("userCount", userCount);
		response.put("todaysUserCount", todaysUserCount);
		
		return ResponseEntity.ok(response);
	}
	
	
	
	
	
	// get the total uploaded Document
	@GetMapping("/uploadedDocument")
	public ResponseEntity<?> fetchTotalDocument()
	{
		
		// get the logged in admin
		
		
		// get the service class method

		Long uploadedDocCount = dashboardService.getTotalUploadedDocument();
		
		Long daywiseCount = dashboardService.getDaywiseUploadedCount();
		
		// set the responses
		Map<String, Object> response = new HashMap<>();
		
		response.put("uploadedDocCount", uploadedDocCount);
		response.put("daywiseCount", daywiseCount);
		
		return ResponseEntity.ok(response);

	}
	
	
	// get the total active shares
	@GetMapping("/fetchActiveShares")
	public ResponseEntity<Long> fetchActiveShares()
	{
		// get the logged in Admin
		
		// get the service class method
		Long activeSharesCount = dashboardService.getTotalActiveShares();
		
		return ResponseEntity.ok(activeSharesCount);
	}
	
	
	// get active user count
	@GetMapping("/onlineUsers")
	public ResponseEntity<Integer> getOnlineUserCount()
	{
		// get the loggedIn admin
		
		// get the service class method
		Integer onlineCount = dashboardService.getOnlineUsersCount();
		
		return ResponseEntity.ok(onlineCount);
	}
	
	
	
	// get the Disabled User count
	
	

	// get the daywise expired Document count 
	@GetMapping("/daywiseExpiredCount")
	public ResponseEntity<Long> fetchDaywiseExpiredDocCount()
	{
		// get the loggedIn Admin
		
		// get the service class method
		Long expiredDocCount = dashboardService.getDaywiseExpiredDocCount();
		
		return ResponseEntity.ok(expiredDocCount);
	}
	
	
	
	// fetch the access type count
	@GetMapping("/accessTypeCount")
	public ResponseEntity<?> fetchAccessTypeCount()
	{
		
		// get the loggedIn Admin
		
		// get the service class method
		Long otpCount = dashboardService.getOtpAccessTypeCount();
		
		Long passCount = dashboardService.getPasswordAccessTypeCount();
		
		Long publicCount = dashboardService.getPublicAccessTypeCount();
		
		// set all these count in the response
		
		Map<String, Object> response = new HashMap<>();
		
		response.put("otpCount", otpCount);
		response.put("passCount", passCount);
		response.put("publicCount", publicCount);
		
		
		return ResponseEntity.ok(response);
	}
	
	
	// fetch the storage usage
	@GetMapping("/storageUsage")
	public ResponseEntity<?> fetchStorageUsage()
	{
		// get the loggedIn Admin 
		
		
		// get the service class method
		Map<String, Object> storageData = dashboardService.getStorateDetails();
		
		System.out.println("Storage Used"+ storageData);
		
		return ResponseEntity.ok(storageData);
	}
	
	
	
	@GetMapping("/storageUsagePerUser")
	public ResponseEntity<?> getStorageUsagePerUser() {
		
		List<StorageUsageDTO> storageUsagePerUser = dashboardService.getStoragePerUser();
		
	    return ResponseEntity.ok( storageUsagePerUser);
	}


}
