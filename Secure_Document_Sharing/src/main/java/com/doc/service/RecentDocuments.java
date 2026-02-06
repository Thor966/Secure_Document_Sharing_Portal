package com.doc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.doc.dto.DocumentPermissionsDTO;
import com.doc.entity.DocumentPermissions;
import com.doc.entity.User;
import com.doc.repository.DocumentPermissionsRepository;
import com.doc.repository.UserRepository;

@Service
public class RecentDocuments implements IRecentDocument 
{

	@Autowired
	private DocumentPermissionsRepository docPermission;
	
	@Autowired
	private UserRepository userRepo;
	
	
	
	// get the recent documents
	@Override
	public Page<DocumentPermissionsDTO> getRecentDocuments(String username, Pageable pageable) {
		
		// get the user class object data
		User userData = userRepo.findByemail(username).orElseThrow(()-> new IllegalAccessError("User Not Found"));
		
		// get the recent document data
		Page<DocumentPermissions> recentDocData = docPermission.findByGrantedBy(userData, pageable);
		
		
		
		return recentDocData.map(doc ->{
			
			 DocumentPermissionsDTO dto =
		                new DocumentPermissionsDTO();
			 
			 dto.setDocumentName(doc.getDocumentId().getOriginalName());
			 dto.setAccessType(doc.getAccessType());
			 dto.setExpiryTime(doc.getExpiryTime());
			 dto.setStatus(doc.getStatus());
			 dto.setInsertedOn(doc.getInsertedOn());
			 dto.setFilePath(doc.getDocumentId().getFilePath());
			 
			 return dto;
		});
	}
}
