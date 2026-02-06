package com.doc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doc.entity.User;
import com.doc.repository.DocumentPermissionsRepository;
import com.doc.repository.UserRepository;

@Service
public class AccessSummaryService implements IAccessSummaryService 
{
	
	
	@Autowired
	private DocumentPermissionsRepository docPermissions;
	
	@Autowired
	private UserRepository userRepo;
	
	
	
	// get the OTP based Documents Count
	@Override
	public Long getOTPBasedDocuments(String username) {
		
		// get the user object
		User user = userRepo.findByemail(username).orElseThrow(()-> new IllegalAccessError("User Not Found"));
		
		// get the count of user Documents Count
		Long otpCount = docPermissions.countByGrantedByAndAccessType(user, "OTP");
		
		return otpCount;
	}
	
	
	
	// get the Password Based Documents Count
	@Override
	public Long getPassBasedDocuments(String username) {
		
		// get the user object
		User user = userRepo.findByemail(username).orElseThrow(()-> new IllegalAccessError("User Not Found"));
		
		// get the password based documents count
		Long passCount = docPermissions.countByGrantedByAndAccessType(user, "PASSWORD");
		
		return passCount;
	}
	
	
	// get the Expired Documents Count
	@Override
	public Long getExpiredDocumentCount(String username) {
		// get the user object
		User user = userRepo.findByemail(username).orElseThrow(()-> new IllegalAccessError("User Not Found"));
		
		//get the Expired Document Count
		Long expiredCount = docPermissions.countByGrantedByAndStatus(user, "EXPIRED");
		
		return expiredCount;
	}
	

}
