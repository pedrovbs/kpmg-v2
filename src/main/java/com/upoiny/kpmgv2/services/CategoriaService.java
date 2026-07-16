package com.upoiny.kpmgv2.services;

import com.upoiny.kpmgv2.entities.Categoria;
import com.upoiny.kpmgv2.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository repository;

    public Page<Categoria> listar(Pageable pageable){
        return repository.findAll(pageable);
    }

    public Categoria buscarPorId(Long id){

        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Categoria não encontrada."));
    }

    public Page<Categoria> pesquisarPorNome(
            String nome,
            Pageable pageable){

        return repository.findByNomeContainingIgnoreCase(nome,pageable);
    }

    public Categoria salvar(Categoria categoria){

        if(repository.existsByNomeIgnoreCase(categoria.getNome())){
            throw new RuntimeException("Já existe uma categoria com este nome.");
        }

        return repository.save(categoria);
    }

    public Categoria atualizar(Long id, Categoria categoriaAtualizada){

        Categoria categoria = buscarPorId(id);

        categoria.setNome(categoriaAtualizada.getNome());
        categoria.setDescricao(categoriaAtualizada.getDescricao());

        return repository.save(categoria);

    }

    public void excluir(Long id){

        Categoria categoria = buscarPorId(id);

        repository.delete(categoria);

    }

}