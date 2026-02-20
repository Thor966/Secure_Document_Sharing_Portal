package com.doc.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.doc.dto.AuditLogsDTO;
import com.doc.dto.GlobalAuditLogsDTO;
import com.doc.entity.ManageAction;
import com.doc.entity.ManageStatus;

public interface IAuditLogsService 
{
	// save the audit logs
	public void logAction(Long userId,
            Long documentId,
            Long docPermissionId,
            ManageAction action, ManageStatus status);

	
	// get the user audit logs
	public Page<AuditLogsDTO> getUserAuditLogs(String username, Pageable pageable);
	
	
	// get all global audit logs
	public Page<GlobalAuditLogsDTO> getGlobalAuditLogs(Pageable pageable);
	
	
	// filter by keyword and status
	Page<GlobalAuditLogsDTO> filterByKeywordAndStatus(String keyword, String status, Pageable pageable);
	
	// filter by keyword only
	Page<GlobalAuditLogsDTO> filterByKeyword(String keyword, Pageable pageable);
	
	// filter by status only
	Page<GlobalAuditLogsDTO> filterByStatus(String status, Pageable pageable);
	
	

}
