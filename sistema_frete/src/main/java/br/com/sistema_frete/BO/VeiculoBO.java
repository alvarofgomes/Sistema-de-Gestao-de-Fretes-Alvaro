package br.com.sistema_frete.BO;

import java.math.BigDecimal;
import java.util.List;

import br.com.sistema_frete.DAO.VeiculoDAO;
import br.com.sistema_frete.exception.CadastroException;
import br.com.sistema_frete.exception.NegocioException;
import br.com.sistema_frete.model.Veiculo;
import br.com.sistema_frete.enums.veiculo.StatusVeiculo;

public class VeiculoBO {

    private final VeiculoDAO veiculoDAO = new VeiculoDAO();

    public List<Veiculo> listarComPaginacao(String filtro, String statusFiltro, int pagina, int registrosPorPagina) throws NegocioException {
        try {
            int offset = (pagina - 1) * registrosPorPagina;
            return veiculoDAO.buscarComPaginacao(filtro, statusFiltro, offset, registrosPorPagina);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NegocioException("Não foi possível listar os veículos.", e);
        }
    }

    public int contarTotal(String filtro, String statusFiltro) throws NegocioException {
        try {
            return veiculoDAO.contarTotal(filtro, statusFiltro);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NegocioException("Não foi possível obter o total de veículos.", e);
        }
    }

    public void salvar(Veiculo veiculo) throws NegocioException {
        try {
            validarCamposObrigatorios(veiculo);
            validarNumericos(veiculo);

            if (veiculo.getId() != null) {
                Veiculo existente = veiculoDAO.buscarPorId(veiculo.getId());

                if (existente == null) {
                    throw new CadastroException("Veículo não encontrado para edição.");
                }

                Veiculo mesmaPlaca = veiculoDAO.buscarPorPlaca(veiculo.getPlaca());
                if (mesmaPlaca != null && !mesmaPlaca.getId().equals(veiculo.getId())) {
                    throw new CadastroException("Já existe veículo cadastrado com esta placa.");
                }
            } else {
                Veiculo duplicado = veiculoDAO.buscarPorPlaca(veiculo.getPlaca());
                if (duplicado != null) {
                    throw new CadastroException("Já existe veículo cadastrado com esta placa.");
                }
            }

            veiculoDAO.salvar(veiculo);

        } catch (CadastroException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new NegocioException("Não foi possível salvar o veículo.", e);
        }
    }

    private void validarCamposObrigatorios(Veiculo veiculo) throws CadastroException {
        if (veiculo == null) {
            throw new CadastroException("Veículo não informado.");
        }

        if (veiculo.getPlaca() == null || veiculo.getPlaca().trim().isEmpty()) {
            throw new CadastroException("A placa é obrigatória.");
        }

        if (!veiculo.getPlaca().matches("^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$")) {
            throw new CadastroException("A placa informada é inválida.");
        }

        if (veiculo.getRntrc() == null || veiculo.getRntrc().trim().isEmpty()) {
            throw new CadastroException("O RNTRC é obrigatório.");
        }

        if (veiculo.getAnoFabricacao() == null) {
            throw new CadastroException("O ano de fabricação é obrigatório.");
        }

        if (veiculo.getTipo() == null) {
            throw new CadastroException("O tipo do veículo é obrigatório.");
        }

        if (veiculo.getStatus() == null) {
            throw new CadastroException("O status do veículo é obrigatório.");
        }
    }

    private void validarNumericos(Veiculo veiculo) throws CadastroException {
        if (menorOuIgualZero(veiculo.getTaraKg())) {
            throw new CadastroException("A tara deve ser maior que zero.");
        }

        if (menorOuIgualZero(veiculo.getCapacidadeKg())) {
            throw new CadastroException("A capacidade de carga deve ser maior que zero.");
        }

        if (menorOuIgualZero(veiculo.getVolumeM3())) {
            throw new CadastroException("O volume deve ser maior que zero.");
        }
    }

    private boolean menorOuIgualZero(BigDecimal valor) {
        return valor == null || valor.compareTo(BigDecimal.ZERO) <= 0;
    }
    
    public void inativar(Integer id) throws NegocioException {
        try {
            if (id == null) {
                throw new CadastroException("ID do veículo não informado.");
            }

            Veiculo veiculo = veiculoDAO.buscarPorId(id);

            if (veiculo == null) {
                throw new CadastroException("Veículo não encontrado.");
            }

            if (veiculo.getStatus() == StatusVeiculo.EM_VIAGEM) {
                throw new CadastroException("Veículo não pode ser inativado: está em viagem.");
            }

            veiculoDAO.inativar(id);

        } catch (CadastroException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new NegocioException("Não foi possível inativar o veículo.", e);
        }
    }
    
}