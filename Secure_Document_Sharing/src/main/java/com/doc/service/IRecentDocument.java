package com.doc.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.doc.dto.DocumentPermissionsDTO;

public interface IRecentDocument
{
	
	
	// get the recent document 
	
	public Page<DocumentPermissionsDTO> getRecentDocuments(String username, Pageable pageable);

}
