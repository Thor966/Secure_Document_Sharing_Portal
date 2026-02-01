package com.doc.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.doc.dto.DocumentPermissionsDTO;
import com.doc.dto.DocumentsDTO;
import com.doc.entity.DocumentPermissions;
import com.doc.entity.Documents;
import com.doc.entity.User;
import com.doc.repository.DocumentPermissionsRepository;
import com.doc.repository.DocumentsRepository;
import com.doc.repository.UserRepository;
import com.doc.util.FileTypeValidator;

@Service
public class DocumentsService implements IDocumentsService
{
	
	@Autowired
	private DocumentsRepository docRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private DocumentPermissionsRepository docPermission;
	
	  @Value("${upload.path}")
	  private String uploadDir;
	

	
	
	// save the documents
	
	@Override
	@CacheEvict(value = "docCache", allEntries = true)
	public Documents saveDocumetsDetails(MultipartFile docFile) {
		
		
		// Validate file exist or not
		if(docFile.isEmpty())
			 throw new IllegalArgumentException("File is Empty");
		
		
		// Validate unsupported File type
		if(!FileTypeValidator.isValid(docFile.getContentType()))
			throw new IllegalArgumentException("Unsupported File Type");
		
		// get logged in user 
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		
		User owner = userRepo.findByemail(username).orElseThrow(()-> new IllegalArgumentException("User Not Found"));
		
		
		
		
		 // Store inside project folder
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        
        // Debug: check where files are stored
        System.out.println("Uploading file to: " + uploadPath);
        
		
		 String fileName = docFile.getOriginalFilename();
         String storedName = UUID.randomUUID() + "_" + fileName;
         String dbPath = "/uploads/" + storedName;
         Path filePath = uploadPath.resolve(storedName);
          
         try
 		{
         // Copy file to disk
         Files.copy(docFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
		}
		catch (Exception e) {
			 e.printStackTrace();
			    throw new RuntimeException("File upload failed", e);
		}
		
     
		
		// save the data of documents
		Documents doc = new Documents();
		doc.setOriginalName(fileName);
		doc.setStoredName(storedName);
		doc.setFilePath(dbPath.toString());
		doc.setFileSize(docFile.getSize());
		doc.setMimeType(docFile.getContentType());
		doc.setEncryptedKey(UUID.randomUUID().toString());
		doc.setOwner(owner);
		
		return docRepo.save(doc);
	}
	
	
	
	
	// get the documents data
	@Override
	public Page<DocumentsDTO> getDocumentData(String username, Pageable pageable) {
		
		// create Documents entity object
		 Page<DocumentsDTO> document = docRepo.findByOwnerEmail(username,pageable)
			        .map(doc -> new DocumentsDTO(doc.getOriginalName(), doc.getDocid(),
			        							 doc.getFilePath(), doc.getInsertedOn()));
		
		
		return document;
	}
	
	


	
	// calculate expiry
	 private LocalDateTime calculateExpiry(String expiry) {

	        LocalDateTime now = LocalDateTime.now();

	        switch (expiry) {
	            case "24H":
	                return now.plusHours(24);
	            case "3D":
	                return now.plusDays(3);
	            case "7D":
	                return now.plusDays(7);
	            case "1D":
	                return now.plusDays(1);
	            case "1W":
	                return now.plusWeeks(1);
	            default:
	                return null;   // No expiry
	        }
	    }
	 

	 // generate OTP
	    private Long generateOtp() {
	        return (long) (Math.random() * 900000 + 100000); // 6 digit OTP
	    }
	
	
	// share documents
	@Override
	@CacheEvict(value="docPermissionCache", allEntries = true)
	public DocumentPermissions shareDocuments(Long docid, String shareType, 
								String recieverMail, String accessType, 
								String expiry, String grantedByUsername) {
		
		// get the sender
				User grantedBy = userRepo.findByemail(grantedByUsername)
						.orElseThrow(()-> new IllegalAccessError(" Sender not found"));
		
		
				
		// validate the SecureDocs User
		
		if("SecureDocs".equalsIgnoreCase(shareType))
		{
			 User secureUser = userRepo.findByemail(recieverMail)
		                .orElseThrow(() ->
		                        new IllegalArgumentException("SecureDocs user not found."));
			 
			 // prevent self-sharing
		        if (secureUser.getEmail().equalsIgnoreCase(grantedByUsername)) {
		            throw new IllegalArgumentException("You cannot share document with yourself.");
		        }
		}
		
		
	
		
		// get the Documents
		
		Documents doc = docRepo.findById(docid).orElseThrow(()-> new IllegalArgumentException("Document are Not Found..."));
		
		
		// Create Permission Entry
		DocumentPermissions perm = new DocumentPermissions();
		
		perm.setDocumentId(doc);
		perm.setGrantedToUser(recieverMail);
		perm.setGrantedBy(grantedBy);
		perm.setAccessType(accessType);
		perm.setShareType(shareType);
		perm.setStatus("ACTIVE");
		
		// Expiry calculations
		LocalDateTime expiryTime = calculateExpiry(expiry);
		perm.setExpiryTime(expiryTime);
		
		// set Encrypted Key 
		perm.setDpEncryptedKey(doc.getEncryptedKey());
		
		// set OTP
		if("OTP".equalsIgnoreCase(accessType))
		{
			perm.setDocOtp(generateOtp());
		}
		
		// set password
		if("PASSWORD".equalsIgnoreCase(accessType))
		{
			perm.setDocPass(UUID.randomUUID().toString().substring(0, 8));
		}
		
		if("Link".equalsIgnoreCase(shareType) || "Email".equalsIgnoreCase(shareType) || "SecureDocs".equalsIgnoreCase(shareType))
		{
			String token = UUID.randomUUID().toString();
			perm.setSecureToken(token);
			
		}
		
		// save the data
	    DocumentPermissions permission = docPermission.save(perm);
		
		
		return permission;
	}
	
	
	// calculate the Expiry time
	private String calculateRemainingTime(LocalDateTime expiryTime) {

	    if (expiryTime == null) {
	        return "No Expiry";
	    }
	   
	    
	    LocalDateTime now = LocalDateTime.now();

	    if (expiryTime.isBefore(now)) {
	        return "Expired";
	    }

	    long hours = java.time.Duration.between(now, expiryTime).toHours();
	    long days = hours / 24;
	    long remainingHours = hours % 24;

	    if (days > 0 && remainingHours > 0) {
	        return days + " day " + remainingHours + " hour left";
	    } 
	    else if (days > 0) {
	        return days + " day left";
	    } 
	    else {
	        return remainingHours + " hour left";
	    }
	}

	
	
	
	
	// get Document Permission Data
	@Override
	@Cacheable(value="docPermissionCache", key="#username")
	public Iterable<DocumentPermissionsDTO> getDocumentPermission(String username) {
		
		User user = userRepo.findByemail(username).orElseThrow(()-> new IllegalArgumentException("User Not Found..."));
		
		// get doc permissions
		List<DocumentPermissions> docPerm = docPermission.findByGrantedBy(user);
		
		// Convert Entity → DTO
	    List<DocumentPermissionsDTO> dtoList = new ArrayList<>();

	    for (DocumentPermissions dp : docPerm) {

	        DocumentPermissionsDTO dto = new DocumentPermissionsDTO();

	        dto.setDpid(dp.getDpid());
	        dto.setDocumentId(dp.getDocumentId().getDocid());
	        dto.setDocumentName(dp.getDocumentId().getOriginalName());
	        dto.setGrantedToUser(dp.getGrantedToUser());
	        dto.setGrantedBy(dp.getGrantedBy().getEmail());
	        dto.setExpiryTime(dp.getExpiryTime());
	        dto.setAccessType(dp.getAccessType().toLowerCase());
	        dto.setStatus(dp.getStatus());
	        dto.setInsertedOn(dp.getInsertedOn());
	        
	        
	     //  Auto mark expired
	        if (dp.getExpiryTime() != null && dp.getExpiryTime().isBefore(LocalDateTime.now())
	                && dp.getStatus().equals("ACTIVE")) {

	            dp.setStatus("EXPIRED");
	            docPermission.save(dp);  
	        }

	        dto.setStatus(dp.getStatus());

	        // Remaining time
	        String remaining = calculateRemainingTime(dp.getExpiryTime());
	        dto.setRemainingTime(remaining);

	        dtoList.add(dto);
	    }
		
		
		
		return dtoList;
	}
	
	
	
	// Revoke the Document permissions
	@Override
	@CacheEvict(value="docPermissionCache", allEntries = true)
	public DocumentPermissions revokeDocumentPermission(Long dpid) {
		DocumentPermissions dp = docPermission.findById(dpid)
									.orElseThrow(()-> new IllegalAccessError("Document Not Found..."));
	
		// set the status 
		dp.setStatus("REVOKE");
		
		// set the Encryption Key as null
		dp.setDpEncryptedKey(null);
		
		// set the opt and password as null
	
			dp.setDocPass(null);
		
			dp.setDocOtp(null);
		
		
		// update value  
		
			DocumentPermissions permission = docPermission.save(dp);
		
		return permission;
	}
	
	
	
	// access Document by Link
	@Override
	public DocumentPermissions accessDocByLink(String token) {
		
		return docPermission.findBySecureToken(token).orElseThrow(()-> new IllegalAccessError("Invalid or Expired Link"));
	}
	
	
	
	
	
	
}
