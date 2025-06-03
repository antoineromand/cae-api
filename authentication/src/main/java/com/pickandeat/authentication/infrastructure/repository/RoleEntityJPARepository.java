package com.pickandeat.authentication.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pickandeat.authentication.infrastructure.model.RoleEntity;

@Repository
public interface RoleEntityJPARepository extends JpaRepository<RoleEntity, Integer> {
    Optional<RoleEntity> findByName(String name);
}
