package br.com.sistema_frete.controller;

import br.com.sistema_frete.DAO.FreteDAO;
import br.com.sistema_frete.BO.SolicitacaoFreteBO;
import br.com.sistema_frete.util.ConnectionFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.List;

@WebServlet("/portal-cliente")
public class PortalClienteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final FreteDAO freteDAO = new FreteDAO();
    private final SolicitacaoFreteBO solicitacaoBO = new SolicitacaoFreteBO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session      = req.getSession(false);
        Integer clienteIdLogado  = (Integer) session.getAttribute("clienteIdLogado");

        if (clienteIdLogado == null) {
            req.setAttribute("mensagemErro",
                    "Seu usuário não está vinculado a um cliente.");
            req.setAttribute("voltarUrl", req.getContextPath() + "/login");
            req.getRequestDispatcher("/erro.jsp").forward(req, resp);
            return;
        }

        try {

            carregarKpis(req, clienteIdLogado);

            String filtro       = req.getParameter("filtro");
            String statusFiltro = req.getParameter("statusFiltro");
            int pagina          = parseIntOrDefault(req.getParameter("pagina"), 1);
            int regPorPagina    = 10;

            int total        = freteDAO.contarTotalPorCliente(
                    clienteIdLogado, filtro, statusFiltro);
            int totalPaginas = Math.max(1, (int) Math.ceil(total / (double) regPorPagina));
            if (pagina > totalPaginas) pagina = totalPaginas;

            List fretes = freteDAO.buscarPorClientePaginado(
                    clienteIdLogado, filtro, statusFiltro,
                    (pagina - 1) * regPorPagina, regPorPagina);

            req.setAttribute("listaFretes",       fretes);
            req.setAttribute("paginaAtual",        pagina);
            req.setAttribute("totalPaginas",       totalPaginas);
            req.setAttribute("filtro",             filtro);
            req.setAttribute("statusFiltro",       statusFiltro);
            req.setAttribute("registrosPorPagina", regPorPagina);

            // sucesso flash
            String sucesso = (String) session.getAttribute("sucesso");
            if (sucesso != null) {
                req.setAttribute("sucesso", sucesso);
                session.removeAttribute("sucesso");
            }

            req.getRequestDispatcher("/portalCliente.jsp").forward(req, resp);

        } catch (Exception e) {
            System.err.println("Erro no PortalClienteServlet:");
            e.printStackTrace();
            req.setAttribute("mensagemErro", "Não foi possível carregar o portal.");
            req.setAttribute("voltarUrl", req.getContextPath() + "/portal-cliente");
            req.getRequestDispatcher("/erro.jsp").forward(req, resp);
        }
    }

    private void carregarKpis(HttpServletRequest req,
                               Integer clienteId) throws SQLException {

        String sql = "SELECT " +
            "COUNT(*) FILTER (WHERE status IN ('EMITIDO','SAIDA_CONFIRMADA')) AS em_aberto, " +
            "COUNT(*) FILTER (WHERE status = 'EM_TRANSITO') AS em_transito, " +
            "COUNT(*) FILTER (WHERE status = 'ENTREGUE') AS entregues, " +
            "COUNT(*) FILTER (WHERE status NOT IN ('ENTREGUE','CANCELADO','NAO_ENTREGUE') " +
            "  AND data_previsao_entrega < CURRENT_DATE) AS em_atraso " +
            "FROM frete WHERE id_remetente=? OR id_destinatario=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, clienteId);
            ps.setInt(2, clienteId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    req.setAttribute("kpiEmAberto",   rs.getInt("em_aberto"));
                    req.setAttribute("kpiEmTransito", rs.getInt("em_transito"));
                    req.setAttribute("kpiEntregues",  rs.getInt("entregues"));
                    req.setAttribute("kpiEmAtraso",   rs.getInt("em_atraso"));
                }
            }
        }

        req.setAttribute("kpiSolicitacoesPendentes",
                solicitacaoBO.contarPendentesPorCliente(clienteId));
    }

    private int parseIntOrDefault(String v, int def) {
        try { return Integer.parseInt(v); } catch (Exception e) { return def; }
    }
}