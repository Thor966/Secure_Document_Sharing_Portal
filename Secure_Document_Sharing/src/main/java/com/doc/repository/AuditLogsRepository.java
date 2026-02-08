package com.doc.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.doc.entity.AuditLogs;

public interface AuditLogsRepository extends CrudRepository<AuditLogs, Long> 
{
	

    Page<AuditLogs> findByUser_Uid(Long userId, Pageable pageable);

}
