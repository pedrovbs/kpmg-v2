package com.upoiny.kpmgv2.services;

import com.upoiny.kpmgv2.entities.Cargo;
import com.upoiny.kpmgv2.repositories.CargoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CargoService {

    @Autowired
    private CargoRepository repository;

    public Page<Cargo> listar(Pageable pageable){

        return repository.findAll(pageable);

    }

    public Cargo buscarPorId(Long id){

        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Cargo não encontrado."));

    }

    public Page<Cargo> pesquisarPorNome(
            String nome,
            Pageable pageable){

        return repository.findByNomeContainingIgnoreCase(nome,pageable);

    }

    public Cargo salvar(Cargo cargo){

        if(repository.existsByNomeIgnoreCase(cargo.getNome())){
            throw new RuntimeException("Já existe um cargo com este nome.");
        }

        return repository.save(cargo);

    }

    public Cargo atualizar(Long id,
                           Cargo cargoAtualizado){

        Cargo cargo = buscarPorId(id);

        cargo.setNome(cargoAtualizado.getNome());
        cargo.setDescricao(cargoAtualizado.getDescricao());
        cargo.setSalarioBase(cargoAtualizado.getSalarioBase());
        cargo.setAtivo(cargoAtualizado.getAtivo());

        return repository.save(cargo);

    }

    public void excluir(Long id){

        repository.delete(buscarPorId(id));

    }

}