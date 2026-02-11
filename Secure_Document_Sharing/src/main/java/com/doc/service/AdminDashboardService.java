package com.doc.service;


import java.time.LocalDateTime;

import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

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
		System.out.println("Principals: " + sessionRegistry.getAllPrincipals());


	    return sessionRegistry.getAllPrincipals()
	            .stream()
	            .filter(principal ->
	                sessionRegistry.getAllSessions(principal, false).size() > 0
	            )
	            .toList()
	            .size();
	}
	

}
