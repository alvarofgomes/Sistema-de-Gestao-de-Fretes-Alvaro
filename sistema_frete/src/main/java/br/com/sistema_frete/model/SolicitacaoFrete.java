package br.com.sistema_frete.model;

import br.com.sistema_frete.enums.solicitacao.StatusSolicitacaoFrete;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SolicitacaoFrete {

	private Integer id;
	private Cliente cliente;
	private String cidadeOrigem;
	private String ufOrigem;
	private String cidadeDestino;
	private String ufDestino;
	private String descricaoCarga;
	private BigDecimal pesoKg;
	private Integer volumes;
	private String observacao;
	private StatusSolicitacaoFrete status;
	private LocalDateTime dataSolicitacao;
	private LocalDateTime dataAnalise;
	private Integer usuarioAnaliseId;
	private String nomeUsuarioAnalise;
	private String motivoRecusa;

	public SolicitacaoFrete() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getCidadeOrigem() {
		return cidadeOrigem;
	}

	public void setCidadeOrigem(String cidadeOrigem) {
		this.cidadeOrigem = cidadeOrigem;
	}

	public String getUfOrigem() {
		return ufOrigem;
	}

	public void setUfOrigem(String ufOrigem) {
		this.ufOrigem = ufOrigem;
	}

	public String getCidadeDestino() {
		return cidadeDestino;
	}

	public void setCidadeDestino(String cidadeDestino) {
		this.cidadeDestino = cidadeDestino;
	}

	public String getUfDestino() {
		return ufDestino;
	}

	public void setUfDestino(String ufDestino) {
		this.ufDestino = ufDestino;
	}

	public String getDescricaoCarga() {
		return descricaoCarga;
	}

	public void setDescricaoCarga(String descricaoCarga) {
		this.descricaoCarga = descricaoCarga;
	}

	public BigDecimal getPesoKg() {
		return pesoKg;
	}

	public void setPesoKg(BigDecimal pesoKg) {
		this.pesoKg = pesoKg;
	}

	public Integer getVolumes() {
		return volumes;
	}

	public void setVolumes(Integer volumes) {
		this.volumes = volumes;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public StatusSolicitacaoFrete getStatus() {
		return status;
	}

	public void setStatus(StatusSolicitacaoFrete status) {
		this.status = status;
	}

	public LocalDateTime getDataSolicitacao() {
		return dataSolicitacao;
	}

	public void setDataSolicitacao(LocalDateTime dataSolicitacao) {
		this.dataSolicitacao = dataSolicitacao;
	}

	public LocalDateTime getDataAnalise() {
		return dataAnalise;
	}

	public void setDataAnalise(LocalDateTime dataAnalise) {
		this.dataAnalise = dataAnalise;
	}

	public Integer getUsuarioAnaliseId() {
		return usuarioAnaliseId;
	}

	public void setUsuarioAnaliseId(Integer usuarioAnaliseId) {
		this.usuarioAnaliseId = usuarioAnaliseId;
	}

	public String getNomeUsuarioAnalise() {
		return nomeUsuarioAnalise;
	}

	public void setNomeUsuarioAnalise(String nomeUsuarioAnalise) {
		this.nomeUsuarioAnalise = nomeUsuarioAnalise;
	}

	public String getMotivoRecusa() {
		return motivoRecusa;
	}

	public void setMotivoRecusa(String motivoRecusa) {
		this.motivoRecusa = motivoRecusa;
	}
}