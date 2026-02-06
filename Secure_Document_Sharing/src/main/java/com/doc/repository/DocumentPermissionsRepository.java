package com.doc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.doc.entity.DocumentPermissions;
import com.doc.entity.User;

public interface DocumentPermissionsRepository extends JpaRepository<DocumentPermissions, Long>
{
	public List<DocumentPermissions> findByGrantedBy(User grantedBy);
	
	public Optional<DocumentPermissions> findBySecureToken(String token);
	
	public Page<DocumentPermissions> findByGrantedToUserAndShareType(String grantedToUser, String shareType, Pageable pageable);
	
	public Long countByGrantedBy(User user);
	
	public Long countByGrantedByAndStatus(User grantedBy, String status);
	
	// get the OTP Based and Password Based Documents Count
	public Long countByGrantedByAndAccessType(User grantedBy, String accessType);
	
	public Page<DocumentPermissions> findByGrantedBy(User grantedBy, Pageable pageable);
	
	


	
	
}
