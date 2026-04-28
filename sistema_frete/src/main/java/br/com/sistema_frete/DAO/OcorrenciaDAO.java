package br.com.sistema_frete.DAO;

import br.com.sistema_frete.enums.ocorrencia.TipoOcorrencia;
import br.com.sistema_frete.model.Ocorrencia;
import br.com.sistema_frete.util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OcorrenciaDAO {

    public void inserir(Ocorrencia ocorrencia, Connection conn) throws SQLException {
        String sql = "INSERT INTO ocorrencia_frete " +
                     "(id_frete, tipo, data_hora, municipio, uf, descricao, nome_recebedor, documento_recebedor) " +
                     "VALUES (?,?,?,?,?,?,?,?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ocorrencia.getIdFrete());
            ps.setString(2, ocorrencia.getTipo().name());
            ps.setTimestamp(3, Timestamp.valueOf(ocorrencia.getDataHora()));
            ps.setString(4, ocorrencia.getMunicipio());
            ps.setString(5, ocorrencia.getUf());
            ps.setString(6, ocorrencia.getDescricao());
            ps.setString(7, ocorrencia.getNomeRecebedor());
            ps.setString(8, ocorrencia.getDocumentoRecebedor());
            ps.executeUpdate();
        }
    }

    public List<Ocorrencia> listarPorFrete(Integer idFrete) {
        List<Ocorrencia> lista = new ArrayList<>();

        String sql = "SELECT * FROM ocorrencia_frete WHERE id_frete=? ORDER BY data_hora ASC";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idFrete);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ocorrencia o = new Ocorrencia();
                    o.setId(rs.getInt("id"));
                    o.setIdFrete(rs.getInt("id_frete"));
                    o.setTipo(TipoOcorrencia.valueOf(rs.getString("tipo")));
                    o.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime());
                    o.setMunicipio(rs.getString("municipio"));
                    o.setUf(rs.getString("uf"));
                    o.setDescricao(rs.getString("descricao"));
                    o.setNomeRecebedor(rs.getString("nome_recebedor"));
                    o.setDocumentoRecebedor(rs.getString("documento_recebedor"));
                    lista.add(o);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao listar ocorrências.", e);
        }
        return lista;
    }

    public Ocorrencia buscarMaisRecente(Integer idFrete, Connection conn) throws SQLException {
        String sql = "SELECT * FROM ocorrencia_frete WHERE id_frete=? ORDER BY data_hora DESC LIMIT 1";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idFrete);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Ocorrencia o = new Ocorrencia();
                    o.setId(rs.getInt("id"));
                    o.setIdFrete(rs.getInt("id_frete"));
                    o.setTipo(TipoOcorrencia.valueOf(rs.getString("tipo")));
                    o.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime());
                    o.setMunicipio(rs.getString("municipio"));
                    o.setUf(rs.getString("uf"));
                    return o;
                }
            }
        }
        return null;
    }
}