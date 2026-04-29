package br.com.sistema_frete.controller;

import br.com.sistema_frete.BO.LoginBO;
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
    private final LoginBO loginBO = new LoginBO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String login = request.getParameter("usuario");
        String senha = request.getParameter("senha");

        try {
            Usuario usuario = loginBO.autenticar(login, senha);

            HttpSession session = request.getSession(true);
            session.setAttribute("usuarioLogado", usuario.getNome());
            session.setAttribute("usuarioLogin", usuario.getLogin());
            session.setAttribute("usuarioPerfil", usuario.getPerfil());

            response.sendRedirect(request.getContextPath() + "/home");

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