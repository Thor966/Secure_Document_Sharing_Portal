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
	
	
	// filter by both keyword and status
	public Page<AdminUserManageDTO> filterByKeywordAndStatus(String keyword, String status, Pageable pageable);
	
	// filter by only keyword
	public Page<AdminUserManageDTO> filterByKeyword(String keyword, Pageable pageable);
	
	// filter by only status
	public Page<AdminUserManageDTO> filterByStatus(String status, Pageable pageable);
	
	
	// update the user status
	public void updateUserStatus(Long uid, String status);

}
