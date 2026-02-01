package com.doc.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.doc.entity.Documents;

public interface DocumentsRepository extends JpaRepository<Documents, Long> {
	
	Page<Documents> findByOwnerEmail(String username, Pageable pageable);
	


}
