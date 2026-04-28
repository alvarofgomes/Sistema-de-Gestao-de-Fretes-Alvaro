package br.com.sistema_frete.controller;

import br.com.sistema_frete.BO.FreteBO;
import br.com.sistema_frete.enums.ocorrencia.TipoOcorrencia;
import br.com.sistema_frete.exception.FreteException;
import br.com.sistema_frete.exception.NegocioException;
import br.com.sistema_frete.model.Ocorrencia;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/ocorrencias")
public class OcorrenciaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final FreteBO freteBO = new FreteBO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Ocorrencia ocorrencia = new Ocorrencia();
        Integer idFrete = null;

        try {
            idFrete = parseIntegerOrNull(req.getParameter("idFrete"));
            if (idFrete == null) throw new FreteException("ID do frete não informado.");

            ocorrencia.setIdFrete(idFrete);

            String tipo = req.getParameter("tipo");
            if (tipo != null && !tipo.trim().isEmpty()) {
                ocorrencia.setTipo(TipoOcorrencia.valueOf(tipo));
            }

            String dataHora = req.getParameter("dataHora");
            if (dataHora != null && !dataHora.trim().isEmpty()) {
                ocorrencia.setDataHora(LocalDateTime.parse(dataHora));
            }

            ocorrencia.setMunicipio(req.getParameter("municipio"));
            ocorrencia.setUf(req.getParameter("uf"));
            ocorrencia.setDescricao(req.getParameter("descricao"));
            ocorrencia.setNomeRecebedor(req.getParameter("nomeRecebedor"));
            ocorrencia.setDocumentoRecebedor(req.getParameter("documentoRecebedor"));

            freteBO.registrarOcorrencia(ocorrencia);

            req.getSession().setAttribute("sucesso", "Ocorrência registrada com sucesso.");
            resp.sendRedirect(req.getContextPath() + "/fretes?acao=detalhe&id=" + idFrete);

        } catch (FreteException e) {
            req.setAttribute("erro", e.getMessage());
            req.setAttribute("tiposOcorrencia", TipoOcorrencia.values());

            try {
                if (idFrete != null) {
                    req.setAttribute("frete", freteBO.buscarPorId(idFrete));
                }
            } catch (NegocioException ex) {
                System.err.println("Erro ao recarregar frete após falha na ocorrência:");
                ex.printStackTrace();
            }

            req.getRequestDispatcher("/detalheFrete.jsp").forward(req, resp);

        } catch (NegocioException e) {
            req.setAttribute("mensagemErro", e.getMessage());
            req.setAttribute("voltarUrl", req.getContextPath() + "/fretes");
            req.getRequestDispatcher("/erro.jsp").forward(req, resp);

        } catch (Exception e) {
            System.err.println("Erro inesperado ao registrar ocorrência:");
            e.printStackTrace();
            req.setAttribute("mensagemErro", "Erro inesperado ao registrar ocorrência.");
            req.setAttribute("voltarUrl", req.getContextPath() + "/fretes");
            req.getRequestDispatcher("/erro.jsp").forward(req, resp);
        }
    }

    private Integer parseIntegerOrNull(String v) {
        try {
            if (v == null || v.trim().isEmpty()) return null;
            return Integer.parseInt(v);
        } catch (Exception e) { return null; }
    }
}