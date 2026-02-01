package com.doc.entity;

import java.io.Serializable;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Data;

@Entity
@Table(name="Documents")
@Data
public class Documents implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@SequenceGenerator(name="doc", sequenceName = "doc_seq", initialValue = 1, allocationSize = 1)
	@GeneratedValue(generator = "doc", strategy = GenerationType.IDENTITY)
	private Long docid;
	
	@Column(nullable = false, unique = true, updatable = false)
	private UUID publicDocId;
	
	private String originalName;
	
	private String storedName;
	
	private String filePath;
	
	private Long fileSize;
	
	private String mimeType;
	
	@ManyToOne()
	@JoinColumn(name="owner_id")
	private User owner;
	
	private String encryptedKey;
	
	
	// Extra properties
	
	@Version
	private Integer updatedCount;
	
	@Column(updatable = false)
	@CreationTimestamp
	private LocalDateTime insertedOn;
	
	@Column(insertable = false)
	@UpdateTimestamp
	private LocalDateTime updatedOn;
	
	
	
	
	
	@PrePersist
	void generatePublicId() {
	    this.publicDocId = UUID.randomUUID();
	}

}
