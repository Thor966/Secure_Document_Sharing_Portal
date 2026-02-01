package com.doc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doc.entity.AuthandAutho;

public interface AuthRepository extends JpaRepository<AuthandAutho, Long> {
	
	 Optional<AuthandAutho> findByUsername(String username);

}
