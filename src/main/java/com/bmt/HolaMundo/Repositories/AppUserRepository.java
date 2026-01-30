package com.bmt.HolaMundo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bmt.HolaMundo.Models.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Integer>{

	public AppUser findByEmail(String email);
}