package br.com.sistema_frete.model;

import br.com.sistema_frete.enums.ocorrencia.TipoOcorrencia;
import java.time.LocalDateTime;

public class Ocorrencia {

	private Integer id;
	private Integer idFrete;
	private TipoOcorrencia tipo;
	private LocalDateTime dataHora;
	private String municipio;
	private String uf;
	private String descricao;
	private String nomeRecebedor;
	private String documentoRecebedor;

	public Ocorrencia() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdFrete() {
		return idFrete;
	}

	public void setIdFrete(Integer idFrete) {
		this.idFrete = idFrete;
	}

	public TipoOcorrencia getTipo() {
		return tipo;
	}

	public void setTipo(TipoOcorrencia tipo) {
		this.tipo = tipo;
	}

	public LocalDateTime getDataHora() {
		return dataHora;
	}

	public void setDataHora(LocalDateTime dataHora) {
		this.dataHora = dataHora;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getNomeRecebedor() {
		return nomeRecebedor;
	}

	public void setNomeRecebedor(String nomeRecebedor) {
		this.nomeRecebedor = nomeRecebedor;
	}

	public String getDocumentoRecebedor() {
		return documentoRecebedor;
	}

	public void setDocumentoRecebedor(String documentoRecebedor) {
		this.documentoRecebedor = documentoRecebedor;
	}
}