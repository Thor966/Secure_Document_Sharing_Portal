package com.doc.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.doc.entity.User;
import com.doc.service.IuserService;

@Controller
public class PageLoader
{
	
	@Autowired
	private IuserService userService;
	
	
	// launch home page
	
	@GetMapping("/")
	public String home()
	{
		return"index";
	}
	
	
	// launch login page
	
	@GetMapping("/login")
	public String login(@RequestParam(value = "error", required = false) String error,
            Map<String, Object> map)
	{
		
		if (error != null) {
            map.put("loginError", "Invalid Username and Password");
        }

		
		return"login";
	}

	
	
	// launch register page
	@GetMapping("/register")
	public String register()
	{
		return"register";
	}
	
	
	
	// launch dashboard page
	@GetMapping("/dashboard")
	public String dashboard()
	{
		
		return"dashboard";
	}
	
	
	// launch my documents page
	@GetMapping("/mydocuments")
	public String myDocuments()
	{
		return "mydocument";
	}
	
	
	
	// launch manage documents page
	@GetMapping("/managedocuments")
	public String manageDocuments()
	{
		return"managedocuments";
	}
	
	
	
	// launch access document page
	@GetMapping("/accessdocument")
	public String accessDocument()
	{
		return "access_document";
	}
	
	
	
	// launch document-preview page
	@GetMapping("/document-preview")
	public String docPreview()
	{
		return "document_preview";
	}
	
	
	
	// launch share-with-me page
	@GetMapping("/share-with-me")
	public String shareWithMe()
	{
		return "share_with_me";
	}
	
	
	
	// launch security activity page
	@GetMapping("/securityActivity")
	public String securityActivity()
	{
		return "security_activity";
	}
	
	
	// launch privacy page
	@GetMapping("/privacyPolicy")
	public String privacyPage()
	{
		return "privacy";
	}
	
	
	// launch terms page
	@GetMapping("/terms")
	public String termsPage()
	{
		return "terms";
	}
	
	
	// launch security policy page
	@GetMapping("/securityPolicy")
	public String securityPage()
	{
		return "security";
	}
	
	
	
	// launching Admin Dashboard
	@GetMapping("/adminDashboard")
	public String manageAdminDashboard()
	{
		return "admin/admin_dashboard";
	}
	
	
	// launch Admin users page
	@GetMapping("/manageUser")
	public String manageAdminUsers()
	{
		return "admin/admin_user";
	}
	
	
	// launch Admin documents page
	@GetMapping("/manageDocument")
	public String manageAdminDocuments()
	{
		return "admin/admin_document";
	}
	
	
	// launch Admin security logs
	@GetMapping("/manageSecurityLogs")
	public String manageAdminSecurityLogs()
	{
		return "admin/admin_security_logs";
	}
	
	
}
