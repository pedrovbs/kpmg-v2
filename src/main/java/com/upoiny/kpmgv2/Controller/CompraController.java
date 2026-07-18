package com.upoiny.kpmgv2.Controller;


import com.upoiny.kpmgv2.dto.CompraRequest;
import com.upoiny.kpmgv2.entities.Compra;
import com.upoiny.kpmgv2.services.CompraService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/compras")
@CrossOrigin(origins = "*")
public class CompraController {

    @Autowired
    private CompraService compraService;

    /**
     * Criar uma nova compra
     *
     * Recebe:
     * fornecedor
     * funcionário responsável
     * produtos e quantidades
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Compra realizarCompra(
            @RequestBody CompraRequest request
    ){

        return compraService.realizarCompra(request);

    }

    /**
     * Listar todas as compras
     */
    @GetMapping
    public Page<Compra> listar(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            Pageable pageable
    ){
        String termo = search == null || search.isBlank() ? status : search;
        return termo == null || termo.isBlank()
                ? compraService.listar(pageable)
                : compraService.pesquisarPorStatus(termo, pageable);

    }

    @GetMapping("/qtdCompras")
    public Map<String, Long> qtdClientes() {
        long total = compraService.contarClientes();

        Map<String, Long> response = new HashMap<>();
        response.put("totalCompras", total);

        return response;
    }

    @PutMapping("/{id}")
    public Compra atualizar(@PathVariable Long id, @RequestBody Compra compra) {
        return compraService.atualizar(id, compra);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        compraService.excluir(id);
    }

    /**
     * Buscar compra por ID
     */
    @GetMapping("/{id}")
    public Compra buscarPorId(
            @PathVariable Long id
    ){

        return compraService.buscarPorId(id);

    }

    /**
     * Cancelar uma compra
     */
    @PutMapping("/{id}/cancelar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelarCompra(
            @PathVariable Long id
    ){

        compraService.cancelarCompra(id);

    }



}
