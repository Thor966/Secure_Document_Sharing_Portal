package com.doc.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Data;

@Entity
@Table(name="Admin")
@Data
public class Admin
{
	
	@Id
	@SequenceGenerator(name="admin",sequenceName = "admin_seq",initialValue = 1,allocationSize = 1)
	@GeneratedValue(generator = "admin_seq",strategy = GenerationType.SEQUENCE)
	private Long aid;
	
	private String username;
	
	private String name;
	
	private String email;
	
	private Long phno;
	
	private String password;
	
	@OneToOne(targetEntity = AuthandAutho.class, cascade = CascadeType.ALL)
	@JoinColumn(name="auth_id", referencedColumnName = "authid", nullable = false)
	private AuthandAutho auth;
	
	
	// Extra Properties
	
	@Version
	private Integer updateOn;
	
	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime insertedOn;
	
	@CreationTimestamp
	@Column(insertable = false)
	private LocalDateTime updatedOn;

}
