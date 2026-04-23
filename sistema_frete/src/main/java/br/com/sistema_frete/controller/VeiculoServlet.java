package br.com.sistema_frete.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.sistema_frete.BO.VeiculoBO;
import br.com.sistema_frete.DAO.VeiculoDAO;
import br.com.sistema_frete.enums.veiculo.StatusVeiculo;
import br.com.sistema_frete.enums.veiculo.TipoVeiculo;
import br.com.sistema_frete.exception.CadastroException;
import br.com.sistema_frete.exception.NegocioException;
import br.com.sistema_frete.model.Veiculo;

@WebServlet("/veiculos")
public class VeiculoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final VeiculoBO veiculoBO = new VeiculoBO();
    private final VeiculoDAO veiculoDAO = new VeiculoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");

        try {
            if ("novo".equalsIgnoreCase(acao)) {
                request.getRequestDispatcher("/formVeiculo.jsp").forward(request, response);
                return;
            }

            if ("editar".equalsIgnoreCase(acao)) {
                editar(request, response);
                return;
            }

            if ("inativar".equalsIgnoreCase(acao)) {
                inativar(request, response);
                return;
            }
            
            listar(request, response);

        } catch (NegocioException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            request.setAttribute("voltarUrl", request.getContextPath() + "/veiculos");
            request.getRequestDispatcher("/erro.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensagemErro", "Ocorreu um erro inesperado ao processar veículos.");
            request.setAttribute("voltarUrl", request.getContextPath() + "/veiculos");
            request.getRequestDispatcher("/erro.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Veiculo veiculo = new Veiculo();

        try {
            preencherVeiculoComParametros(request, veiculo);

            veiculoBO.salvar(veiculo);

            String filtroRetorno = request.getParameter("filtroRetorno");
            String paginaRetorno = request.getParameter("paginaRetorno");
            String registrosPorPaginaRetorno = request.getParameter("registrosPorPaginaRetorno");

            request.getSession().setAttribute("sucesso",
                    veiculo.getId() != null ? "Veículo atualizado com sucesso." : "Veículo cadastrado com sucesso.");

            StringBuilder redirect = new StringBuilder(request.getContextPath() + "/veiculos");
            redirect.append("?filtro=").append(filtroRetorno != null ? filtroRetorno : "");
            redirect.append("&pagina=").append(paginaRetorno != null && !paginaRetorno.isEmpty() ? paginaRetorno : "1");
            redirect.append("&registrosPorPagina=").append(registrosPorPaginaRetorno != null && !registrosPorPaginaRetorno.isEmpty() ? registrosPorPaginaRetorno : "10");

            response.sendRedirect(redirect.toString());

        } catch (CadastroException e) {
            request.setAttribute("erro", e.getMessage());
            request.setAttribute("veiculo", veiculo);
            request.getRequestDispatcher("/formVeiculo.jsp").forward(request, response);

        } catch (NegocioException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            request.setAttribute("voltarUrl", request.getContextPath() + "/veiculos");
            request.getRequestDispatcher("/erro.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensagemErro", "Ocorreu um erro inesperado ao salvar o veículo.");
            request.setAttribute("voltarUrl", request.getContextPath() + "/veiculos");
            request.getRequestDispatcher("/erro.jsp").forward(request, response);
        }
    }

    private void listar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, NegocioException {

        String filtro = request.getParameter("filtro");
        String statusFiltro = request.getParameter("statusFiltro");

        int paginaAtual = parseIntOrDefault(request.getParameter("pagina"), 1);
        int registrosPorPagina = parseIntOrDefault(request.getParameter("registrosPorPagina"), 10);

        if (paginaAtual < 1) {
            paginaAtual = 1;
        }

        if (registrosPorPagina < 1) {
            registrosPorPagina = 10;
        }

        int totalRegistros = veiculoBO.contarTotal(filtro, statusFiltro);
        int totalPaginas = (int) Math.ceil(totalRegistros / (double) registrosPorPagina);

        if (totalPaginas == 0) {
            totalPaginas = 1;
        }

        if (paginaAtual > totalPaginas) {
            paginaAtual = totalPaginas;
        }

        List<Veiculo> listaVeiculos = veiculoBO.listarComPaginacao(
                filtro,
                statusFiltro,
                paginaAtual,
                registrosPorPagina
        );

        request.setAttribute("listaVeiculos", listaVeiculos);
        request.setAttribute("paginaAtual", paginaAtual);
        request.setAttribute("totalPaginas", totalPaginas);
        request.setAttribute("filtro", filtro);
        request.setAttribute("statusFiltro", statusFiltro);
        request.setAttribute("registrosPorPagina", registrosPorPagina);

        String sucesso = (String) request.getSession().getAttribute("sucesso");
        if (sucesso != null) {
            request.setAttribute("sucesso", sucesso);
            request.getSession().removeAttribute("sucesso");
        }

        request.getRequestDispatcher("/listaVeiculos.jsp").forward(request, response);
    }

    private void editar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, NegocioException {

        Integer id = parseIntegerOrNull(request.getParameter("id"));

        if (id == null) {
            throw new NegocioException("ID do veículo não informado.");
        }

        Veiculo veiculo = veiculoDAO.buscarPorId(id);

        if (veiculo == null) {
            throw new NegocioException("Veículo não encontrado.");
        }

        request.setAttribute("veiculo", veiculo);
        request.setAttribute("filtro", request.getParameter("filtro"));
        request.setAttribute("paginaAtual", request.getParameter("pagina"));
        request.setAttribute("registrosPorPagina", request.getParameter("registrosPorPagina"));

        request.getRequestDispatcher("/formVeiculo.jsp").forward(request, response);
    }

    private void inativar(HttpServletRequest request, HttpServletResponse response)
            throws IOException, NegocioException {

        Integer id = parseIntegerOrNull(request.getParameter("id"));

        if (id == null) {
            throw new NegocioException("ID do veículo não informado.");
        }

        veiculoBO.inativar(id);

        response.sendRedirect(request.getContextPath() + "/veiculos");
    }

	private void preencherVeiculoComParametros(HttpServletRequest request, Veiculo veiculo) {
        veiculo.setId(parseIntegerOrNull(request.getParameter("id")));
        veiculo.setPlaca(request.getParameter("placa") != null ? request.getParameter("placa").toUpperCase() : null);
        veiculo.setRntrc(request.getParameter("rntrc"));
        veiculo.setAnoFabricacao(parseIntegerOrNull(request.getParameter("anoFabricacao")));
        veiculo.setTaraKg(parseBigDecimalOrNull(request.getParameter("taraKg")));
        veiculo.setCapacidadeKg(parseBigDecimalOrNull(request.getParameter("capacidadeKg")));
        veiculo.setVolumeM3(parseBigDecimalOrNull(request.getParameter("volumeM3")));

        String tipo = request.getParameter("tipo");
        if (tipo != null && !tipo.trim().isEmpty()) {
            veiculo.setTipo(TipoVeiculo.valueOf(tipo));
        }

        String status = request.getParameter("status");
        if (status != null && !status.trim().isEmpty()) {
            veiculo.setStatus(StatusVeiculo.valueOf(status));
        }
    }

    private int parseIntOrDefault(String valor, int valorPadrao) {
        try {
            return Integer.parseInt(valor);
        } catch (Exception e) {
            return valorPadrao;
        }
    }

    private Integer parseIntegerOrNull(String valor) {
        try {
            if (valor == null || valor.trim().isEmpty()) {
                return null;
            }
            return Integer.parseInt(valor);
        } catch (Exception e) {
            return null;
        }
    }

    private BigDecimal parseBigDecimalOrNull(String valor) {
        try {
            if (valor == null || valor.trim().isEmpty()) {
                return null;
            }
            return new BigDecimal(valor.replace(",", "."));
        } catch (Exception e) {
            return null;
        }
    }
    
}