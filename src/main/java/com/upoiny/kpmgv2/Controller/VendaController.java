package com.upoiny.kpmgv2.Controller;

import com.upoiny.kpmgv2.dto.*;
import com.upoiny.kpmgv2.entities.Venda;
import com.upoiny.kpmgv2.services.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@RestController
@RequestMapping("/api/vendas")
@CrossOrigin(origins = "*")
public class VendaController {

    @Autowired
    private VendaService service;


    @GetMapping
    public Page<VendaResponse> listar(Pageable pageable) {
        return service.listar(pageable);
    }
    @GetMapping("/{id}")
    public Venda buscarPorId(@PathVariable Long id) {

        return service.buscarPorId(id);

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Venda salvar(@RequestBody VendaRequest venda) {

        System.out.println("========== JSON RECEBIDO ==========");

        System.out.println("Cliente ID: " + venda.getClienteId());
        System.out.println("Funcionário ID: " + venda.getFuncionarioId());
        System.out.println("Forma de Pagamento: " + venda.getFormaPagamento());
        System.out.println("Desconto: " + venda.getDesconto());
        System.out.println("Observações: " + venda.getObservacoes());

        System.out.println("Itens:");

        if (venda.getItens() != null) {
            venda.getItens().forEach(item -> {
                System.out.println(
                        "Produto ID: " + item.getProdutoId()
                                + " | Quantidade: " + item.getQuantidade()
                );
            });
        }

        System.out.println("===================================");

        return service.realizarVenda(venda);
    }



    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {

        service.excluir(id);

    }

    @GetMapping("/cliente-maior-comprador")
    public ResponseEntity<ClienteMaiorCompradorDTO> obterClienteMaiorComprador() {

        ClienteMaiorCompradorDTO dto = service.obterClienteMaiorComprador();

        if (dto == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/kpis/clientes-que-mais-compraram")
    public List<ClienteVendaKpiResponse> buscarClientesQueMaisCompraram() {

        return service.buscarClientesQueMaisCompraram();
    }
    @GetMapping("/kpis/resumo-financeiro")
    public VendaValorKpiResponse buscarResumoFinanceiroVendas() {

        return service.buscarResumoFinanceiroVendas();
    }

    @GetMapping("/kpis/produtos-mais-vendidos")
    public List<ProdutoVendaKpiResponse> buscarProdutosMaisVendidos() {

        return service.buscarProdutosMaisVendidos();
    }

}
