package com.upoiny.kpmgv2.services;

import com.upoiny.kpmgv2.entities.Cliente;
import com.upoiny.kpmgv2.repositories.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.upoiny.kpmgv2.entities.Cliente;
import com.upoiny.kpmgv2.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;

    /**
     * Lista todos os clientes com paginação.
     */
    public Page<Cliente> listar(Pageable pageable) {
        return repository.findAll(pageable);
    }

    /**
     * Busca um cliente pelo ID.
     */
    public Cliente buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));
    }

    /**
     * Busca um cliente pelo CPF.
     */
    public Optional<Cliente> buscarPorCpf(String cpf) {
        return repository.findByCpf(cpf);
    }

    /**
     * Pesquisa clientes pelo nome.
     */
    public Page<Cliente> pesquisarPorNome(String nome, Pageable pageable) {
        return repository.findByNomeContainingIgnoreCase(nome, pageable);
    }

    /**
     * Pesquisa clientes pelo e-mail.
     */
    public Page<Cliente> pesquisarPorEmail(String email, Pageable pageable) {
        return repository.findByEmailContainingIgnoreCase(email, pageable);
    }

    /**
     * Pesquisa clientes pelo telefone.
     */
    public Page<Cliente> pesquisarPorTelefone(String telefone, Pageable pageable) {
        return repository.findByTelefoneContaining(telefone, pageable);
    }

    /**
     * Cadastra um novo cliente.
     */
    public Cliente salvar(Cliente cliente) {

        if (repository.existsByCpf(cliente.getCpf())) {
            throw new RuntimeException("Já existe um cliente cadastrado com este CPF.");
        }

        return repository.save(cliente);
    }

    /**
     * Atualiza um cliente existente.
     */
    public Cliente atualizar(Long id, Cliente clienteAtualizado) {

        Cliente cliente = buscarPorId(id);

        if (!cliente.getCpf().equals(clienteAtualizado.getCpf())
                && repository.existsByCpf(clienteAtualizado.getCpf())) {
            throw new RuntimeException("Já existe um cliente cadastrado com este CPF.");
        }

        cliente.setNome(clienteAtualizado.getNome());
        cliente.setCpf(clienteAtualizado.getCpf());
        cliente.setEmail(clienteAtualizado.getEmail());
        cliente.setTelefone(clienteAtualizado.getTelefone());
        cliente.setCidade(clienteAtualizado.getCidade());
        cliente.setEstado(clienteAtualizado.getEstado());

        return repository.save(cliente);
    }

    /**
     * Remove um cliente.
     */
    public void excluir(Long id) {

        Cliente cliente = buscarPorId(id);

        repository.delete(cliente);
    }

}
