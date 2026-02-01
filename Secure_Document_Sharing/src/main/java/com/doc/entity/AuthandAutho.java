package com.doc.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Data;

@Entity
@Table(name="AuthandAutho")
@Data
public class AuthandAutho implements Serializable
{
	
	private static final long serialVersionUID = 1L;

	
	@Id
	@SequenceGenerator(name="auth", sequenceName = "auth_seq", initialValue = 1, allocationSize = 1)
	@GeneratedValue(generator = "auth", strategy = GenerationType.IDENTITY)
	private Long authid;
	
	@Column(nullable = false)
	private String username;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false)
	private String role;
	
	
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
