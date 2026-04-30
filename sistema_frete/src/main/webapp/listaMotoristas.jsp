<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Motoristas - Sistema de Fretes</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>

<c:set var="paginaAtualMenu" value="motoristas" scope="request"/>
<jsp:include page="/includes/header.jsp"/>

<div class="container">

    <div class="page-header">
        <h1>Motoristas</h1>
    </div>

    <div class="topo-botoes">
        <a href="${pageContext.request.contextPath}/home" class="btn btn-secondary">Voltar</a>
        <a href="${pageContext.request.contextPath}/motoristas?acao=novo" class="btn btn-success">Novo Motorista</a>
    </div>

    <c:if test="${not empty sucesso}">
        <div class="msg-sucesso">${sucesso}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/motoristas" method="get" class="filtro-form">
        <label for="filtro">Nome:</label>
        <input type="text" id="filtro" name="filtro" value="${filtro}" placeholder="Buscar motorista..." />
        <input type="hidden" name="pagina" value="1" />
        <input type="hidden" name="registrosPorPagina" value="${registrosPorPagina != null ? registrosPorPagina : 10}" />
        <button type="submit" class="btn btn-primary">Buscar</button>
    </form>

    <div class="table-container">
        <table class="table table-motoristas">
            <thead>
                <tr>
                    <th>#</th>
                    <th>Nome</th>
                    <th>CPF</th>
                    <th>Telefone</th>
                    <th>CNH</th>
                    <th>Categoria</th>
                    <th>Validade CNH</th>
                    <th>Vínculo</th>
                    <th>Status</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty listaMotoristas}">
                        <c:forEach var="motorista" items="${listaMotoristas}">
                            <tr>
                                <td>${motorista.id}</td>
                                <td>${motorista.nome}</td>
                                <td>${motorista.cpf}</td>
                                <td>${not empty motorista.telefone ? motorista.telefone : '—'}</td>
                                <td>${motorista.cnhNumero}</td>
                                <td>${motorista.cnhCategoria}</td>
                                <td>${motorista.cnhValidade}</td>
                                <td>${motorista.tipoVinculo}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${motorista.status == 'ATIVO'}">
                                            <span class="badge status-ativo">ATIVO</span>
                                        </c:when>
                                        <c:when test="${motorista.status == 'SUSPENSO'}">
                                            <span class="badge status-suspenso">SUSPENSO</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge status-inativo">INATIVO</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="acoes">
                                    <a class="link-editar"
                                       href="${pageContext.request.contextPath}/motoristas?acao=editar&id=${motorista.id}&filtro=${filtro}&pagina=${paginaAtual}&registrosPorPagina=${registrosPorPagina}">
                                        Editar
                                    </a>
                                    <c:if test="${motorista.status != 'INATIVO'}">
                                        <a class="link-inativar"
                                           href="${pageContext.request.contextPath}/motoristas?acao=inativar&id=${motorista.id}"
                                           onclick="return confirm('Inativar o motorista ${motorista.nome}?');">
                                            Inativar
                                        </a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="10" class="mensagem-vazia">Nenhum motorista encontrado.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>

    <div class="paginacao">
        <c:if test="${paginaAtual > 1}">
            <a href="${pageContext.request.contextPath}/motoristas?filtro=${filtro}&pagina=${paginaAtual - 1}&registrosPorPagina=${registrosPorPagina}">← Anterior</a>
        </c:if>
        <span>Página ${paginaAtual} de ${totalPaginas}</span>
        <c:if test="${paginaAtual < totalPaginas}">
            <a href="${pageContext.request.contextPath}/motoristas?filtro=${filtro}&pagina=${paginaAtual + 1}&registrosPorPagina=${registrosPorPagina}">Próximo →</a>
        </c:if>
    </div>

</div>
</body>
</html>