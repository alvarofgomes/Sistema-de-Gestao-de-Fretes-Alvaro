package br.com.sistema_frete.controller;

import br.com.sistema_frete.BO.ClienteBO;
import br.com.sistema_frete.BO.UsuarioBO;
import br.com.sistema_frete.DAO.ClienteDAO;
import br.com.sistema_frete.DAO.UsuarioDAO;
import br.com.sistema_frete.enums.cliente.StatusCliente;
import br.com.sistema_frete.enums.cliente.TipoCliente;
import br.com.sistema_frete.enums.usuario.PerfilUsuario;
import br.com.sistema_frete.enums.usuario.StatusUsuario;
import br.com.sistema_frete.exception.CadastroException;
import br.com.sistema_frete.exception.NegocioException;
import br.com.sistema_frete.model.Cliente;
import br.com.sistema_frete.model.Usuario;

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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String acao = req.getParameter("acao");

        try {
            if ("novo".equalsIgnoreCase(acao)) {
            	req.getRequestDispatcher("/formCliente.jsp").forward(req, resp);
                return;
            }

            if ("editar".equalsIgnoreCase(acao)) {
                editar(req, resp);
                return;
            }

            if ("excluir".equalsIgnoreCase(acao)) {
                excluir(req, resp);
                return;
            }

            listar(req, resp);

        } catch (CadastroException e) {
        	req.setAttribute("mensagemErro", e.getMessage());
        	req.setAttribute("voltarUrl", req.getContextPath() + "/clientes");
        	req.getRequestDispatcher("/erro.jsp").forward(req, resp);
        } catch (NegocioException e) {
        	req.setAttribute("mensagemErro", e.getMessage());
        	req.setAttribute("voltarUrl", req.getContextPath() + "/clientes");
        	req.getRequestDispatcher("/erro.jsp").forward(req, resp);
        } catch (Exception e) {
            System.err.println("Erro inesperado no ClienteServlet:");
            e.printStackTrace();

            req.setAttribute("mensagemErro", "Ocorreu um erro inesperado ao processar clientes.");
            req.setAttribute("voltarUrl", req.getContextPath() + "/clientes");
            req.getRequestDispatcher("/erro.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Cliente cliente = new Cliente();

        try {
            preencherClienteComParametros(req, cliente);
            clienteBO.salvar(cliente);

            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");
            
            //ADICIONAR: criar login se foi preenchido
            String loginNovo = req.getParameter("loginNovo");
            String senhaNova = req.getParameter("senhaNova");

            if (loginNovo != null && !loginNovo.trim().isEmpty()
                    && senhaNova != null && !senhaNova.trim().isEmpty()) {

                // busca o id do cliente recém salvo pelo CNPJ
                ClienteDAO clienteDAO = new ClienteDAO();
                Cliente clienteSalvo = clienteDAO.buscarPorCnpj(cliente.getCnpj());

                if (clienteSalvo != null) {
                    Usuario usuario = new Usuario();
                    usuario.setNome(req.getParameter("nomeResponsavel"));
                    usuario.setLogin(loginNovo.trim());
                    usuario.setPerfil(PerfilUsuario.CLIENTE);
                    usuario.setStatus(StatusUsuario.ATIVO);
                    usuario.setClienteId(clienteSalvo.getId());

                    UsuarioBO usuarioBO = new UsuarioBO();
                    usuarioBO.salvar(usuario, senhaNova);
                }
            }

            req.getSession().setAttribute("sucesso",
                    cliente.getId() != null
                            ? "Cliente atualizado com sucesso."
                            : "Cliente cadastrado com sucesso.");

            StringBuilder redirect = new StringBuilder(
            		req.getContextPath() + "/clientes");
            String filtroRetorno = req.getParameter("filtroRetorno");
            String paginaRetorno = req.getParameter("paginaRetorno");
            String regPorPagina  = req.getParameter("registrosPorPaginaRetorno");

            redirect.append("?filtro=").append(filtroRetorno != null ? filtroRetorno : "");
            redirect.append("&pagina=").append(paginaRetorno != null && !paginaRetorno.isEmpty() ? paginaRetorno : "1");
            redirect.append("&registrosPorPagina=").append(regPorPagina != null && !regPorPagina.isEmpty() ? regPorPagina : "10");

            resp.sendRedirect(redirect.toString());

        } catch (CadastroException e) {
        	req.setAttribute("erro", e.getMessage());
        	req.setAttribute("cliente", cliente);

            // recarrega possuiUsuario para o form não perder o estado
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            if (cliente.getId() != null) {
            	req.setAttribute("possuiUsuario",
                        usuarioDAO.clientePossuiUsuario(cliente.getId()));
            }

            req.getRequestDispatcher("/formCliente.jsp").forward(req, resp);

        } catch (NegocioException e) {
        	req.setAttribute("mensagemErro", e.getMessage());
        	req.setAttribute("voltarUrl", req.getContextPath() + "/clientes");
        	req.getRequestDispatcher("/erro.jsp").forward(req, resp);

        } catch (Exception e) {
            System.err.println("Erro inesperado ao salvar cliente:");
            e.printStackTrace();
            req.setAttribute("mensagemErro", "Ocorreu um erro inesperado ao salvar o cliente.");
            req.setAttribute("voltarUrl", req.getContextPath() + "/clientes");
            req.getRequestDispatcher("/erro.jsp").forward(req, resp);
        }
    }

    private void listar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException, NegocioException {

        String filtro = req.getParameter("filtro");
        int paginaAtual = parseIntOrDefault(req.getParameter("pagina"), 1);
        int registrosPorPagina = parseIntOrDefault(req.getParameter("registrosPorPagina"), 10);

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

        req.setAttribute("listaClientes", listaClientes);
        req.setAttribute("paginaAtual", paginaAtual);
        req.setAttribute("totalPaginas", totalPaginas);
        req.setAttribute("filtro", filtro);
        req.setAttribute("registrosPorPagina", registrosPorPagina);

        String sucesso = (String) req.getSession().getAttribute("sucesso");
        if (sucesso != null) {
        	req.setAttribute("sucesso", sucesso);
        	req.getSession().removeAttribute("sucesso");
        }
        
        req.getRequestDispatcher("/listaClientes.jsp").forward(req, resp);
    }

    private void editar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException, NegocioException {

        Integer id = parseIntegerOrNull(req.getParameter("id"));

        if (id == null) {
            throw new NegocioException("ID do cliente não informado.");
        }

        Cliente cliente = clienteBO.buscarPorId(id);
        req.setAttribute("cliente", cliente);

        //verifica se já tem usuário vinculado
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        boolean possuiUsuario = usuarioDAO.clientePossuiUsuario(id);
        req.setAttribute("possuiUsuario", possuiUsuario);

        req.setAttribute("filtro", req.getParameter("filtro"));
        req.setAttribute("paginaAtual", req.getParameter("pagina"));
        req.setAttribute("registrosPorPagina", req.getParameter("registrosPorPagina"));

        req.getRequestDispatcher("/formCliente.jsp").forward(req, resp);
    }

    private void excluir(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, NegocioException {

        Long id = parseLongOrNull(req.getParameter("id"));

        if (id == null) {
            throw new NegocioException("ID do cliente não informado.");
        }

        clienteBO.excluir(id);
        req.getSession().setAttribute("sucesso", "Cliente excluído com sucesso.");
        resp.sendRedirect(req.getContextPath() + "/clientes");
    }

    private void preencherClienteComParametros(HttpServletRequest req, Cliente cliente) {
        cliente.setId(parseIntegerOrNull(req.getParameter("id")));
        cliente.setRazaoSocial(req.getParameter("razaoSocial"));
        cliente.setNomeFantasia(req.getParameter("nomeFantasia"));
        cliente.setCnpj(req.getParameter("cnpj"));
        cliente.setInscricaoEstadual(req.getParameter("inscricaoEstadual"));
        cliente.setLogradouro(req.getParameter("logradouro"));
        cliente.setNumero(req.getParameter("numero"));
        cliente.setComplemento(req.getParameter("complemento"));
        cliente.setBairro(req.getParameter("bairro"));
        cliente.setCidade(req.getParameter("cidade"));
        cliente.setUf(req.getParameter("uf"));
        cliente.setCep(req.getParameter("cep"));
        cliente.setTelefone(req.getParameter("telefone"));
        cliente.setEmail(req.getParameter("email"));

        String status = req.getParameter("status");
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