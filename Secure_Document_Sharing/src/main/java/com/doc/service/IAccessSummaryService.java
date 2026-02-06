package com.doc.service;

public interface IAccessSummaryService

{
	
	// get the OTP Based Documents Count
	public Long getOTPBasedDocuments(String username);
	
	// get the Password Based Document Count
	public Long getPassBasedDocuments(String username);
	
	// get the Expired Document Count
	public Long getExpiredDocumentCount(String username);

}
