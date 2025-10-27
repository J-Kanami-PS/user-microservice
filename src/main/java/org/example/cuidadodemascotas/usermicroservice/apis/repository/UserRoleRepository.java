package org.example.cuidadodemascotas.usermicroservice.apis.repository;

import org.example.cuidadodemascota.commons.entities.user.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    @Query("SELECT ur FROM UserRole ur WHERE ur.id = :id AND ur.active = true")
    Optional<UserRole> findByIdAndActiveTrue(@Param("id") Long id);

    @Query("SELECT ur FROM UserRole ur WHERE ur.user.id = :userId AND ur.active = true")
    List<UserRole> findByUserIdAndActiveTrue(@Param("userId") Long userId);

    @Query("SELECT ur FROM UserRole ur WHERE ur.role.id = :roleId AND ur.active = true")
    List<UserRole> findByRoleIdAndActiveTrue(@Param("roleId") Long roleId);

    @Query("SELECT ur FROM UserRole ur WHERE ur.user.id = :userId AND ur.role.id = :roleId AND ur.active = true")
    Optional<UserRole> findByUserIdAndRoleIdAndActiveTrue(
            @Param("userId") Long userId,
            @Param("roleId") Long roleId
    );

    boolean existsByUserIdAndRoleIdAndActiveTrue(Long userId, Long roleId);
}