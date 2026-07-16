package com.upoiny.kpmgv2.repositories;

import com.upoiny.kpmgv2.entities.Funcionario;
import com.upoiny.kpmgv2.entities.Cargo;
import com.upoiny.kpmgv2.entities.Departamento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;


public interface FuncionarioRepository
        extends JpaRepository<Funcionario, Long> {


    // Buscar funcionário pelo CPF

    Optional<Funcionario> findByCpf(String cpf);



    // Verificar CPF existente

    boolean existsByCpf(String cpf);



    // Buscar por nome

    Page<Funcionario> findByNomeContainingIgnoreCase(
            String nome,
            Pageable pageable
    );



    // Buscar por email

    Page<Funcionario> findByEmailContainingIgnoreCase(
            String email,
            Pageable pageable
    );



    // Buscar por status

    Page<Funcionario> findByStatus(
            String status,
            Pageable pageable
    );



    // Buscar por departamento

    Page<Funcionario> findByDepartamento(
            Departamento departamento,
            Pageable pageable
    );



    // Buscar por cargo

    Page<Funcionario> findByCargo(
            Cargo cargo,
            Pageable pageable
    );

}