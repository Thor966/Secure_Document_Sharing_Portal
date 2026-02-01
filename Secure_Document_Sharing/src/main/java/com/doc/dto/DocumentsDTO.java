package com.doc.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DocumentsDTO
{

	private Long docid; 
	private String originalName;
	private String storedName;
	private String filePath;
	private Long fileSize;
	private String mimeType;
	private String encryptedKey;
	private LocalDateTime insertedOn;
	
	
	 public DocumentsDTO(String storedName,Long docid, String filePath, LocalDateTime insertedOn) {
	        this.storedName = storedName;
	        
	        this.docid = docid;
	        
	        this.filePath = filePath;
	        
	        this.insertedOn = insertedOn;
	    }
}
