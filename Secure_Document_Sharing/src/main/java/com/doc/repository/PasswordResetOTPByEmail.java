package com.doc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doc.entity.PasswordResetOtp;

public interface PasswordResetOTPByEmail extends JpaRepository<PasswordResetOtp, Long> 
{
	
	Optional<PasswordResetOtp> 
    findTopByEmailAndIsUsedFalseOrderByInsertedOnDesc(String email);
}
