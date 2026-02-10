package com.doc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.doc.entity.Documents;
import com.doc.entity.User;
import com.doc.repository.DocumentPermissionsRepository;
import com.doc.repository.DocumentsRepository;
import com.doc.repository.UserRepository;

@Service
public class StatsService implements IStatsService
{
	
	@Autowired
	private DocumentsRepository docRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private DocumentPermissionsRepository docPermission;
	
	
	
	// get the total document count
	@Override
	@Cacheable(value="totalDocCountCache", key="#username")
	public Long totalDocumentCount(String username) {
		// get the user id by username
		User user = userRepo.findByemail(username).orElseThrow(()-> new IllegalAccessError("User Not Found"));
		
		// count the Document 
		Long docCount = docRepo.countByOwner(user);
		
		
		return docCount;
	}
	
	
	
	// get the shared document count
	@Override
	@Cacheable(value="sharedDocCountCache", key="#username")
	public Long sharedDocumentCount(String username) {
		// get the user id by username
		User user = userRepo.findByemail(username).orElseThrow(()-> new IllegalAccessError("User Not Found"));
		
		// count the shared Doc
		Long sharedDocCount = docPermission.countByGrantedBy(user);
		
		return sharedDocCount;
	}
	
	
	// get the active link count
	@Override
	@Cacheable(value="activeLinkCountCache", key="#username")
	public Long getActiveLinkCount(String username) {
		User user = userRepo.findByemail(username).orElseThrow(()-> new IllegalAccessError("User Not Found"));
		
		
		// get the active link count
		Long linkCount = docPermission.countByGrantedByAndStatus(user,"ACTIVE");
		
		return linkCount;
	}

}
