package br.com.sistema_frete.controller;

import br.com.sistema_frete.util.ConnectionFactory;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/portal-cliente/relatorio")
public class PortalClienteRelatorioServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session     = req.getSession(false);
        Integer clienteIdSessao = (Integer) session.getAttribute("clienteIdLogado");

        if (clienteIdSessao == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String dataInicio = req.getParameter("dataInicio");
        String dataFim    = req.getParameter("dataFim");

        if (dataInicio == null || dataInicio.trim().isEmpty()
                || dataFim == null || dataFim.trim().isEmpty()) {
            resp.setContentType("text/html; charset=UTF-8");
            resp.getWriter().println(
                "<h3 style='color:red;font-family:Arial;padding:20px'>" +
                "Informe a data inicial e a data final.</h3>");
            return;
        }

        Connection conn = null;
        try {
        	String jrxmlPath = getServletContext()
        	        .getRealPath("/relatorios/relatorio_cliente_periodo.jrxml");

            JasperReport report = JasperCompileManager.compileReport(jrxmlPath);

            Map<String, Object> params = new HashMap<>();
            // ID sempre vem da sessão — cliente nunca escolhe outro
            params.put("ID_CLIENTE",   clienteIdSessao);
            params.put("DATA_INICIAL", java.sql.Date.valueOf(dataInicio));
            params.put("DATA_FINAL",   java.sql.Date.valueOf(dataFim));

            conn = ConnectionFactory.getConnection();
            JasperPrint print = JasperFillManager.fillReport(report, params, conn);

            resp.setContentType("application/pdf");
            resp.setHeader("Content-Disposition",
                    "inline; filename=\"meus_fretes.pdf\"");

            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(print));
            exporter.setExporterOutput(
                    new SimpleOutputStreamExporterOutput(resp.getOutputStream()));
            exporter.exportReport();

        } catch (Exception e) {
            System.err.println("Erro ao gerar relatório do portal:");
            e.printStackTrace();
            resp.setContentType("text/html; charset=UTF-8");
            resp.getWriter().println(
                "<h3 style='color:red;font-family:Arial;padding:20px'>" +
                "Erro ao gerar o relatório.</h3>");
        } finally {
            if (conn != null) try { conn.close(); } catch (Exception ignored) {}
        }
    }
}