package br.com.sistema_frete.model;

import java.math.BigDecimal;
import br.com.sistema_frete.enums.veiculo.*;

public class Veiculo {

    private Integer id;
    private String placa;
    private String rntrc;
    private Integer anoFabricacao;
    private TipoVeiculo tipo;
    private BigDecimal taraKg;
    private BigDecimal capacidadeKg;
    private BigDecimal volumeM3;
    private StatusVeiculo status;

    public Veiculo() {
	}

	public Veiculo(Integer id, String placa, String rntrc, Integer anoFabricacao, TipoVeiculo tipo, BigDecimal taraKg,
			BigDecimal capacidadeKg, BigDecimal volumeM3, StatusVeiculo status) {

		this.id = id;
		this.placa = placa;
		this.rntrc = rntrc;
		this.anoFabricacao = anoFabricacao;
		this.tipo = tipo;
		this.taraKg = taraKg;
		this.capacidadeKg = capacidadeKg;
		this.volumeM3 = volumeM3;
		this.status = status;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getRntrc() {
        return rntrc;
    }

    public void setRntrc(String rntrc) {
        this.rntrc = rntrc;
    }

    public Integer getAnoFabricacao() {
        return anoFabricacao;
    }

    public void setAnoFabricacao(Integer anoFabricacao) {
        this.anoFabricacao = anoFabricacao;
    }

    public TipoVeiculo getTipo() {
        return tipo;
    }

    public void setTipo(TipoVeiculo tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getTaraKg() {
        return taraKg;
    }

    public void setTaraKg(BigDecimal taraKg) {
        this.taraKg = taraKg;
    }

    public BigDecimal getCapacidadeKg() {
        return capacidadeKg;
    }

    public void setCapacidadeKg(BigDecimal capacidadeKg) {
        this.capacidadeKg = capacidadeKg;
    }

    public BigDecimal getVolumeM3() {
        return volumeM3;
    }

    public void setVolumeM3(BigDecimal volumeM3) {
        this.volumeM3 = volumeM3;
    }

    public StatusVeiculo getStatus() {
        return status;
    }

    public void setStatus(StatusVeiculo status) {
        this.status = status;
    }
}