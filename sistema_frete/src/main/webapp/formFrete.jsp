<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Novo Frete - Sistema de Fretes</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <script>
        function calcularValores() {
            var valorFrete   = parseFloat(document.getElementById('valorFrete').value.replace(',', '.')) || 0;
            var aliquotaIcms = parseFloat(document.getElementById('aliquotaIcms').value.replace(',', '.')) || 0;
            var valorIcms    = (valorFrete * aliquotaIcms / 100).toFixed(2);
            var valorTotal   = (valorFrete + parseFloat(valorIcms)).toFixed(2);
            document.getElementById('valorIcmsExibido').innerText = 'R$ ' + valorIcms.replace('.', ',');
            document.getElementById('valorTotalExibido').innerText = 'R$ ' + valorTotal.replace('.', ',');
        }
    </script>
</head>

<body>

<c:set var="paginaAtualMenu" value="fretes" scope="request"/>
<jsp:include page="/includes/header.jsp"/>

<div class="container container-form">

    <div class="page-header">
        <h1>Novo Frete</h1>
    </div>

    <div class="topo-botoes">
        <a href="${pageContext.request.contextPath}/fretes" class="btn btn-secondary">Voltar</a>
    </div>

    <c:if test="${not empty erro}">
        <div class="erro">${erro}</div>
    </c:if>

	<form action="${pageContext.request.contextPath}/fretes" method="post" class="form-cadastro">

        <h3>Partes Envolvidas</h3>

        <label for="idRemetente">Remetente:</label>
        <select id="idRemetente" name="idRemetente" class="form-control">
            <option value="">Selecione o remetente</option>
            <c:forEach var="c" items="${clientes}">
                <option value="${c.id}"
                    ${frete != null && frete.remetente != null && frete.remetente.id == c.id ? 'selected' : ''}>
                    ${c.razaoSocial}
                </option>
            </c:forEach>
        </select>

        <label for="idDestinatario">Destinatário:</label>
        <select id="idDestinatario" name="idDestinatario" class="form-control">
            <option value="">Selecione o destinatário</option>
            <c:forEach var="c" items="${clientes}">
                <option value="${c.id}"
                    ${frete != null && frete.destinatario != null && frete.destinatario.id == c.id ? 'selected' : ''}>
                    ${c.razaoSocial}
                </option>
            </c:forEach>
        </select>

        <label for="idMotorista">Motorista:</label>
        <select id="idMotorista" name="idMotorista" class="form-control">
            <option value="">Selecione o motorista</option>
            <c:forEach var="m" items="${motoristas}">
                <option value="${m.id}"
                    ${frete != null && frete.motorista != null && frete.motorista.id == m.id ? 'selected' : ''}>
                    ${m.nome} — CNH: ${m.cnhNumero} (${m.cnhCategoria})
                </option>
            </c:forEach>
        </select>

        <label for="idVeiculo">Veículo:</label>
        <select id="idVeiculo" name="idVeiculo" class="form-control">
            <option value="">Selecione o veículo</option>
            <c:forEach var="v" items="${veiculos}">
                <option value="${v.id}"
                    ${frete != null && frete.veiculo != null && frete.veiculo.id == v.id ? 'selected' : ''}>
                    ${v.placa} — ${v.tipo} — Cap: ${v.capacidadeKg} kg
                </option>
            </c:forEach>
        </select>

        <h3>Origem e Destino</h3>

        <label for="municipioOrigem">Município de Origem:</label>
        <input type="text" id="municipioOrigem" name="municipioOrigem" class="form-control"
               value="${frete != null ? frete.municipioOrigem : ''}" />

        <label for="ufOrigem">UF de Origem:</label>
        <input type="text" id="ufOrigem" name="ufOrigem" class="form-control" maxlength="2"
               style="width:80px"
               value="${frete != null ? frete.ufOrigem : ''}" />

        <label for="municipioDestino">Município de Destino:</label>
        <input type="text" id="municipioDestino" name="municipioDestino" class="form-control"
               value="${frete != null ? frete.municipioDestino : ''}" />

        <label for="ufDestino">UF de Destino:</label>
        <input type="text" id="ufDestino" name="ufDestino" class="form-control" maxlength="2"
               style="width:80px"
               value="${frete != null ? frete.ufDestino : ''}" />

        <h3>Carga</h3>

        <label for="descricaoCarga">Descrição da Carga:</label>
        <input type="text" id="descricaoCarga" name="descricaoCarga" class="form-control"
               value="${frete != null ? frete.descricaoCarga : ''}" />

        <label for="pesoKg">Peso Bruto (kg):</label>
        <input type="number" id="pesoKg" name="pesoKg" class="form-control"
               step="0.01" min="0.01"
               value="${frete != null ? frete.pesoKg : ''}" />

        <label for="volumes">Volumes:</label>
        <input type="number" id="volumes" name="volumes" class="form-control"
               min="1"
               value="${frete != null ? frete.volumes : ''}" />

        <h3>Valores</h3>

        <label for="valorFrete">Valor do Frete (R$):</label>
        <input type="number" id="valorFrete" name="valorFrete" class="form-control"
               step="0.01" min="0.01" oninput="calcularValores()"
               value="${frete != null ? frete.valorFrete : ''}" />

        <label for="aliquotaIcms">Alíquota ICMS (%):</label>
        <input type="number" id="aliquotaIcms" name="aliquotaIcms" class="form-control"
               step="0.01" min="0" value="${frete != null ? frete.aliquotaIcms : '12'}"
               oninput="calcularValores()" />

        <p>Valor ICMS: <strong id="valorIcmsExibido">—</strong></p>
        <p>Valor Total: <strong id="valorTotalExibido">—</strong></p>

        <h3>Datas</h3>

        <label for="dataPrevisaoEntrega">Data Prevista de Entrega:</label>
        <input type="date" id="dataPrevisaoEntrega" name="dataPrevisaoEntrega" class="form-control"
               value="${frete != null ? frete.dataPrevisaoEntrega : ''}" />

        <div class="acoes-form" style="margin-top: 20px;">
            <button type="submit" class="btn btn-primary">Registrar Frete</button>
            <a href="${pageContext.request.contextPath}/fretes" class="btn btn-secondary">Cancelar</a>
        </div>

    </form>

</div>

</body>
</html>