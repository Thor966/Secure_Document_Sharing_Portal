package com.doc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doc.entity.DocumentPermissions;
import com.doc.entity.User;

public interface DocumentPermissionsRepository extends JpaRepository<DocumentPermissions, Long>
{
	public List<DocumentPermissions> findByGrantedBy(User grantedBy);
	
	public Optional<DocumentPermissions> findBySecureToken(String token);
}
