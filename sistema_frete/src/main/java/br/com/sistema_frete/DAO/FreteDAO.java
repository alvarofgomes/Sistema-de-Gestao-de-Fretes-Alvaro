package br.com.sistema_frete.DAO;

import br.com.sistema_frete.enums.frete.FreteStatus;
import br.com.sistema_frete.enums.veiculo.StatusVeiculo;
import br.com.sistema_frete.model.Cliente;
import br.com.sistema_frete.model.Frete;
import br.com.sistema_frete.model.Motorista;
import br.com.sistema_frete.model.Veiculo;
import br.com.sistema_frete.util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FreteDAO {

    public int contarFretesPorCliente(Long idCliente) {
        String sql = "SELECT COUNT(*) FROM frete WHERE id_remetente=? OR id_destinatario=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idCliente);
            ps.setLong(2, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao contar fretes por cliente.", e);
        }
    }

    public boolean buscarFretesAtivosPorMotorista(Long idMotorista) {
        String sql = "SELECT 1 FROM frete WHERE id_motorista=? " +
                     "AND status IN ('EMITIDO','SAIDA_CONFIRMADA','EM_TRANSITO') LIMIT 1";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idMotorista);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar fretes ativos por motorista.", e);
        }
    }

    public void inserir(Frete frete, Connection conn) throws SQLException {
        String sql = "INSERT INTO frete (numero, id_remetente, id_destinatario, id_motorista, " +
                     "id_veiculo, municipio_origem, uf_origem, municipio_destino, uf_destino, " +
                     "descricao_carga, peso_kg, volumes, valor_frete, aliquota_icms, valor_icms, " +
                     "valor_total, status, data_emissao, data_previsao_entrega) " +
                     "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, frete.getNumero());
            ps.setInt(2, frete.getRemetente().getId());
            ps.setInt(3, frete.getDestinatario().getId());
            ps.setInt(4, frete.getMotorista().getId());
            ps.setInt(5, frete.getVeiculo().getId());
            ps.setString(6, frete.getMunicipioOrigem());
            ps.setString(7, frete.getUfOrigem());
            ps.setString(8, frete.getMunicipioDestino());
            ps.setString(9, frete.getUfDestino());
            ps.setString(10, frete.getDescricaoCarga());
            ps.setBigDecimal(11, frete.getPesoKg());
            ps.setInt(12, frete.getVolumes());
            ps.setBigDecimal(13, frete.getValorFrete());
            ps.setBigDecimal(14, frete.getAliquotaIcms());
            ps.setBigDecimal(15, frete.getValorIcms());
            ps.setBigDecimal(16, frete.getValorTotal());
            ps.setString(17, frete.getStatus().name());
            ps.setDate(18, Date.valueOf(frete.getDataEmissao()));
            ps.setDate(19, Date.valueOf(frete.getDataPrevisaoEntrega()));
            ps.executeUpdate();
        }
    }

    public void atualizarStatus(Integer idFrete, FreteStatus novoStatus, Connection conn) throws SQLException {
        String sql = "UPDATE frete SET status=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, novoStatus.name());
            ps.setInt(2, idFrete);
            ps.executeUpdate();
        }
    }

    public void atualizarDataSaida(Integer idFrete, Connection conn) throws SQLException {
        String sql = "UPDATE frete SET data_saida=NOW(), status=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, FreteStatus.SAIDA_CONFIRMADA.name());
            ps.setInt(2, idFrete);
            ps.executeUpdate();
        }
    }

    public void atualizarDataEntrega(Integer idFrete, FreteStatus status, Connection conn) throws SQLException {
        String sql = "UPDATE frete SET data_entrega=NOW(), status=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setInt(2, idFrete);
            ps.executeUpdate();
        }
    }

    public Frete buscarPorId(Integer id) {
        String sql = "SELECT f.*, " +
                     "r.id r_id, r.razao_social r_razao, r.nome_fantasia r_fantasia, " +
                     "d.id d_id, d.razao_social d_razao, d.nome_fantasia d_fantasia, " +
                     "m.id m_id, m.nome m_nome, m.cpf m_cpf, " +
                     "v.id v_id, v.placa v_placa, v.tipo v_tipo, v.capacidade_kg v_cap " +
                     "FROM frete f " +
                     "JOIN cliente r ON r.id = f.id_remetente " +
                     "JOIN cliente d ON d.id = f.id_destinatario " +
                     "JOIN motorista m ON m.id = f.id_motorista " +
                     "JOIN veiculo v ON v.id = f.id_veiculo " +
                     "WHERE f.id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearFrete(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar frete por ID.", e);
        }
        return null;
    }

    public List<Frete> buscarComPaginacao(String filtro, String statusFiltro, int offset, int limit) {
        List<Frete> fretes = new ArrayList<>();

        String sql = "SELECT f.id, f.numero, f.status, f.data_emissao, f.data_previsao_entrega, " +
                     "r.razao_social r_razao, d.razao_social d_razao, " +
                     "m.nome m_nome, v.placa v_placa, " +
                     "f.municipio_destino, f.uf_destino " +
                     "FROM frete f " +
                     "JOIN cliente r ON r.id = f.id_remetente " +
                     "JOIN cliente d ON d.id = f.id_destinatario " +
                     "JOIN motorista m ON m.id = f.id_motorista " +
                     "JOIN veiculo v ON v.id = f.id_veiculo " +
                     "WHERE (f.numero ILIKE ? OR r.razao_social ILIKE ? OR d.razao_social ILIKE ?) " +
                     "AND (? = '' OR f.status = ?) " +
                     "ORDER BY f.id DESC LIMIT ? OFFSET ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String like = "%" + (filtro == null ? "" : filtro.trim()) + "%";
            String sf = statusFiltro == null ? "" : statusFiltro.trim();
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            ps.setString(4, sf);
            ps.setString(5, sf);
            ps.setInt(6, limit);
            ps.setInt(7, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Frete f = new Frete();
                    f.setId(rs.getInt("id"));
                    f.setNumero(rs.getString("numero"));
                    f.setStatus(FreteStatus.valueOf(rs.getString("status")));
                    f.setDataEmissao(rs.getDate("data_emissao").toLocalDate());
                    f.setDataPrevisaoEntrega(rs.getDate("data_previsao_entrega").toLocalDate());
                    f.setMunicipioDestino(rs.getString("municipio_destino"));
                    f.setUfDestino(rs.getString("uf_destino"));

                    Cliente r = new Cliente(); r.setRazaoSocial(rs.getString("r_razao"));
                    Cliente d = new Cliente(); d.setRazaoSocial(rs.getString("d_razao"));
                    Motorista m = new Motorista(); m.setNome(rs.getString("m_nome"));
                    Veiculo v = new Veiculo(); v.setPlaca(rs.getString("v_placa"));

                    f.setRemetente(r); f.setDestinatario(d);
                    f.setMotorista(m); f.setVeiculo(v);
                    fretes.add(f);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao listar fretes.", e);
        }
        return fretes;
    }

    public int contarTotal(String filtro, String statusFiltro) {
        String sql = "SELECT COUNT(*) FROM frete f " +
                     "JOIN cliente r ON r.id = f.id_remetente " +
                     "JOIN cliente d ON d.id = f.id_destinatario " +
                     "WHERE (f.numero ILIKE ? OR r.razao_social ILIKE ? OR d.razao_social ILIKE ?) " +
                     "AND (? = '' OR f.status = ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String like = "%" + (filtro == null ? "" : filtro.trim()) + "%";
            String sf = statusFiltro == null ? "" : statusFiltro.trim();
            ps.setString(1, like); ps.setString(2, like); ps.setString(3, like);
            ps.setString(4, sf);  ps.setString(5, sf);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao contar fretes.", e);
        }
    }

    public void atualizarStatusVeiculo(Integer idVeiculo, StatusVeiculo status, Connection conn) throws SQLException {
        String sql = "UPDATE veiculo SET status=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setInt(2, idVeiculo);
            ps.executeUpdate();
        }
    }

    private Frete mapearFrete(ResultSet rs) throws SQLException {
        Frete f = new Frete();
        f.setId(rs.getInt("id"));
        f.setNumero(rs.getString("numero"));
        f.setStatus(FreteStatus.valueOf(rs.getString("status")));
        f.setMunicipioOrigem(rs.getString("municipio_origem"));
        f.setUfOrigem(rs.getString("uf_origem"));
        f.setMunicipioDestino(rs.getString("municipio_destino"));
        f.setUfDestino(rs.getString("uf_destino"));
        f.setDescricaoCarga(rs.getString("descricao_carga"));
        f.setPesoKg(rs.getBigDecimal("peso_kg"));
        f.setVolumes(rs.getInt("volumes"));
        f.setValorFrete(rs.getBigDecimal("valor_frete"));
        f.setAliquotaIcms(rs.getBigDecimal("aliquota_icms"));
        f.setValorIcms(rs.getBigDecimal("valor_icms"));
        f.setValorTotal(rs.getBigDecimal("valor_total"));
        f.setDataEmissao(rs.getDate("data_emissao").toLocalDate());
        f.setDataPrevisaoEntrega(rs.getDate("data_previsao_entrega").toLocalDate());

        Timestamp saida = rs.getTimestamp("data_saida");
        if (saida != null) f.setDataSaida(saida.toLocalDateTime());

        Timestamp entrega = rs.getTimestamp("data_entrega");
        if (entrega != null) f.setDataEntrega(entrega.toLocalDateTime());

        Cliente r = new Cliente();
        r.setId(rs.getInt("r_id"));
        r.setRazaoSocial(rs.getString("r_razao"));
        r.setNomeFantasia(rs.getString("r_fantasia"));

        Cliente d = new Cliente();
        d.setId(rs.getInt("d_id"));
        d.setRazaoSocial(rs.getString("d_razao"));
        d.setNomeFantasia(rs.getString("d_fantasia"));

        Motorista m = new Motorista();
        m.setId(rs.getInt("m_id"));
        m.setNome(rs.getString("m_nome"));
        m.setCpf(rs.getString("m_cpf"));

        Veiculo v = new Veiculo();
        v.setId(rs.getInt("v_id"));
        v.setPlaca(rs.getString("v_placa"));
        v.setCapacidadeKg(rs.getBigDecimal("v_cap"));

        f.setRemetente(r); f.setDestinatario(d);
        f.setMotorista(m); f.setVeiculo(v);
        return f;
    }
}