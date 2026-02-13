package com.doc.service;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

import com.doc.dto.StorageUsageDTO;
import com.doc.repository.DocumentPermissionsRepository;
import com.doc.repository.DocumentsRepository;
import com.doc.repository.UserRepository;

@Service
public class AdminDashboardService implements IAdminDashboardService
{
	
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private DocumentsRepository docRepo;
	
	@Autowired
	private DocumentPermissionsRepository permissionRepo;
	
	@Autowired
	private SessionRegistry sessionRegistry;
	
	
	
	// get the total register user count
	@Override
	public Long getTotalUsersCount() {
		
		return userRepo.count();
	}
	
	
	// get the today(daywise) register user
	@Override
	public Long getTodaysRegisterCount() {
		
		LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
	    LocalDateTime endOfDay = startOfDay.plusDays(1);

	    return userRepo.countByInsertedOnBetween(startOfDay, endOfDay);
	}
	
	
	
	
	// get the total uploaded document
	@Override
	public Long getTotalUploadedDocument() {
		
		return docRepo.count();
	}
	
	
	// get daywise uploaded count
	@Override
	public Long getDaywiseUploadedCount() {
		
		 LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
		    LocalDateTime endOfDay = startOfDay.plusDays(1);

		    return docRepo.countByInsertedOnBetween(startOfDay, endOfDay);
	}
	
	
	
	// get the total shared shares
	@Override
	public Long getTotalActiveShares() {
		
		return permissionRepo.countByStatus("ACTIVE");
	}
	
	
	
	// get active users count
	@Override
	public Integer getOnlineUsersCount() {


	    return sessionRegistry.getAllPrincipals()
	            .stream()
	            .filter(principal ->
	                sessionRegistry.getAllSessions(principal, false).size() > 0
	            )
	            .toList()
	            .size();
	}
	
	
	
	// get the Disbled User Count
	
	
	// get the daywise Expired doc count
	@Override
	public Long getDaywiseExpiredDocCount() {
		
		LocalDateTime startOf = LocalDateTime.now().toLocalDate().atStartOfDay();
		LocalDateTime endOf = startOf.plusDays(1);
		
		return permissionRepo.countByInsertedOnBetweenAndStatus(startOf, endOf, "EXPIRED");
	}
	
	
	// get the otp access type count
	@Override
	public Long getOtpAccessTypeCount() {
		Long otpCount = permissionRepo.countByAccessType("OTP");
		return otpCount;
	}
	
	
	// get the Password type count
	@Override
	public Long getPasswordAccessTypeCount() {
		Long passCount = permissionRepo.countByAccessType("PASSWORD");
		return passCount;
	}
	
	
	// get the public type count
	@Override
	public Long getPublicAccessTypeCount() {
		Long publicCount = permissionRepo.countByAccessType("NO ACCESS");
		return publicCount;
	}
	
	
	// get the storage details
	@Override
	public Map<String, Object> getStorateDetails() {
		
		// get the used storage in bytes
		Long usedBytes = docRepo.getTotalStoraeUsed();
		
		// get the system limit storage
		Long maxStorageBytes = 10L * 1024 * 1024 * 1024;
		
		// get the used storage in gb
		Double usedGB = usedBytes / (1024.0 *1024.0 *1024.0);
		
		// get the maximum storage in gb
		Double maxGB = maxStorageBytes / (1024.0 * 1024.0 * 1024.0);
		
		// get the usage in percentage
		Double percentage = (usedBytes * 100.0) / maxStorageBytes;
		
		
		// set all these in response
		Map<String, Object> response = new HashMap<>();
		
		response.put("usedGB",String.format("%.2f", usedGB));
		response.put("maxGB",String.format("%.2f", maxGB));
		response.put("percentage", Math.min(percentage, 100));
		
		return response;
	}
	
	
	
	// get the storage per user
	@Override
	public List<StorageUsageDTO> getStoragePerUser() {
		
		return docRepo.getStorageUsagePerUser();
	}

}
