package com.bmt.HolaMundo.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.bmt.HolaMundo.Models.AppUser;

public interface AppUserRepository
        extends JpaRepository<AppUser, Integer>,
                JpaSpecificationExecutor<AppUser> {

    Optional<AppUser> findByEmail(String email);

    boolean existsByEmail(String email);
}