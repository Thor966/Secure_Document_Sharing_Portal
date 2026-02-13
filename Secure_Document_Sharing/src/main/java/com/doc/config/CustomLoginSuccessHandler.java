package com.doc.config;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.doc.entity.User;
import com.doc.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
	
	
	  @Autowired
	    private UserRepository userRepo;

		

	  @Override
	  public void onAuthenticationSuccess(HttpServletRequest request,
	                                      HttpServletResponse response,
	                                      Authentication authentication)
	          throws IOException {

	      String email = authentication.getName();

	      User user = userRepo.findByemail(email).orElseThrow();

	      user.setLastLogin(LocalDateTime.now());
	      userRepo.save(user);

	      boolean isAdmin = authentication.getAuthorities()
	              .stream()
	              .anyMatch(a -> a.getAuthority().equals("ADMIN"));

	      if (isAdmin) {
	          response.sendRedirect("/Secure_Document_Sharing/adminDashboard");
	      } else {
	          response.sendRedirect("/Secure_Document_Sharing/dashboard");
	      }
	  }

	}


