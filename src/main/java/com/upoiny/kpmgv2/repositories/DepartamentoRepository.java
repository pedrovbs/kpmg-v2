package com.upoiny.kpmgv2.repositories;

import com.upoiny.kpmgv2.entities.Departamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {

    Optional<Departamento> findByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCase(String nome);

    Page<Departamento> findByNomeContainingIgnoreCase(
            String nome,
            Pageable pageable
    );

}