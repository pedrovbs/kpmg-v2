package com.upoiny.kpmgv2.services;

import com.upoiny.kpmgv2.dto.ItemVendaRequest;
import com.upoiny.kpmgv2.dto.VendaRequest;
import com.upoiny.kpmgv2.entities.Cliente;
import com.upoiny.kpmgv2.entities.Funcionario;
import com.upoiny.kpmgv2.entities.ItemVenda;
import com.upoiny.kpmgv2.entities.Produto;
import com.upoiny.kpmgv2.entities.Venda;
import com.upoiny.kpmgv2.repositories.ClienteRepository;
import com.upoiny.kpmgv2.repositories.FuncionarioRepository;
import com.upoiny.kpmgv2.repositories.ProdutoRepository;
import com.upoiny.kpmgv2.repositories.VendaRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ClienteRepository clienteRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final ProdutoRepository produtoRepository;

    public VendaService(VendaRepository vendaRepository,
                        ClienteRepository clienteRepository,
                        FuncionarioRepository funcionarioRepository,
                        ProdutoRepository produtoRepository) {
        this.vendaRepository = vendaRepository;
        this.clienteRepository = clienteRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.produtoRepository = produtoRepository;
    }

    public Page<Venda> listar(Pageable pageable) {
        return vendaRepository.findAll(pageable);
    }

    public Venda buscarPorId(Long id) {
        return vendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada."));
    }

    public Page<Venda> listarPorCliente(Cliente cliente, Pageable pageable) {
        return vendaRepository.findByCliente(cliente, pageable);
    }

    public Page<Venda> listarPorFuncionario(Funcionario funcionario, Pageable pageable) {
        return vendaRepository.findByFuncionario(funcionario, pageable);
    }

    public Page<Venda> listarPorStatus(String status, Pageable pageable) {
        return vendaRepository.findByStatusContainingIgnoreCase(status, pageable);
    }

    public Page<Venda> listarPorFormaPagamento(String formaPagamento, Pageable pageable) {
        return vendaRepository.findByFormaPagamentoContainingIgnoreCase(formaPagamento, pageable);
    }

    @Transactional
    public Venda realizarVenda(VendaRequest request) {
        if (request.getItens() == null || request.getItens().isEmpty()) {
            throw new RuntimeException("A venda deve possuir ao menos um item.");
        }
        if (request.getFormaPagamento() == null || request.getFormaPagamento().isBlank()) {
            throw new RuntimeException("A forma de pagamento é obrigatória.");
        }

        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));
        Funcionario funcionario = funcionarioRepository.findById(request.getFuncionarioId())
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado."));

        Venda venda = new Venda();
        venda.setCliente(cliente);
        venda.setFuncionario(funcionario);
        venda.setDataVenda(LocalDateTime.now());
        venda.setFormaPagamento(request.getFormaPagamento());
        venda.setStatus("FINALIZADA");
        venda.setObservacoes(request.getObservacoes());

        double valorTotal = 0.0;
        for (ItemVendaRequest itemRequest : request.getItens()) {
            if (itemRequest.getQuantidade() == null || itemRequest.getQuantidade() <= 0) {
                throw new RuntimeException("A quantidade do item deve ser maior que zero.");
            }

            Produto produto = produtoRepository.findById(itemRequest.getProdutoId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado."));
            if (produto.getPrecoVenda() == null) {
                throw new RuntimeException("Produto sem preço de venda cadastrado.");
            }
            if (produto.getEstoque() == null || produto.getEstoque() < itemRequest.getQuantidade()) {
                throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNome());
            }

            double subtotal = produto.getPrecoVenda() * itemRequest.getQuantidade();
            ItemVenda item = new ItemVenda();
            item.setVenda(venda);
            item.setProduto(produto);
            item.setQuantidade(itemRequest.getQuantidade());
            item.setValorUnitario(produto.getPrecoVenda());
            item.setSubtotal(subtotal);
            venda.getItens().add(item);

            produto.setEstoque(produto.getEstoque() - itemRequest.getQuantidade());
            produtoRepository.save(produto);
            valorTotal += subtotal;
        }

        double desconto = request.getDesconto() == null ? 0.0 : request.getDesconto();
        if (desconto < 0 || desconto > valorTotal) {
            throw new RuntimeException("O desconto deve estar entre zero e o valor total da venda.");
        }
        venda.setValorTotal(valorTotal);
        venda.setDesconto(desconto);
        venda.setValorFinal(valorTotal - desconto);

        return vendaRepository.save(venda);
    }

    @Transactional
    public Venda atualizar(Long id, Venda atualizada) {
        Venda venda = buscarPorId(id);
        venda.setFormaPagamento(atualizada.getFormaPagamento());
        venda.setObservacoes(atualizada.getObservacoes());
        return vendaRepository.save(venda);
    }

    @Transactional
    public void excluir(Long id) {
        Venda venda = buscarPorId(id);
        for (ItemVenda item : venda.getItens()) {
            Produto produto = item.getProduto();
            produto.setEstoque(produto.getEstoque() + item.getQuantidade());
            produtoRepository.save(produto);
        }
        vendaRepository.delete(venda);
    }
}
