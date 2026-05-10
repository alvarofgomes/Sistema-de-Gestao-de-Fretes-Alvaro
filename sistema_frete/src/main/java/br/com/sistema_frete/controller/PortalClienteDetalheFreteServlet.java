package br.com.sistema_frete.controller;

import br.com.sistema_frete.DAO.FreteDAO;
import br.com.sistema_frete.DAO.OcorrenciaDAO;
import br.com.sistema_frete.model.Frete;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/portal-cliente/frete")
public class PortalClienteDetalheFreteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final FreteDAO freteDAO = new FreteDAO();
    private final OcorrenciaDAO ocorrenciaDAO = new OcorrenciaDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            HttpSession session = req.getSession(false);

            if (session == null || session.getAttribute("clienteIdLogado") == null) {
                req.setAttribute("mensagemErro", "Sessão inválida. Faça login novamente.");
                req.setAttribute("voltarUrl", req.getContextPath() + "/login");
                req.getRequestDispatcher("/erro.jsp").forward(req, resp);
                return;
            }

            Integer clienteIdLogado = (Integer) session.getAttribute("clienteIdLogado");

            String idParam = req.getParameter("id");
            if (idParam == null || idParam.trim().isEmpty()) {
                req.setAttribute("mensagemErro", "Frete não informado.");
                req.setAttribute("voltarUrl", req.getContextPath() + "/portal-cliente");
                req.getRequestDispatcher("/erro.jsp").forward(req, resp);
                return;
            }

            Integer idFrete = Integer.parseInt(idParam);
            Frete frete = freteDAO.buscarPorId(idFrete);

            if (frete == null) {
                req.setAttribute("mensagemErro", "Frete não encontrado.");
                req.setAttribute("voltarUrl", req.getContextPath() + "/portal-cliente");
                req.getRequestDispatcher("/erro.jsp").forward(req, resp);
                return;
            }

            boolean permitido =
                    (frete.getRemetente() != null
                            && frete.getRemetente().getId() != null
                            && frete.getRemetente().getId().equals(clienteIdLogado))
                    ||
                    (frete.getDestinatario() != null
                            && frete.getDestinatario().getId() != null
                            && frete.getDestinatario().getId().equals(clienteIdLogado));

            if (!permitido) {
                req.setAttribute("mensagemErro", "Você não possui permissão para visualizar este frete.");
                req.setAttribute("voltarUrl", req.getContextPath() + "/portal-cliente");
                req.getRequestDispatcher("/erro.jsp").forward(req, resp);
                return;
            }

            req.setAttribute("frete", frete);
            req.setAttribute("ocorrencias", ocorrenciaDAO.listarPorFrete(idFrete));
            req.getRequestDispatcher("/detalheFreteCliente.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            req.setAttribute("mensagemErro", "Código do frete inválido.");
            req.setAttribute("voltarUrl", req.getContextPath() + "/portal-cliente");
            req.getRequestDispatcher("/erro.jsp").forward(req, resp);

        } catch (Exception e) {
            System.err.println("Erro ao carregar detalhe do frete no portal do cliente:");
            e.printStackTrace();

            req.setAttribute("mensagemErro", "Não foi possível carregar os detalhes do frete.");
            req.setAttribute("voltarUrl", req.getContextPath() + "/portal-cliente");
            req.getRequestDispatcher("/erro.jsp").forward(req, resp);
        }
    }
}