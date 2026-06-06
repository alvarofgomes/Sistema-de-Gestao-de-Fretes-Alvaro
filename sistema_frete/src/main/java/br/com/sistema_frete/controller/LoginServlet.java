package br.com.sistema_frete.controller;

import br.com.sistema_frete.BO.UsuarioBO;
import br.com.sistema_frete.enums.usuario.PerfilUsuario;
import br.com.sistema_frete.exception.CadastroException;
import br.com.sistema_frete.exception.NegocioException;
import br.com.sistema_frete.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final UsuarioBO usuarioBO = new UsuarioBO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // se já está logado, redireciona direto
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuarioLogado") != null) {
            String perfil = (String) session.getAttribute("perfilUsuario");
            if (PerfilUsuario.CLIENTE.name().equals(perfil)) {
                response.sendRedirect(request.getContextPath() + "/portal-cliente");
            } else {
                response.sendRedirect(request.getContextPath() + "/home");
            }
            return;
        }

        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String login = request.getParameter("usuario");
        String senha = request.getParameter("senha");

        try {
            Usuario usuario = usuarioBO.autenticar(login, senha);

            HttpSession session = request.getSession(true);

            session.setAttribute("usuarioLogado",  usuario.getNome());
            session.setAttribute("usuarioLogin",   usuario.getLogin());
            session.setAttribute("perfilUsuario",  usuario.getPerfil().name()); 
            session.setAttribute("usuarioId",      usuario.getId());

            // clienteId na sessão se perfil CLIENTE
            if (usuario.getPerfil() == PerfilUsuario.CLIENTE) {
                session.setAttribute("clienteIdLogado", usuario.getClienteId());
                response.sendRedirect(request.getContextPath() + "/portal-cliente");
            } else {
                response.sendRedirect(request.getContextPath() + "/home");
            }

        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            request.getRequestDispatcher("/login.jsp").forward(request, response);

        } catch (NegocioException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            request.getRequestDispatcher("/erro.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensagemErro", "Ocorreu um erro inesperado ao realizar login.");
            request.getRequestDispatcher("/erro.jsp").forward(request, response);
        }
    }
}