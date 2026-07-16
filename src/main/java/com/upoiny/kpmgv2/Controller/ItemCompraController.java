package com.upoiny.kpmgv2.Controller;

import com.upoiny.kpmgv2.entities.ItemCompra;
import com.upoiny.kpmgv2.services.ItemCompraService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/itens-compra")
@CrossOrigin(origins = "*")
public class ItemCompraController {

    private final ItemCompraService service;

    public ItemCompraController(ItemCompraService service) {
        this.service = service;
    }

    @GetMapping
    public Page<ItemCompra> listar(@RequestParam(required = false) String search,
                                   @RequestParam(required = false) Long compraId,
                                   @RequestParam(required = false) Long produtoId,
                                   Pageable pageable) {
        return service.listar(search, compraId, produtoId, pageable);
    }

    @GetMapping("/{id}")
    public ItemCompra buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemCompra salvar(@RequestBody ItemCompra item) {
        return service.salvar(item);
    }

    @PutMapping("/{id}")
    public ItemCompra atualizar(@PathVariable Long id, @RequestBody ItemCompra item) {
        return service.atualizar(id, item);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }
}
