package com.upoiny.kpmgv2.Controller;

import com.upoiny.kpmgv2.entities.Venda;
import com.upoiny.kpmgv2.dto.VendaRequest;
import com.upoiny.kpmgv2.services.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendas")
@CrossOrigin(origins = "*")
public class VendaController {

    @Autowired
    private VendaService service;

    @GetMapping
    public Page<Venda> listar(@RequestParam(required = false) String search,
                              @RequestParam(required = false) String status,
                              @RequestParam(required = false) String formaPagamento,
                              Pageable pageable) {
        String termo = search == null || search.isBlank() ? status : search;
        if (termo != null && !termo.isBlank()) {
            return service.listarPorStatus(termo, pageable);
        }
        if (formaPagamento != null && !formaPagamento.isBlank()) {
            return service.listarPorFormaPagamento(formaPagamento, pageable);
        }
        return service.listar(pageable);

    }

    @GetMapping("/{id}")
    public Venda buscarPorId(@PathVariable Long id) {

        return service.buscarPorId(id);

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Venda salvar(@RequestBody VendaRequest venda) {
        return service.realizarVenda(venda);
    }

    @PutMapping("/{id}")
    public Venda atualizar(
            @PathVariable Long id,
            @RequestBody Venda venda) {

        return service.atualizar(id, venda);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {

        service.excluir(id);

    }

}
