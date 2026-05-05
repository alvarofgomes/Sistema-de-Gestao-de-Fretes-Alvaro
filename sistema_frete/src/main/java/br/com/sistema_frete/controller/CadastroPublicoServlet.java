package br.com.sistema_frete.controller;

import br.com.sistema_frete.BO.CadastroPublicoBO;
import br.com.sistema_frete.exception.CadastroException;
import br.com.sistema_frete.exception.NegocioException;
import br.com.sistema_frete.model.Cliente;
import br.com.sistema_frete.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/cadastro")
public class CadastroPublicoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final CadastroPublicoBO cadastroBO = new CadastroPublicoBO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/cadastro.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            // dados da empresa (cliente)
            Cliente cliente = new Cliente();
            cliente.setRazaoSocial(req.getParameter("razaoSocial"));
            cliente.setNomeFantasia(req.getParameter("nomeFantasia"));
            cliente.setCnpj(req.getParameter("cnpj"));
            cliente.setTelefone(req.getParameter("telefone"));
            cliente.setEmail(req.getParameter("email"));
            cliente.setLogradouro(req.getParameter("logradouro"));
            cliente.setNumero(req.getParameter("numero"));
            cliente.setComplemento(req.getParameter("complemento"));
            cliente.setBairro(req.getParameter("bairro"));
            cliente.setCidade(req.getParameter("cidade"));
            cliente.setUf(req.getParameter("uf"));
            cliente.setCep(req.getParameter("cep"));

            // dados de acesso (usuario)
            Usuario usuario = new Usuario();
            usuario.setNome(req.getParameter("nomeResponsavel"));
            usuario.setLogin(req.getParameter("login"));

            String senha        = req.getParameter("senha");
            String confirmaSenha = req.getParameter("confirmaSenha");

            cadastroBO.cadastrar(cliente, usuario, senha, confirmaSenha);

            // redireciona pro login com mensagem de sucesso
            req.getSession().setAttribute("sucessoCadastro",
                    "Cadastro realizado com sucesso! Faça login para acessar o sistema.");
            resp.sendRedirect(req.getContextPath() + "/login");

        } catch (CadastroException e) {
            req.setAttribute("erro", e.getMessage());
            // repopula os campos para não perder o que foi digitado
            req.setAttribute("razaoSocial",    req.getParameter("razaoSocial"));
            req.setAttribute("nomeFantasia",   req.getParameter("nomeFantasia"));
            req.setAttribute("cnpj",           req.getParameter("cnpj"));
            req.setAttribute("telefone",       req.getParameter("telefone"));
            req.setAttribute("email",          req.getParameter("email"));
            req.setAttribute("logradouro",     req.getParameter("logradouro"));
            req.setAttribute("numero",         req.getParameter("numero"));
            req.setAttribute("complemento",    req.getParameter("complemento"));
            req.setAttribute("bairro",         req.getParameter("bairro"));
            req.setAttribute("municipio",      req.getParameter("municipio"));
            req.setAttribute("uf",             req.getParameter("uf"));
            req.setAttribute("cep",            req.getParameter("cep"));
            req.setAttribute("nomeResponsavel", req.getParameter("nomeResponsavel"));
            req.setAttribute("login",          req.getParameter("login"));
            req.getRequestDispatcher("/cadastro.jsp").forward(req, resp);

        } catch (NegocioException e) {
            req.setAttribute("mensagemErro", e.getMessage());
            req.setAttribute("voltarUrl", req.getContextPath() + "/cadastro");
            req.getRequestDispatcher("/erro.jsp").forward(req, resp);

        } catch (Exception e) {
            System.err.println("Erro inesperado no CadastroPublicoServlet:");
            e.printStackTrace();
            req.setAttribute("mensagemErro", "Ocorreu um erro inesperado. Tente novamente.");
            req.setAttribute("voltarUrl", req.getContextPath() + "/cadastro");
            req.getRequestDispatcher("/erro.jsp").forward(req, resp);
        }
    }
}