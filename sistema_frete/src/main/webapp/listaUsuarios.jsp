<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Usuários - Sistema de Fretes</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>

<c:set var="paginaAtualMenu" value="usuarios" scope="request"/>
<jsp:include page="/includes/header.jsp"/>

<div class="container">
    <div class="page-header"><h1>Usuários</h1></div>

    <div class="topo-botoes">
        <a href="${pageContext.request.contextPath}/home" class="btn btn-secondary">Voltar</a>
        <a href="${pageContext.request.contextPath}/usuarios?acao=novo" class="btn btn-success">+ Novo Usuário</a>
    </div>

    <c:if test="${not empty sucesso}">
        <div class="msg-sucesso">${sucesso}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/usuarios" method="get" class="filtro-form">
        <label for="filtro">Nome / Login:</label>
        <input type="text" id="filtro" name="filtro" value="${filtro}" placeholder="Buscar usuário..." />
        <input type="hidden" name="pagina" value="1" />
        <input type="hidden" name="registrosPorPagina" value="${registrosPorPagina != null ? registrosPorPagina : 10}" />
        <button type="submit" class="btn btn-primary">Buscar</button>
    </form>

    <div class="table-container">
        <table class="table">
            <thead>
                <tr>
                    <th>#</th>
                    <th>Nome</th>
                    <th>Login</th>
                    <th>Perfil</th>
                    <th>Cliente Vinculado</th>
                    <th>Status</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty listaUsuarios}">
                        <c:forEach var="u" items="${listaUsuarios}">
                            <tr>
                                <td>${u.id}</td>
                                <td>${u.nome}</td>
                                <td>${u.login}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${u.perfil == 'ADMIN'}">
                                            <span class="badge status-transito">ADMIN</span>
                                        </c:when>
                                        <c:when test="${u.perfil == 'OPERADOR'}">
                                            <span class="badge status-emitido">OPERADOR</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge status-saida">CLIENTE</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${not empty u.nomeCliente ? u.nomeCliente : '—'}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${u.status == 'ATIVO'}">
                                            <span class="badge status-ativo">ATIVO</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge status-inativo">INATIVO</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="acoes">
                                    <a class="link-editar"
                                       href="${pageContext.request.contextPath}/usuarios?acao=editar&id=${u.id}">
                                        Editar
                                    </a>
                                    <c:if test="${u.status != 'INATIVO'}">
                                        <a class="link-inativar"
                                           href="${pageContext.request.contextPath}/usuarios?acao=inativar&id=${u.id}"
                                           onclick="return confirm('Inativar o usuário ${u.nome}?');">
                                            Inativar
                                        </a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr><td colspan="7" class="mensagem-vazia">Nenhum usuário encontrado.</td></tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>

    <div class="paginacao">
        <c:if test="${paginaAtual > 1}">
            <a href="${pageContext.request.contextPath}/usuarios?filtro=${filtro}&pagina=${paginaAtual - 1}&registrosPorPagina=${registrosPorPagina}">← Anterior</a>
        </c:if>
        <span>Página ${paginaAtual} de ${totalPaginas}</span>
        <c:if test="${paginaAtual < totalPaginas}">
            <a href="${pageContext.request.contextPath}/usuarios?filtro=${filtro}&pagina=${paginaAtual + 1}&registrosPorPagina=${registrosPorPagina}">Próximo →</a>
        </c:if>
    </div>
</div>
</body>
</html>