package com.doc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import com.doc.service.DefaultAdminRegistrationService;


@Controller
public class DefaultAdminRegistrationController implements CommandLineRunner
{
	
	 @Autowired
	    private DefaultAdminRegistrationService service;

	    @Override
	    public void run(String... args) throws Exception {
	        
	        
	        service.saveAdminData();
	        
	        
	    }

}
