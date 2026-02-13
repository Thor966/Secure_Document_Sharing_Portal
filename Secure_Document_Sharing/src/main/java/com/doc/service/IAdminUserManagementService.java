package com.doc.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.doc.dto.AdminUserManageDTO;

public interface IAdminUserManagementService 
{
	
	
	
	// get All Users Data
	Page<AdminUserManageDTO> getAllUsersData(Pageable pageable);
	
	// get the disabled user count
	Long getDisabledUserCount();

}
