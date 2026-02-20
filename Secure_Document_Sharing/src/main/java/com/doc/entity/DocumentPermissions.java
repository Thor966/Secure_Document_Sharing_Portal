package com.doc.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name="document_permissions")
@Data
public class DocumentPermissions  implements Serializable
{
	
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="dpseq", sequenceName = "dpseq", initialValue = 1, allocationSize = 1)
	@GeneratedValue(generator = "dpseq", strategy = GenerationType.IDENTITY)
	private Long dpid;
	
	@ManyToOne
	@JoinColumn(name="document_id")
	private Documents documentId;
	
	private String grantedToUser;
	
	@ManyToOne
	@JoinColumn(name="granted_by_user")
	private User grantedBy;
	
	private LocalDateTime expiryTime;
	
	
	private String accessType;
	
	private String shareType;
	
	private String dpEncryptedKey;
	
	@Column(unique = true)
	private String secureToken;

	
	private String docPass;
	
	private Long docOtp;
	
	private String status;
	
	
	// Extra properties
	
	@Version
	private Integer updateCount;

	@CreationTimestamp
	@Column(updatable = false)
	@DateTimeFormat(pattern = "dd-MM-yyyy, HH:mm")
	private LocalDateTime insertedOn;
	
	@UpdateTimestamp
	@Column(insertable = false)
	private LocalDateTime updateOn;
}
