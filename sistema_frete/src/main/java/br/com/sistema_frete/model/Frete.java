package br.com.sistema_frete.model;

import br.com.sistema_frete.enums.frete.FreteStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Frete {

	private Integer id;
	private String numero;
	private Cliente remetente;
	private Cliente destinatario;
	private Motorista motorista;
	private Veiculo veiculo;
	private String municipioOrigem;
	private String ufOrigem;
	private String municipioDestino;
	private String ufDestino;
	private String descricaoCarga;
	private BigDecimal pesoKg;
	private Integer volumes;
	private BigDecimal valorFrete;
	private BigDecimal aliquotaIcms;
	private BigDecimal valorIcms;
	private BigDecimal valorTotal;
	private FreteStatus status;
	private LocalDate dataEmissao;
	private LocalDate dataPrevisaoEntrega;
	private LocalDateTime dataSaida;
	private LocalDateTime dataEntrega;

	public Frete() {
	}

	
	
	public Frete(Integer id, String numero, Cliente remetente, Cliente destinatario, Motorista motorista,
			Veiculo veiculo, String municipioOrigem, String ufOrigem, String municipioDestino, String ufDestino,
			String descricaoCarga, BigDecimal pesoKg, Integer volumes, BigDecimal valorFrete, BigDecimal aliquotaIcms,
			BigDecimal valorIcms, BigDecimal valorTotal, FreteStatus status, LocalDate dataEmissao,
			LocalDate dataPrevisaoEntrega, LocalDateTime dataSaida, LocalDateTime dataEntrega) {
		this.id = id;
		this.numero = numero;
		this.remetente = remetente;
		this.destinatario = destinatario;
		this.motorista = motorista;
		this.veiculo = veiculo;
		this.municipioOrigem = municipioOrigem;
		this.ufOrigem = ufOrigem;
		this.municipioDestino = municipioDestino;
		this.ufDestino = ufDestino;
		this.descricaoCarga = descricaoCarga;
		this.pesoKg = pesoKg;
		this.volumes = volumes;
		this.valorFrete = valorFrete;
		this.aliquotaIcms = aliquotaIcms;
		this.valorIcms = valorIcms;
		this.valorTotal = valorTotal;
		this.status = status;
		this.dataEmissao = dataEmissao;
		this.dataPrevisaoEntrega = dataPrevisaoEntrega;
		this.dataSaida = dataSaida;
		this.dataEntrega = dataEntrega;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Cliente getRemetente() {
		return remetente;
	}

	public void setRemetente(Cliente remetente) {
		this.remetente = remetente;
	}

	public Cliente getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(Cliente destinatario) {
		this.destinatario = destinatario;
	}

	public Motorista getMotorista() {
		return motorista;
	}

	public void setMotorista(Motorista motorista) {
		this.motorista = motorista;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public String getMunicipioOrigem() {
		return municipioOrigem;
	}

	public void setMunicipioOrigem(String municipioOrigem) {
		this.municipioOrigem = municipioOrigem;
	}

	public String getUfOrigem() {
		return ufOrigem;
	}

	public void setUfOrigem(String ufOrigem) {
		this.ufOrigem = ufOrigem;
	}

	public String getMunicipioDestino() {
		return municipioDestino;
	}

	public void setMunicipioDestino(String municipioDestino) {
		this.municipioDestino = municipioDestino;
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

	public BigDecimal getValorFrete() {
		return valorFrete;
	}

	public void setValorFrete(BigDecimal valorFrete) {
		this.valorFrete = valorFrete;
	}

	public BigDecimal getAliquotaIcms() {
		return aliquotaIcms;
	}

	public void setAliquotaIcms(BigDecimal aliquotaIcms) {
		this.aliquotaIcms = aliquotaIcms;
	}

	public BigDecimal getValorIcms() {
		return valorIcms;
	}

	public void setValorIcms(BigDecimal valorIcms) {
		this.valorIcms = valorIcms;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public FreteStatus getStatus() {
		return status;
	}

	public void setStatus(FreteStatus status) {
		this.status = status;
	}

	public LocalDate getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(LocalDate dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public LocalDate getDataPrevisaoEntrega() {
		return dataPrevisaoEntrega;
	}

	public void setDataPrevisaoEntrega(LocalDate dataPrevisaoEntrega) {
		this.dataPrevisaoEntrega = dataPrevisaoEntrega;
	}

	public LocalDateTime getDataSaida() {
		return dataSaida;
	}

	public void setDataSaida(LocalDateTime dataSaida) {
		this.dataSaida = dataSaida;
	}

	public LocalDateTime getDataEntrega() {
		return dataEntrega;
	}

	public void setDataEntrega(LocalDateTime dataEntrega) {
		this.dataEntrega = dataEntrega;
	}
}