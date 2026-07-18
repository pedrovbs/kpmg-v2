package com.upoiny.kpmgv2.services;

import com.upoiny.kpmgv2.entities.Fornecedor;
import com.upoiny.kpmgv2.repositories.FornecedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FornecedorService {

    @Autowired
    private FornecedorRepository repository;

    public Page<Fornecedor> listar(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Fornecedor buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Fornecedor não encontrado."));
    }

    public Page<Fornecedor> pesquisarPorRazaoSocial(
            String razaoSocial,
            Pageable pageable) {

        return repository.findByRazaoSocialContainingIgnoreCase(
                razaoSocial,
                pageable
        );
    }

    public Page<Fornecedor> pesquisarPorNomeFantasia(
            String nomeFantasia,
            Pageable pageable) {

        return repository.findByNomeFantasiaContainingIgnoreCase(
                nomeFantasia,
                pageable
        );
    }

    public long contarForncecedores() {
        return repository.count();
    }

    public Fornecedor salvar(Fornecedor fornecedor) {

        if (repository.existsByCnpj(fornecedor.getCnpj())) {
            throw new RuntimeException("Já existe um fornecedor com este CNPJ.");
        }

        return repository.save(fornecedor);
    }

    public Fornecedor atualizar(Long id, Fornecedor fornecedorAtualizado) {

        Fornecedor fornecedor = buscarPorId(id);

        fornecedor.setRazaoSocial(fornecedorAtualizado.getRazaoSocial());
        fornecedor.setNomeFantasia(fornecedorAtualizado.getNomeFantasia());
        fornecedor.setCnpj(fornecedorAtualizado.getCnpj());
        fornecedor.setEmail(fornecedorAtualizado.getEmail());
        fornecedor.setTelefone(fornecedorAtualizado.getTelefone());
        fornecedor.setCidade(fornecedorAtualizado.getCidade());
        fornecedor.setEstado(fornecedorAtualizado.getEstado());

        return repository.save(fornecedor);
    }

    public void excluir(Long id) {

        Fornecedor fornecedor = buscarPorId(id);

        repository.delete(fornecedor);
    }

}