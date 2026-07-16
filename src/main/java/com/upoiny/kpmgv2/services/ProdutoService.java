package com.upoiny.kpmgv2.services;

import com.upoiny.kpmgv2.entities.Categoria;
import com.upoiny.kpmgv2.entities.Fornecedor;
import com.upoiny.kpmgv2.entities.Produto;
import com.upoiny.kpmgv2.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;

    public Page<Produto> listar(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Produto buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Produto não encontrado."));
    }

    public Page<Produto> pesquisarPorNome(
            String nome,
            Pageable pageable) {

        return repository.findByNomeContainingIgnoreCase(nome, pageable);
    }

    public Page<Produto> pesquisarPorCategoria(
            Categoria categoria,
            Pageable pageable) {

        return repository.findByCategoria(categoria, pageable);
    }

    public Page<Produto> pesquisarPorFornecedor(
            Fornecedor fornecedor,
            Pageable pageable) {

        return repository.findByFornecedor(fornecedor, pageable);
    }

    public Page<Produto> estoqueBaixo(Pageable pageable) {

        return repository.findByEstoqueLessThanEqual(10, pageable);
    }

    public Produto salvar(Produto produto) {

        return repository.save(produto);
    }

    public Produto atualizar(Long id, Produto produtoAtualizado) {

        Produto produto = buscarPorId(id);

        produto.setNome(produtoAtualizado.getNome());
        produto.setDescricao(produtoAtualizado.getDescricao());
        produto.setPrecoVenda(produtoAtualizado.getPrecoVenda());
        produto.setPrecoCompra(produtoAtualizado.getPrecoCompra());
        produto.setEstoque(produtoAtualizado.getEstoque());
        produto.setEstoqueMinimo(produtoAtualizado.getEstoqueMinimo());
        produto.setCategoria(produtoAtualizado.getCategoria());
        produto.setFornecedor(produtoAtualizado.getFornecedor());

        return repository.save(produto);
    }

    public void excluir(Long id) {

        Produto produto = buscarPorId(id);

        repository.delete(produto);
    }

}