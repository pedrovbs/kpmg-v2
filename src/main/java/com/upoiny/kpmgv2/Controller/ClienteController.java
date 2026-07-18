package com.upoiny.kpmgv2.Controller;

import com.upoiny.kpmgv2.entities.Cliente;
import com.upoiny.kpmgv2.services.ClienteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @GetMapping
    public Map<String, Object> listar(@RequestParam(required = false) String search, Pageable pageable) {
        Page<Cliente> page = (search == null || search.isBlank())
                ? service.listar(pageable)
                : service.pesquisarPorNome(search, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("clientes", page.getContent());
        response.put("totalClientes", page.getTotalElements());
        response.put("clientesNaPagina", page.getNumberOfElements());

        return response;
    }

    @GetMapping("/qtdClientes")
    public Map<String, Long> qtdClientes() {
        long total = service.contarClientes();

        Map<String, Long> response = new HashMap<>();
        response.put("totalClientes", total);

        return response;
    }


    @GetMapping("/pesquisar")
    public Page<Cliente> pesquisar(@RequestParam String nome, Pageable pageable) {
        return service.pesquisarPorNome(nome, pageable);
    }

    @GetMapping("/{id}")
    public Cliente buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente salvar(@RequestBody Cliente cliente) {
        return service.salvar(cliente);
    }

    @PutMapping("/{id}")
    public Cliente atualizar(@PathVariable Long id, @RequestBody Cliente cliente) {
        return service.atualizar(id, cliente);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }
}
