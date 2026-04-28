package br.com.sistema_frete.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;

public final class GeradorNumeroFrete {

    private GeradorNumeroFrete() {}

    public static String gerar(Connection conn) throws SQLException {
        int ano = Year.now().getValue();

        String sql = "SELECT COUNT(*) AS total FROM frete " +
                     "WHERE EXTRACT(YEAR FROM data_emissao) = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ano);
            try (ResultSet rs = ps.executeQuery()) {
                int sequencial = rs.next() ? rs.getInt("total") + 1 : 1;
                return String.format("FRT-%d-%05d", ano, sequencial);
            }
        }
    }
}