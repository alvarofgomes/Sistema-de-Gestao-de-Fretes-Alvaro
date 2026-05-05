package br.com.sistema_frete.controller;

import br.com.sistema_frete.DAO.FreteDAO;
import br.com.sistema_frete.exception.NegocioException;
import br.com.sistema_frete.model.Frete;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/portal-cliente")
public class PortalClienteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final FreteDAO freteDAO = new FreteDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session    = req.getSession(false);
        Integer clienteIdLogado = (Integer) session.getAttribute("clienteIdLogado");

        if (clienteIdLogado == null) {
            req.setAttribute("mensagemErro",
                    "Seu usuário não está vinculado a um cliente. Contate o administrador.");
            req.setAttribute("voltarUrl", req.getContextPath() + "/login");
            req.getRequestDispatcher("/erro.jsp").forward(req, resp);
            return;
        }

        try {
            String filtro       = req.getParameter("filtro");
            String statusFiltro = req.getParameter("statusFiltro");
            int pagina          = parseIntOrDefault(req.getParameter("pagina"), 1);
            int regPorPagina    = 10;

            int total        = freteDAO.contarTotalPorCliente(clienteIdLogado, filtro, statusFiltro);
            int totalPaginas = Math.max(1, (int) Math.ceil(total / (double) regPorPagina));
            if (pagina > totalPaginas) pagina = totalPaginas;

            List<Frete> fretes = freteDAO.buscarPorClientePaginado(
                    clienteIdLogado, filtro, statusFiltro,
                    (pagina - 1) * regPorPagina, regPorPagina);

            req.setAttribute("listaFretes",      fretes);
            req.setAttribute("paginaAtual",      pagina);
            req.setAttribute("totalPaginas",     totalPaginas);
            req.setAttribute("filtro",           filtro);
            req.setAttribute("statusFiltro",     statusFiltro);
            req.setAttribute("registrosPorPagina", regPorPagina);

            req.getRequestDispatcher("/portalCliente.jsp").forward(req, resp);

        } catch (Exception e) {
            System.err.println("Erro no PortalClienteServlet:");
            e.printStackTrace();
            req.setAttribute("mensagemErro", "Não foi possível carregar seus fretes.");
            req.setAttribute("voltarUrl", req.getContextPath() + "/portal-cliente");
            req.getRequestDispatcher("/erro.jsp").forward(req, resp);
        }
    }

    private int parseIntOrDefault(String v, int def) {
        try { return Integer.parseInt(v); } catch (Exception e) { return def; }
    }
}