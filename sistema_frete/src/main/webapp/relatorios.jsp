<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Relatórios - Sistema de Fretes</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>

<c:set var="paginaAtualMenu" value="relatorios" scope="request"/>
<jsp:include page="/includes/header.jsp"/>

<div class="container">

    <div class="page-header">
        <h1>Relatórios</h1>
    </div>

    <div class="topo-botoes">
        <a href="${pageContext.request.contextPath}/home" class="btn btn-secondary">Voltar</a>
    </div>

    <c:if test="${not empty erro}">
        <div class="msg-erro">${erro}</div>
    </c:if>

    <%-- ── RELATÓRIO 1: FRETES EM ABERTO ── --%>
    <div class="form-section">
        <div class="form-section-title">Fretes em Aberto</div>
        <p style="font-size:13px; color:var(--text-muted); margin-bottom:16px;">
            Lista todos os fretes com status <strong>Emitido</strong>,
            <strong>Saída Confirmada</strong> ou <strong>Em Trânsito</strong>,
            com indicação de dias em atraso.
        </p>
        <a href="${pageContext.request.contextPath}/relatorios/fretes"
           target="_blank" class="btn btn-primary">
            Gerar PDF — Fretes em Aberto
        </a>
    </div>

    <%-- ── RELATÓRIO 2: ROMANEIO DE CARGA ── --%>
    <div class="form-section">
        <div class="form-section-title">Romaneio de Carga</div>
        <p style="font-size:13px; color:var(--text-muted); margin-bottom:16px;">
            Lista os fretes de um motorista em uma data específica,
            com totais de peso e volumes.
        </p>

        <form action="${pageContext.request.contextPath}/relatorios/romaneio"
              method="get" target="_blank">
            <div class="form-grid">
                <div class="form-group">
                    <label>Motorista *</label>
                    <select name="idMotorista" required>
                        <option value="">Selecione o motorista</option>
                        <c:forEach var="m" items="${motoristas}">
                            <option value="${m.id}">${m.nome}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label>Data do Romaneio *</label>
                    <input type="date" name="data" required />
                </div>
            </div>
            <div style="margin-top:12px;">
                <button type="submit" class="btn btn-primary">
                    Gerar PDF — Romaneio
                </button>
            </div>
        </form>
    </div>

    <%-- ── RELATÓRIO 3: FRETES POR CLIENTE NO PERÍODO (diferencial) ── --%>
    <div class="form-section">
        <div class="form-section-title">Fretes por Cliente no Período</div>
        <p style="font-size:13px; color:var(--text-muted); margin-bottom:16px;">
            Extrato de fretes de um cliente em um intervalo de datas,
            com valor total consolidado no rodapé.
        </p>

        <form action="${pageContext.request.contextPath}/relatorios/cliente"
              method="get" target="_blank">
            <div class="form-grid">
                <div class="form-group">
                    <label>Cliente *</label>
                    <select name="idCliente" required>
                        <option value="">Selecione o cliente</option>
                        <c:forEach var="c" items="${clientes}">
                            <option value="${c.id}">${c.razaoSocial}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label>Data Inicial *</label>
                    <input type="date" name="dataInicio" required />
                </div>
                <div class="form-group">
                    <label>Data Final *</label>
                    <input type="date" name="dataFim" required />
                </div>
            </div>
            <div style="margin-top:12px;">
                <button type="submit" class="btn btn-primary">
                    Gerar PDF — Extrato do Cliente
                </button>
            </div>
        </form>
    </div>

</div>
</body>
</html>