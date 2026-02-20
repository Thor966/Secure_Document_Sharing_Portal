package com.doc.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.doc.entity.AuditLogs;
import com.doc.entity.DocumentPermissions;
import com.doc.entity.ManageStatus;

public interface AuditLogsRepository extends JpaRepository<AuditLogs, Long> 
{
	

    Page<AuditLogs> findByUser_Uid(Long userId, Pageable pageable);

    
    
    // filter the audit logs by keyword and status
    @Query("""
    	    SELECT al FROM AuditLogs al
    	    WHERE (:email IS NULL OR LOWER(al.user.email) LIKE LOWER(CONCAT('%', :email, '%')))
    	    AND (:status IS NULL OR al.status = :status)
    	""")
    	Page<AuditLogs> searchByUserEmailAndStatus(@Param("email") String email, @Param("status") String status, 
    												Pageable pageable);
    
    
    
    
 // filter by email only
 	@Query("SELECT al FROM AuditLogs al WHERE (:email IS NULL OR LOWER(al.user.email) LIKE LOWER(CONCAT('%', :email, '%')))")
 	Page<AuditLogs> searchByKeywords(@Param("email") String email, Pageable pageable);
 	
 	
 	
 	// filter by status only
 	@Query("SELECT al FROM AuditLogs al WHERE (:status IS NULL OR LOWER(al.status) LIKE LOWER(CONCAT('%', :status, '%')))")
	Page<AuditLogs> searchByStatus(@Param("status") String status, Pageable pageable);
    
    
    
    
    
    
}
