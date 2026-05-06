<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Meus Fretes - Sistema de Fretes</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <style>
        body {
            background:
                linear-gradient(rgba(6,20,50,.15), rgba(6,20,50,.30)),
                url('${pageContext.request.contextPath}/assets/images/OptimusPrimeNoPrime.png');
            background-size: cover;
            background-position: center;
            background-repeat: no-repeat;
            background-attachment: fixed;
        }
    </style>
</head>
<body>

<header class="app-header">
    <a class="brand" href="${pageContext.request.contextPath}/portal-cliente">
        Sistema de Gestão de Fretes
    </a>
    <nav class="app-nav">
        <span style="color:rgba(255,255,255,0.7); font-size:13px;">
            Olá, <strong>${sessionScope.usuarioLogado}</strong>
        </span>
        <a href="${pageContext.request.contextPath}/logout" class="sair">Sair</a>
    </nav>
</header>

<div class="container" style="margin-top: 28px;">

    <div class="page-header">
        <h1>Meus Fretes</h1>
    </div>

    <c:if test="${not empty sucesso}">
        <div class="msg-sucesso">${sucesso}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/portal-cliente" method="get" class="filtro-form">
        <label for="filtro">Número do Frete:</label>
        <input type="text" id="filtro" name="filtro" value="${filtro}" placeholder="Buscar..." />

        <label for="statusFiltro">Status:</label>
        <select id="statusFiltro" name="statusFiltro">
            <option value="">Todos</option>
            <option value="EMITIDO"          ${statusFiltro == 'EMITIDO'          ? 'selected' : ''}>Emitido</option>
            <option value="SAIDA_CONFIRMADA" ${statusFiltro == 'SAIDA_CONFIRMADA' ? 'selected' : ''}>Saída Confirmada</option>
            <option value="EM_TRANSITO"      ${statusFiltro == 'EM_TRANSITO'      ? 'selected' : ''}>Em Trânsito</option>
            <option value="ENTREGUE"         ${statusFiltro == 'ENTREGUE'         ? 'selected' : ''}>Entregue</option>
            <option value="NAO_ENTREGUE"     ${statusFiltro == 'NAO_ENTREGUE'     ? 'selected' : ''}>Não Entregue</option>
            <option value="CANCELADO"        ${statusFiltro == 'CANCELADO'        ? 'selected' : ''}>Cancelado</option>
        </select>

        <input type="hidden" name="pagina" value="1" />
        <button type="submit" class="btn btn-primary">Buscar</button>
    </form>

    <div class="table-container">
        <table class="table table-fretes">
            <thead>
                <tr>
                    <th>Número</th>
                    <th>Remetente</th>
                    <th>Destinatário</th>
                    <th>Destino</th>
                    <th>Previsão de Entrega</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty listaFretes}">
                        <c:forEach var="frete" items="${listaFretes}">
                            <tr>
                                <td><strong>${frete.numero}</strong></td>
                                <td>${frete.remetente.razaoSocial}</td>
                                <td>${frete.destinatario.razaoSocial}</td>
                                <td>${frete.municipioDestino}/${frete.ufDestino}</td>
                                <td>${frete.dataPrevisaoEntrega}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${frete.status == 'EMITIDO'}">
                                            <span class="badge status-emitido">EMITIDO</span>
                                        </c:when>
                                        <c:when test="${frete.status == 'SAIDA_CONFIRMADA'}">
                                            <span class="badge status-saida">SAÍDA CONFIRMADA</span>
                                        </c:when>
                                        <c:when test="${frete.status == 'EM_TRANSITO'}">
                                            <span class="badge status-transito">EM TRÂNSITO</span>
                                        </c:when>
                                        <c:when test="${frete.status == 'ENTREGUE'}">
                                            <span class="badge status-entregue">ENTREGUE</span>
                                        </c:when>
                                        <c:when test="${frete.status == 'NAO_ENTREGUE'}">
                                            <span class="badge status-nao-entregue">NÃO ENTREGUE</span>
                                        </c:when>
                                        <c:when test="${frete.status == 'CANCELADO'}">
                                            <span class="badge status-cancelado">CANCELADO</span>
                                        </c:when>
                                        <c:otherwise>${frete.status}</c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="6" class="mensagem-vazia">Nenhum frete encontrado.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>

    <div class="paginacao">
        <c:if test="${paginaAtual > 1}">
            <a href="${pageContext.request.contextPath}/portal-cliente?filtro=${filtro}&statusFiltro=${statusFiltro}&pagina=${paginaAtual - 1}">← Anterior</a>
        </c:if>
        <span>Página ${paginaAtual} de ${totalPaginas}</span>
        <c:if test="${paginaAtual < totalPaginas}">
            <a href="${pageContext.request.contextPath}/portal-cliente?filtro=${filtro}&statusFiltro=${statusFiltro}&pagina=${paginaAtual + 1}">Próximo →</a>
        </c:if>
    </div>

</div>
</body>
</html>