package com.doc.dto;

public class DocumentStorageProjection 
{
	
	private Long docId;
    private Long totalStorage;

    public DocumentStorageProjection(Long docId, Long totalStorage) {
        this.docId = docId;
        this.totalStorage = totalStorage;
    }

    public Long getdocId() {
        return docId;
    }

    public Long getTotalStorage() {
        return totalStorage;
    }

}
