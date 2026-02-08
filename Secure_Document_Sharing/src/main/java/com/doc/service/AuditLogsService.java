package com.doc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.doc.dto.AuditLogsDTO;
import com.doc.entity.AuditLogs;
import com.doc.entity.DocumentPermissions;
import com.doc.entity.Documents;
import com.doc.entity.ManageAction;
import com.doc.entity.User;
import com.doc.repository.AuditLogsRepository;
import com.doc.repository.DocumentPermissionsRepository;
import com.doc.repository.DocumentsRepository;
import com.doc.repository.UserRepository;

@Service
public class AuditLogsService implements IAuditLogsService 
{
	
	@Autowired
	private AuditLogsRepository auditRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private DocumentsRepository docRepo;

	@Autowired
	private DocumentPermissionsRepository docPermission;
	
	
	
	// save the audit logs 
	@Override
	public void logAction(Long userId, Long documentId, Long docPermissionId, ManageAction action) 
	{
		
		// first set the user data into logs
		User user = userRepo.findById(userId).orElseThrow(()-> new IllegalAccessError("User Not Found"));
		
		AuditLogs log = new AuditLogs();
		
		// set the logs based user
		log.setUser(user);
		log.setAction(action);
		
		
		// set the document data into logs
		if(documentId != null)
		{
			Documents doc = docRepo.findById(documentId).orElseThrow(()-> new IllegalAccessError("Document Not Found"));
			
			// set the doc related log
			log.setDocument(doc);
		}
		
		
		// set the document permission data into logs
		if(docPermissionId != null)
		{
			DocumentPermissions docperm = docPermission.findById(docPermissionId)
														.orElseThrow(()-> new IllegalArgumentException("Document Permission are Not Found"));
			
			
			// set the permission related logs
			log.setDocPermission(docperm);
			
		}
		
		
		//save the the log object
		
		auditRepo.save(log);
		
		
		
	}
	
	
	
	// get the audit logs
	@Override
	public Page<AuditLogsDTO> getUserAuditLogs(String username, Pageable pageable) {
		// get the user data first
		User user = userRepo.findByemail(username).orElseThrow(()-> new IllegalAccessError("User Not Found!"));
		
		// get the Security Audit logs
		Page<AuditLogs> auditLogs = auditRepo.findByUser_Uid(user.getUid(), pageable);
		
		
		return auditLogs.map(logs->{
			
			// create dto object
			AuditLogsDTO dto = new AuditLogsDTO();
			
			// set the values in dto
			dto.setDocumentName(logs.getDocument().getOriginalName());
			dto.setAction(logs.getAction().toString());
			
			if (logs.getDocPermission() != null) 
			{
	            dto.setPerformedBy(logs.getDocPermission().getGrantedToUser());
	        } else 
	        {
	            dto.setPerformedBy(user.getEmail());
	        }
			
			dto.setTime(logs.getInsertedOn());
			
			return dto;
			
		});
	}

}
