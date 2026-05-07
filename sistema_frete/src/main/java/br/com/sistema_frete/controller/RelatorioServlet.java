package br.com.sistema_frete.controller;

import br.com.sistema_frete.BO.ClienteBO;
import br.com.sistema_frete.BO.MotoristaBO;
import br.com.sistema_frete.exception.NegocioException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/relatorios")
public class RelatorioServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final MotoristaBO motoristaBO = new MotoristaBO();
    private final ClienteBO   clienteBO   = new ClienteBO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("motoristas", motoristaBO.listarAtivos());
            req.setAttribute("clientes",   clienteBO.listarTodos());
            req.getRequestDispatcher("/relatorios.jsp").forward(req, resp);

        } catch (NegocioException e) {
            req.setAttribute("mensagemErro", e.getMessage());
            req.setAttribute("voltarUrl", req.getContextPath() + "/home");
            req.getRequestDispatcher("/erro.jsp").forward(req, resp);
        }
    }
}