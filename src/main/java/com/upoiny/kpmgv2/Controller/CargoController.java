package com.upoiny.kpmgv2.Controller;

import com.upoiny.kpmgv2.entities.Cargo;
import com.upoiny.kpmgv2.services.CargoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cargos")
@CrossOrigin(origins = "*")
public class CargoController {

    @Autowired
    private CargoService service;

    @GetMapping
    public Page<Cargo> listar(@RequestParam(required = false) String search, Pageable pageable){
        return search == null || search.isBlank()
                ? service.listar(pageable)
                : service.pesquisarPorNome(search, pageable);
    }

    @GetMapping("/{id}")
    public Cargo buscarPorId(@PathVariable Long id){

        return service.buscarPorId(id);

    }

    @GetMapping("/pesquisar")
    public Page<Cargo> pesquisar(
            @RequestParam String nome,
            Pageable pageable){

        return service.pesquisarPorNome(nome,pageable);

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cargo salvar(@RequestBody Cargo cargo){

        return service.salvar(cargo);

    }

    @PutMapping("/{id}")
    public Cargo atualizar(
            @PathVariable Long id,
            @RequestBody Cargo cargo){

        return service.atualizar(id,cargo);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id){

        service.excluir(id);

    }

}
