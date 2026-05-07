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

@WebServlet("/relatorios/fretes")
public class RelatorioFreteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Connection conn = null;
        try {
            // caminho do jrxml dentro do webapp
            String jrxmlPath = getServletContext()
                    .getRealPath("/relatorios/relatorio_fretes_abertos.jrxml");

            // compila o jrxml
            JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlPath);

            // abre conexão e preenche
            conn = ConnectionFactory.getConnection();
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport, null, conn);

            // exporta para PDF na resposta HTTP
            resp.setContentType("application/pdf");
            resp.setHeader("Content-Disposition",
                    "inline; filename=\"fretes_em_aberto.pdf\"");

            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(
                    new SimpleOutputStreamExporterOutput(resp.getOutputStream()));
            exporter.exportReport();

        } catch (Exception e) {
            System.err.println("Erro ao gerar relatório de fretes em aberto:");
            e.printStackTrace();
            resp.setContentType("text/html; charset=UTF-8");
            resp.getWriter().println(
                "<h3 style='color:red;font-family:Arial;padding:20px'>" +
                "Erro ao gerar o relatório. Tente novamente.</h3>");
        } finally {
            if (conn != null) try { conn.close(); } catch (Exception ignored) {}
        }
    }
}