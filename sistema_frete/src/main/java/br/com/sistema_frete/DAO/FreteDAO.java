package br.com.sistema_frete.DAO;

import br.com.sistema_frete.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FreteDAO {

    public int contarFretesPorCliente(Long idCliente) {
        String sql = "SELECT COUNT(*) AS total " +
                     "FROM frete " +
                     "WHERE id_remetente = ? OR id_destinatario = ?";

        try (Connection conn = new ConnectionFactory().recuperarConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, idCliente);
            ps.setLong(2, idCliente);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao contar fretes por cliente.", e);
        }

        return 0;
    }

    public boolean buscarFretesAtivosPorMotorista(Long idMotorista) {
        String sql = "SELECT 1 " +
                     "FROM frete " +
                     "WHERE id_motorista = ? " +
                     "AND status IN ('EMITIDO', 'SAIDA_CONFIRMADA', 'EM_TRANSITO') " +
                     "LIMIT 1";

        try (Connection conn = new ConnectionFactory().recuperarConexao();
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
}