package com.doc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.doc.dto.AdminDocumentManageDTO;
import com.doc.entity.DocumentPermissions;
import com.doc.entity.ManageAction;
import com.doc.entity.ManageStatus;
import com.doc.entity.User;
import com.doc.repository.DocumentPermissionsRepository;
import com.doc.repository.DocumentsRepository;
import com.doc.repository.UserRepository;


@Service
public class AdminDocumentService implements IAdminDocumentService 
{
	
	@Autowired
	private DocumentPermissionsRepository permissionRepo;
	
	@Autowired
	private DocumentsRepository docRepo;
	
	@Autowired
	private IAuditLogsService logService;

	
	
	// get total shared doc count
		@Override
		public Long getTotalSharedDocCount() {
			
			return permissionRepo.count();
		}
	
	// get the total active doc count
	@Override
	public Long getTotalActiveDocCount() {
		
		return permissionRepo.countByStatus("ACTIVE");
	}
	
	
	// get the total Expired Document
	@Override
	public Long getTotalExpiredDocumentCount() {
		
		return permissionRepo.countByStatus("EXPIRED");
	}
	
	
	// get the total revoked doc count
	@Override
	public Long getTotalRevokeDocumentCount() {
		return permissionRepo.countByStatus("REVOKE");
	}
	
	
	
	
	
	// get all documents Data
	@Override
	public Page<AdminDocumentManageDTO> getAllDocuments(Pageable pageable) {
		
		// get all documents
		Page<DocumentPermissions> docData = permissionRepo.findAll(pageable);
		
		
		
		return docData.map(doc->{
			
			// create the dto object first
			AdminDocumentManageDTO dto = new AdminDocumentManageDTO();
			
			dto.setDpid(doc.getDpid());
			dto.setDocumentName(doc.getDocumentId().getOriginalName());
			dto.setOwner(doc.getGrantedBy().getEmail());
			dto.setAccessType(doc.getAccessType());
			dto.setStatus(doc.getStatus());
			
			// Convert bytes to MB
	        double storageMB = doc.getDocumentId().getFileSize() != null
	                ? doc.getDocumentId().getFileSize() / 1024.0 / 1024.0
	                : 0.0;
			
			dto.setSize(storageMB);
			dto.setExpiry(doc.getExpiryTime());
			
			// get the user first
			User user = doc.getGrantedBy();
			Long shareCount = permissionRepo.countByGrantedBy(user);
			
			dto.setShares(shareCount);
			dto.setUploaded(doc.getInsertedOn());
			
			
			return dto;
			
		});
	}
	
	
	
	// filter by both keyword and status
	@Override
	public Page<AdminDocumentManageDTO> filterByKeywordAndStatus(String email, String status, Pageable pageable) {
		
		Page<DocumentPermissions> filterByBoth = permissionRepo.searchByKeywordAndStatus(email, status, pageable);
		
		
		return filterByBoth.map(filter->{
			
			// create the dto object first
			
			AdminDocumentManageDTO dto = new AdminDocumentManageDTO();
			
			dto.setDpid(filter.getDpid());
			dto.setDocumentName(filter.getDocumentId().getOriginalName());
			dto.setOwner(filter.getGrantedBy().getEmail());
			dto.setAccessType(filter.getAccessType());
			dto.setStatus(filter.getStatus());
			
			// Convert bytes to MB
	        double storageMB = filter.getDocumentId().getFileSize() != null
	                ? filter.getDocumentId().getFileSize() / 1024.0 / 1024.0
	                : 0.0;
			
			dto.setSize(storageMB);
			dto.setExpiry(filter.getExpiryTime());
			
			// get the user first
			User user = filter.getGrantedBy();
			Long shareCount = permissionRepo.countByGrantedBy(user);
			
			dto.setShares(shareCount);
			dto.setUploaded(filter.getInsertedOn());
			
			
			return dto;
			
			
		});
	}
	
	
	
	
	// filter by keyword only
	@Override
	public Page<AdminDocumentManageDTO> filterByKeyword(String email, Pageable pageable) {
		
		// get the data by filter email
		Page<DocumentPermissions> filterByEmail = permissionRepo.searchByKeywords(email, pageable);
		
		
		return filterByEmail.map(filter->{
			
			// first ceate the dto object
			
			AdminDocumentManageDTO dto = new AdminDocumentManageDTO();
			
			dto.setDpid(filter.getDpid());
			dto.setDocumentName(filter.getDocumentId().getOriginalName());
			dto.setOwner(filter.getGrantedBy().getEmail());
			dto.setAccessType(filter.getAccessType());
			dto.setStatus(filter.getStatus());
			
			// Convert bytes to MB
	        double storageMB = filter.getDocumentId().getFileSize() != null
	                ? filter.getDocumentId().getFileSize() / 1024.0 / 1024.0
	                : 0.0;
			
			dto.setSize(storageMB);
			dto.setExpiry(filter.getExpiryTime());
			
			// get the user first
			User user = filter.getGrantedBy();
			Long shareCount = permissionRepo.countByGrantedBy(user);
			
			dto.setShares(shareCount);
			dto.setUploaded(filter.getInsertedOn());
			
			
			return dto;
			
		});
	}
	
	
	
	// filter by status only
	@Override
	public Page<AdminDocumentManageDTO> filterByStatus(String status, Pageable pageable) {
		
		
		Page<DocumentPermissions> filterByStatus = permissionRepo.searchByStatus(status, pageable);
		
		
		
		return filterByStatus.map(filter->{
			
			// get the dto class first
			
			AdminDocumentManageDTO dto = new AdminDocumentManageDTO();
			
			dto.setDpid(filter.getDpid());
			dto.setDocumentName(filter.getDocumentId().getOriginalName());
			dto.setOwner(filter.getGrantedBy().getEmail());
			dto.setAccessType(filter.getAccessType());
			dto.setStatus(filter.getStatus());
			
			// Convert bytes to MB
	        double storageMB = filter.getDocumentId().getFileSize() != null
	                ? filter.getDocumentId().getFileSize() / 1024.0 / 1024.0
	                : 0.0;
			
			dto.setSize(storageMB);
			dto.setExpiry(filter.getExpiryTime());
			
			// get the user first
			User user = filter.getGrantedBy();
			Long shareCount = permissionRepo.countByGrantedBy(user);
			
			dto.setShares(shareCount);
			dto.setUploaded(filter.getInsertedOn());
			
			
			return dto;
			
		});
	}
	
	
	
	// user force revoke
	@Override
	public void forceRevokeDoc(Long dpid, String action) {
		
		// first get the user object
		DocumentPermissions perm = permissionRepo.findById(dpid).orElseThrow(()-> new IllegalArgumentException("User Not found"));
		
		// set the status
		perm.setStatus(action);
		perm.setSecureToken(null);
		perm.setDpEncryptedKey(null);
		
		
		perm.setDocOtp(null);
		
		perm.setDocPass(null);
		
		
		logService.logAction(perm.getGrantedBy().getUid(), perm.getDocumentId().getDocid(), dpid, ManageAction.FORCE_REVOKE, ManageStatus.ADMIN);
		
		// save the doc permission status
		permissionRepo.save(perm);
		
	}
	
	
	

}
