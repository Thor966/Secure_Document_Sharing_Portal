package com.doc.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.doc.service.IForgotPasswordService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;

@RestController
public class ForgotPasswordController 
{
	
	@Autowired
	private IForgotPasswordService forgotService;
	
	
	
	// forgot password api
	@PostMapping("/forgot-password")
	public ResponseEntity<?> processForgotPassword(
	        @RequestBody Map<String, String> request,HttpSession session)
	        throws MessagingException {

	    Map<String, Object> response = new HashMap<>();

	    String email = request.get("email");

	    if (email == null || email.isBlank()) {
	        response.put("error", "Email is required.");
	        return ResponseEntity.badRequest().body(response);
	    }

	    forgotService.sendOTPByEmail(email);
	    
	 // Store email in session
	   session.setAttribute("RESET_EMAIL", email);

	    response.put("message", "If the email is registered, an OTP has been sent.");
	    response.put("next", "/Secure_Document_Sharing/otp-verify");

	    return ResponseEntity.ok(response);
	}
	
	
	
	
	// verify the otp
	@PostMapping("/verify-otp")
	public ResponseEntity<?> verifyOtp(
	        @RequestBody Map<String, String> request,
	        HttpSession session) {
		
		 Map<String, Object> response = new HashMap<>();

	    String email = (String) session.getAttribute("RESET_EMAIL");

	    if (email == null) {
	        return ResponseEntity.badRequest()
	                .body("Session expired. Please try again.");
	    }

	    String otp = request.get("otp");

	    boolean valid = forgotService.verifyOtp(email, otp);

	    if (!valid) {
	        return ResponseEntity.badRequest()
	                .body("Invalid OTP");
	    }
	    
	 // Mark OTP verified in session
	    session.setAttribute("OTP_VERIFIED", true);

	    response.put("next", "/Secure_Document_Sharing/reset-passwordPage");
	    response.put("message", "OTP verified");

	    return ResponseEntity.ok(response);
	}
	
	
	
	// reset password
	 @PostMapping("/reset-password")
	    public ResponseEntity<?> resetPassword(
	            @RequestBody Map<String, String> request,
	            HttpSession session) {

	        String email = (String) session.getAttribute("RESET_EMAIL");
	        Boolean verified = (Boolean) session.getAttribute("OTP_VERIFIED");

	        if (email == null || verified == null || !verified) {
	            return ResponseEntity.badRequest()
	                    .body("Unauthorized request");
	        }

	        String newPassword = request.get("password");

	        if (newPassword == null || newPassword.length() < 6) {
	            return ResponseEntity.badRequest()
	                    .body("Password must be at least 6 characters");
	        }

	        forgotService.updatePassword(email, newPassword);

	        // Destroy session after successful reset
	        session.invalidate();

	        return ResponseEntity.ok("Password reset successful");
	    }
	
	
	

}
