package com.piggy.PIGGY.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.piggy.PIGGY.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
}