package com.doc.controller;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.doc.dto.DocumentPermissionsDTO;
import com.doc.dto.DocumentsDTO;
import com.doc.dto.UserDTO;
import com.doc.entity.DocumentPermissions;
import com.doc.entity.ManageAction;
import com.doc.entity.User;
import com.doc.repository.DocumentPermissionsRepository;
import com.doc.repository.UserRepository;
import com.doc.service.IAuditLogsService;
import com.doc.service.IDocumentsService;
import com.doc.service.IuserService;
import com.doc.util.ConfirmationEmail;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class DocumentController 
{
	
	@Autowired
	private IDocumentsService docService;
	
	@Autowired
	private IuserService userService;
	
	@Autowired
	private UserRepository userRepo;
	
	
	@Autowired
	private DocumentPermissionsRepository docPermission;
	
	@Autowired
	private ConfirmationEmail emailConfirmation;
	
	@Autowired
	private IAuditLogsService logService;
	
	
	 @Value("${upload.path}")
	  private String uploadDir;
	
	
	 // get the loggedin user
	public UserDTO getLoggedInUser()
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		
		UserDTO user = userService.getByUsername(username);
		
		return user;
	}
	
	// upload the documents
	
	@PostMapping("/uploadDocuments")
	public ResponseEntity<String> saveDocuments(@RequestParam("filePath") MultipartFile filePath, String username)
	{
		// get the logged in user
		UserDTO user = getLoggedInUser();
		
		// get the service class method
		 docService.saveDocumetsDetails(filePath, user.getEmail());
		String msg = "Document Added Successfully...";
		return new ResponseEntity<String>(msg,HttpStatus.OK);
	}
	
	
	
	
	
	// fetch the documents
	@GetMapping("/fetchDocumentDetails")
	public ResponseEntity<Page<DocumentsDTO>> fetchDocumentName(
											@PageableDefault(page=0, size=10, sort="insertedOn", direction=Direction.ASC)
											Pageable pageable)
	{
		// get the username 
		UserDTO user = getLoggedInUser();
	
		String userName = user.getEmail();
		
		// get the service class method 
		Page<DocumentsDTO> doc =  docService.getDocumentData(userName, pageable);
		
		return new ResponseEntity<Page<DocumentsDTO>>(doc,HttpStatus.OK);
	}
	
	
	
	
	// download documents
	@GetMapping("/download")
	public ResponseEntity<Resource> downloadDocuments(@RequestParam("path") String filePath) throws Exception
	{
		// get the logged in user
		
		UserDTO user = getLoggedInUser();
		
		
		// Build physical file path
		 String storedName = Paths.get(filePath).getFileName().toString();

		 Path file = Paths.get(uploadDir).resolve(storedName);
	    
	    
	    Resource resource = new UrlResource(file.toUri());

	    if (!resource.exists() || !resource.isReadable()) {
	        return ResponseEntity.notFound().build();
	    }

	    String fileName = file.getFileName().toString();
	    
	    
	    return ResponseEntity.ok()
	            .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
	            .body(resource);
		
	}
	
	
	// Share documents
	@PostMapping("/shareDocument")
	public ResponseEntity<?> shareDocuments(@RequestParam("documentId") Long documentId,
												 @RequestParam("shareType") String shareType,
												 @RequestParam("receiverMail") String receiverMail,
												 @RequestParam("accessType") String accessType,
												 @RequestParam("expiry") String expiry, HttpServletRequest req) throws MessagingException
	{
		
		try
		{
		
		// get logged in user
		UserDTO user = getLoggedInUser();
		
		// get the service class method
		DocumentPermissions perm = docService.shareDocuments(documentId, shareType, receiverMail, accessType, expiry, user.getEmail());
		
		// Email and SecureDocs Confirmation
		if("Email".equalsIgnoreCase(shareType)|| "SecureDocs".equalsIgnoreCase(shareType))
		{
			emailConfirmation.sendConfirmationEmail(user,perm);
		}
		
		
		
		// Generate Secure Link(Share via secure link)
		if("Link".equalsIgnoreCase(shareType))
		{
			// Generate the Secure Link
			
			String baseUrl = req.getScheme() + "://" +
                    req.getServerName() + ":" +
                    req.getServerPort() +
                    req.getContextPath();
			
			String link = baseUrl + "/access-document.html?token=" + perm.getSecureToken();
			
			 Map<String, Object> response = new HashMap<>();
			    response.put("link", link);
			    response.put("accessType", accessType);

			    if ("OTP".equals(accessType)) {
			        response.put("otp", perm.getDocOtp());
			    }

			    if ("PASSWORD".equals(accessType)) {
			        response.put("password", perm.getDocPass()); 
			    }

			    // return response
			    return ResponseEntity.ok(response);
			
		}
		
		
		
		String msg = "Document Share Successfully...";
		
		return new ResponseEntity<String>(msg,HttpStatus.OK);
		
		
		}
		catch(IllegalArgumentException iae)
		{
			return ResponseEntity
	                .badRequest()
	                .body(Map.of("error", iae.getMessage()));
		}
		
		
		
	}
	
	
	
	
	// get Document permissionData
	@GetMapping("/docPermission")
	public ResponseEntity<Iterable<DocumentPermissionsDTO>> fetchDocumentPermissionData()
	{
		
		// get the logged in user
		UserDTO user = getLoggedInUser();
		
		String username = user.getEmail();
		// get the service class method
		Iterable<DocumentPermissionsDTO> docPerm = docService.getDocumentPermission(username);
		
		return new ResponseEntity<Iterable<DocumentPermissionsDTO>>(docPerm,HttpStatus.OK);
		
	}
	
	
	
	
	// Revoke the Document Permissions
	@PostMapping("/revokePermission/{dpid}")
	public ResponseEntity<String> revokeDocPermissions(@PathVariable("dpid") Long dpid) throws MessagingException
	{
		
		// get the logged in user
		UserDTO user = getLoggedInUser();
		
		// get the service class method
		DocumentPermissions perm = docService.revokeDocumentPermission(dpid);
		
		// revoke confirmation Email
		emailConfirmation.revokeConfirmationEmail(user, perm);
		
		String msg = "Document are Revoked Successfully!";
		
		return new ResponseEntity<String>(msg, HttpStatus.OK);
	}
	
	
	
	
	// get the document access through Link
	@GetMapping("/docaccess/{token}")
	public ResponseEntity<?> accessDocumentByLink(@PathVariable("token") String token)
	{

		// get the document by token
		DocumentPermissions dp =  docService.accessDocByLink(token);
		
		
		
		//  Status check
	    if (!dp.getStatus().equals("ACTIVE")) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN)
	                .body(Map.of("error", "This link is no longer active"));
	    }

	    //  Expiry check
	    if (dp.getExpiryTime() != null && dp.getExpiryTime().isBefore(LocalDateTime.now())) {

	        dp.setStatus("EXPIRED");
	        docPermission.save(dp);

	        return ResponseEntity.status(HttpStatus.FORBIDDEN)
	                .body(Map.of("error", "This link has expired"));
	    }

	    //  Response based on access type
	    Map<String, Object> response = new HashMap<>();
	    response.put("documentName", dp.getDocumentId().getOriginalName());
	    response.put("accessType", dp.getAccessType());
	    response.put("token", token);
	    
	    
	    if (dp.getAccessType().equals("NO ACCESS")) {
	        response.put("next", "/Secure_Document_Sharing/document-preview?token=" + token);
	    }
	    
	    
	    return ResponseEntity.ok(response);
		
		
	}
	
	
	
	// Verify the access
	@PostMapping("/docaccess/verify")
	public ResponseEntity<?> verifyAccess(@RequestBody Map<String, String> req) {

	    String token = req.get("token");
	    String value = req.get("value");   // otp or password

	    DocumentPermissions dp = docService.accessDocByLink(token);
	           

	    //  Status check
	    if (!dp.getStatus().equals("ACTIVE")) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN)
	                .body(Map.of("error", "Access revoked"));
	    }

	    // Expiry check
	    if (dp.getExpiryTime() != null && dp.getExpiryTime().isBefore(LocalDateTime.now())) {
	        dp.setStatus("EXPIRED");
	        docPermission.save(dp);
	        return ResponseEntity.status(HttpStatus.FORBIDDEN)
	                .body(Map.of("error", "Link expired"));
	    }

	    //  OTP verification
	    if (dp.getAccessType().equals("OTP")) {
	    	
	    	// check OTP verification
	        if (!value.equals(String.valueOf(dp.getDocOtp()))) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                    .body(Map.of("error", "Invalid OTP"));
	        }
	        
	        
	        // set the audit logs for OTP
	        logService.logAction(dp.getGrantedBy().getUid(), dp.getDocumentId().getDocid(), dp.getDpid(), ManageAction.OTP);
	    }

	    
	    
	    // Password verification
	    if (dp.getAccessType().equals("PASSWORD")) {
	    	
	    	// check password validity
	        if (!value.equals(dp.getDocPass())) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                    .body(Map.of("error", "Invalid password"));
	        }
	        
	        
	        // set the audit logs for PASSWORD
	        logService.logAction(dp.getGrantedBy().getUid(), dp.getDocumentId().getDocid(), dp.getDpid(), ManageAction.PASSWORD);
	    }

	    //  Verified → redirect to preview page
	    return ResponseEntity.ok(Map.of(
	            "message", "Access granted",
	            "previewUrl", "/Secure_Document_Sharing/document-preview?token=" + token
	    ));
	}


	
	
	
	// Preview Doc via Email
	@GetMapping("/api/preview/{token}")
	public ResponseEntity<?> previewDocument(@PathVariable String token) throws Exception
	{

		// get the service class method
	    DocumentPermissions dp = docService.accessDocByLink(token);

	    if (!"ACTIVE".equals(dp.getStatus())) {
	        throw new RuntimeException("Access revoked");
	    }

	    if (dp.getExpiryTime() != null && dp.getExpiryTime().isBefore(LocalDateTime.now())) {
	        dp.setStatus("EXPIRED");
	        docPermission.save(dp);
	        throw new RuntimeException("Link expired");
	    }

	    //  Response based on access type
	    Map<String, Object> response = new HashMap<>();
	    response.put("documentName", dp.getDocumentId().getOriginalName());
	    response.put("grantedBy", dp.getGrantedBy().getEmail());
	    response.put("expiry", dp.getExpiryTime());
	    response.put("filePath", dp.getDocumentId().getFilePath());
	  
	    
	    //set the audit logs for view
	    logService.logAction(dp.getGrantedBy().getUid(), dp.getDocumentId().getDocid(), dp.getDpid(), ManageAction.VIEW);

	    return ResponseEntity.ok(response);
	}


	
	
	// download the doc by token
	@GetMapping("/secure-download/{token}")
	public ResponseEntity<Resource> downloadDocument(@PathVariable String token) throws Exception 
	{
	    // Get permission by token
	    DocumentPermissions dp = docService.accessDocByLink(token);

	    if (dp == null) {
	        return ResponseEntity.notFound().build();
	    }

	    // Check status
	    if (!"ACTIVE".equals(dp.getStatus())) {
	        throw new RuntimeException("Access revoked");
	    }

	    // Check expiry
	    if (dp.getExpiryTime() != null &&
	        dp.getExpiryTime().isBefore(LocalDateTime.now())) {

	        dp.setStatus("EXPIRED");
	        docPermission.save(dp);
	        throw new RuntimeException("Link expired");
	    }


	    String storedName = dp.getDocumentId().getStoredName();

	    Path file = Paths.get(uploadDir).resolve(storedName);

	    Resource resource = new UrlResource(file.toUri());

	    if (!resource.exists() || !resource.isReadable()) {
	        return ResponseEntity.notFound().build();
	    }

	    String fileName = file.getFileName().toString();
	    
	    
	    //set the audit logs for view
	    logService.logAction(dp.getGrantedBy().getUid(), dp.getDocumentId().getDocid(), dp.getDpid(), ManageAction.DOWNLOAD);

	    return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION,
	                    "attachment; filename=\"" + fileName + "\"")
	            .body(resource);
	}

	
	
	// fetch the document for the Secure Doc user (Shared Documents)
	
	@GetMapping("/fetchSecureDoc")
	public ResponseEntity<?> fetchShareWithMeDoc(@PageableDefault(page=0, size=10, sort="insertedOn", direction=Direction.ASC) Pageable pageable)
	{
		
		// get the first logged in user
		UserDTO user = getLoggedInUser();
	
		// get the service class method
		Page<DocumentPermissionsDTO> secureDoc = docService.getShareWithMeData(user.getEmail(), "SecureDocs", pageable);
		
		return  ResponseEntity.ok(secureDoc);
		
	}
	
	
	
	
	


}
