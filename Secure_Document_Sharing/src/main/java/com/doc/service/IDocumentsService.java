package com.doc.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.doc.dto.DocumentPermissionsDTO;
import com.doc.dto.DocumentsDTO;
import com.doc.entity.DocumentPermissions;
import com.doc.entity.Documents;

public interface IDocumentsService 
{
	
	// save the Documents details
	public Documents saveDocumetsDetails(MultipartFile docFile);
	
	// get documents details
	public Page<DocumentsDTO> getDocumentData(String username, Pageable pageable);
	
	
	// share documents
	public DocumentPermissions shareDocuments(Long docid, String shareType,
								 String recieverMail, String accessType,
								 String expiry, String grantedByUsername);
	
	// get documents permissions Data
	public Iterable<DocumentPermissionsDTO> getDocumentPermission(String username);
	
	
	// Revoke the Document Permission
	public DocumentPermissions revokeDocumentPermission(Long dpid);
	
	
	// access Document By Link
	public DocumentPermissions accessDocByLink(String token);

}
