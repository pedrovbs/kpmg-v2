package com.upoiny.kpmgv2.Controller;

import com.upoiny.kpmgv2.entities.Categoria;
import com.upoiny.kpmgv2.services.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
public class CategoriaController {

    @Autowired
    private CategoriaService service;

    @GetMapping
    public Page<Categoria> listar(@RequestParam(required = false) String search, Pageable pageable){
        return search == null || search.isBlank()
                ? service.listar(pageable)
                : service.pesquisarPorNome(search, pageable);
    }

    @GetMapping("/{id}")
    public Categoria buscarPorId(@PathVariable Long id){

        return service.buscarPorId(id);

    }

    @GetMapping("/pesquisar")
    public Page<Categoria> pesquisar(

            @RequestParam String nome,
            Pageable pageable){

        return service.pesquisarPorNome(nome,pageable);

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Categoria salvar(@RequestBody Categoria categoria){

        return service.salvar(categoria);

    }

    @PutMapping("/{id}")
    public Categoria atualizar(

            @PathVariable Long id,

            @RequestBody Categoria categoria){

        return service.atualizar(id,categoria);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id){

        service.excluir(id);

    }

}
