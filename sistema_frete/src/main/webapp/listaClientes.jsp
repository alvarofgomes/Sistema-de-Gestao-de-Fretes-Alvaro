<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Clientes - Sistema de Fretes</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">

    <script>
        function confirmarExclusao(nome, url) {
            if (confirm("Deseja realmente excluir o cliente \"" + nome + "\"?")) {
                window.location.href = url;
            }
        }
    </script>
</head>

<body>

<c:set var="paginaAtualMenu" value="clientes" scope="request"/>
<jsp:include page="/includes/header.jsp"/>

<div class="container">

    <div class="page-header">
        <h1>Clientes</h1>
    </div>

    <div class="topo-botoes">
        <a href="${pageContext.request.contextPath}/home.jsp" class="btn btn-secondary">
            Voltar
        </a>

        <a href="${pageContext.request.contextPath}/clientes?acao=novo&filtro=${filtro}&pagina=${paginaAtual}&registrosPorPagina=${registrosPorPagina}" class="btn btn-success">
            Novo Cliente
        </a>
    </div>

    <c:if test="${not empty sucesso}">
        <div class="msg-sucesso">
            ${sucesso}
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/clientes" method="get" class="filtro-form">
        <label for="filtro">Nome / Razão Social:</label>

        <input type="text" id="filtro" name="filtro" value="${filtro}" />

        <input type="hidden" name="pagina" value="1" />
        <input type="hidden" name="registrosPorPagina" value="${registrosPorPagina != null ? registrosPorPagina : 10}" />

        <button type="submit" class="btn btn-primary">
            Buscar
        </button>
    </form>

    <table class="table">
        <thead>
            <tr>
                <th>ID</th>
                <th>Razão Social</th>
                <th>Nome Fantasia</th>
                <th>CNPJ</th>
                <th>Status</th>
                <th>Ações</th>
            </tr>
        </thead>

        <tbody>
            <c:choose>
                <c:when test="${not empty listaClientes}">
                    <c:forEach var="cliente" items="${listaClientes}">
                        <tr>
                            <td>${cliente.id}</td>
                            <td>${cliente.razaoSocial}</td>
                            <td>${cliente.nomeFantasia}</td>
                            <td>${cliente.cnpj}</td>
                            <td>${cliente.status}</td>
                            <td class="acoes">
                                <a class="link-editar"
                                   href="${pageContext.request.contextPath}/clientes?acao=editar&id=${cliente.id}&filtro=${filtro}&pagina=${paginaAtual}&registrosPorPagina=${registrosPorPagina}">
                                    Editar
                                </a>

                                <a class="link-excluir"
                                   href="javascript:void(0);"
                                   onclick="confirmarExclusao('${cliente.razaoSocial}', '${pageContext.request.contextPath}/clientes?acao=excluir&id=${cliente.id}')">
                                    Excluir
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </c:when>

                <c:otherwise>
                    <tr>
                        <td colspan="6" class="mensagem-vazia">Nenhum cliente encontrado.</td>
                    </tr>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>

    <div class="paginacao">
        <c:if test="${paginaAtual > 1}">
            <a href="${pageContext.request.contextPath}/clientes?filtro=${filtro}&pagina=${paginaAtual - 1}&registrosPorPagina=${registrosPorPagina}">
                Anterior
            </a>
        </c:if>

        <span>Página ${paginaAtual} de ${totalPaginas}</span>

        <c:if test="${paginaAtual < totalPaginas}">
            <a href="${pageContext.request.contextPath}/clientes?filtro=${filtro}&pagina=${paginaAtual + 1}&registrosPorPagina=${registrosPorPagina}">
                Próximo
            </a>
        </c:if>
    </div>

</div>

</body>
</html>