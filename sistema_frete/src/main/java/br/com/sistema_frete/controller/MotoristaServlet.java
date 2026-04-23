package br.com.sistema_frete.controller;

import br.com.sistema_frete.BO.MotoristaBO;
import br.com.sistema_frete.enums.motorista.CategoriaCNH;
import br.com.sistema_frete.enums.motorista.StatusMotorista;
import br.com.sistema_frete.enums.motorista.TipoVinculo;
import br.com.sistema_frete.exception.CadastroException;
import br.com.sistema_frete.exception.NegocioException;
import br.com.sistema_frete.model.Motorista;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/motoristas")
public class MotoristaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final MotoristaBO motoristaBO = new MotoristaBO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");

        try {
            if ("inativar".equalsIgnoreCase(acao)) {
                inativar(request, response);
                return;
            }

            if ("novo".equalsIgnoreCase(acao)) {
                request.getRequestDispatcher("/formMotorista.jsp").forward(request, response);
                return;
            }

            listar(request, response);

        } catch (CadastroException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            request.setAttribute("voltarUrl", request.getContextPath() + "/motoristas");
            request.getRequestDispatcher("/erro.jsp").forward(request, response);
        } catch (NegocioException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            request.setAttribute("voltarUrl", request.getContextPath() + "/motoristas");
            request.getRequestDispatcher("/erro.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("Erro inesperado no MotoristaController:");
            e.printStackTrace();

            request.setAttribute("mensagemErro", "Ocorreu um erro inesperado ao processar motoristas.");
            request.setAttribute("voltarUrl", request.getContextPath() + "/motoristas");
            request.getRequestDispatcher("/erro.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Motorista motorista = new Motorista();

        try {
            preencherMotoristaComParametros(request, motorista);

            motoristaBO.salvar(motorista);

            response.sendRedirect(request.getContextPath() + "/motoristas");

        } catch (CadastroException e) {
            request.setAttribute("erro", e.getMessage());
            request.setAttribute("motorista", motorista);
            request.getRequestDispatcher("/formMotorista.jsp").forward(request, response);

        } catch (NegocioException e) {
            request.setAttribute("mensagemErro", e.getMessage());
            request.setAttribute("voltarUrl", request.getContextPath() + "/motoristas");
            request.getRequestDispatcher("/erro.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("Erro inesperado ao salvar motorista:");
            e.printStackTrace();

            request.setAttribute("mensagemErro", "Ocorreu um erro inesperado ao salvar o motorista.");
            request.setAttribute("voltarUrl", request.getContextPath() + "/motoristas");
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

        int totalRegistros = motoristaBO.contarTotal(filtro);
        int totalPaginas = (int) Math.ceil(totalRegistros / (double) registrosPorPagina);

        if (totalPaginas == 0) {
            totalPaginas = 1;
        }

        if (paginaAtual > totalPaginas) {
            paginaAtual = totalPaginas;
        }

        List<Motorista> listaMotoristas = motoristaBO.listarComPaginacao(filtro, paginaAtual, registrosPorPagina);

        request.setAttribute("listaMotoristas", listaMotoristas);
        request.setAttribute("paginaAtual", paginaAtual);
        request.setAttribute("totalPaginas", totalPaginas);
        request.setAttribute("filtro", filtro);
        request.setAttribute("registrosPorPagina", registrosPorPagina);

        request.getRequestDispatcher("/listaMotoristas.jsp").forward(request, response);
    }

    private void inativar(HttpServletRequest request, HttpServletResponse response)
            throws IOException, NegocioException {

        Long id = parseLongOrNull(request.getParameter("id"));

        if (id == null) {
            throw new NegocioException("ID do motorista não informado.");
        }

        motoristaBO.inativar(id);
        response.sendRedirect(request.getContextPath() + "/motoristas");
    }

    private void preencherMotoristaComParametros(HttpServletRequest request, Motorista motorista) {
        motorista.setId(parseIntegerOrNull(request.getParameter("id")));
        motorista.setNome(request.getParameter("nome"));
        motorista.setCpf(request.getParameter("cpf"));
        motorista.setTelefone(request.getParameter("telefone"));
        motorista.setCnhNumero(request.getParameter("cnhNumero"));

        String dataNascimento = request.getParameter("dataNascimento");
        if (dataNascimento != null && !dataNascimento.trim().isEmpty()) {
            motorista.setDataNascimento(LocalDate.parse(dataNascimento));
        }

        String cnhValidade = request.getParameter("cnhValidade");
        if (cnhValidade != null && !cnhValidade.trim().isEmpty()) {
            motorista.setCnhValidade(LocalDate.parse(cnhValidade));
        }

        String cnhCategoria = request.getParameter("cnhCategoria");
        if (cnhCategoria != null && !cnhCategoria.trim().isEmpty()) {
            motorista.setCnhCategoria(CategoriaCNH.valueOf(cnhCategoria));
        }

        String tipoVinculo = request.getParameter("tipoVinculo");
        if (tipoVinculo != null && !tipoVinculo.trim().isEmpty()) {
            motorista.setTipoVinculo(TipoVinculo.valueOf(tipoVinculo));
        }

        String status = request.getParameter("status");
        if (status != null && !status.trim().isEmpty()) {
            motorista.setStatus(StatusMotorista.valueOf(status));
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