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
	
	
	@GetMapping("/")
	public String home()
	{
		return"index";
	}
	
	@GetMapping("/login")
	public String login(@RequestParam(value = "error", required = false) String error,
            Map<String, Object> map)
	{
		
		if (error != null) {
            map.put("loginError", "Invalid Username and Password");
        }

		
		return"login";
	}

	
	@GetMapping("/register")
	public String register()
	{
		return"register";
	}
	
	@GetMapping("/dashboard")
	public String dashboard()
	{
		
		return"dashboard";
	}
	
	
	@GetMapping("/mydocuments")
	public String myDocuments()
	{
		return "mydocument";
	}
	
	
	@GetMapping("/managedocuments")
	public String manageDocuments()
	{
		return"managedocuments";
	}
	
	@GetMapping("/accessdocument")
	public String accessDocument()
	{
		return "access_document";
	}
	
	
	@GetMapping("/document-preview")
	public String docPreview()
	{
		return "document_preview";
	}
}
