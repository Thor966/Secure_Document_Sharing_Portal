package com.doc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doc.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
	
	Optional<User> findByAuth_Username(String username);
	
	Optional<User> findByemail(String email);

}
