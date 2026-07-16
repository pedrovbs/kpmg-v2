package com.upoiny.kpmgv2.Controller;

import com.upoiny.kpmgv2.entities.Departamento;
import com.upoiny.kpmgv2.services.DepartamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/departamentos")
@CrossOrigin(origins = "*")
public class DepartamentoController {

    @Autowired
    private DepartamentoService service;

    @GetMapping
    public Page<Departamento> listar(@RequestParam(required = false) String search, Pageable pageable){
        return search == null || search.isBlank()
                ? service.listar(pageable)
                : service.pesquisarPorNome(search, pageable);
    }

    @GetMapping("/{id}")
    public Departamento buscarPorId(@PathVariable Long id){

        return service.buscarPorId(id);

    }

    @GetMapping("/pesquisar")
    public Page<Departamento> pesquisar(
            @RequestParam String nome,
            Pageable pageable){

        return service.pesquisarPorNome(nome,pageable);

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Departamento salvar(@RequestBody Departamento departamento){

        return service.salvar(departamento);

    }

    @PutMapping("/{id}")
    public Departamento atualizar(
            @PathVariable Long id,
            @RequestBody Departamento departamento){

        return service.atualizar(id,departamento);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id){

        service.excluir(id);

    }

}
