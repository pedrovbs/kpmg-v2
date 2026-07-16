package com.upoiny.kpmgv2.Controller;

import com.upoiny.kpmgv2.entities.Categoria;
import com.upoiny.kpmgv2.entities.Fornecedor;
import com.upoiny.kpmgv2.entities.Produto;
import com.upoiny.kpmgv2.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {

    @Autowired
    private ProdutoService service;

    @GetMapping
    public Page<Produto> listar(@RequestParam(required = false) String search, Pageable pageable) {
        return search == null || search.isBlank()
                ? service.listar(pageable)
                : service.pesquisarPorNome(search, pageable);
    }

    @GetMapping("/{id}")
    public Produto buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @GetMapping("/pesquisar")
    public Page<Produto> pesquisar(
            @RequestParam String nome,
            Pageable pageable) {

        return service.pesquisarPorNome(nome, pageable);
    }

    @GetMapping("/estoque-baixo")
    public Page<Produto> estoqueBaixo(Pageable pageable) {
        return service.estoqueBaixo(pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Produto salvar(@RequestBody Produto produto) {
        return service.salvar(produto);
    }

    @PutMapping("/{id}")
    public Produto atualizar(
            @PathVariable Long id,
            @RequestBody Produto produto) {

        return service.atualizar(id, produto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }

}
