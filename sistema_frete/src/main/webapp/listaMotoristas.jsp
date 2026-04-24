<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Motoristas - Sistema de Fretes</title>
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

        .msg-sucesso {
            background: #d1e7dd;
            color: #0f5132;
            padding: 10px;
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
            text-decoration: none;
            font-weight: bold;
            margin-right: 8px;
        }

        .link-editar {
            color: #0d6efd;
        }

        .link-inativar {
            color: #c0392b;
        }

        .status-inativo {
            color: gray;
            font-style: italic;
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
    <a href="${pageContext.request.contextPath}/motoristas?acao=novo&filtro=${filtro}&pagina=${paginaAtual}&registrosPorPagina=${registrosPorPagina}" class="btn-novo">
        Novo Motorista
    </a>
</div>

<c:if test="${not empty sucesso}">
    <div class="msg-sucesso">${sucesso}</div>
</c:if>

<form action="${pageContext.request.contextPath}/motoristas" method="get" class="filtro-form">
    <label for="filtro">Nome:</label>
    <input type="text" id="filtro" name="filtro" value="${filtro}" />

    <input type="hidden" name="pagina" value="1" />
    <input type="hidden" name="registrosPorPagina" value="${registrosPorPagina != null ? registrosPorPagina : 10}" />

    <button type="submit">Buscar</button>
</form>

<table>
    <thead>
        <tr>
            <th>ID</th>
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
                        <td>${motorista.telefone}</td>
                        <td>${motorista.cnhNumero}</td>
                        <td>${motorista.cnhCategoria}</td>
                        <td>${motorista.cnhValidade}</td>
                        <td>${motorista.tipoVinculo}</td>
                        <td>
                            <c:choose>
                                <c:when test="${motorista.status == 'ATIVO'}">
                                    <span style="color:green; font-weight:bold;">ATIVO</span>
                                </c:when>
                                <c:when test="${motorista.status == 'INATIVO'}">
                                    <span style="color:gray;">INATIVO</span>
                                </c:when>
                                <c:when test="${motorista.status == 'SUSPENSO'}">
                                    <span style="color:#b58100; font-weight:bold;">SUSPENSO</span>
                                </c:when>
                                <c:otherwise>
                                    ${motorista.status}
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="acoes">
                            <a class="link-editar"
                               href="${pageContext.request.contextPath}/motoristas?acao=editar&id=${motorista.id}&filtro=${filtro}&pagina=${paginaAtual}&registrosPorPagina=${registrosPorPagina}">
                                Editar
                            </a>

                            <c:choose>
                                <c:when test="${motorista.status != 'INATIVO'}">
                                    |
                                    <a class="link-inativar"
                                       href="${pageContext.request.contextPath}/motoristas?acao=inativar&id=${motorista.id}"
                                       onclick="return confirm('Tem certeza que deseja inativar este motorista?');">
                                        Inativar
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <span class="status-inativo">Inativado</span>
                                </c:otherwise>
                            </c:choose>
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

<div class="paginacao">
    <c:if test="${paginaAtual > 1}">
        <a href="${pageContext.request.contextPath}/motoristas?filtro=${filtro}&pagina=${paginaAtual - 1}&registrosPorPagina=${registrosPorPagina}">
            Anterior
        </a>
    </c:if>

    <span>Página ${paginaAtual} de ${totalPaginas}</span>

    <c:if test="${paginaAtual < totalPaginas}">
        <a href="${pageContext.request.contextPath}/motoristas?filtro=${filtro}&pagina=${paginaAtual + 1}&registrosPorPagina=${registrosPorPagina}">
            Próximo
        </a>
    </c:if>
</div>

</body>
</html>