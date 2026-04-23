package br.com.sistema_frete.controller;

import br.com.sistema_frete.BO.ClienteBO;
import br.com.sistema_frete.enums.cliente.StatusCliente;
import br.com.sistema_frete.enums.cliente.TipoCliente;
import br.com.sistema_frete.exception.CadastroException;
import br.com.sistema_frete.exception.NegocioException;
import br.com.sistema_frete.model.Cliente;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/clientes")
public class ClienteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final ClienteBO clienteBO = new ClienteBO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");

        try {
            if ("excluir".equalsIgnoreCase(acao)) {
                excluir(request, response);
                return;
            }

            if ("novo".equalsIgnoreCase(acao)) {
                request.getRequestDispatcher("/formCliente.jsp").forward(request, response);
                return;
            }

            listar(request, response);

        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            request.setAttribute("voltarUrl", request.getContextPath() + "/clientes");
            request.getRequestDispatcher("/erro.jsp").forward(request, response);
        } catch (NegocioException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            request.setAttribute("voltarUrl", request.getContextPath() + "/clientes");
            request.getRequestDispatcher("/erro.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("Erro inesperado no ClienteController:");
            e.printStackTrace();

            request.setAttribute("mensagemErro", "Ocorreu um erro inesperado ao processar clientes.");
            request.setAttribute("voltarUrl", request.getContextPath() + "/clientes");
            request.getRequestDispatcher("/erro.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Cliente cliente = new Cliente();

        try {
            preencherClienteComParametros(request, cliente);

            clienteBO.salvar(cliente);

            response.sendRedirect(request.getContextPath() + "/clientes");

        } catch (CadastroException e) {
            request.setAttribute("erro", e.getMessage());
            request.setAttribute("cliente", cliente);
            request.getRequestDispatcher("/formCliente.jsp").forward(request, response);

        } catch (NegocioException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            request.setAttribute("voltarUrl", request.getContextPath() + "/clientes");
            request.getRequestDispatcher("/erro.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("Erro inesperado ao salvar cliente:");
            e.printStackTrace();

            request.setAttribute("mensagemErro", "Ocorreu um erro inesperado ao salvar o cliente.");
            request.setAttribute("voltarUrl", request.getContextPath() + "/clientes");
            request.getRequestDispatcher("/erro.jsp").forward(request, response);
        }
    }

    private void listar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, NegocioException {

        String filtro = request.getParameter("filtro");
        int paginaAtual = parseIntOrDefault(request.getParameter("pagina"), 1);
        int registrosPorPagina = parseIntOrDefault(request.getParameter("registrosPorPagina"), 10);

        if (paginaAtual < 1) {
            paginaAtual = 1;
        }

        if (registrosPorPagina < 1) {
            registrosPorPagina = 10;
        }

        int totalRegistros = clienteBO.contarTotal(filtro);
        int totalPaginas = (int) Math.ceil(totalRegistros / (double) registrosPorPagina);

        if (totalPaginas == 0) {
            totalPaginas = 1;
        }

        if (paginaAtual > totalPaginas) {
            paginaAtual = totalPaginas;
        }

        List<Cliente> listaClientes = clienteBO.listarComPaginacao(filtro, paginaAtual, registrosPorPagina);

        request.setAttribute("listaClientes", listaClientes);
        request.setAttribute("paginaAtual", paginaAtual);
        request.setAttribute("totalPaginas", totalPaginas);
        request.setAttribute("filtro", filtro);
        request.setAttribute("registrosPorPagina", registrosPorPagina);

        request.getRequestDispatcher("/listaClientes.jsp").forward(request, response);
    }

    private void excluir(HttpServletRequest request, HttpServletResponse response)
            throws IOException, NegocioException {

        Long id = parseLongOrNull(request.getParameter("id"));

        if (id == null) {
            throw new NegocioException("ID do cliente não informado.");
        }

        clienteBO.excluir(id);
        response.sendRedirect(request.getContextPath() + "/clientes");
    }

    private void preencherClienteComParametros(HttpServletRequest request, Cliente cliente) {
        cliente.setId(parseIntegerOrNull(request.getParameter("id")));
        cliente.setRazaoSocial(request.getParameter("razaoSocial"));
        cliente.setNomeFantasia(request.getParameter("nomeFantasia"));
        cliente.setCnpj(request.getParameter("cnpj"));
        cliente.setInscricaoEstadual(request.getParameter("inscricaoEstadual"));
        cliente.setLogradouro(request.getParameter("logradouro"));
        cliente.setNumero(request.getParameter("numero"));
        cliente.setComplemento(request.getParameter("complemento"));
        cliente.setBairro(request.getParameter("bairro"));
        cliente.setMunicipio(request.getParameter("municipio"));
        cliente.setUf(request.getParameter("uf"));
        cliente.setCep(request.getParameter("cep"));
        cliente.setTelefone(request.getParameter("telefone"));
        cliente.setEmail(request.getParameter("email"));

        String tipo = request.getParameter("tipo");
        if (tipo != null && !tipo.trim().isEmpty()) {
            cliente.setTipo(TipoCliente.valueOf(tipo));
        }

        String status = request.getParameter("status");
        if (status != null && !status.trim().isEmpty()) {
            cliente.setStatus(StatusCliente.valueOf(status));
        }
    }

    private int parseIntOrDefault(String valor, int valorPadrao) {
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            return valorPadrao;
        }
    }

    private Integer parseIntegerOrNull(String valor) {
        try {
            if (valor == null || valor.trim().isEmpty()) {
                return null;
            }
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Long parseLongOrNull(String valor) {
        try {
            return Long.parseLong(valor);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}