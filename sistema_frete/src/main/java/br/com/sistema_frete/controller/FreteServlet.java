package br.com.sistema_frete.controller;

import br.com.sistema_frete.BO.ClienteBO;
import br.com.sistema_frete.BO.FreteBO;
import br.com.sistema_frete.BO.MotoristaBO;
import br.com.sistema_frete.BO.VeiculoBO;
import br.com.sistema_frete.enums.frete.FreteStatus;
import br.com.sistema_frete.exception.FreteException;
import br.com.sistema_frete.exception.NegocioException;
import br.com.sistema_frete.model.Cliente;
import br.com.sistema_frete.model.Frete;
import br.com.sistema_frete.model.Motorista;
import br.com.sistema_frete.model.Veiculo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/fretes")
public class FreteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final FreteBO freteBO = new FreteBO();
    private final ClienteBO clienteBO = new ClienteBO();
    private final MotoristaBO motoristaBO = new MotoristaBO();
    private final VeiculoBO veiculoBO = new VeiculoBO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String acao = req.getParameter("acao");

        try {
            if ("novo".equalsIgnoreCase(acao)) {
                carregarFormNovo(req, resp);
                return;
            }
            if ("detalhe".equalsIgnoreCase(acao)) {
                detalhe(req, resp);
                return;
            }
            if ("confirmarSaida".equalsIgnoreCase(acao)) {
                confirmarSaida(req, resp);
                return;
            }
            if ("cancelar".equalsIgnoreCase(acao)) {
                cancelar(req, resp);
                return;
            }
            listar(req, resp);

        } catch (FreteException e) {
            req.setAttribute("mensagemErro", e.getMessage());
            req.setAttribute("voltarUrl", req.getContextPath() + "/fretes");
            req.getRequestDispatcher("/erro.jsp").forward(req, resp);
        } catch (NegocioException e) {
            req.setAttribute("mensagemErro", e.getMessage());
            req.setAttribute("voltarUrl", req.getContextPath() + "/fretes");
            req.getRequestDispatcher("/erro.jsp").forward(req, resp);
        } catch (Exception e) {
            System.err.println("Erro inesperado no FreteServlet:");
            e.printStackTrace();
            req.setAttribute("mensagemErro", "Ocorreu um erro inesperado.");
            req.setAttribute("voltarUrl", req.getContextPath() + "/fretes");
            req.getRequestDispatcher("/erro.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Frete frete = new Frete();
        try {
            preencherFrete(req, frete);
            freteBO.registrarFrete(frete);
            req.getSession().setAttribute("sucesso", "Frete " + frete.getNumero() + " registrado com sucesso.");
            resp.sendRedirect(req.getContextPath() + "/fretes");

        } catch (FreteException e) {
            req.setAttribute("erro", e.getMessage());
            recarregarFormComErro(req, resp, frete);
        } catch (NegocioException e) {
            req.setAttribute("mensagemErro", e.getMessage());
            req.setAttribute("voltarUrl", req.getContextPath() + "/fretes");
            req.getRequestDispatcher("/erro.jsp").forward(req, resp);
        } catch (Exception e) {
            System.err.println("Erro inesperado ao salvar frete:");
            e.printStackTrace();
            req.setAttribute("mensagemErro", "Ocorreu um erro inesperado ao salvar o frete.");
            req.setAttribute("voltarUrl", req.getContextPath() + "/fretes");
            req.getRequestDispatcher("/erro.jsp").forward(req, resp);
        }
    }

    private void listar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException, NegocioException {

        String filtro = req.getParameter("filtro");
        String statusFiltro = req.getParameter("statusFiltro");
        int pagina = parseIntOrDefault(req.getParameter("pagina"), 1);
        int regPorPagina = parseIntOrDefault(req.getParameter("registrosPorPagina"), 10);

        if (pagina < 1) pagina = 1;
        if (regPorPagina < 1) regPorPagina = 10;

        int total = freteBO.contarTotal(filtro, statusFiltro);
        int totalPaginas = Math.max(1, (int) Math.ceil(total / (double) regPorPagina));
        if (pagina > totalPaginas) pagina = totalPaginas;

        List<Frete> lista = freteBO.listarComPaginacao(filtro, statusFiltro, pagina, regPorPagina);

        req.setAttribute("listaFretes", lista);
        req.setAttribute("paginaAtual", pagina);
        req.setAttribute("totalPaginas", totalPaginas);
        req.setAttribute("filtro", filtro);
        req.setAttribute("statusFiltro", statusFiltro);
        req.setAttribute("registrosPorPagina", regPorPagina);
        req.setAttribute("todosStatus", FreteStatus.values());

        String sucesso = (String) req.getSession().getAttribute("sucesso");
        if (sucesso != null) {
            req.setAttribute("sucesso", sucesso);
            req.getSession().removeAttribute("sucesso");
        }

        req.getRequestDispatcher("/listaFretes.jsp").forward(req, resp);
    }

    private void carregarFormNovo(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException, NegocioException {
        req.setAttribute("clientes", clienteBO.listarTodos());
        req.setAttribute("motoristas", motoristaBO.listarAtivos());
        req.setAttribute("veiculos", veiculoBO.listarDisponiveis());
        req.getRequestDispatcher("/formFrete.jsp").forward(req, resp);
    }

    private void recarregarFormComErro(HttpServletRequest req, HttpServletResponse resp, Frete frete)
            throws ServletException, IOException {
        try {
            req.setAttribute("clientes", clienteBO.listarTodos());
            req.setAttribute("motoristas", motoristaBO.listarAtivos());
            req.setAttribute("veiculos", veiculoBO.listarDisponiveis());
            req.setAttribute("frete", frete);
            req.getRequestDispatcher("/formFrete.jsp").forward(req, resp);
        } catch (NegocioException e) {
            req.setAttribute("mensagemErro", e.getMessage());
            req.getRequestDispatcher("/erro.jsp").forward(req, resp);
        }
    }

    private void detalhe(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException, NegocioException {

        Integer id = parseIntegerOrNull(req.getParameter("id"));
        if (id == null) throw new NegocioException("ID do frete não informado.");

        Frete frete = freteBO.buscarPorId(id);
        req.setAttribute("frete", frete);
        req.getRequestDispatcher("/detalheFrete.jsp").forward(req, resp);
    }

    private void confirmarSaida(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, NegocioException {

        Integer id = parseIntegerOrNull(req.getParameter("id"));
        if (id == null) throw new NegocioException("ID do frete não informado.");

        freteBO.confirmarSaida(id);
        req.getSession().setAttribute("sucesso", "Saída confirmada com sucesso.");
        resp.sendRedirect(req.getContextPath() + "/fretes?acao=detalhe&id=" + id);
    }

    private void cancelar(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, NegocioException {

        Integer id = parseIntegerOrNull(req.getParameter("id"));
        if (id == null) throw new NegocioException("ID do frete não informado.");

        freteBO.cancelar(id);
        req.getSession().setAttribute("sucesso", "Frete cancelado com sucesso.");
        resp.sendRedirect(req.getContextPath() + "/fretes");
    }

    private void preencherFrete(HttpServletRequest req, Frete frete) {
        Cliente rem = new Cliente();
        rem.setId(parseIntegerOrNull(req.getParameter("idRemetente")));
        frete.setRemetente(rem);

        Cliente dest = new Cliente();
        dest.setId(parseIntegerOrNull(req.getParameter("idDestinatario")));
        frete.setDestinatario(dest);

        Motorista mot = new Motorista();
        mot.setId(parseIntegerOrNull(req.getParameter("idMotorista")));
        frete.setMotorista(mot);

        Veiculo vei = new Veiculo();
        vei.setId(parseIntegerOrNull(req.getParameter("idVeiculo")));
        frete.setVeiculo(vei);

        frete.setMunicipioOrigem(req.getParameter("municipioOrigem"));
        frete.setUfOrigem(req.getParameter("ufOrigem"));
        frete.setMunicipioDestino(req.getParameter("municipioDestino"));
        frete.setUfDestino(req.getParameter("ufDestino"));
        frete.setDescricaoCarga(req.getParameter("descricaoCarga"));
        frete.setPesoKg(parseBigDecimalOrNull(req.getParameter("pesoKg")));
        frete.setVolumes(parseIntegerOrNull(req.getParameter("volumes")));
        frete.setValorFrete(parseBigDecimalOrNull(req.getParameter("valorFrete")));
        frete.setAliquotaIcms(parseBigDecimalOrNull(req.getParameter("aliquotaIcms")));

        // calcula ICMS e total automaticamente
        if (frete.getValorFrete() != null && frete.getAliquotaIcms() != null) {
            BigDecimal icms = frete.getValorFrete()
                    .multiply(frete.getAliquotaIcms())
                    .divide(new BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP);
            frete.setValorIcms(icms);
            frete.setValorTotal(frete.getValorFrete().add(icms));
        }

        String dataPrev = req.getParameter("dataPrevisaoEntrega");
        if (dataPrev != null && !dataPrev.trim().isEmpty()) {
            frete.setDataPrevisaoEntrega(LocalDate.parse(dataPrev));
        }
        frete.setDataEmissao(LocalDate.now());
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

    private BigDecimal parseBigDecimalOrNull(String v) {
        try {
            if (v == null || v.trim().isEmpty()) return null;
            return new BigDecimal(v.replace(",", "."));
        } catch (Exception e) { return null; }
    }
}