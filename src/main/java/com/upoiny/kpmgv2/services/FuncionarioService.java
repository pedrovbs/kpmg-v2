package com.upoiny.kpmgv2.services;

import com.upoiny.kpmgv2.entities.Funcionario;
import com.upoiny.kpmgv2.repositories.FuncionarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



@Service
public class FuncionarioService {


    @Autowired
    private FuncionarioRepository repository;



    // ============================
    // Listar funcionários
    // ============================

    public Page<Funcionario> listar(
            Pageable pageable
    ){

        return repository.findAll(pageable);

    }



    // ============================
    // Buscar por ID
    // ============================

    public Funcionario buscarPorId(Long id){

        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Funcionário não encontrado."
                        )
                );

    }



    // ============================
    // Criar funcionário
    // ============================

    public Funcionario salvar(
            Funcionario funcionario
    ){


        if(repository.existsByCpf(funcionario.getCpf())){

            throw new RuntimeException(
                    "CPF já cadastrado."
            );

        }


        funcionario.setStatus("ATIVO");


        funcionario.setDataCadastro(
                java.time.LocalDate.now()
        );


        funcionario.setUltimaAtualizacao(
                java.time.LocalDate.now()
        );


        return repository.save(funcionario);

    }

    public long contarFuncionarios() {
        return repository.count();
    }




    // ============================
    // Atualizar funcionário
    // ============================

    public Funcionario atualizar(
            Long id,
            Funcionario dados
    ){

        Funcionario funcionario =
                buscarPorId(id);

        funcionario.setNome(
                dados.getNome()
        );
        funcionario.setTelefone(
                dados.getTelefone()
        );

        funcionario.setEmail(
                dados.getEmail()
        );

        funcionario.setDepartamento(
                dados.getDepartamento()
        );
        funcionario.setCep(dados.getCep());

        funcionario.setLogradouro(dados.getLogradouro());

        funcionario.setNumero(dados.getNumero());

        funcionario.setComplemento(dados.getComplemento());

        funcionario.setBairro(dados.getBairro());

        funcionario.setCidade(dados.getCidade());

        funcionario.setEstado(dados.getEstado());


        funcionario.setCargo(
                dados.getCargo()
        );


        funcionario.setSalario(
                dados.getSalario()
        );

        funcionario.setUltimaAtualizacao(
                java.time.LocalDate.now()
        );

        return repository.save(funcionario);

    }



    // ============================
    // Excluir
    // ============================

    public void excluir(Long id){

        Funcionario funcionario =
                buscarPorId(id);


        repository.delete(funcionario);

    }

    // ============================
    // Pesquisar nome
    // ============================

    public Page<Funcionario> buscarNome(
            String nome,
            Pageable pageable
    ){

        return repository
                .findByNomeContainingIgnoreCase(
                        nome,
                        pageable
                );

    }

}