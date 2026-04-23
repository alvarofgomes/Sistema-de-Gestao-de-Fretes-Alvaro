<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Veículos - Sistema de Fretes</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; }

        .btn-novo {
            display: inline-block;
            padding: 10px 14px;
            background: #198754;
            color: white;
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

        .filtro-form input {
            padding: 6px;
            width: 250px;
        }

        .filtro-form select {
            padding: 6px;
            margin-left: 8px;
        }

        .filtro-form button {
            padding: 7px 12px;
            cursor: pointer;
            margin-left: 8px;
        }

        table { border-collapse: collapse; width: 100%; }

        table th, table td {
            border: 1px solid #ccc;
            padding: 8px;
            text-align: left;
        }

        table th { background: #f2f2f2; }

        .acoes a {
            text-decoration: none;
            font-weight: bold;
            color: #0d6efd;
            margin-right: 6px;
        }

        .acoes .link-inativar {
            color: #c0392b;
        }

        .status-disponivel {
            color: green;
            font-weight: bold;
        }

        .status-viagem {
            color: #0d6efd;
            font-weight: bold;
        }

        .status-manutencao {
            color: #b58100;
            font-weight: bold;
        }

        .status-inativo {
            color: gray;
            font-style: italic;
        }

        .paginacao { margin-top: 20px; }

        .paginacao a {
            margin-right: 10px;
            text-decoration: none;
            color: #0d6efd;
        }

        .mensagem-vazia { text-align: center; }
    </style>
</head>
<body>

<a href="${pageContext.request.contextPath}/veiculos?acao=novo&filtro=${filtro}&statusFiltro=${statusFiltro}&pagina=${paginaAtual}&registrosPorPagina=${registrosPorPagina}" class="btn-novo">
    Novo Veículo
</a>

<c:if test="${not empty sucesso}">
    <div class="msg-sucesso">${sucesso}</div>
</c:if>

<form action="${pageContext.request.contextPath}/veiculos" method="get" class="filtro-form">
    <label for="filtro">Placa / RNTRC:</label>
    <input type="text" id="filtro" name="filtro" value="${filtro}" />

    <label for="statusFiltro">Status:</label>
    <select id="statusFiltro" name="statusFiltro">
        <option value="">Todos</option>
        <option value="DISPONIVEL" ${statusFiltro == 'DISPONIVEL' ? 'selected' : ''}>Disponível</option>
        <option value="EM_VIAGEM" ${statusFiltro == 'EM_VIAGEM' ? 'selected' : ''}>Em viagem</option>
        <option value="EM_MANUTENCAO" ${statusFiltro == 'EM_MANUTENCAO' ? 'selected' : ''}>Em manutenção</option>
        <option value="INATIVO" ${statusFiltro == 'INATIVO' ? 'selected' : ''}>Inativo</option>
    </select>

    <input type="hidden" name="pagina" value="1" />
    <input type="hidden" name="registrosPorPagina" value="${registrosPorPagina != null ? registrosPorPagina : 10}" />

    <button type="submit">Buscar</button>
</form>

<table>
    <thead>
        <tr>
            <th>ID</th>
            <th>Placa</th>
            <th>RNTRC</th>
            <th>Ano</th>
            <th>Tipo</th>
            <th>Tara KG</th>
            <th>Capacidade KG</th>
            <th>Volume m³</th>
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
                        <td>${veiculo.placa}</td>
                        <td>${veiculo.rntrc}</td>
                        <td>${veiculo.anoFabricacao}</td>
                        <td>${veiculo.tipo}</td>
                        <td>${veiculo.taraKg}</td>
                        <td>${veiculo.capacidadeKg}</td>
                        <td>${veiculo.volumeM3}</td>
                        <td>
                            <c:choose>
                                <c:when test="${veiculo.status == 'DISPONIVEL'}">
                                    <span class="status-disponivel">DISPONÍVEL</span>
                                </c:when>
                                <c:when test="${veiculo.status == 'EM_VIAGEM'}">
                                    <span class="status-viagem">EM VIAGEM</span>
                                </c:when>
                                <c:when test="${veiculo.status == 'EM_MANUTENCAO'}">
                                    <span class="status-manutencao">EM MANUTENÇÃO</span>
                                </c:when>
                                <c:when test="${veiculo.status == 'INATIVO'}">
                                    <span class="status-inativo">INATIVO</span>
                                </c:when>
                                <c:otherwise>
                                    ${veiculo.status}
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="acoes">
                            <a href="${pageContext.request.contextPath}/veiculos?acao=editar&id=${veiculo.id}&filtro=${filtro}&statusFiltro=${statusFiltro}&pagina=${paginaAtual}&registrosPorPagina=${registrosPorPagina}">
                                Editar
                            </a>

                            <c:if test="${veiculo.status != 'INATIVO'}">
                                |
                                <a class="link-inativar"
                                   href="${pageContext.request.contextPath}/veiculos?acao=inativar&id=${veiculo.id}"
                                   onclick="return confirm('Tem certeza que deseja inativar este veículo?');">
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

<div class="paginacao">
    <c:if test="${paginaAtual > 1}">
        <a href="${pageContext.request.contextPath}/veiculos?filtro=${filtro}&statusFiltro=${statusFiltro}&pagina=${paginaAtual - 1}&registrosPorPagina=${registrosPorPagina}">
            Anterior
        </a>
    </c:if>

    <span>Página ${paginaAtual} de ${totalPaginas}</span>

    <c:if test="${paginaAtual < totalPaginas}">
        <a href="${pageContext.request.contextPath}/veiculos?filtro=${filtro}&statusFiltro=${statusFiltro}&pagina=${paginaAtual + 1}&registrosPorPagina=${registrosPorPagina}">
            Próximo
        </a>
    </c:if>
</div>

</body>
</html>