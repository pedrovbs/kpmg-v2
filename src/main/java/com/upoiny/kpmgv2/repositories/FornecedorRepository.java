package com.upoiny.kpmgv2.repositories;

import com.upoiny.kpmgv2.entities.Fornecedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {

    Optional<Fornecedor> findByCnpj(String cnpj);

    boolean existsByCnpj(String cnpj);

    Page<Fornecedor> findByRazaoSocialContainingIgnoreCase(
            String razaoSocial,
            Pageable pageable
    );

    Page<Fornecedor> findByNomeFantasiaContainingIgnoreCase(
            String nomeFantasia,
            Pageable pageable
    );

}