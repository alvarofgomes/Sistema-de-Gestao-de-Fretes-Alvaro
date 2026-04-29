package br.com.sistema_frete.controller;

import br.com.sistema_frete.util.ConnectionFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try (Connection conn = ConnectionFactory.getConnection()) {

            req.setAttribute("fretesEmAberto",
                    contar(conn, "SELECT COUNT(*) FROM frete WHERE status = 'EMITIDO'"));

            req.setAttribute("fretesEmTransito",
                    contar(conn, "SELECT COUNT(*) FROM frete WHERE status = 'EM_TRANSITO'"));

            req.setAttribute("veiculosDisponiveis",
                    contar(conn, "SELECT COUNT(*) FROM veiculo WHERE status = 'DISPONIVEL'"));

            req.setAttribute("fretesAtrasados",
                    contar(conn, "SELECT COUNT(*) FROM frete " +
                            "WHERE status NOT IN ('ENTREGUE','CANCELADO','NAO_ENTREGUE') " +
                            "AND data_previsao_entrega < CURRENT_DATE"));

        } catch (Exception e) {
            System.err.println("Erro ao carregar KPIs da home:");
            e.printStackTrace();
            // não quebra a home se falhar apenas exibe zero
            req.setAttribute("fretesEmAberto", 0);
            req.setAttribute("fretesEmTransito", 0);
            req.setAttribute("veiculosDisponiveis", 0);
            req.setAttribute("fretesAtrasados", 0);
        }

        req.getRequestDispatcher("/home.jsp").forward(req, resp);
    }

    private int contar(Connection conn, String sql) throws Exception {
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
}