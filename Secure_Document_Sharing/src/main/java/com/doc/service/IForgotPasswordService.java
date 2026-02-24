package com.doc.service;

import jakarta.mail.MessagingException;

public interface IForgotPasswordService 
{
	
	// send otp via email
	public boolean sendOTPByEmail(String email) throws MessagingException;
	// verify otp
	public boolean verifyOtp(String email, String enteredOtp);

	// reset password
	public void updatePassword(String email, String newPassword);
}
