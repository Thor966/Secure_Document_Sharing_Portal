package com.doc.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class GlobalAuditLogsDTO
{
	
	private LocalDateTime timestamp;
	private String username;
	private String documentName;
	private String status;
	private String action;
	private String source;

}
