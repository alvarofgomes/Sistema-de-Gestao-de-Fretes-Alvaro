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

@WebServlet("/relatorios/romaneio")
public class RelatorioRomaneioServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idMotoristaStr = req.getParameter("idMotorista");
        String data           = req.getParameter("data");

        // validação básica
        if (idMotoristaStr == null || idMotoristaStr.trim().isEmpty()
                || data == null || data.trim().isEmpty()) {
            resp.setContentType("text/html; charset=UTF-8");
            resp.getWriter().println(
                "<h3 style='color:red;font-family:Arial;padding:20px'>" +
                "Selecione o motorista e a data para gerar o romaneio.</h3>");
            return;
        }

        Connection conn = null;
        try {
            String jrxmlPath = getServletContext()
                    .getRealPath("/relatorios/relatorio_romaneio.jrxml");

            JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlPath);

            // parâmetros passados para o JRXML
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("ID_MOTORISTA", Integer.parseInt(idMotoristaStr));
            parametros.put("DATA_ROMANEIO", java.sql.Date.valueOf(data));

            conn = ConnectionFactory.getConnection();
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport, parametros, conn);

            resp.setContentType("application/pdf");
            resp.setHeader("Content-Disposition",
                    "inline; filename=\"romaneio_" + data + ".pdf\"");

            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(
                    new SimpleOutputStreamExporterOutput(resp.getOutputStream()));
            exporter.exportReport();

        } catch (Exception e) {
            System.err.println("Erro ao gerar romaneio:");
            e.printStackTrace();
            resp.setContentType("text/html; charset=UTF-8");
            resp.getWriter().println(
                "<h3 style='color:red;font-family:Arial;padding:20px'>" +
                "Erro ao gerar o romaneio. Verifique os parâmetros.</h3>");
        } finally {
            if (conn != null) try { conn.close(); } catch (Exception ignored) {}
        }
    }
}