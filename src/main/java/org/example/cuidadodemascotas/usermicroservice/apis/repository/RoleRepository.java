package org.example.cuidadodemascotas.usermicroservice.apis.repository;

import org.example.cuidadodemascota.commons.entities.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    @Query("SELECT r FROM Role r ORDER BY r.name ASC")
    List<Role> findAllOrdered();

    @Query("SELECT r FROM Role r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Role> searchByName(@Param("name") String name);
}
