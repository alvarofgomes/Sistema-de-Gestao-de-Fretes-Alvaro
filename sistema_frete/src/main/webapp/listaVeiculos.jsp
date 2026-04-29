<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Veículos - Sistema de Fretes</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>

<c:set var="paginaAtualMenu" value="veiculos" scope="request"/>
<jsp:include page="/includes/header.jsp"/>

<div class="container">

    <div class="page-header">
        <h1>Veículos</h1>
    </div>

    <div class="topo-botoes">
        <a href="${pageContext.request.contextPath}/home.jsp" class="btn btn-secondary">Voltar</a>
        <a href="${pageContext.request.contextPath}/veiculos?acao=novo" class="btn btn-success">Novo Veículo</a>
    </div>

    <c:if test="${not empty sucesso}">
        <div class="msg-sucesso">${sucesso}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/veiculos" method="get" class="filtro-form">
        <label for="filtro">Placa / RNTRC:</label>
        <input type="text" id="filtro" name="filtro" value="${filtro}" placeholder="Buscar veículo..." />
        <label for="statusFiltro">Status:</label>
        <select id="statusFiltro" name="statusFiltro">
            <option value="">Todos</option>
            <option value="DISPONIVEL"    ${statusFiltro == 'DISPONIVEL'    ? 'selected' : ''}>Disponível</option>
            <option value="EM_VIAGEM"     ${statusFiltro == 'EM_VIAGEM'     ? 'selected' : ''}>Em Viagem</option>
            <option value="EM_MANUTENCAO" ${statusFiltro == 'EM_MANUTENCAO' ? 'selected' : ''}>Em Manutenção</option>
            <option value="INATIVO"       ${statusFiltro == 'INATIVO'       ? 'selected' : ''}>Inativo</option>
        </select>
        <input type="hidden" name="pagina" value="1" />
        <input type="hidden" name="registrosPorPagina" value="${registrosPorPagina != null ? registrosPorPagina : 10}" />
        <button type="submit" class="btn btn-primary">Buscar</button>
    </form>

    <div class="table-container">
        <table class="table">
            <thead>
                <tr>
                    <th>#</th>
                    <th>Placa</th>
                    <th>RNTRC</th>
                    <th>Ano</th>
                    <th>Tipo</th>
                    <th>Tara (kg)</th>
                    <th>Capacidade (kg)</th>
                    <th>Volume (m³)</th>
                    <th>Status</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty listaVeiculos}">
                        <c:forEach var="veiculo" items="${listaVeiculos}">
                            <tr>
                                <td>${veiculo.id}</td>
                                <td><strong>${veiculo.placa}</strong></td>
                                <td>${veiculo.rntrc}</td>
                                <td>${veiculo.anoFabricacao}</td>
                                <td>${veiculo.tipo}</td>
                                <td>${veiculo.taraKg}</td>
                                <td>${veiculo.capacidadeKg}</td>
                                <td>${veiculo.volumeM3}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${veiculo.status == 'DISPONIVEL'}">
                                            <span class="badge status-disponivel">DISPONÍVEL</span>
                                        </c:when>
                                        <c:when test="${veiculo.status == 'EM_VIAGEM'}">
                                            <span class="badge status-em-viagem">EM VIAGEM</span>
                                        </c:when>
                                        <c:when test="${veiculo.status == 'EM_MANUTENCAO'}">
                                            <span class="badge status-manutencao">MANUTENÇÃO</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge status-inativo">INATIVO</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="acoes">
                                    <a class="link-editar"
                                       href="${pageContext.request.contextPath}/veiculos?acao=editar&id=${veiculo.id}&filtro=${filtro}&statusFiltro=${statusFiltro}&pagina=${paginaAtual}&registrosPorPagina=${registrosPorPagina}">
                                        Editar
                                    </a>
                                    <c:if test="${veiculo.status != 'INATIVO'}">
                                        <a class="link-inativar"
                                           href="${pageContext.request.contextPath}/veiculos?acao=inativar&id=${veiculo.id}"
                                           onclick="return confirm('Inativar o veículo ${veiculo.placa}?');">
                                            Inativar
                                        </a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="10" class="mensagem-vazia">Nenhum veículo encontrado.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>

    <div class="paginacao">
        <c:if test="${paginaAtual > 1}">
            <a href="${pageContext.request.contextPath}/veiculos?filtro=${filtro}&statusFiltro=${statusFiltro}&pagina=${paginaAtual - 1}&registrosPorPagina=${registrosPorPagina}">← Anterior</a>
        </c:if>
        <span>Página ${paginaAtual} de ${totalPaginas}</span>
        <c:if test="${paginaAtual < totalPaginas}">
            <a href="${pageContext.request.contextPath}/veiculos?filtro=${filtro}&statusFiltro=${statusFiltro}&pagina=${paginaAtual + 1}&registrosPorPagina=${registrosPorPagina}">Próximo →</a>
        </c:if>
    </div>

</div>
</body>
</html>