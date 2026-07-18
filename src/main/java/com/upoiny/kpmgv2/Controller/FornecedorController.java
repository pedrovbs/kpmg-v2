package com.upoiny.kpmgv2.Controller;

import com.upoiny.kpmgv2.entities.Fornecedor;
import com.upoiny.kpmgv2.services.FornecedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/fornecedores")
@CrossOrigin(origins = "*")
public class FornecedorController {

    @Autowired
    private FornecedorService service;

    @GetMapping
    public Page<Fornecedor> listar(@RequestParam(required = false) String search, Pageable pageable) {
        return search == null || search.isBlank()
                ? service.listar(pageable)
                : service.pesquisarPorRazaoSocial(search, pageable);
    }

    @GetMapping("/{id}")
    public Fornecedor buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @GetMapping("/pesquisar/razao-social")
    public Page<Fornecedor> pesquisarPorRazaoSocial(
            @RequestParam String razaoSocial,
            Pageable pageable) {

        return service.pesquisarPorRazaoSocial(
                razaoSocial,
                pageable
        );
    }
    @GetMapping("/qtdFornecedores")
    public Map<String, Long> qtdClientes() {
        long total = service.contarForncecedores();

        Map<String, Long> response = new HashMap<>();
        response.put("totalFornecedor", total);

        return response;
    }


    @GetMapping("/pesquisar/nome-fantasia")
    public Page<Fornecedor> pesquisarPorNomeFantasia(
            @RequestParam String nomeFantasia,
            Pageable pageable) {

        return service.pesquisarPorNomeFantasia(
                nomeFantasia,
                pageable
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Fornecedor salvar(@RequestBody Fornecedor fornecedor) {
        return service.salvar(fornecedor);
    }

    @PutMapping("/{id}")
    public Fornecedor atualizar(
            @PathVariable Long id,
            @RequestBody Fornecedor fornecedor) {

        return service.atualizar(id, fornecedor);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }

}
