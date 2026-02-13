package com.doc.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.doc.dto.StorageUsageDTO;
import com.doc.dto.UserStorageProjection;
import com.doc.entity.Documents;
import com.doc.entity.User;

public interface DocumentsRepository extends JpaRepository<Documents, Long> {
	
	Page<Documents> findByOwnerEmail(String username, Pageable pageable);
		
	public Long countByOwner(User owner);
	
	
	// get the count of document daywise
	public Long countByInsertedOnBetween(LocalDateTime start, LocalDateTime end);
	
	// get the total storage used
	 @Query("SELECT COALESCE(SUM(d.fileSize),0) FROM Documents d")
	public Long getTotalStoraeUsed();
	 
	 
	 
	 @Query("""
		       SELECT new com.doc.dto.StorageUsageDTO(
		           d.owner.email,
		           SUM(d.fileSize)
		       )
		       FROM Documents d
		       GROUP BY d.owner.email
		       ORDER BY SUM(d.fileSize) DESC
		       """)
	 public	List<StorageUsageDTO> getStorageUsagePerUser();
	 
	 
	 
	 
	 @Query("""
		       SELECT new com.doc.dto.UserStorageProjection(
		           u.uid,
		           COALESCE(SUM(d.fileSize), 0)
		       )
		       FROM User u
		       LEFT JOIN Documents d ON d.owner.uid = u.uid
		       GROUP BY u.uid
		       """)
		List<UserStorageProjection> getStorageUsageForAllUsers();



	
	
	


}
