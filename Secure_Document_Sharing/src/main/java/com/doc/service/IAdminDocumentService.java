package com.doc.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.doc.dto.AdminDocumentManageDTO;

public interface IAdminDocumentService 
{
	
	
	// get all Documents 
	Page<AdminDocumentManageDTO> getAllDocuments(Pageable pageable);
	
	// filter by the both email and status
	Page<AdminDocumentManageDTO> filterByKeywordAndStatus(String email, String status, Pageable pageable);
	
	// filter by email only
	Page<AdminDocumentManageDTO> filterByKeyword(String email, Pageable pageable);
	
	
	// filter by status only
	Page<AdminDocumentManageDTO> filterByStatus(String status, Pageable pageable);

}
