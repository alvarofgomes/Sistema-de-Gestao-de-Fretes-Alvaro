package br.com.sistema_frete.model;

import java.time.LocalDate;
import br.com.sistema_frete.enums.motorista.*;

public class Motorista {

    private Integer id;
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    private String telefone;
    private String cnhNumero;
    private CategoriaCNH cnhCategoria;
    private LocalDate cnhValidade;
    private TipoVinculo tipoVinculo;
    private StatusMotorista status;

    public Motorista() {
	}
    
	public Motorista(Integer id, String nome, String cpf, LocalDate dataNascimento, String telefone, String cnhNumero,
			CategoriaCNH cnhCategoria, LocalDate cnhValidade, TipoVinculo tipoVinculo, StatusMotorista status) {
		this.id = id;
		this.nome = nome;
		this.cpf = cpf;
		this.dataNascimento = dataNascimento;
		this.telefone = telefone;
		this.cnhNumero = cnhNumero;
		this.cnhCategoria = cnhCategoria;
		this.cnhValidade = cnhValidade;
		this.tipoVinculo = tipoVinculo;
		this.status = status;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCnhNumero() {
        return cnhNumero;
    }

    public void setCnhNumero(String cnhNumero) {
        this.cnhNumero = cnhNumero;
    }

    public CategoriaCNH getCnhCategoria() {
        return cnhCategoria;
    }

    public void setCnhCategoria(CategoriaCNH cnhCategoria) {
        this.cnhCategoria = cnhCategoria;
    }

    public LocalDate getCnhValidade() {
        return cnhValidade;
    }

    public void setCnhValidade(LocalDate cnhValidade) {
        this.cnhValidade = cnhValidade;
    }

    public TipoVinculo getTipoVinculo() {
        return tipoVinculo;
    }

    public void setTipoVinculo(TipoVinculo tipoVinculo) {
        this.tipoVinculo = tipoVinculo;
    }

    public StatusMotorista getStatus() {
        return status;
    }

    public void setStatus(StatusMotorista status) {
        this.status = status;
    }
}