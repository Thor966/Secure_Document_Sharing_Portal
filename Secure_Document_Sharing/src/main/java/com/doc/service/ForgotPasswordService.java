package com.doc.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.doc.entity.AuthandAutho;
import com.doc.entity.PasswordResetOtp;
import com.doc.entity.User;
import com.doc.repository.AuthRepository;
import com.doc.repository.PasswordResetOTPByEmail;
import com.doc.repository.UserRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class ForgotPasswordService implements IForgotPasswordService
{
	
	private static final int MAX_ATTEMPTS = 3;
	
	@Autowired
	 private PasswordResetOTPByEmail resetRepo;
	
	@Autowired
	 private UserRepository userRepo;
	
	@Autowired
	private AuthRepository authRepo;
	
	@Autowired
	private JavaMailSender sender;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	
	// send the otp by email
	@Override
	public boolean sendOTPByEmail(String email) throws MessagingException {
		
		// get the user data 
		User user = userRepo.findByemail(email).orElseThrow(()-> new IllegalAccessError("User Data Not Found!"));
		
		if(user == null)
		{
			return false;
		}
		
		// get the otp 
		String otp = generateOtp();
		
		String hashedOtp = encoder.encode(otp);
		
		// set these data into db
		PasswordResetOtp resetOtp = new PasswordResetOtp();
		
		resetOtp.setEmail(email);
		resetOtp.setOtp(hashedOtp);
		resetOtp.setAttempts(0);
		resetOtp.setExpiryTime(LocalDateTime.now().plusMinutes(5));
		resetOtp.setUsed(false);
		
		// save these data
		resetRepo.save(resetOtp);
		
		// send email
		sendEmail(email, otp, user);
		
		return true;
	}
	
	
	// generate the 6 digit otp
	private String generateOtp() {
        return String.valueOf(
                100000 + new java.security.SecureRandom().nextInt(900000)
        );
    }
	
	
	// send email to user with otp
	private void sendEmail(String email, String otp, User user) throws MessagingException {

		String msg =
				"Dear "+ user.getFirstName()+",\n\n"+

				"We received a request to reset your SecureDocs account password.\n\n"+

				"Your One-Time Password (OTP) is: " + otp + ".\n\n"+


				"This OTP is valid for 5 minutes.\n\n"+

				"If you did not request a password reset, please ignore this email.\n"+ 
				"For security reasons, do not share this code with anyone.\n\n"+

				"Regards,\n"+
				"SecureDocs Security Team";
		
		String subject = "SecureDocs | Password Reset Verification Code";
       
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setTo(email);
		helper.setSubject(subject);
		helper.setSentDate(new Date());
		helper.setText(msg);

        sender.send(message);
    }
	
	
	
	// verify OTP 
	 @Override
	    public boolean verifyOtp(String email, String enteredOtp) {

	        // Get latest unused OTP
	        Optional<PasswordResetOtp> optionalOtp =
	        		resetRepo.findTopByEmailAndIsUsedFalseOrderByInsertedOnDesc(email);

	        if (optionalOtp.isEmpty()) {
	            return false;
	        }

	        PasswordResetOtp otpEntity = optionalOtp.get();
	        
	        
	        // Check expiry
	        if (otpEntity.getExpiryTime().isBefore(LocalDateTime.now())) {
	            return false;
	        }

	        // Check max attempts
	        if (otpEntity.getAttempts() >= MAX_ATTEMPTS) {
	            return false;
	        }

	        // Check OTP match (BCrypt compare)
	        boolean matches = encoder.matches(enteredOtp, otpEntity.getOtp());

	        if (!matches) {
	            otpEntity.setAttempts(otpEntity.getAttempts() + 1);
	            resetRepo.save(otpEntity);
	            return false;
	        }

	        // Success → mark as used
	        otpEntity.setUsed(true);
	        resetRepo.save(otpEntity);

	        return true;
	    }
	 
	 
	 
	 
	 // reset user password
	 @Override
	 public void updatePassword(String email, String newPassword) {

	     User user = userRepo.findByemail(email).orElseThrow(()-> new IllegalArgumentException("User Not Found!"));
	     AuthandAutho auth = authRepo.findByUsername(email).orElseThrow(()-> new IllegalArgumentException("User Not Found"));

	     String encodedPass = encoder.encode(newPassword);
	     
	     user.setPassword(encodedPass);
	     auth.setPassword(encodedPass);
	     
	     
	     // update passwords
	     userRepo.save(user);
	     authRepo.save(auth);
	     
	 }
	 
	 

}
