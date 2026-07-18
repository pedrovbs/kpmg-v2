package com.upoiny.kpmgv2.Controller;

import com.upoiny.kpmgv2.entities.Funcionario;
import com.upoiny.kpmgv2.services.FuncionarioService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/funcionarios")
@CrossOrigin(origins = "*")
public class FuncionarioController {



    @Autowired
    private FuncionarioService service;
    // ============================
    // Listar
    // ============================

    @GetMapping
    public Page<Funcionario> listar(
            @RequestParam(required = false) String search,
            Pageable pageable
    ){
        return search == null || search.isBlank()
                ? service.listar(pageable)
                : service.buscarNome(search, pageable);

    }
    // ============================
    // Buscar por ID
    // ============================

    @GetMapping("/{id}")
    public Funcionario buscarPorId(
            @PathVariable Long id
    ){

        return service.buscarPorId(id);

    }
    // ============================
    // Criar
    // ============================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Funcionario salvar(
            @RequestBody Funcionario funcionario
    ){

        return service.salvar(funcionario);

    }

    // ============================
    // Atualizar
    // ============================

    @PutMapping("/{id}")
    public Funcionario atualizar(
            @PathVariable Long id,
            @RequestBody Funcionario funcionario
    ){

        return service.atualizar(
                id,
                funcionario
        );

    }
    // ============================
    // Excluir
    // ============================

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(
            @PathVariable Long id
    ){

        service.excluir(id);

    }
    // ============================
    // Pesquisa por nome
    // ============================

    @GetMapping("/qtdFuncionarios")
    public Map<String, Long> qtdClientes() {
        long total = service.contarFuncionarios();

        Map<String, Long> response = new HashMap<>();
        response.put("totalFuncionarios", total);

        return response;
    }

    @GetMapping("/buscar")
    public Page<Funcionario> buscarNome(
            @RequestParam String nome,
            Pageable pageable
    ){

        return service.buscarNome(
                nome,
                pageable
        );

    }
}
