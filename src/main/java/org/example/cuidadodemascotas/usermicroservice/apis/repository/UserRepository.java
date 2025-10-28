package org.example.cuidadodemascotas.usermicroservice.apis.repository;

import org.example.cuidadodemascota.commons.entities.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.id = :id AND u.active = true")
    Optional<User> findByIdAndActiveTrue(@Param("id") Long id);

    @Query("SELECT u FROM User u WHERE u.active = true ORDER BY u.id DESC")
    Page<User> findAllActive(Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.active = true")
    List<User> findAllActive();

    @Query("SELECT DISTINCT u FROM User u " +
            "JOIN u.userRoles ur " +
            "WHERE ur.role.id = :roleId AND u.active = true AND ur.active = true " +
            "ORDER BY u.id DESC")
    Page<User> findByRoleIdAndActiveTrue(@Param("roleId") Long roleId, Pageable pageable);

    @Query("SELECT DISTINCT u FROM User u " +
            "JOIN u.userRoles ur " +
            "WHERE ur.role.id = :roleId AND u.active = true AND ur.active = true")
    List<User> findByRoleIdAndActiveTrue(@Param("roleId") Long roleId);

    @Query("SELECT DISTINCT u FROM User u " +
            "JOIN u.userRoles ur " +
            "JOIN ur.role r " +
            "WHERE r.name = :roleName AND u.active = true AND ur.active = true " +
            "ORDER BY u.id DESC")
    Page<User> findByRoleNameAndActiveTrue(@Param("roleName") String roleName, Pageable pageable);

    @Query("SELECT DISTINCT u FROM User u " +
            "JOIN u.userRoles ur " +
            "JOIN ur.role r " +
            "WHERE r.name = :roleName AND u.active = true AND ur.active = true")
    List<User> findByRoleNameAndActiveTrue(@Param("roleName") String roleName);

    /**
     * Buscar usuario por email
     */
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.active = true")
    Optional<User> findByEmailAndActiveTrue(@Param("email") String email);

    /**
     * Verificar si existe un email
     */
    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    /**
     * Buscar usuarios por nombre o apellido
     */
    @Query("SELECT u FROM User u WHERE " +
            "(LOWER(CAST(u.name AS string)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(CAST(u.lastName AS string)) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
            "u.active = true " +
            "ORDER BY u.id DESC")
    Page<User> searchByNameOrLastName(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Búsqueda con filtros múltiples (incluyendo roles)
     */
    @Query("SELECT DISTINCT u FROM User u " +
            "LEFT JOIN u.userRoles ur " +
            "WHERE (:roleId IS NULL OR ur.role.id = :roleId) AND " +
            "(:searchTerm IS NULL OR " +
            " LOWER(CAST(u.name AS string)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            " LOWER(CAST(u.lastName AS string)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            " LOWER(CAST(u.email AS string)) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
            "u.active = true AND " +
            "(ur IS NULL OR ur.active = true) " +
            "ORDER BY u.id DESC")
    Page<User> findByFilters(
            @Param("roleId") Long roleId,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );

    /**
     * Búsqueda con nombre de rol en lugar de ID
     */
    @Query("SELECT DISTINCT u FROM User u " +
            "JOIN u.userRoles ur " +
            "JOIN ur.role r " +
            "WHERE (:roleName IS NULL OR r.name = :roleName) AND " +
            "(:searchTerm IS NULL OR " +
            " LOWER(CAST(u.name AS string)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            " LOWER(CAST(u.lastName AS string)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            " LOWER(CAST(u.email AS string)) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
            "u.active = true AND ur.active = true " +
            "ORDER BY u.id DESC")
    Page<User> findByRoleNameAndSearch(
            @Param("roleName") String roleName,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );

    /**
     * Contar usuarios activos por role
     */
    @Query("SELECT COUNT(DISTINCT u) FROM User u " +
            "JOIN u.userRoles ur " +
            "WHERE ur.role.id = :roleId AND u.active = true AND ur.active = true")
    long countByRoleId(@Param("roleId") Long roleId);

    /**
     * Contar usuarios por nombre de rol
     */
    @Query("SELECT COUNT(DISTINCT u) FROM User u " +
            "JOIN u.userRoles ur " +
            "JOIN ur.role r " +
            "WHERE r.name = :roleName AND u.active = true AND ur.active = true")
    long countByRoleName(@Param("roleName") String roleName);

    @Query("SELECT COUNT(u) FROM User u WHERE u.active = true")
    long countByActiveTrue();

    /**
     * Buscar usuarios con un estado de disponibilidad específico
     */
    @Query("SELECT u FROM User u " +
            "JOIN u.userRoles ur " +
            "JOIN ur.role r " +
            "WHERE r.name = :roleName AND u.state = :availabilityState AND u.active = true AND ur.active = true")
    List<User> findByRoleNameAndAvailabilityState(
            @Param("roleName") String roleName,
            @Param("availabilityState") String availabilityState
    );
}
