package br.com.sistema_frete.DAO;

import br.com.sistema_frete.enums.solicitacao.StatusSolicitacaoFrete;
import br.com.sistema_frete.model.Cliente;
import br.com.sistema_frete.model.SolicitacaoFrete;
import br.com.sistema_frete.util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SolicitacaoFreteDAO {

    public void inserir(SolicitacaoFrete s) {
        String sql = "INSERT INTO solicitacao_frete " +
                     "(id_cliente, cidade_origem, uf_origem, cidade_destino, uf_destino, " +
                     "descricao_carga, peso_kg, volumes, observacao, status, data_solicitacao) " +
                     "VALUES (?,?,?,?,?,?,?,?,?,?,NOW())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1,    s.getCliente().getId());
            ps.setString(2, s.getCidadeOrigem());
            ps.setString(3, s.getUfOrigem());
            ps.setString(4, s.getCidadeDestino());
            ps.setString(5, s.getUfDestino());
            ps.setString(6, s.getDescricaoCarga());
            ps.setBigDecimal(7, s.getPesoKg());
            ps.setInt(8,    s.getVolumes());
            ps.setString(9, s.getObservacao());
            ps.setString(10, s.getStatus().name());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao inserir solicitação.", e);
        }
    }

    public SolicitacaoFrete buscarPorId(Integer id) {
        String sql = "SELECT s.*, c.razao_social AS razao_cliente, " +
                     "u.nome AS nome_analise " +
                     "FROM solicitacao_frete s " +
                     "JOIN cliente c ON c.id = s.id_cliente " +
                     "LEFT JOIN usuario u ON u.id = s.usuario_analise_id " +
                     "WHERE s.id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar solicitação.", e);
        }
        return null;
    }

    public List<SolicitacaoFrete> buscarPorClientePaginado(Integer clienteId,
            String filtro, String status, int offset, int limit) {

        String sql = "SELECT s.*, c.razao_social AS razao_cliente, " +
                     "u.nome AS nome_analise " +
                     "FROM solicitacao_frete s " +
                     "JOIN cliente c ON c.id = s.id_cliente " +
                     "LEFT JOIN usuario u ON u.id = s.usuario_analise_id " +
                     "WHERE s.id_cliente = ? " +
                     "AND (s.cidade_destino ILIKE ? OR s.descricao_carga ILIKE ?) " +
                     "AND (? = '' OR s.status = ?) " +
                     "ORDER BY s.data_solicitacao DESC LIMIT ? OFFSET ?";

        return executarListagem(sql, clienteId, filtro, status, offset, limit);
    }

    public int contarPorCliente(Integer clienteId, String filtro, String status) {
        String sql = "SELECT COUNT(*) FROM solicitacao_frete s " +
                     "WHERE s.id_cliente = ? " +
                     "AND (s.cidade_destino ILIKE ? OR s.descricao_carga ILIKE ?) " +
                     "AND (? = '' OR s.status = ?)";

        return executarContagem(sql, clienteId, filtro, status);
    }

    public List<SolicitacaoFrete> buscarComPaginacao(String filtro,
            String status, int offset, int limit) {

        String sql = "SELECT s.*, c.razao_social AS razao_cliente, " +
                     "u.nome AS nome_analise " +
                     "FROM solicitacao_frete s " +
                     "JOIN cliente c ON c.id = s.id_cliente " +
                     "LEFT JOIN usuario u ON u.id = s.usuario_analise_id " +
                     "WHERE (c.razao_social ILIKE ? OR s.descricao_carga ILIKE ?) " +
                     "AND (? = '' OR s.status = ?) " +
                     "ORDER BY s.data_solicitacao DESC LIMIT ? OFFSET ?";

        return executarListagemAdmin(sql, filtro, status, offset, limit);
    }

    public int contarTotal(String filtro, String status) {
        String sql = "SELECT COUNT(*) FROM solicitacao_frete s " +
                     "JOIN cliente c ON c.id = s.id_cliente " +
                     "WHERE (c.razao_social ILIKE ? OR s.descricao_carga ILIKE ?) " +
                     "AND (? = '' OR s.status = ?)";

        return executarContagemAdmin(sql, filtro, status);
    }

    public void atualizarStatus(Integer id, StatusSolicitacaoFrete status,
                                 Integer usuarioAnaliseId, String motivoRecusa) {
        String sql = "UPDATE solicitacao_frete SET status=?, data_analise=NOW(), " +
                     "usuario_analise_id=?, motivo_recusa=? WHERE id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status.name());
            if (usuarioAnaliseId != null) ps.setInt(2, usuarioAnaliseId);
            else                          ps.setNull(2, Types.INTEGER);
            ps.setString(3, motivoRecusa);
            ps.setInt(4, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar status da solicitação.", e);
        }
    }

    public int contarPendentesPorCliente(Integer clienteId) {
        String sql = "SELECT COUNT(*) FROM solicitacao_frete " +
                     "WHERE id_cliente=? AND status='PENDENTE'";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao contar solicitações pendentes.", e);
        }
    }

    public int contarPendentesGeral() {
        String sql = "SELECT COUNT(*) FROM solicitacao_frete WHERE status='PENDENTE'";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao contar solicitações pendentes.", e);
        }
    }

    private List<SolicitacaoFrete> executarListagem(String sql, Integer clienteId,
            String filtro, String status, int offset, int limit) {

        List<SolicitacaoFrete> lista = new ArrayList<>();
        String like = "%" + (filtro == null ? "" : filtro.trim()) + "%";
        String sf   = status == null ? "" : status.trim();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, clienteId);
            ps.setString(2, like);
            ps.setString(3, like);
            ps.setString(4, sf);
            ps.setString(5, sf);
            ps.setInt(6, limit);
            ps.setInt(7, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao listar solicitações.", e);
        }
        return lista;
    }

    private int executarContagem(String sql, Integer clienteId,
            String filtro, String status) {

        String like = "%" + (filtro == null ? "" : filtro.trim()) + "%";
        String sf   = status == null ? "" : status.trim();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, clienteId);
            ps.setString(2, like);
            ps.setString(3, like);
            ps.setString(4, sf);
            ps.setString(5, sf);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao contar solicitações.", e);
        }
    }

    private List<SolicitacaoFrete> executarListagemAdmin(String sql,
            String filtro, String status, int offset, int limit) {

        List<SolicitacaoFrete> lista = new ArrayList<>();
        String like = "%" + (filtro == null ? "" : filtro.trim()) + "%";
        String sf   = status == null ? "" : status.trim();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, sf);
            ps.setString(4, sf);
            ps.setInt(5, limit);
            ps.setInt(6, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao listar solicitações.", e);
        }
        return lista;
    }

    private int executarContagemAdmin(String sql, String filtro, String status) {
        String like = "%" + (filtro == null ? "" : filtro.trim()) + "%";
        String sf   = status == null ? "" : status.trim();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, sf);
            ps.setString(4, sf);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao contar solicitações.", e);
        }
    }

    private SolicitacaoFrete mapear(ResultSet rs) throws SQLException {
        SolicitacaoFrete s = new SolicitacaoFrete();
        s.setId(rs.getInt("id"));
        s.setCidadeOrigem(rs.getString("cidade_origem"));
        s.setUfOrigem(rs.getString("uf_origem"));
        s.setCidadeDestino(rs.getString("cidade_destino"));
        s.setUfDestino(rs.getString("uf_destino"));
        s.setDescricaoCarga(rs.getString("descricao_carga"));
        s.setPesoKg(rs.getBigDecimal("peso_kg"));
        s.setVolumes(rs.getInt("volumes"));
        s.setObservacao(rs.getString("observacao"));
        s.setMotivoRecusa(rs.getString("motivo_recusa"));

        String status = rs.getString("status");
        if (status != null) s.setStatus(StatusSolicitacaoFrete.valueOf(status));

        Timestamp dataSol = rs.getTimestamp("data_solicitacao");
        if (dataSol != null) s.setDataSolicitacao(dataSol.toLocalDateTime());

        Timestamp dataAna = rs.getTimestamp("data_analise");
        if (dataAna != null) s.setDataAnalise(dataAna.toLocalDateTime());

        int usuarioId = rs.getInt("usuario_analise_id");
        if (!rs.wasNull()) s.setUsuarioAnaliseId(usuarioId);

        s.setNomeUsuarioAnalise(rs.getString("nome_analise"));

        Cliente c = new Cliente();
        c.setId(rs.getInt("id_cliente"));
        c.setRazaoSocial(rs.getString("razao_cliente"));
        s.setCliente(c);

        return s;
    }
}