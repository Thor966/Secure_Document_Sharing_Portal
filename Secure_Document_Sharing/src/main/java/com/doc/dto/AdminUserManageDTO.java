package com.doc.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AdminUserManageDTO
{
	
	private Long uid;
	private String firstName;
	private String lastName;
	private String email;
	private String status;
	private LocalDateTime lastLogin;
	private LocalDateTime joinOn;
	private Double storageUsed;
	
	

}
