package org.example.cuidadodemascotas.usermicroservice.apis.repository;

import org.example.cuidadodemascota.commons.entities.user.Role;
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

    // ==================== BÁSICOS ====================

    @Query("SELECT u FROM User u WHERE u.id = :id AND u.active = true")
    Optional<User> findByIdAndActiveTrue(@Param("id") Long id);

    @Query("SELECT u FROM User u WHERE u.active = true ORDER BY u.id DESC")
    Page<User> findAllActive(Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.active = true")
    List<User> findAllActive();

    // ==================== BÚSQUEDA POR ROL (ID) ====================

    @Query("SELECT DISTINCT u FROM User u " +
            "JOIN u.userRoles ur " +
            "WHERE ur.role.id = :roleId " +
            "AND ur.active = true " +
            "AND u.active = true " +
            "ORDER BY u.id DESC")
    Page<User> findByRoleIdAndActiveTrue(@Param("roleId") Long roleId, Pageable pageable);

    @Query("SELECT DISTINCT u FROM User u " +
            "JOIN u.userRoles ur " +
            "WHERE ur.role.id = :roleId " +
            "AND ur.active = true " +
            "AND u.active = true")
    List<User> findByRoleIdAndActiveTrue(@Param("roleId") Long roleId);

    // ==================== BÚSQUEDA POR ROL (NOMBRE) ====================

    @Query("SELECT DISTINCT u FROM User u " +
            "JOIN u.userRoles ur " +
            "WHERE UPPER(ur.role.name) = UPPER(:roleName) " +
            "AND ur.active = true " +
            "AND u.active = true " +
            "ORDER BY u.id DESC")
    Page<User> findByRoleNameAndActiveTrue(@Param("roleName") String roleName, Pageable pageable);

    @Query("SELECT DISTINCT u FROM User u " +
            "JOIN u.userRoles ur " +
            "WHERE UPPER(ur.role.name) = UPPER(:roleName) " +
            "AND ur.active = true " +
            "AND u.active = true")
    List<User> findByRoleNameAndActiveTrue(@Param("roleName") String roleName);

    // ==================== BÚSQUEDA POR EMAIL ====================

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.active = true")
    Optional<User> findByEmailAndActiveTrue(@Param("email") String email);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    // ==================== BÚSQUEDA POR NOMBRE/APELLIDO ====================

    @Query("SELECT u FROM User u " +
            "WHERE u.active = true " +
            "AND (LOWER(u.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "     LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
            "ORDER BY u.id DESC")
    Page<User> searchByNameOrLastName(@Param("searchTerm") String searchTerm, Pageable pageable);

    // ==================== BÚSQUEDA CON FILTROS (CORREGIDO) ====================

    /**
     * Query optimizado que maneja correctamente roleId y searchTerm opcionales
     * Si roleId es NULL, busca en TODOS los usuarios
     */
    @Query("SELECT DISTINCT u FROM User u " +
            "WHERE u.active = true " +
            "AND (:roleId IS NULL OR EXISTS (" +
            "    SELECT 1 FROM UserRole ur " +
            "    WHERE ur.user = u " +
            "    AND ur.role.id = :roleId " +
            "    AND ur.active = true" +
            ")) " +
            "AND (:searchTerm IS NULL OR :searchTerm = '' OR " +
            "    LOWER(u.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "    LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "    LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
            "ORDER BY u.id DESC")
    Page<User> findByFilters(
            @Param("roleId") Long roleId,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );

    /**
     * Query optimizado con nombre de rol en lugar de ID
     * Si roleName es NULL o vacío, busca en TODOS los usuarios
     */
    @Query("SELECT DISTINCT u FROM User u " +
            "WHERE u.active = true " +
            "AND (:roleName IS NULL OR :roleName = '' OR EXISTS (" +
            "    SELECT 1 FROM UserRole ur " +
            "    WHERE ur.user = u " +
            "    AND UPPER(ur.role.name) = UPPER(:roleName) " +
            "    AND ur.active = true" +
            ")) " +
            "AND (:searchTerm IS NULL OR :searchTerm = '' OR " +
            "    LOWER(u.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "    LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "    LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
            "ORDER BY u.id DESC")
    Page<User> findByRoleNameAndSearch(
            @Param("roleName") String roleName,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );

    // ==================== CUIDADORES DISPONIBLES ====================

    @Query("SELECT DISTINCT u FROM User u " +
            "JOIN u.userRoles ur " +
            "WHERE UPPER(ur.role.name) = 'CARER' " +
            "AND ur.active = true " +
            "AND u.active = true " +
            "AND u.state = org.example.cuidadodemascota.commons.entities.enums.AvailabilityStateEnum.AVAILABLE")
    List<User> findAvailableCarers();

    /**
     * MÉTODO ALTERNATIVO: Buscar por nombre de rol y estado
     */
    @Query("SELECT DISTINCT u FROM User u " +
            "JOIN u.userRoles ur " +
            "WHERE UPPER(ur.role.name) = UPPER(:roleName) " +
            "AND ur.active = true " +
            "AND u.active = true " +
            "AND CAST(u.state AS string) = :availabilityState")
    List<User> findByRoleNameAndAvailabilityState(
            @Param("roleName") String roleName,
            @Param("availabilityState") String availabilityState
    );

    // ==================== OBTENER ROLES DE USUARIO ====================

    @Query("SELECT ur.role FROM UserRole ur " +
            "WHERE ur.user.id = :userId " +
            "AND ur.active = true")
    List<Role> findRolesByUserId(@Param("userId") Long userId);

    // ==================== VERIFICAR SI TIENE ROL ====================

    @Query("SELECT CASE WHEN COUNT(ur) > 0 THEN true ELSE false END " +
            "FROM UserRole ur " +
            "WHERE ur.user.id = :userId " +
            "AND UPPER(ur.role.name) = UPPER(:roleName) " +
            "AND ur.active = true")
    boolean hasRole(@Param("userId") Long userId, @Param("roleName") String roleName);

    // ==================== CONTADORES ====================

    @Query("SELECT COUNT(DISTINCT u) FROM User u " +
            "JOIN u.userRoles ur " +
            "WHERE ur.role.id = :roleId " +
            "AND ur.active = true " +
            "AND u.active = true")
    long countByRoleId(@Param("roleId") Long roleId);

    @Query("SELECT COUNT(DISTINCT u) FROM User u " +
            "JOIN u.userRoles ur " +
            "WHERE UPPER(ur.role.name) = UPPER(:roleName) " +
            "AND ur.active = true " +
            "AND u.active = true")
    long countByRoleName(@Param("roleName") String roleName);

    @Query("SELECT COUNT(u) FROM User u WHERE u.active = true")
    long countByActiveTrue();

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.userRoles WHERE u.email = :email AND u.active = true")
    Optional<User> findByEmailAndActiveTrueWithRoles(@Param("email") String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.userRoles ur LEFT JOIN FETCH ur.role WHERE u.email = :email AND u.active = true")
    Optional<User> findByEmailWithRoles(@Param("email") String email);
}