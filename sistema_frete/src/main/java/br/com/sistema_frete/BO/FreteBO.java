package br.com.sistema_frete.BO;

import br.com.sistema_frete.DAO.FreteDAO;
import br.com.sistema_frete.DAO.MotoristaDAO;
import br.com.sistema_frete.DAO.OcorrenciaDAO;
import br.com.sistema_frete.DAO.VeiculoDAO;
import br.com.sistema_frete.enums.frete.FreteStatus;
import br.com.sistema_frete.enums.veiculo.StatusVeiculo;
import br.com.sistema_frete.exception.FreteException;
import br.com.sistema_frete.exception.NegocioException;
import br.com.sistema_frete.model.Frete;
import br.com.sistema_frete.model.Motorista;
import br.com.sistema_frete.model.Ocorrencia;
import br.com.sistema_frete.model.Veiculo;
import br.com.sistema_frete.util.ConnectionFactory;
import br.com.sistema_frete.util.GeradorNumeroFrete;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

public class FreteBO {

    private final FreteDAO freteDAO = new FreteDAO();
    private final VeiculoDAO veiculoDAO = new VeiculoDAO();
    private final MotoristaDAO motoristaDAO = new MotoristaDAO();
    private final OcorrenciaDAO ocorrenciaDAO = new OcorrenciaDAO();

    public List<Frete> listarComPaginacao(String filtro, String statusFiltro,
                                          int pagina, int registrosPorPagina) throws NegocioException {
        try {
            int offset = (pagina - 1) * registrosPorPagina;
            return freteDAO.buscarComPaginacao(filtro, statusFiltro, offset, registrosPorPagina);
        } catch (Exception e) {
            System.err.println("Erro ao listar fretes:");
            e.printStackTrace();
            throw new NegocioException("Não foi possível listar os fretes.", e);
        }
    }

    public int contarTotal(String filtro, String statusFiltro) throws NegocioException {
        try {
            return freteDAO.contarTotal(filtro, statusFiltro);
        } catch (Exception e) {
            System.err.println("Erro ao contar fretes:");
            e.printStackTrace();
            throw new NegocioException("Não foi possível contar os fretes.", e);
        }
    }

    public Frete buscarPorId(Integer id) throws NegocioException {
        try {
            if (id == null) throw new FreteException("ID do frete não informado.");
            Frete frete = freteDAO.buscarPorId(id);
            if (frete == null) throw new FreteException("Frete não encontrado.");
            return frete;
        } catch (FreteException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Erro ao buscar frete:");
            e.printStackTrace();
            throw new NegocioException("Não foi possível buscar o frete.", e);
        }
    }

    public void registrarFrete(Frete frete) throws NegocioException {
        Connection conn = null;
        try {

            validarCamposObrigatorios(frete);

            Veiculo veiculo = veiculoDAO.buscarPorId(frete.getVeiculo().getId());
            if (veiculo == null)
                throw new FreteException("Veículo não encontrado.");
            if (veiculo.getStatus() != StatusVeiculo.DISPONIVEL)
                throw new FreteException("O veículo selecionado não está disponível.");
            if (frete.getPesoKg().compareTo(veiculo.getCapacidadeKg()) > 0)
                throw new FreteException("O peso da carga (" + frete.getPesoKg() + " kg) " +
                        "excede a capacidade do veículo (" + veiculo.getCapacidadeKg() + " kg).");

            Motorista motorista = motoristaDAO.buscarPorId(frete.getMotorista().getId());
            if (motorista == null)
                throw new FreteException("Motorista não encontrado.");
            if (motorista.getStatus().name().equals("INATIVO") ||
                motorista.getStatus().name().equals("SUSPENSO"))
                throw new FreteException("O motorista selecionado não está ativo.");
            if (freteDAO.buscarFretesAtivosPorMotorista(motorista.getId().longValue()))
                throw new FreteException("O motorista já possui um frete em andamento.");
            if (motorista.getCnhValidade() == null ||
                motorista.getCnhValidade().isBefore(frete.getDataEmissao()))
                throw new FreteException("A CNH do motorista está vencida ou inválida.");

            if (!frete.getDataPrevisaoEntrega().isAfter(frete.getDataEmissao()))
                throw new FreteException("A data prevista de entrega deve ser posterior à data de emissão.");

            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            String numero = GeradorNumeroFrete.gerar(conn);
            frete.setNumero(numero);
            frete.setStatus(FreteStatus.EMITIDO);
            frete.setDataEmissao(LocalDate.now());

            freteDAO.inserir(frete, conn);
            conn.commit();

        } catch (FreteException e) {
            rollback(conn);
            throw e;
        } catch (Exception e) {
            rollback(conn);
            System.err.println("Erro ao registrar frete:");
            e.printStackTrace();
            throw new NegocioException("Não foi possível registrar o frete.", e);
        } finally {
            fechar(conn);
        }
    }

