package com.doc.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Data;

@Entity
@Table(name="audit_logs")
@Data
public class AuditLogs implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@SequenceGenerator(name="audit", sequenceName="audit_seq", initialValue = 1, allocationSize = 1)
	@GeneratedValue(generator = "audit", strategy = GenerationType.IDENTITY)
	private Long auditId;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name="document_id")
	private Documents document;
	
	@ManyToOne
	@JoinColumn(name="docpermId", nullable = true)
	private DocumentPermissions docPermission;
	
	@Enumerated(EnumType.STRING)
	private ManageAction action;

	
	
	
	// Extra Properties
	@Version
	private Integer updateCount;
	
	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime insertedOn;
	
	@UpdateTimestamp
	@Column(insertable = false)
	private LocalDateTime updatedOn;
}
