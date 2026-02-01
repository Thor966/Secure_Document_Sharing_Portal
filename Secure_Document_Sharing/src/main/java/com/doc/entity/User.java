package com.doc.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Data;

@Entity
@Table(name = "user")
@Data
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long uid;

	@Column(nullable = false, unique = true, updatable = false)
	private UUID publicId;

	@Column(name = "first_name", nullable = false)
	private String firstName;
	
	@Column(name = "last_name", nullable = false)
	private String lastName;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	private boolean enable = true;
	
	@OneToOne(targetEntity = AuthandAutho.class, cascade = CascadeType.ALL)
	@JoinColumn(name="auth_id", referencedColumnName = "authid", nullable = false)
	private AuthandAutho auth;

	// Extra Properties

	@Version
	private Integer updatedCount;

	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime insertedOn;

	@UpdateTimestamp
	@Column(insertable = false)
	private LocalDateTime updatedOn;
	
	
	@PrePersist
	void generatePublicId() {
	    this.publicId = UUID.randomUUID();
	}


}