    public void confirmarSaida(Integer idFrete) throws NegocioException {
        Connection conn = null;
        try {
            Frete frete = freteDAO.buscarPorId(idFrete);
            if (frete == null) throw new FreteException("Frete não encontrado.");
            if (frete.getStatus() != FreteStatus.EMITIDO)
                throw new FreteException("Apenas fretes com status EMITIDO podem ter saída confirmada.");

            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            freteDAO.atualizarDataSaida(idFrete, conn);

            freteDAO.atualizarStatusVeiculo(frete.getVeiculo().getId(), StatusVeiculo.EM_VIAGEM, conn);

            conn.commit();

        } catch (FreteException e) {
            rollback(conn);
            throw e;
        } catch (Exception e) {
            rollback(conn);
            System.err.println("Erro ao confirmar saída:");
            e.printStackTrace();
            throw new NegocioException("Não foi possível confirmar a saída.", e);
        } finally {
            fechar(conn);
        }
    }

    public void cancelar(Integer idFrete) throws NegocioException {
        Connection conn = null;
        try {
            Frete frete = freteDAO.buscarPorId(idFrete);
            if (frete == null) throw new FreteException("Frete não encontrado.");
            if (frete.getStatus() != FreteStatus.EMITIDO)
                throw new FreteException("Somente fretes com status EMITIDO podem ser cancelados.");

            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            freteDAO.atualizarStatus(idFrete, FreteStatus.CANCELADO, conn);
            conn.commit();

        } catch (FreteException e) {
            rollback(conn);
            throw e;
        } catch (Exception e) {
            rollback(conn);
            System.err.println("Erro ao cancelar frete:");
            e.printStackTrace();
            throw new NegocioException("Não foi possível cancelar o frete.", e);
        } finally {
            fechar(conn);
        }
    }

