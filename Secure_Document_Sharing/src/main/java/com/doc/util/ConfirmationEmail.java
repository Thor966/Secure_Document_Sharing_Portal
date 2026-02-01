package com.doc.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.doc.dto.UserDTO;
import com.doc.entity.DocumentPermissions;
import com.doc.repository.DocumentPermissionsRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class ConfirmationEmail
{
	
	@Autowired
	private JavaMailSender sender;
	
	@Autowired
	private DocumentPermissionsRepository docPermission;
	
	
	
	
	// send Share Confirmation Email to the User
	public void sendConfirmationEmail(UserDTO user , DocumentPermissions permission) throws MessagingException
	{
		
		
		
		// Email Message
		
		String msg = "Dear " + permission.getGrantedToUser() + ",\n\n" +
	             "A document has been securely shared with you on SecureDocs.\n\n" +
	             "Document Sharing Details:\n" +
	             "----------------------------------------\n" +
	             "Document Name : " + permission.getDocumentId().getOriginalName() + "\n" +" "+
	             "Shared By     : " + permission.getGrantedBy().getFirstName() + permission.getGrantedBy().getLastName() 
	             						+ " (" + permission.getGrantedBy().getEmail() + ")\n" +
	             
	             "Access Type   : " + permission.getAccessType() + "\n" +
	             "Share Method  : " + permission.getShareType() + "\n" +
	             "Expiry        : " + (permission.getExpiryTime() != null 
	                                   ? permission.getExpiryTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")) 
	                                   : "No Expiry") + "\n" +
	             "----------------------------------------\n\n" +
	             "You can now access this document securely using your SecureDocs account.\n\n";


	// If OTP based access
	if (permission.getAccessType().equals("OTP")) {
	    msg += "Access OTP : " + permission.getDocOtp() + "\n\n" +
	           "Please do not share this OTP with anyone.\n\n";
	}

	// If password based access
	if (permission.getAccessType().equals("PASSWORD")) {
	    msg += "Document Password : " + permission.getDocPass() + "\n\n" +
	           "Please keep this password confidential.\n\n";
	}

	// generate the SecureLink
	String secureLink = "http://localhost:9579/Secure_Document_Sharing/access-document.html?token=" + permission.getSecureToken();
	
	
	// If Email sharing
	if (permission.getShareType().equals("Email"))
	{
			
	    msg += "Secure Access Link:\n" +
	           secureLink + "\n\n";
	}
	
	// If SecureDocs sharing
		if (permission.getShareType().equals("SecureDocs"))
		{
				
		    msg += "Secure Access Link:\n" +
		           secureLink + "\n\n";
		}

	msg += "If you face any issues while accessing the document, please contact the sender or our support team.\n\n" +
	       "Thank you for using SecureDocs.\n\n" +
	       "Regards,\n" +
	       "SecureDocs Team\n" +
	       "support@securedocs.com";
	
	
	// Reciever email
	String recieverEmail = permission.getGrantedToUser();
	
	// Email Subject
	String subject = "SecureDocs | Document Shared With You - " + permission.getDocumentId().getOriginalName();

	
	// Create the Mimetype Object for Sending the Email
	MimeMessage message = sender.createMimeMessage();
	MimeMessageHelper helper = new MimeMessageHelper(message);
	
	helper.setTo(recieverEmail);
	helper.setSubject(subject);
	helper.setSentDate(new Date());
	helper.setText(msg);
	
	// send the message
	sender.send(message);

	}
	
	
	
	
	
	// Revoke Confirmation Email
	public void revokeConfirmationEmail(UserDTO user, DocumentPermissions revokePermission) throws MessagingException
	{
		// Email message
		
		
		String revokeMsg = "Dear " + revokePermission.getGrantedToUser() + ",\n\n" +
	             "Access to a document shared with you on SecureDocs has been revoked by the owner.\n\n" +
	             "Revoked Document Details:\n" +
	             "----------------------------------------\n" +
	             "Document Name : " + revokePermission.getDocumentId().getOriginalName() + "\n" +
	             "Revoked By    : " + revokePermission.getGrantedBy().getFirstName() + " " 
	             						+ revokePermission.getGrantedBy().getLastName()
	             						+ " (" + revokePermission.getGrantedBy().getEmail() + ")\n" +
	             						
	             "Revoked On    : " + LocalDateTime.now()
	                                    .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")) + "\n" +
	             "----------------------------------------\n\n" +
	             "You will no longer be able to access this document using the previous link or credentials.\n\n" +
	             "If you believe this was done by mistake, please contact the document owner.\n\n" +
	             "Regards,\n" +
	             "SecureDocs Team";
		
		
		
		//Reciever Email
		String revokeRecieverEmail = revokePermission.getGrantedToUser(); 
		
		// Email Subject
		String subject = "SecureDocs | Document Revoked - " + revokePermission.getDocumentId().getOriginalName();
		
		// create Mimetype object
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setTo(revokeRecieverEmail);
		helper.setSubject(subject);
		helper.setSentDate(new Date());
		helper.setText(revokeMsg);
		
		// send the message
		sender.send(message);

	}
	
	

}
