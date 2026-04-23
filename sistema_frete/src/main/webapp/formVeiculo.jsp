<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Cadastro de Veículo</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f5f5f5; }
        .container {
            width: 600px;
            margin: 40px auto;
            background: white;
            padding: 20px;
            border-radius: 6px;
        }
        input, select {
            width: 100%;
            margin-bottom: 12px;
            padding: 8px;
            box-sizing: border-box;
        }
        .erro {
            background: #f8d7da;
            color: #842029;
            padding: 10px;
            margin-bottom: 15px;
            border-radius: 4px;
        }
        .acoes-form {
            display: flex;
            gap: 10px;
        }
        .btn-salvar {
            padding: 10px;
            background: #0d6efd;
            color: white;
            border: none;
            cursor: pointer;
            width: 100%;
        }
        .btn-cancelar {
            display: inline-block;
            width: 100%;
            text-align: center;
            padding: 10px;
            background: #6c757d;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            box-sizing: border-box;
        }
    </style>

    <script>
        function mascaraPlaca(campo) {
            campo.value = campo.value.toUpperCase().replace(/[^A-Z0-9]/g, '').substring(0, 7);
        }
    </script>
</head>
<body>

<div class="container">
    <h2>${veiculo.id != null ? 'Editar Veículo' : 'Cadastro de Veículo'}</h2>

    <c:if test="${not empty erro}">
        <div class="erro">${erro}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/veiculos" method="post">
        <input type="hidden" name="id" value="${veiculo.id}" />
        <input type="hidden" name="filtroRetorno" value="${param.filtro != null ? param.filtro : filtro}" />
        <input type="hidden" name="statusFiltroRetorno" value="${param.statusFiltro != null ? param.statusFiltro : statusFiltro}" />
        <input type="hidden" name="paginaRetorno" value="${param.pagina != null ? param.pagina : paginaAtual}" />
        <input type="hidden" name="registrosPorPaginaRetorno" value="${param.registrosPorPagina != null ? param.registrosPorPagina : registrosPorPagina}" />

        <label>Placa:</label>
        <input type="text" name="placa" value="${veiculo.placa}" onkeyup="mascaraPlaca(this)" maxlength="7" required />

        <label>RNTRC:</label>
        <input type="text" name="rntrc" value="${veiculo.rntrc}" required />

        <label>Ano de Fabricação:</label>
        <input type="number" name="anoFabricacao" value="${veiculo.anoFabricacao}" required />

        <label>Tipo:</label>
        <select name="tipo" required>
            <option value="">Selecione</option>
            <option value="TRUCK" ${veiculo.tipo == 'TRUCK' ? 'selected' : ''}>Truck</option>
            <option value="CARRETA" ${veiculo.tipo == 'CARRETA' ? 'selected' : ''}>Carreta</option>
            <option value="VAN" ${veiculo.tipo == 'VAN' ? 'selected' : ''}>Van</option>
            <option value="UTILITARIO" ${veiculo.tipo == 'UTILITARIO' ? 'selected' : ''}>Utilitário</option>
        </select>

        <label>Tara KG:</label>
        <input type="text" name="taraKg" value="${veiculo.taraKg}" required />

        <label>Capacidade KG:</label>
        <input type="text" name="capacidadeKg" value="${veiculo.capacidadeKg}" required />

        <label>Volume m³:</label>
        <input type="text" name="volumeM3" value="${veiculo.volumeM3}" required />

        <label>Status:</label>
        <select name="status" required>
            <option value="">Selecione</option>
            <option value="DISPONIVEL" ${veiculo.status == 'DISPONIVEL' ? 'selected' : ''}>Disponível</option>
            <option value="EM_VIAGEM" ${veiculo.status == 'EM_VIAGEM' ? 'selected' : ''}>Em Viagem</option>
            <option value="EM_MANUTENCAO" ${veiculo.status == 'EM_MANUTENCAO' ? 'selected' : ''}>Em Manutenção</option>
            <option value="INATIVO" ${veiculo.status == 'INATIVO' ? 'selected' : ''}>Inativo</option>
        </select>

        <div class="acoes-form">
            <button type="submit" class="btn-salvar">Salvar</button>

            <a class="btn-cancelar"
               href="${pageContext.request.contextPath}/veiculos?filtro=${param.filtro != null ? param.filtro : filtro}&statusFiltro=${param.statusFiltro != null ? param.statusFiltro : statusFiltro}&pagina=${param.pagina != null ? param.pagina : paginaAtual}&registrosPorPagina=${param.registrosPorPagina != null ? param.registrosPorPagina : registrosPorPagina}">
                Cancelar
            </a>
        </div>
    </form>
</div>

</body>
</html>