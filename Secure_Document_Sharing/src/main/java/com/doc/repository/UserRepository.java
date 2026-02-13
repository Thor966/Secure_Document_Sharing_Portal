package com.doc.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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
	
	

	

}
