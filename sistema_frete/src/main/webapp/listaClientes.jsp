<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Lista de Clientes</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 30px;
        }

        .topo {
            margin-bottom: 20px;
        }

        .btn-novo {
            display: inline-block;
            padding: 10px 14px;
            background: #198754;
            color: #fff;
            text-decoration: none;
            border-radius: 4px;
            margin-bottom: 15px;
        }

        .filtro-form {
            margin-bottom: 20px;
        }

        .filtro-form input[type="text"] {
            padding: 6px;
            width: 250px;
        }

        .filtro-form button {
            padding: 7px 12px;
            cursor: pointer;
        }

        table {
            border-collapse: collapse;
            width: 100%;
        }

        table th,
        table td {
            border: 1px solid #ccc;
            padding: 8px;
            text-align: left;
        }

        table th {
            background: #f2f2f2;
        }

        .acoes a {
            color: #c0392b;
            text-decoration: none;
            font-weight: bold;
        }

        .paginacao {
            margin-top: 20px;
        }

        .paginacao a {
            margin-right: 10px;
            text-decoration: none;
            color: #0d6efd;
        }

        .mensagem-vazia {
            text-align: center;
        }
    </style>
</head>
<body>

<div class="topo">
    <a href="${pageContext.request.contextPath}/clientes?acao=novo" class="btn-novo">
        Novo Cliente
    </a>
</div>

<form action="${pageContext.request.contextPath}/clientes" method="get" class="filtro-form">
    <label for="filtro">Nome / Razão Social:</label>
    <input type="text" id="filtro" name="filtro" value="${filtro}" />

    <input type="hidden" name="pagina" value="1" />
    <input type="hidden" name="registrosPorPagina" value="${registrosPorPagina != null ? registrosPorPagina : 10}" />

    <button type="submit">Buscar</button>
</form>

<table>
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
                            <a href="${pageContext.request.contextPath}/clientes?acao=excluir&id=${cliente.id}"
                               onclick="return confirm('Tem certeza que deseja excluir este cliente?');">
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

</body>
</html>