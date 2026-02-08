package com.doc.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.doc.dto.AuditLogsDTO;
import com.doc.entity.ManageAction;

public interface IAuditLogsService 
{
	// save the audit logs
	public void logAction(Long userId,
            Long documentId,
            Long docPermissionId,
            ManageAction action);

	
	// get the user audit logs
	public Page<AuditLogsDTO> getUserAuditLogs(String username, Pageable pageable);
	
	

}
