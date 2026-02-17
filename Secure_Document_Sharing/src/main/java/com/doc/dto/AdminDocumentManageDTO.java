package com.doc.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AdminDocumentManageDTO
{
	
	
	private Long dpid;
	private String documentName;
	private String owner;
	private String accessType;
	private String status;
	private Double size;
	private LocalDateTime expiry;
	private Long shares;
	private LocalDateTime uploaded;

}
