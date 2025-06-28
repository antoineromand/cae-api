package com.pickandeat.authentication.infrastructure.repository;

import com.pickandeat.authentication.infrastructure.model.RoleEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleEntityJPARepository extends JpaRepository<RoleEntity, Integer> {
    Optional<RoleEntity> findByName(String name);

    @Query(
            """
           SELECT r
           FROM RoleEntity r
           LEFT JOIN FETCH r.scopes
           WHERE r.name = :name
           """)
    Optional<RoleEntity> findByNameWithScopes(@Param("name") String name);
}
