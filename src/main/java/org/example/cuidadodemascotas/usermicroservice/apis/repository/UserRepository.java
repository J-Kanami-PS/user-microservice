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

    /**
     * Obtener usuarios por role
     */
    /*
    @Query("SELECT u FROM User u WHERE u.role.id = :roleId AND u.active = true ORDER BY u.id DESC")
    Page<User> findByRoleIdAndActiveTrue(@Param("roleId") Long roleId, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.role.id = :roleId AND u.active = true")
    List<User> findByRoleIdAndActiveTrue(@Param("roleId") Long roleId);
     */

    /**
     * Obtener usuarios por nombre de role
     */
    /*
    @Query("SELECT u FROM User u WHERE u.role.name = :roleName AND u.active = true ORDER BY u.id DESC")
    Page<User> findByRoleNameAndActiveTrue(@Param("roleName") String roleName, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.role.name = :roleName AND u.active = true")
    List<User> findByRoleNameAndActiveTrue(@Param("roleName") String roleName);
    */

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
            "(LOWER(u.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
            "u.active = true " +
            "ORDER BY u.id DESC")
    Page<User> searchByNameOrLastName(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Búsqueda con filtros múltiples
     */
    @Query("SELECT u FROM User u WHERE " +
            //"(:roleId IS NULL OR u.role.id = :roleId) AND " +
            "(:searchTerm IS NULL OR " +
            "(u.name) LIKE (CONCAT('%', :searchTerm, '%')) OR " +
            "(u.lastName) LIKE (CONCAT('%', :searchTerm, '%')) OR " +
            "(u.email) LIKE (CONCAT('%', :searchTerm, '%'))) AND " +
            "u.active = true " +
            "ORDER BY u.id DESC")
    Page<User> findByFilters(
            //@Param("roleId") Long roleId,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );

    /**
     * Contar usuarios activos por role
     */
    /*
    @Query("SELECT COUNT(u) FROM User u WHERE u.role.id = :roleId AND u.active = true")
    long countByRoleId(@Param("roleId") Long roleId);

    @Query("SELECT COUNT(u) FROM User u WHERE u.active = true")
    long countByActiveTrue();
     */
}