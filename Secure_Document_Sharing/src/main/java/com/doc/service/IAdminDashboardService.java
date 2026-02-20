package com.doc.service;

import java.util.List;
import java.util.Map;

import com.doc.dto.AdminDTO;
import com.doc.dto.StorageUsageDTO;

public interface IAdminDashboardService 
{
	
	// get admin by username
	public AdminDTO getAdminByUsername(String username);
	
	// get the total register user count
	public Long getTotalUsersCount();
	
	// get the dayswise register user count
	public Long getTodaysRegisterCount();
	
	
	// get the total uploaded documents
	public Long getTotalUploadedDocument();
	
	
	
	// get the daywise uploaded document count
	public Long getDaywiseUploadedCount();
	
	
	// get the total active link shares
	public Long getTotalActiveShares();
	
	
	// get the active user count
	public Integer getOnlineUsersCount();
	
	// get the disabled user count
	public Long getDisabledUserCount();
	
	
	// get the daywise expired Document count
	public Long getDaywiseExpiredDocCount();
	
	// get the force revoked doc count
	public Long getForceRevokeCount();
	
	// get OTP access type links count
	public Long getOtpAccessTypeCount();
	
	// get the password access type count
	public Long getPasswordAccessTypeCount();
	
	// get the public access type count
	public Long getPublicAccessTypeCount();
	
	// get the storage details
	public Map<String,Object> getStorateDetails();
	
	// get the storage per user
	public  List<StorageUsageDTO> getStoragePerUser();
	

}
