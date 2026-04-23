package br.com.sistema_frete.controller;

import br.com.sistema_frete.BO.*;
import br.com.sistema_frete.exception.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

        String usuario = request.getParameter("usuario");
        String senha = request.getParameter("senha");

        try {
            loginBO.autenticar(usuario, senha);

            HttpSession session = request.getSession(true);
            session.setAttribute("usuarioLogado", usuario);

            response.sendRedirect(request.getContextPath() + "/home.jsp");
        } catch (NegocioException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("Erro inesperado no login: " + e.getMessage());
            request.setAttribute("mensagemErro", "Ocorreu um erro inesperado ao processar o login.");
            request.getRequestDispatcher("/erro.jsp").forward(request, response);
        }
    }
}