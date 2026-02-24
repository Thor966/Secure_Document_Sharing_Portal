package com.doc.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="passwordResetOtp")
@Data
public class PasswordResetOtp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long presetid;

    private String email;

    private String otp;
    
    private int attempts;

    private LocalDateTime expiryTime;

    private boolean isUsed;

    
    // Extra properties
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime insertedOn;
    
    @UpdateTimestamp
    @Column(insertable = false)
    private LocalDateTime updatedOn;
}