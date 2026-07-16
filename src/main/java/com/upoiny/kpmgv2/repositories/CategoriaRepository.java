package com.upoiny.kpmgv2.repositories;

import com.upoiny.kpmgv2.entities.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCase(String nome);

    Page<Categoria> findByNomeContainingIgnoreCase(
            String nome,
            Pageable pageable
    );

}