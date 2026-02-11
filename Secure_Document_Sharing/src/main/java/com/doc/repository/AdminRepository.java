package com.doc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doc.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {

}
