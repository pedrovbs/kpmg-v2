package com.upoiny.kpmgv2.services;

import com.upoiny.kpmgv2.entities.Departamento;
import com.upoiny.kpmgv2.repositories.DepartamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DepartamentoService {

    @Autowired
    private DepartamentoRepository repository;

    public Page<Departamento> listar(Pageable pageable){
        return repository.findAll(pageable);
    }

    public Departamento buscarPorId(Long id){

        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Departamento não encontrado."));

    }

    public Page<Departamento> pesquisarPorNome(
            String nome,
            Pageable pageable){

        return repository.findByNomeContainingIgnoreCase(nome,pageable);

    }

    public Departamento salvar(Departamento departamento){

        if(repository.existsByNomeIgnoreCase(departamento.getNome())){
            throw new RuntimeException("Já existe um departamento com este nome.");
        }

        return repository.save(departamento);

    }

    public Departamento atualizar(Long id,
                                  Departamento departamentoAtualizado){

        Departamento departamento = buscarPorId(id);

        departamento.setNome(departamentoAtualizado.getNome());
        departamento.setDescricao(departamentoAtualizado.getDescricao());
        departamento.setAtivo(departamentoAtualizado.getAtivo());

        return repository.save(departamento);

    }

    public void excluir(Long id){

        repository.delete(buscarPorId(id));

    }

}