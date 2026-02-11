package com.doc.service;


public interface IAdminDashboardService 
{
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

}
