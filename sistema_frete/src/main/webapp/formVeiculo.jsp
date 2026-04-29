<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>${veiculo.id != null ? 'Editar' : 'Novo'} Veículo - Sistema de Fretes</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <script>
        function mascaraPlaca(c) {
            c.value = c.value.toUpperCase().replace(/[^A-Z0-9]/g,'').substring(0,7);
        }
    </script>
</head>
<body>

<c:set var="paginaAtualMenu" value="veiculos" scope="request"/>
<jsp:include page="/includes/header.jsp"/>

<div class="container container-form">

    <div class="page-header">
        <h1>${veiculo.id != null ? 'Editar Veículo' : 'Novo Veículo'}</h1>
    </div>

    <c:if test="${not empty erro}">
        <div class="msg-erro">${erro}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/veiculos" method="post">
        <input type="hidden" name="id"                        value="${veiculo.id}" />
        <input type="hidden" name="filtroRetorno"             value="${param.filtro != null ? param.filtro : filtro}" />
        <input type="hidden" name="statusFiltroRetorno"       value="${param.statusFiltro != null ? param.statusFiltro : statusFiltro}" />
        <input type="hidden" name="paginaRetorno"             value="${param.pagina != null ? param.pagina : paginaAtual}" />
        <input type="hidden" name="registrosPorPaginaRetorno" value="${param.registrosPorPagina != null ? param.registrosPorPagina : registrosPorPagina}" />

        <%-- SEÇÃO 1: Identificação --%>
        <div class="form-section">
            <div class="form-section-title">Identificação</div>
            <div class="form-grid cols-3">
                <div class="form-group">
                    <label>Placa *</label>
                    <input type="text" name="placa" value="${veiculo.placa}"
                           onkeyup="mascaraPlaca(this)" maxlength="7" required />
                </div>
                <div class="form-group">
                    <label>RNTRC *</label>
                    <input type="text" name="rntrc" value="${veiculo.rntrc}" required />
                </div>
                <div class="form-group">
                    <label>Ano de Fabricação *</label>
                    <input type="number" name="anoFabricacao" value="${veiculo.anoFabricacao}"
                           min="1950" max="2030" required />
                </div>
                <div class="form-group">
                    <label>Tipo *</label>
                    <select name="tipo" required>
                        <option value="">Selecione</option>
                        <option value="TRUCK"      ${veiculo.tipo == 'TRUCK'      ? 'selected' : ''}>Truck</option>
                        <option value="CARRETA"    ${veiculo.tipo == 'CARRETA'    ? 'selected' : ''}>Carreta</option>
                        <option value="VAN"        ${veiculo.tipo == 'VAN'        ? 'selected' : ''}>Van</option>
                        <option value="UTILITARIO" ${veiculo.tipo == 'UTILITARIO' ? 'selected' : ''}>Utilitário</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Status *</label>
                    <select name="status" required>
                        <option value="">Selecione</option>
                        <option value="DISPONIVEL"    ${veiculo.status == 'DISPONIVEL'    ? 'selected' : ''}>Disponível</option>
                        <option value="EM_VIAGEM"     ${veiculo.status == 'EM_VIAGEM'     ? 'selected' : ''}>Em Viagem</option>
                        <option value="EM_MANUTENCAO" ${veiculo.status == 'EM_MANUTENCAO' ? 'selected' : ''}>Em Manutenção</option>
                        <option value="INATIVO"       ${veiculo.status == 'INATIVO'       ? 'selected' : ''}>Inativo</option>
                    </select>
                </div>
            </div>
        </div>

        <div class="form-section">
            <div class="form-section-title">Capacidade e Dimensões</div>
            <div class="form-grid cols-3">
                <div class="form-group">
                    <label>Tara (kg) *</label>
                    <input type="text" name="taraKg" value="${veiculo.taraKg}" required />
                </div>
                <div class="form-group">
                    <label>Capacidade de Carga (kg) *</label>
                    <input type="text" name="capacidadeKg" value="${veiculo.capacidadeKg}" required />
                </div>
                <div class="form-group">
                    <label>Volume (m³) *</label>
                    <input type="text" name="volumeM3" value="${veiculo.volumeM3}" required />
                </div>
            </div>
        </div>

        <div class="acoes-form">
            <button type="submit" class="btn btn-primary">Salvar</button>
            <a class="btn btn-secondary"
               href="${pageContext.request.contextPath}/veiculos?filtro=${param.filtro != null ? param.filtro : filtro}&statusFiltro=${param.statusFiltro != null ? param.statusFiltro : statusFiltro}&pagina=${param.pagina != null ? param.pagina : paginaAtual}&registrosPorPagina=${param.registrosPorPagina != null ? param.registrosPorPagina : registrosPorPagina}">
                Cancelar
            </a>
        </div>
    </form>
</div>

</body>
</html>