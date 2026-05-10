package br.com.sistema_frete.controller;

import br.com.sistema_frete.BO.SolicitacaoFreteBO;
import br.com.sistema_frete.enums.usuario.PerfilUsuario;
import br.com.sistema_frete.exception.CadastroException;
import br.com.sistema_frete.exception.FreteException;
import br.com.sistema_frete.exception.NegocioException;
import br.com.sistema_frete.model.Cliente;
import br.com.sistema_frete.model.SolicitacaoFrete;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/solicitacoes-frete")
public class SolicitacaoFreteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final SolicitacaoFreteBO bo = new SolicitacaoFreteBO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String acao   = req.getParameter("acao");
        HttpSession s = req.getSession(false);
        String perfil = s != null ? (String) s.getAttribute("perfilUsuario") : null;

        try {
            if ("nova".equalsIgnoreCase(acao)) {
                req.getRequestDispatcher("/formSolicitacaoFrete.jsp")
                   .forward(req, resp);
                return;
            }

            if ("cancelar".equalsIgnoreCase(acao)) {
                cancelar(req, resp);
                return;
            }

            if ("aprovar".equalsIgnoreCase(acao)) {
                aprovar(req, resp);
                return;
            }

            if ("recusar".equalsIgnoreCase(acao)) {
                recusar(req, resp);
                return;
            }

            // listagem — decide por perfil
            if (PerfilUsuario.CLIENTE.name().equals(perfil)) {
                listarCliente(req, resp);
            } else {
                listarAdmin(req, resp);
            }

        } catch (CadastroException | FreteException e) {
            req.setAttribute("mensagemErro", e.getMessage());
            req.setAttribute("voltarUrl", req.getContextPath() + "/solicitacoes-frete");
            req.getRequestDispatcher("/erro.jsp").forward(req, resp);
        } catch (NegocioException e) {
            req.setAttribute("mensagemErro", e.getMessage());
            req.setAttribute("voltarUrl", req.getContextPath() + "/solicitacoes-frete");
            req.getRequestDispatcher("/erro.jsp").forward(req, resp);
        } catch (Exception e) {
            System.err.println("Erro inesperado no SolicitacaoFreteServlet:");
            e.printStackTrace();
            req.setAttribute("mensagemErro", "Ocorreu um erro inesperado.");
            req.setAttribute("voltarUrl", req.getContextPath() + "/solicitacoes-frete");
            req.getRequestDispatcher("/erro.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session     = req.getSession(false);
        Integer clienteIdSessao = (Integer) session.getAttribute("clienteIdLogado");

        SolicitacaoFrete solicitacao = new SolicitacaoFrete();
        Cliente cliente = new Cliente();
        solicitacao.setCliente(cliente);

        try {
            preencherSolicitacao(req, solicitacao);
            bo.criar(solicitacao, clienteIdSessao);

            session.setAttribute("sucesso", "Solicitação enviada com sucesso!");
            resp.sendRedirect(req.getContextPath() + "/solicitacoes-frete");

        } catch (CadastroException e) {
            req.setAttribute("erro", e.getMessage());
            req.setAttribute("solicitacao", solicitacao);
            req.getRequestDispatcher("/formSolicitacaoFrete.jsp").forward(req, resp);

        } catch (NegocioException e) {
            req.setAttribute("mensagemErro", e.getMessage());
            req.setAttribute("voltarUrl", req.getContextPath() + "/solicitacoes-frete");
            req.getRequestDispatcher("/erro.jsp").forward(req, resp);

        } catch (Exception e) {
            System.err.println("Erro ao salvar solicitação:");
            e.printStackTrace();
            req.setAttribute("mensagemErro", "Erro ao salvar a solicitação.");
            req.setAttribute("voltarUrl", req.getContextPath() + "/solicitacoes-frete");
            req.getRequestDispatcher("/erro.jsp").forward(req, resp);
        }
    }

    private void listarCliente(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException, NegocioException {

        Integer clienteId = (Integer) req.getSession().getAttribute("clienteIdLogado");
        String filtro     = req.getParameter("filtro");
        String status     = req.getParameter("statusFiltro");
        int pagina        = parseIntOrDefault(req.getParameter("pagina"), 1);
        int regPorPagina  = 10;

        int total        = bo.contarPorCliente(clienteId, filtro, status);
        int totalPaginas = Math.max(1, (int) Math.ceil(total / (double) regPorPagina));
        if (pagina > totalPaginas) pagina = totalPaginas;

        List<SolicitacaoFrete> lista = bo.listarPorCliente(
                clienteId, filtro, status, pagina, regPorPagina);

        req.setAttribute("listaSolicitacoes", lista);
        req.setAttribute("paginaAtual",       pagina);
        req.setAttribute("totalPaginas",      totalPaginas);
        req.setAttribute("filtro",            filtro);
        req.setAttribute("statusFiltro",      status);
        req.setAttribute("perfilAtual",       "CLIENTE");

        carregarSucesso(req);
        req.getRequestDispatcher("/listaSolicitacoesFrete.jsp").forward(req, resp);
    }

    private void listarAdmin(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException, NegocioException {

        String filtro    = req.getParameter("filtro");
        String status    = req.getParameter("statusFiltro");
        int pagina       = parseIntOrDefault(req.getParameter("pagina"), 1);
        int regPorPagina = 10;

        int total        = bo.contarTodas(filtro, status);
        int totalPaginas = Math.max(1, (int) Math.ceil(total / (double) regPorPagina));
        if (pagina > totalPaginas) pagina = totalPaginas;

        List<SolicitacaoFrete> lista = bo.listarTodas(filtro, status, pagina, regPorPagina);

        req.setAttribute("listaSolicitacoes", lista);
        req.setAttribute("paginaAtual",       pagina);
        req.setAttribute("totalPaginas",      totalPaginas);
        req.setAttribute("filtro",            filtro);
        req.setAttribute("statusFiltro",      status);
        req.setAttribute("perfilAtual",       "ADMIN");

        carregarSucesso(req);
        req.getRequestDispatcher("/listaSolicitacoesFrete.jsp").forward(req, resp);
    }

    private void cancelar(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, NegocioException {

        Integer id = parseIntegerOrNull(req.getParameter("id"));
        Integer clienteId = (Integer) req.getSession().getAttribute("clienteIdLogado");

        bo.cancelar(id, clienteId);
        req.getSession().setAttribute("sucesso", "Solicitação cancelada.");
        resp.sendRedirect(req.getContextPath() + "/solicitacoes-frete");
    }

    private void aprovar(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, NegocioException {

        Integer id = parseIntegerOrNull(req.getParameter("id"));
        Integer usuarioId = (Integer) req.getSession().getAttribute("usuarioId");

        bo.aprovar(id, usuarioId);
        req.getSession().setAttribute("sucesso", "Solicitação aprovada com sucesso.");
        resp.sendRedirect(req.getContextPath() + "/solicitacoes-frete");
    }

    private void recusar(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, NegocioException {

        Integer id = parseIntegerOrNull(req.getParameter("id"));
        Integer usuarioId = (Integer) req.getSession().getAttribute("usuarioId");
        String motivo = req.getParameter("motivo");

        bo.recusar(id, usuarioId, motivo);
        req.getSession().setAttribute("sucesso", "Solicitação recusada.");
        resp.sendRedirect(req.getContextPath() + "/solicitacoes-frete");
    }

    private void preencherSolicitacao(HttpServletRequest req,
                                       SolicitacaoFrete s) {
        s.setCidadeOrigem(req.getParameter("cidadeOrigem"));
        s.setUfOrigem(req.getParameter("ufOrigem"));
        s.setCidadeDestino(req.getParameter("cidadeDestino"));
        s.setUfDestino(req.getParameter("ufDestino"));
        s.setDescricaoCarga(req.getParameter("descricaoCarga"));
        s.setObservacao(req.getParameter("observacao"));
        s.setPesoKg(parseBigDecimalOrNull(req.getParameter("pesoKg")));
        s.setVolumes(parseIntegerOrNull(req.getParameter("volumes")));
    }

    private void carregarSucesso(HttpServletRequest req) {
        String sucesso = (String) req.getSession().getAttribute("sucesso");
        if (sucesso != null) {
            req.setAttribute("sucesso", sucesso);
            req.getSession().removeAttribute("sucesso");
        }
    }

    private int parseIntOrDefault(String v, int def) {
        try { return Integer.parseInt(v); } catch (Exception e) { return def; }
    }

    private Integer parseIntegerOrNull(String v) {
        try {
            if (v == null || v.trim().isEmpty()) return null;
            return Integer.parseInt(v);
        } catch (Exception e) { return null; }
    }

    private BigDecimal parseBigDecimalOrNull(String v) {
        try {
            if (v == null || v.trim().isEmpty()) return null;
            return new BigDecimal(v.replace(",", "."));
        } catch (Exception e) { return null; }
    }
}