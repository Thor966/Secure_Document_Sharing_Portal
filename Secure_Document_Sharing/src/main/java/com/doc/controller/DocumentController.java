package com.doc.controller;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.doc.repository.DocumentPermissionsRepository;
import com.doc.service.IDocumentsService;
import com.doc.service.IuserService;
import com.doc.util.ConfirmationEmail;
import jakarta.mail.MessagingException;

@RestController
public class DocumentController 
{
	
	@Autowired
	private IDocumentsService docService;
	
	@Autowired
	private IuserService userService;
	
	@Autowired
	private DocumentPermissionsRepository docPermission;
	
	@Autowired
	private ConfirmationEmail emailConfirmation;
	
	
	public UserDTO getLoggedInUser()
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		
		UserDTO user = userService.getByUsername(username);
		
		return user;
	}
	
	// upload the documents
	
	@PostMapping("/uploadDocuments")
	public ResponseEntity<String> saveDocuments(@RequestParam("filePath") MultipartFile filePath)
	{
		// get the service class method
		 docService.saveDocumetsDetails(filePath);
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
	    Path file = Paths.get("C:/Users/Aniket/Maid_For_you_Portal2/Secure_Document_Sharing" + filePath);

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
												 @RequestParam("expiry") String expiry) throws MessagingException
	{
		// get logged in user
		UserDTO user = getLoggedInUser();
		
		// get the service class method
		DocumentPermissions perm = docService.shareDocuments(documentId, shareType, receiverMail, accessType, expiry, user.getEmail());
		
		// Email and SecureDocs Confirmation
		if(shareType.equals("Email") || shareType.equals("SecureDocs"))
		{
			emailConfirmation.sendConfirmationEmail(user,perm);
		}
		
		// Generate Secure Link(Share via secure link)
		if(shareType.equals("Link"))
		{
			// Generate the Secure Link
			String link = "http://localhost:9579/Secure_Document_Sharing/access-document.html?token=" + perm.getSecureToken();
			// return
			return ResponseEntity.ok(link);
			
		}
		
		String msg = "Document Share Successfully...";
		
		
		return new ResponseEntity<String>(msg,HttpStatus.OK);
		
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
		
		System.out.println("Access token received: " + token);

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
	        if (!value.equals(String.valueOf(dp.getDocOtp()))) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                    .body(Map.of("error", "Invalid OTP"));
	        }
	    }

	    // Password verification
	    if (dp.getAccessType().equals("PASSWORD")) {
	        if (!value.equals(dp.getDocPass())) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                    .body(Map.of("error", "Invalid password"));
	        }
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
	  

	    return ResponseEntity.ok(response);
	}


	
	
	// download the doc by token
	@GetMapping("/download/{token}")
	public ResponseEntity<Resource> downloadDocument(@PathVariable String token) throws Exception {


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


	    // Build physical file path
	    String filePath = dp.getDocumentId().getFilePath();

	    Path file = Paths.get("C:/Users/Aniket/Maid_For_you_Portal2/Secure_Document_Sharing" + filePath);
	    Resource resource = new UrlResource(file.toUri());

	    if (!resource.exists() || !resource.isReadable()) {
	        return ResponseEntity.notFound().build();
	    }

	    String fileName = file.getFileName().toString();

	    return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION,
	                    "attachment; filename=\"" + fileName + "\"")
	            .body(resource);
	}

	
	
	
	
	
	
	
	


}
