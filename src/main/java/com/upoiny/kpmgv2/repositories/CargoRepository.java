package com.upoiny.kpmgv2.repositories;

import com.upoiny.kpmgv2.entities.Cargo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CargoRepository extends JpaRepository<Cargo, Long> {

    Optional<Cargo> findByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCase(String nome);

    Page<Cargo> findByNomeContainingIgnoreCase(
            String nome,
            Pageable pageable
    );

}