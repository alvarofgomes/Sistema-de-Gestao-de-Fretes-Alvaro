package br.com.sistema_frete.BO;

import br.com.sistema_frete.DAO.SolicitacaoFreteDAO;
import br.com.sistema_frete.enums.solicitacao.StatusSolicitacaoFrete;
import br.com.sistema_frete.exception.CadastroException;
import br.com.sistema_frete.exception.FreteException;
import br.com.sistema_frete.exception.NegocioException;
import br.com.sistema_frete.model.SolicitacaoFrete;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class SolicitacaoFreteBO {

    private final SolicitacaoFreteDAO dao = new SolicitacaoFreteDAO();

    private static final List<String> UFS_VALIDAS = Arrays.asList(
        "AC","AL","AP","AM","BA","CE","DF","ES","GO","MA",
        "MT","MS","MG","PA","PB","PR","PE","PI","RJ","RN",
        "RS","RO","RR","SC","SP","SE","TO"
    );

    public void criar(SolicitacaoFrete solicitacao,
                      Integer clienteIdSessao) throws NegocioException {
    	try {
    	    if (clienteIdSessao == null) {
    	        throw new CadastroException("Sessão inválida. Faça login novamente.");
    	    }

    	    solicitacao.getCliente().setId(clienteIdSessao);

    	    validarCampos(solicitacao);

    	    solicitacao.setStatus(StatusSolicitacaoFrete.PENDENTE);
    	    dao.inserir(solicitacao);

    	} catch (CadastroException e) {
    	    throw e;
    	} catch (Exception e) {
    	    System.err.println("Erro ao criar solicitação:");
    	    e.printStackTrace();
    	    throw new NegocioException("Não foi possível criar a solicitação.", e);
    	}
    }

    public void cancelar(Integer id,
                         Integer clienteIdSessao) throws NegocioException {
        try {
            if (id == null)
                throw new CadastroException("ID da solicitação não informado.");

            SolicitacaoFrete s = dao.buscarPorId(id);

            if (s == null)
                throw new CadastroException("Solicitação não encontrada.");

            // garante que cliente só cancela as próprias
            if (!s.getCliente().getId().equals(clienteIdSessao))
                throw new FreteException("Acesso negado.");

            if (s.getStatus() != StatusSolicitacaoFrete.PENDENTE)
                throw new FreteException(
                    "Apenas solicitações PENDENTES podem ser canceladas.");

            dao.atualizarStatus(id, StatusSolicitacaoFrete.CANCELADA, null, null);

        } catch (CadastroException | FreteException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Erro ao cancelar solicitação:");
            e.printStackTrace();
            throw new NegocioException("Não foi possível cancelar a solicitação.", e);
        }
    }

    public void aprovar(Integer id,
                        Integer usuarioAnaliseId) throws NegocioException {
        try {
            SolicitacaoFrete s = buscarValidando(id);

            if (s.getStatus() != StatusSolicitacaoFrete.PENDENTE)
                throw new FreteException(
                    "Apenas solicitações PENDENTES podem ser aprovadas.");

            dao.atualizarStatus(id, StatusSolicitacaoFrete.APROVADA,
                    usuarioAnaliseId, null);

        } catch (CadastroException | FreteException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Erro ao aprovar solicitação:");
            e.printStackTrace();
            throw new NegocioException("Não foi possível aprovar a solicitação.", e);
        }
    }

    public void recusar(Integer id, Integer usuarioAnaliseId,
                        String motivo) throws NegocioException {
        try {
            if (motivo == null || motivo.trim().isEmpty())
                throw new CadastroException("O motivo da recusa é obrigatório.");

            SolicitacaoFrete s = buscarValidando(id);

            if (s.getStatus() != StatusSolicitacaoFrete.PENDENTE)
                throw new FreteException(
                    "Apenas solicitações PENDENTES podem ser recusadas.");

            dao.atualizarStatus(id, StatusSolicitacaoFrete.RECUSADA,
                    usuarioAnaliseId, motivo.trim());

        } catch (CadastroException | FreteException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Erro ao recusar solicitação:");
            e.printStackTrace();
            throw new NegocioException("Não foi possível recusar a solicitação.", e);
        }
    }

    public List<SolicitacaoFrete> listarPorCliente(Integer clienteId,
            String filtro, String status, int pagina,
            int registrosPorPagina) throws NegocioException {
        try {
            int offset = (pagina - 1) * registrosPorPagina;
            return dao.buscarPorClientePaginado(clienteId, filtro,
                    status, offset, registrosPorPagina);
        } catch (Exception e) {
            System.err.println("Erro ao listar solicitações do cliente:");
            e.printStackTrace();
            throw new NegocioException("Não foi possível listar as solicitações.", e);
        }
    }

    public int contarPorCliente(Integer clienteId, String filtro,
                                 String status) throws NegocioException {
        try {
            return dao.contarPorCliente(clienteId, filtro, status);
        } catch (Exception e) {
            System.err.println("Erro ao contar solicitações do cliente:");
            e.printStackTrace();
            throw new NegocioException("Erro ao contar solicitações.", e);
        }
    }

    public List<SolicitacaoFrete> listarTodas(String filtro, String status,
            int pagina, int registrosPorPagina) throws NegocioException {
        try {
            int offset = (pagina - 1) * registrosPorPagina;
            return dao.buscarComPaginacao(filtro, status, offset, registrosPorPagina);
        } catch (Exception e) {
            System.err.println("Erro ao listar todas as solicitações:");
            e.printStackTrace();
            throw new NegocioException("Não foi possível listar as solicitações.", e);
        }
    }

    public int contarTodas(String filtro, String status) throws NegocioException {
        try {
            return dao.contarTotal(filtro, status);
        } catch (Exception e) {
            System.err.println("Erro ao contar solicitações:");
            e.printStackTrace();
            throw new NegocioException("Erro ao contar solicitações.", e);
        }
    }

    public int contarPendentesPorCliente(Integer clienteId) {
        try {
            return dao.contarPendentesPorCliente(clienteId);
        } catch (Exception e) {
            System.err.println("Erro ao contar pendentes:");
            e.printStackTrace();
            return 0;
        }
    }

    public int contarPendentesGeral() {
        try {
            return dao.contarPendentesGeral();
        } catch (Exception e) {
            System.err.println("Erro ao contar pendentes geral:");
            e.printStackTrace();
            return 0;
        }
    }

    public SolicitacaoFrete buscarPorId(Integer id) throws NegocioException {
        try {
            if (id == null)
                throw new CadastroException("ID da solicitação não informado.");

            SolicitacaoFrete s = dao.buscarPorId(id);

            if (s == null)
                throw new CadastroException("Solicitação não encontrada.");

            return s;

        } catch (CadastroException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Erro ao buscar solicitação:");
            e.printStackTrace();
            throw new NegocioException("Não foi possível buscar a solicitação.", e);
        }
    }

    public void marcarComoConvertida(Integer id,
                                      Integer usuarioId) throws NegocioException {
        try {
            if (id == null)
                throw new CadastroException("ID da solicitação não informado.");

            SolicitacaoFrete s = dao.buscarPorId(id);

            if (s == null)
                throw new CadastroException("Solicitação não encontrada.");

            if (s.getStatus() == StatusSolicitacaoFrete.CONVERTIDA)
                throw new CadastroException(
                    "Esta solicitação já foi convertida em frete.");

            if (s.getStatus() == StatusSolicitacaoFrete.RECUSADA ||
                s.getStatus() == StatusSolicitacaoFrete.CANCELADA)
                throw new CadastroException(
                    "Não é possível converter uma solicitação " +
                    s.getStatus().name().toLowerCase() + ".");

            if (s.getStatus() != StatusSolicitacaoFrete.APROVADA)
                throw new CadastroException(
                    "Apenas solicitações APROVADAS podem ser convertidas em frete.");

            dao.atualizarStatus(id, StatusSolicitacaoFrete.CONVERTIDA,
                    usuarioId, null);

        } catch (CadastroException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Erro ao marcar solicitação como convertida:");
            e.printStackTrace();
            throw new NegocioException(
                "Não foi possível converter a solicitação.", e);
        }
    }

    private SolicitacaoFrete buscarValidando(Integer id) throws NegocioException {
        if (id == null)
            throw new CadastroException("ID da solicitação não informado.");
        SolicitacaoFrete s = dao.buscarPorId(id);
        if (s == null)
            throw new CadastroException("Solicitação não encontrada.");
        return s;
    }

    private void validarCampos(SolicitacaoFrete solicitacao) throws CadastroException {
        if (solicitacao.getCidadeOrigem() == null || solicitacao.getCidadeOrigem().trim().isEmpty()) {
            throw new CadastroException("Cidade de origem é obrigatória.");
        }

        if (solicitacao.getUfOrigem() == null || solicitacao.getUfOrigem().trim().isEmpty()) {
            throw new CadastroException("UF de origem é obrigatória.");
        }

        if (solicitacao.getCidadeDestino() == null || solicitacao.getCidadeDestino().trim().isEmpty()) {
            throw new CadastroException("Cidade de destino é obrigatória.");
        }

        if (solicitacao.getUfDestino() == null || solicitacao.getUfDestino().trim().isEmpty()) {
            throw new CadastroException("UF de destino é obrigatória.");
        }

        if (solicitacao.getDescricaoCarga() == null || solicitacao.getDescricaoCarga().trim().isEmpty()) {
            throw new CadastroException("Descrição da carga é obrigatória.");
        }

        if (solicitacao.getPesoKg() == null || solicitacao.getPesoKg().doubleValue() <= 0) {
            throw new CadastroException("Peso da carga deve ser maior que zero.");
        }

        if (solicitacao.getVolumes() == null || solicitacao.getVolumes() <= 0) {
            throw new CadastroException("Quantidade de volumes deve ser maior que zero.");
        }
    }
}