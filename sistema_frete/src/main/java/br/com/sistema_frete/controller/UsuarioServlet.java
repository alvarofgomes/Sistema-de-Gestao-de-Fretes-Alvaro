package br.com.sistema_frete.controller;

import br.com.sistema_frete.BO.ClienteBO;
import br.com.sistema_frete.BO.UsuarioBO;
import br.com.sistema_frete.enums.usuario.PerfilUsuario;
import br.com.sistema_frete.enums.usuario.StatusUsuario;
import br.com.sistema_frete.exception.CadastroException;
import br.com.sistema_frete.exception.NegocioException;
import br.com.sistema_frete.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/usuarios")
public class UsuarioServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final UsuarioBO usuarioBO = new UsuarioBO();
    private final ClienteBO clienteBO = new ClienteBO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String acao = req.getParameter("acao");

        try {
            if ("novo".equalsIgnoreCase(acao)) {
                req.setAttribute("clientes", clienteBO.listarTodos());
                req.getRequestDispatcher("/formUsuario.jsp").forward(req, resp);
                return;
            }

            if ("editar".equalsIgnoreCase(acao)) {
                editar(req, resp);
                return;
            }

            if ("inativar".equalsIgnoreCase(acao)) {
                inativar(req, resp);
                return;
            }

            listar(req, resp);

        } catch (CadastroException e) {
            req.setAttribute("mensagemErro", e.getMessage());
            req.setAttribute("voltarUrl", req.getContextPath() + "/usuarios");
            req.getRequestDispatcher("/erro.jsp").forward(req, resp);
        } catch (NegocioException e) {
            req.setAttribute("mensagemErro", e.getMessage());
            req.setAttribute("voltarUrl", req.getContextPath() + "/usuarios");
            req.getRequestDispatcher("/erro.jsp").forward(req, resp);
        } catch (Exception e) {
            System.err.println("Erro inesperado no UsuarioServlet:");
            e.printStackTrace();
            req.setAttribute("mensagemErro", "Ocorreu um erro inesperado.");
            req.setAttribute("voltarUrl", req.getContextPath() + "/usuarios");
            req.getRequestDispatcher("/erro.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Usuario usuario = new Usuario();
        String senhaDigitada = null;

        try {
            preencherUsuario(req, usuario);
            senhaDigitada = req.getParameter("senha");

            usuarioBO.salvar(usuario, senhaDigitada);

            req.getSession().setAttribute("sucesso",
                    usuario.getId() != null
                            ? "Usuário atualizado com sucesso."
                            : "Usuário cadastrado com sucesso.");

            resp.sendRedirect(req.getContextPath() + "/usuarios");

        } catch (CadastroException e) {
            req.setAttribute("erro", e.getMessage());
            req.setAttribute("usuario", usuario);
            try {
                req.setAttribute("clientes", clienteBO.listarTodos());
            } catch (NegocioException ex) {
                System.err.println("Erro ao recarregar clientes:" + ex.getMessage());
            }
            req.getRequestDispatcher("/formUsuario.jsp").forward(req, resp);

        } catch (NegocioException e) {
            req.setAttribute("mensagemErro", e.getMessage());
            req.setAttribute("voltarUrl", req.getContextPath() + "/usuarios");
            req.getRequestDispatcher("/erro.jsp").forward(req, resp);

        } catch (Exception e) {
            System.err.println("Erro inesperado ao salvar usuário:");
            e.printStackTrace();
            req.setAttribute("mensagemErro", "Ocorreu um erro inesperado ao salvar o usuário.");
            req.setAttribute("voltarUrl", req.getContextPath() + "/usuarios");
            req.getRequestDispatcher("/erro.jsp").forward(req, resp);
        }
    }

    private void listar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException, NegocioException {

        String filtro = req.getParameter("filtro");
        int pagina        = parseIntOrDefault(req.getParameter("pagina"), 1);
        int regPorPagina  = parseIntOrDefault(req.getParameter("registrosPorPagina"), 10);

        if (pagina < 1) pagina = 1;
        if (regPorPagina < 1) regPorPagina = 10;

        int total       = usuarioBO.contarTotal(filtro);
        int totalPaginas = Math.max(1, (int) Math.ceil(total / (double) regPorPagina));
        if (pagina > totalPaginas) pagina = totalPaginas;

        List<Usuario> lista = usuarioBO.listarComPaginacao(filtro, pagina, regPorPagina);

        req.setAttribute("listaUsuarios", lista);
        req.setAttribute("paginaAtual", pagina);
        req.setAttribute("totalPaginas", totalPaginas);
        req.setAttribute("filtro", filtro);
        req.setAttribute("registrosPorPagina", regPorPagina);

        String sucesso = (String) req.getSession().getAttribute("sucesso");
        if (sucesso != null) {
            req.setAttribute("sucesso", sucesso);
            req.getSession().removeAttribute("sucesso");
        }

        req.getRequestDispatcher("/listaUsuarios.jsp").forward(req, resp);
    }

    private void editar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException, NegocioException {

        Integer id = parseIntegerOrNull(req.getParameter("id"));
        if (id == null) throw new NegocioException("ID do usuário não informado.");

        Usuario usuario = usuarioBO.buscarPorId(id);
        req.setAttribute("usuario", usuario);
        req.setAttribute("clientes", clienteBO.listarTodos());
        req.getRequestDispatcher("/formUsuario.jsp").forward(req, resp);
    }

    private void inativar(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, NegocioException {

        Integer id = parseIntegerOrNull(req.getParameter("id"));
        if (id == null) throw new NegocioException("ID do usuário não informado.");

        usuarioBO.inativar(id);
        req.getSession().setAttribute("sucesso", "Usuário inativado com sucesso.");
        resp.sendRedirect(req.getContextPath() + "/usuarios");
    }

    private void preencherUsuario(HttpServletRequest req, Usuario usuario) {
        usuario.setId(parseIntegerOrNull(req.getParameter("id")));
        usuario.setNome(req.getParameter("nome"));
        usuario.setLogin(req.getParameter("login"));

        String perfil = req.getParameter("perfil");
        if (perfil != null && !perfil.isEmpty())
            usuario.setPerfil(PerfilUsuario.valueOf(perfil));

        String status = req.getParameter("status");
        if (status != null && !status.isEmpty())
            usuario.setStatus(StatusUsuario.valueOf(status));

        usuario.setClienteId(parseIntegerOrNull(req.getParameter("clienteId")));
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
}