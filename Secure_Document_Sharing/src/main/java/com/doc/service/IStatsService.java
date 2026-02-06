package com.doc.service;

public interface IStatsService 
{
	
	// get the total Document count by username
	public Long totalDocumentCount(String username);
	
	// get the shared documents count
	public Long sharedDocumentCount(String username);
	
	// get the active link count
	public Long getActiveLinkCount(String username);

}