    public void registrarOcorrencia(Ocorrencia ocorrencia) throws NegocioException {
        Connection conn = null;
        try {
            Frete frete = freteDAO.buscarPorId(ocorrencia.getIdFrete());
            if (frete == null) throw new FreteException("Frete não encontrado.");

            // status que não aceitam ocorrência
            if (frete.getStatus() == FreteStatus.ENTREGUE ||
                frete.getStatus() == FreteStatus.NAO_ENTREGUE ||
                frete.getStatus() == FreteStatus.CANCELADO)
                throw new FreteException("Não é possível registrar ocorrência em frete " +
                        frete.getStatus().name() + ".");

            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            Ocorrencia maisRecente = ocorrenciaDAO.buscarMaisRecente(frete.getId(), conn);
            if (maisRecente != null &&
                !ocorrencia.getDataHora().isAfter(maisRecente.getDataHora()))
                throw new FreteException("A data/hora da ocorrência deve ser posterior à última ocorrência registrada.");

            // validações por tipo
            switch (ocorrencia.getTipo()) {
                case AVARIA:
                case EXTRAVIO:
                case OUTROS:
                    if (ocorrencia.getDescricao() == null || ocorrencia.getDescricao().trim().isEmpty())
                        throw new FreteException("Descrição obrigatória para o tipo " +
                                ocorrencia.getTipo().name() + ".");
                    break;

                case ENTREGA_REALIZADA:
                    if (ocorrencia.getNomeRecebedor() == null || ocorrencia.getNomeRecebedor().trim().isEmpty())
                        throw new FreteException("Nome do recebedor é obrigatório para Entrega Realizada.");
                    if (ocorrencia.getDocumentoRecebedor() == null || ocorrencia.getDocumentoRecebedor().trim().isEmpty())
                        throw new FreteException("Documento do recebedor é obrigatório para Entrega Realizada.");
                    break;

                default:
                    break;
            }

            ocorrenciaDAO.inserir(ocorrencia, conn);

            // transições automáticas de status
            if (ocorrencia.getTipo() == br.com.sistema_frete.enums.ocorrencia.TipoOcorrencia.EM_ROTA
                && frete.getStatus() == FreteStatus.SAIDA_CONFIRMADA) {
                freteDAO.atualizarStatus(frete.getId(), FreteStatus.EM_TRANSITO, conn);
            }

            if (ocorrencia.getTipo() == br.com.sistema_frete.enums.ocorrencia.TipoOcorrencia.ENTREGA_REALIZADA) {
                freteDAO.atualizarDataEntrega(frete.getId(), FreteStatus.ENTREGUE, conn);
                freteDAO.atualizarStatusVeiculo(frete.getVeiculo().getId(), StatusVeiculo.DISPONIVEL, conn);
            }

            conn.commit();

        } catch (FreteException e) {
            rollback(conn);
            throw e;
        } catch (Exception e) {
            rollback(conn);
            System.err.println("Erro ao registrar ocorrência:");
            e.printStackTrace();
            throw new NegocioException("Não foi possível registrar a ocorrência.", e);
        } finally {
            fechar(conn);
        }
    }

    private void rollback(Connection conn) {
        if (conn != null) {
            try { conn.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
        }
    }

    private void fechar(Connection conn) {
        if (conn != null) {
            try { conn.close(); } catch (Exception ex) { ex.printStackTrace(); }
        }
    }

    private void validarCamposObrigatorios(Frete frete) throws FreteException {
        if (frete.getRemetente() == null || frete.getRemetente().getId() == null)
            throw new FreteException("Remetente é obrigatório.");
        if (frete.getDestinatario() == null || frete.getDestinatario().getId() == null)
            throw new FreteException("Destinatário é obrigatório.");
        if (frete.getRemetente().getId().equals(frete.getDestinatario().getId()))
            throw new FreteException("Remetente e destinatário não podem ser o mesmo cliente.");
        if (frete.getMotorista() == null || frete.getMotorista().getId() == null)
            throw new FreteException("Motorista é obrigatório.");
        if (frete.getVeiculo() == null || frete.getVeiculo().getId() == null)
            throw new FreteException("Veículo é obrigatório.");
        if (frete.getMunicipioOrigem() == null || frete.getMunicipioOrigem().trim().isEmpty())
            throw new FreteException("Município de origem é obrigatório.");
        if (frete.getUfOrigem() == null || frete.getUfOrigem().trim().isEmpty())
            throw new FreteException("UF de origem é obrigatória.");
        if (frete.getMunicipioDestino() == null || frete.getMunicipioDestino().trim().isEmpty())
            throw new FreteException("Município de destino é obrigatório.");
        if (frete.getUfDestino() == null || frete.getUfDestino().trim().isEmpty())
            throw new FreteException("UF de destino é obrigatória.");
        if (frete.getDescricaoCarga() == null || frete.getDescricaoCarga().trim().isEmpty())
            throw new FreteException("Descrição da carga é obrigatória.");
        if (frete.getPesoKg() == null || frete.getPesoKg().compareTo(java.math.BigDecimal.ZERO) <= 0)
            throw new FreteException("Peso da carga deve ser maior que zero.");
        if (frete.getVolumes() == null || frete.getVolumes() <= 0)
            throw new FreteException("Quantidade de volumes deve ser maior que zero.");
        if (frete.getValorFrete() == null || frete.getValorFrete().compareTo(java.math.BigDecimal.ZERO) <= 0)
            throw new FreteException("Valor do frete deve ser maior que zero.");
        if (frete.getDataPrevisaoEntrega() == null)
            throw new FreteException("Data prevista de entrega é obrigatória.");
    }
}