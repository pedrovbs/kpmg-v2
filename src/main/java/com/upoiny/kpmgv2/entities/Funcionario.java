package com.upoiny.kpmgv2.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "funcionarios")
@Getter
@Setter
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ============================
    // Dados Pessoais
    // ============================

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(unique = true)
    private String rg;

    private LocalDate dataNascimento;

    @Column(nullable = false)
    private String sexo;

    private String estadoCivil;

    // ============================
    // Contato
    // ============================

    @Column(nullable = false)
    private String telefone;

    @Column(nullable = false)
    private String email;

    // ============================
    // Endereço
    // ============================

    private String cep;

    private String logradouro;

    private String numero;

    private String complemento;

    private String bairro;

    private String cidade;

    private String estado;

    // ============================
    // Dados Profissionais
    // ============================

    @Column(nullable = false)
    private LocalDate dataAdmissao;

    private LocalDate dataDemissao;

    @Column(nullable = false)
    private String matricula;

    @Column(nullable = false)
    private String status;

    // ATIVO
    // AFASTADO
    // FERIAS
    // DESLIGADO

    @ManyToOne
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;

    @ManyToOne
    @JoinColumn(name = "cargo_id")
    private Cargo cargo;

    // ============================
    // Dados Financeiros
    // ============================

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal salario;

    // ============================
    // Controle
    // ============================

    private LocalDate dataCadastro;

    private LocalDate ultimaAtualizacao;

}