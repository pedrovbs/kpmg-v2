package com.upoiny.kpmgv2.Controller;

import com.upoiny.kpmgv2.entities.ItemVenda;
import com.upoiny.kpmgv2.entities.Produto;
import com.upoiny.kpmgv2.entities.Venda;
import com.upoiny.kpmgv2.services.ItemVendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/itens-venda")
@CrossOrigin(origins = "*")
public class ItemVendaController {

    @Autowired
    private ItemVendaService service;

    @GetMapping
    public Page<ItemVenda> listar(@RequestParam(required = false) String search,
                                  @RequestParam(required = false) Long vendaId,
                                  @RequestParam(required = false) Long produtoId,
                                  Pageable pageable) {
        if (search != null && !search.isBlank()) {
            return service.pesquisarPorProduto(search, pageable);
        }
        if (vendaId != null) {
            Venda venda = new Venda();
            venda.setId(vendaId);
            return service.listarPorVenda(venda, pageable);
        }
        if (produtoId != null) {
            Produto produto = new Produto();
            produto.setId(produtoId);
            return service.listarPorProduto(produto, pageable);
        }
        return service.listar(pageable);
    }

    @GetMapping("/{id}")
    public ItemVenda buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemVenda salvar(@RequestBody ItemVenda itemVenda) {
        return service.salvar(itemVenda);
    }

    @PutMapping("/{id}")
    public ItemVenda atualizar(
            @PathVariable Long id,
            @RequestBody ItemVenda itemVenda) {

        return service.atualizar(id, itemVenda);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {

        service.excluir(id);

    }

}
