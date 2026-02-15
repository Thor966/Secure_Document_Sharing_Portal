package com.doc.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.doc.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
	// find the user by username
	Optional<User> findByAuth_Username(String username);
	
	// find the user by email
	Optional<User> findByemail(String email);
	
	// get the todays register user count
	Long countByInsertedOnBetween(LocalDateTime start, LocalDateTime end);
	
	// get the disabled user count
	Long countByStatus(String status);
	
	
	
	// filter both keyword and status
		@Query("""
				SELECT u FROM User u
				WHERE  (:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')))
				AND (:status IS NULL OR LOWER (u.status) LIKE LOWER(CONCAT('%', :status, '%')))
				
				""")
		Page<User> searchByKeywordAndStatus(@Param("email") String email,
												@Param("status") String Status, Pageable pageable);
		
		
		// filter Only using keyword
		@Query("SELECT k FROM User k WHERE LOWER(k.email) LIKE(CONCAT('%', :email, '%'))")
		Page<User> searchByKeyword(@Param("email") String email, Pageable pageable);
		
		
		// filter only using status
		@Query("SELECT s FROM User s WHERE LOWER(s.status) LIKE(CONCAT('%', :status, '%'))")
		Page<User> searchByStatus(@Param("status") String status, Pageable pageable);
	
	

	

}
