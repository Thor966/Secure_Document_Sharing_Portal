package com.doc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doc.dto.AdminDTO;
import com.doc.dto.AuditLogsDTO;
import com.doc.dto.StorageUsageDTO;
import com.doc.service.IAdminDashboardService;

@RestController
public class AdminDashboardController
{
	
	@Autowired
	private IAdminDashboardService dashboardService;
	
	@Autowired
	private IAdminDashboardService adminRepo;
	
	
	
	
	// get the logged in admin
	@PreAuthorize("hasAuthority('ADMIN')")
	public AdminDTO getLoggedInAdmin()
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		
		AdminDTO admin = adminRepo.getAdminByUsername(username);
		
		return admin;
		
		
	}
	
	
	
	// fetch the User stats
	@GetMapping("/adminDashboardUserStats")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<?> fetchUserStats()
	{
		// get the logged In User
		AdminDTO admin = getLoggedInAdmin();
		
		// get the total register count and daywise user count
		Long userCount = dashboardService.getTotalUsersCount();
		
		// get the todays register count
		Long todaysUserCount = dashboardService.getTodaysRegisterCount();
		
		// get the online users
		Integer onlineCount = dashboardService.getOnlineUsersCount();
		
		// get disabled User count
		Long disableUserCount = dashboardService.getDisabledUserCount();
		
		
		// set all these in response
		Map<String, Object> response = new HashMap<>();
		
		response.put("userCount", userCount);
		response.put("todaysUserCount", todaysUserCount);
		response.put("onlineCount", onlineCount);
		response.put("disableUserCount", disableUserCount);
		
		
		return ResponseEntity.ok(response);
		
	}
	
	
	
	// fetch the document stats
	@GetMapping("/adminDashboardDocStats")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<?> fetchDocStats()
	{
		
		// get the logged in admin
		AdminDTO admin = getLoggedInAdmin();
		
		
		// get the total uploaded doc count service method
		Long uploadedDocCount = dashboardService.getTotalUploadedDocument();

		// get the daywise uploaded doc count
		Long daywiseCount = dashboardService.getDaywiseUploadedCount();
		
		// get the daywise expired Document count 
		Long expiredDocCount = dashboardService.getDaywiseExpiredDocCount();
		
		// get the force revoked doc count
		Long forceRevokeDocCount = dashboardService.getForceRevokeCount();
		
		// set the responses
		Map<String, Object> response = new HashMap<>();
		
		response.put("uploadedDocCount", uploadedDocCount);
		response.put("daywiseCount", daywiseCount);
		response.put("expiredDocCount", expiredDocCount);
		response.put("forceRevokeDocCount", forceRevokeDocCount);
		
		return ResponseEntity.ok(response);

	}
	
	
	
	
	
	// fetch the Shares Stats
	@GetMapping("/adminSharesStats")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<?> fetchSharesStats()
	{
		
		// get the loggedIn Admin
		AdminDTO admin = getLoggedInAdmin();
		
		// get the active shares
		Long activeSharesCount = dashboardService.getTotalActiveShares();
		
		// get the otp count
		Long otpCount = dashboardService.getOtpAccessTypeCount();
		
		// get the pass count
		Long passCount = dashboardService.getPasswordAccessTypeCount();
		
		// get the public count
		Long publicCount = dashboardService.getPublicAccessTypeCount();
		
		// set all these count in the response
		
		Map<String, Object> response = new HashMap<>();
		
		response.put("activeSharesCount", activeSharesCount);
		response.put("otpCount", otpCount);
		response.put("passCount", passCount);
		response.put("publicCount", publicCount);
		
		
		return ResponseEntity.ok(response);
	}
	
	
	
	
	// fetch the storage usage
	@GetMapping("/storageUsage")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<?> fetchStorageUsage()
	{
		// get the loggedIn Admin 
		AdminDTO admin = getLoggedInAdmin();
		
		
		// get the service class method
		Map<String, Object> storageData = dashboardService.getStorateDetails();
		
		System.out.println("Storage Used"+ storageData);
		
		return ResponseEntity.ok(storageData);
	}
	
	
	// get the used storage by user
	@GetMapping("/storageUsagePerUser")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<?> getStorageUsagePerUser()
	{
		// get the logged in user
		AdminDTO admin = getLoggedInAdmin();
		
		List<StorageUsageDTO> storageUsagePerUser = dashboardService.getStoragePerUser();
		
	    return ResponseEntity.ok( storageUsagePerUser);
	}
	
	
	
	// get the recent action of the admin
	@GetMapping("/recentAdminAction")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<?> fetchRecentAdminActions(@PageableDefault(page=0, size=5, sort="insertedOn", direction=Direction.DESC) Pageable pageable)
	{
		
		// get the logged in user
		AdminDTO admin = getLoggedInAdmin();
		
		// get the service class method 
		Page<AuditLogsDTO> recentAction = dashboardService.getAdminRecentAction(pageable);
		
		
		Map<String, Object> response = new HashMap<>();
		
		response.put("content", recentAction.getContent());
	    response.put("currentPage", recentAction.getNumber());
	    response.put("pageSize", recentAction.getSize());
	    response.put("totalElements", recentAction.getTotalElements());
	    response.put("totalPages", recentAction.getTotalPages());
	    response.put("first", recentAction.isFirst());
	    response.put("last", recentAction.isLast());
		
	    return ResponseEntity.ok(response);
	}


}
