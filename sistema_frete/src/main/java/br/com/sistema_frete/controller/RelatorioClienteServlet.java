package br.com.sistema_frete.controller;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

import br.com.sistema_frete.util.ConnectionFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/relatorios/cliente")
public class RelatorioClienteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idClienteStr = req.getParameter("idCliente");
        String dataInicio   = req.getParameter("dataInicio");
        String dataFim      = req.getParameter("dataFim");

        if (idClienteStr == null || idClienteStr.trim().isEmpty()
                || dataInicio == null || dataInicio.trim().isEmpty()
                || dataFim == null || dataFim.trim().isEmpty()) {
            resp.setContentType("text/html; charset=UTF-8");
            resp.getWriter().println(
                "<h3 style='color:red;font-family:Arial;padding:20px'>" +
                "Preencha todos os filtros para gerar o relatório.</h3>");
            return;
        }

        Connection conn = null;
        try {
            String jrxmlPath = getServletContext()
                    .getRealPath("/relatorios/relatorio_cliente_periodo.jrxml");

            JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlPath);

            Map<String, Object> parametros = new HashMap<>();
            parametros.put("ID_CLIENTE",   Integer.parseInt(idClienteStr));
            parametros.put("DATA_INICIO",  java.sql.Date.valueOf(dataInicio));
            parametros.put("DATA_FIM",     java.sql.Date.valueOf(dataFim));

            conn = ConnectionFactory.getConnection();
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport, parametros, conn);

            resp.setContentType("application/pdf");
            resp.setHeader("Content-Disposition",
                    "inline; filename=\"fretes_cliente.pdf\"");

            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(
                    new SimpleOutputStreamExporterOutput(resp.getOutputStream()));
            exporter.exportReport();

        } catch (Exception e) {
            System.err.println("Erro ao gerar relatório por cliente:");
            e.printStackTrace();
            resp.setContentType("text/html; charset=UTF-8");
            resp.getWriter().println(
                "<h3 style='color:red;font-family:Arial;padding:20px'>" +
                "Erro ao gerar o relatório. Verifique os parâmetros.</h3>");
        } finally {
            if (conn != null) try { conn.close(); } catch (Exception ignored) {}
        }
    }
}