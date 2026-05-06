<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Novo Frete - Sistema de Fretes</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">

<script>
function formatarMoeda(campo) {
    let valor = campo.value.replace(/\D/g, "");

    if (valor === "") {
        campo.value = "";
        return;
    }

    valor = (parseInt(valor) / 100).toFixed(2) + "";

    valor = valor.replace(".", ",");
    valor = valor.replace(/\B(?=(\d{3})+(?!\d))/g, ".");

    campo.value = valor;
}

function desformatarMoeda(valor) {
    return valor.replace(/\./g, "").replace(",", ".");
}

function calcularValores() {
    let valorFrete = parseFloat(desformatarMoeda(document.getElementById("valorFrete").value)) || 0;
    let aliquotaIcms = parseFloat(desformatarMoeda(document.getElementById("aliquotaIcms").value)) || 0;

    let valorIcms = (valorFrete * aliquotaIcms / 100).toFixed(2);
    let valorTotal = (valorFrete + parseFloat(valorIcms)).toFixed(2);

    document.getElementById("valorIcmsExibido").innerText =
        "R$ " + valorIcms.replace(".", ",");

    document.getElementById("valorTotalExibido").innerText =
        "R$ " + valorTotal.replace(".", ",");
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
                        data-cidade="${c.cidade}"
                        data-uf="${c.uf}"
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
                        data-cidade="${c.cidade}"
                        data-uf="${c.uf}"
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

        <label for="municipioOrigem">Cidade de Origem:</label>
        <input type="text" id="municipioOrigem" name="municipioOrigem" class="form-control"
               value="${frete != null ? frete.municipioOrigem : ''}" />

        <label for="ufOrigem">UF de Origem:</label>
        <select id="ufOrigem" name="ufOrigem" class="form-control">
            <option value="">Selecione</option>
            <option value="AC" ${frete != null && frete.ufOrigem == 'AC' ? 'selected' : ''}>AC - Acre</option>
            <option value="AL" ${frete != null && frete.ufOrigem == 'AL' ? 'selected' : ''}>AL - Alagoas</option>
            <option value="AP" ${frete != null && frete.ufOrigem == 'AP' ? 'selected' : ''}>AP - Amapá</option>
            <option value="AM" ${frete != null && frete.ufOrigem == 'AM' ? 'selected' : ''}>AM - Amazonas</option>
            <option value="BA" ${frete != null && frete.ufOrigem == 'BA' ? 'selected' : ''}>BA - Bahia</option>
            <option value="CE" ${frete != null && frete.ufOrigem == 'CE' ? 'selected' : ''}>CE - Ceará</option>
            <option value="DF" ${frete != null && frete.ufOrigem == 'DF' ? 'selected' : ''}>DF - Distrito Federal</option>
            <option value="ES" ${frete != null && frete.ufOrigem == 'ES' ? 'selected' : ''}>ES - Espírito Santo</option>
            <option value="GO" ${frete != null && frete.ufOrigem == 'GO' ? 'selected' : ''}>GO - Goiás</option>
            <option value="MA" ${frete != null && frete.ufOrigem == 'MA' ? 'selected' : ''}>MA - Maranhão</option>
            <option value="MT" ${frete != null && frete.ufOrigem == 'MT' ? 'selected' : ''}>MT - Mato Grosso</option>
            <option value="MS" ${frete != null && frete.ufOrigem == 'MS' ? 'selected' : ''}>MS - Mato Grosso do Sul</option>
            <option value="MG" ${frete != null && frete.ufOrigem == 'MG' ? 'selected' : ''}>MG - Minas Gerais</option>
            <option value="PA" ${frete != null && frete.ufOrigem == 'PA' ? 'selected' : ''}>PA - Pará</option>
            <option value="PB" ${frete != null && frete.ufOrigem == 'PB' ? 'selected' : ''}>PB - Paraíba</option>
            <option value="PR" ${frete != null && frete.ufOrigem == 'PR' ? 'selected' : ''}>PR - Paraná</option>
            <option value="PE" ${frete != null && frete.ufOrigem == 'PE' ? 'selected' : ''}>PE - Pernambuco</option>
            <option value="PI" ${frete != null && frete.ufOrigem == 'PI' ? 'selected' : ''}>PI - Piauí</option>
            <option value="RJ" ${frete != null && frete.ufOrigem == 'RJ' ? 'selected' : ''}>RJ - Rio de Janeiro</option>
            <option value="RN" ${frete != null && frete.ufOrigem == 'RN' ? 'selected' : ''}>RN - Rio Grande do Norte</option>
            <option value="RS" ${frete != null && frete.ufOrigem == 'RS' ? 'selected' : ''}>RS - Rio Grande do Sul</option>
            <option value="RO" ${frete != null && frete.ufOrigem == 'RO' ? 'selected' : ''}>RO - Rondônia</option>
            <option value="RR" ${frete != null && frete.ufOrigem == 'RR' ? 'selected' : ''}>RR - Roraima</option>
            <option value="SC" ${frete != null && frete.ufOrigem == 'SC' ? 'selected' : ''}>SC - Santa Catarina</option>
            <option value="SP" ${frete != null && frete.ufOrigem == 'SP' ? 'selected' : ''}>SP - São Paulo</option>
            <option value="SE" ${frete != null && frete.ufOrigem == 'SE' ? 'selected' : ''}>SE - Sergipe</option>
            <option value="TO" ${frete != null && frete.ufOrigem == 'TO' ? 'selected' : ''}>TO - Tocantins</option>
        </select>

        <label for="municipioDestino">Cidade de Destino:</label>
        <input type="text" id="municipioDestino" name="municipioDestino" class="form-control"
               value="${frete != null ? frete.municipioDestino : ''}" />

        <label for="ufDestino">UF de Destino:</label>
        <select id="ufDestino" name="ufDestino" class="form-control">
            <option value="">Selecione</option>
            <option value="AC" ${frete != null && frete.ufDestino == 'AC' ? 'selected' : ''}>AC - Acre</option>
            <option value="AL" ${frete != null && frete.ufDestino == 'AL' ? 'selected' : ''}>AL - Alagoas</option>
            <option value="AP" ${frete != null && frete.ufDestino == 'AP' ? 'selected' : ''}>AP - Amapá</option>
            <option value="AM" ${frete != null && frete.ufDestino == 'AM' ? 'selected' : ''}>AM - Amazonas</option>
            <option value="BA" ${frete != null && frete.ufDestino == 'BA' ? 'selected' : ''}>BA - Bahia</option>
            <option value="CE" ${frete != null && frete.ufDestino == 'CE' ? 'selected' : ''}>CE - Ceará</option>
            <option value="DF" ${frete != null && frete.ufDestino == 'DF' ? 'selected' : ''}>DF - Distrito Federal</option>
            <option value="ES" ${frete != null && frete.ufDestino == 'ES' ? 'selected' : ''}>ES - Espírito Santo</option>
            <option value="GO" ${frete != null && frete.ufDestino == 'GO' ? 'selected' : ''}>GO - Goiás</option>
            <option value="MA" ${frete != null && frete.ufDestino == 'MA' ? 'selected' : ''}>MA - Maranhão</option>
            <option value="MT" ${frete != null && frete.ufDestino == 'MT' ? 'selected' : ''}>MT - Mato Grosso</option>
            <option value="MS" ${frete != null && frete.ufDestino == 'MS' ? 'selected' : ''}>MS - Mato Grosso do Sul</option>
            <option value="MG" ${frete != null && frete.ufDestino == 'MG' ? 'selected' : ''}>MG - Minas Gerais</option>
            <option value="PA" ${frete != null && frete.ufDestino == 'PA' ? 'selected' : ''}>PA - Pará</option>
            <option value="PB" ${frete != null && frete.ufDestino == 'PB' ? 'selected' : ''}>PB - Paraíba</option>
            <option value="PR" ${frete != null && frete.ufDestino == 'PR' ? 'selected' : ''}>PR - Paraná</option>
            <option value="PE" ${frete != null && frete.ufDestino == 'PE' ? 'selected' : ''}>PE - Pernambuco</option>
            <option value="PI" ${frete != null && frete.ufDestino == 'PI' ? 'selected' : ''}>PI - Piauí</option>
            <option value="RJ" ${frete != null && frete.ufDestino == 'RJ' ? 'selected' : ''}>RJ - Rio de Janeiro</option>
            <option value="RN" ${frete != null && frete.ufDestino == 'RN' ? 'selected' : ''}>RN - Rio Grande do Norte</option>
            <option value="RS" ${frete != null && frete.ufDestino == 'RS' ? 'selected' : ''}>RS - Rio Grande do Sul</option>
            <option value="RO" ${frete != null && frete.ufDestino == 'RO' ? 'selected' : ''}>RO - Rondônia</option>
            <option value="RR" ${frete != null && frete.ufDestino == 'RR' ? 'selected' : ''}>RR - Roraima</option>
            <option value="SC" ${frete != null && frete.ufDestino == 'SC' ? 'selected' : ''}>SC - Santa Catarina</option>
            <option value="SP" ${frete != null && frete.ufDestino == 'SP' ? 'selected' : ''}>SP - São Paulo</option>
            <option value="SE" ${frete != null && frete.ufDestino == 'SE' ? 'selected' : ''}>SE - Sergipe</option>
            <option value="TO" ${frete != null && frete.ufDestino == 'TO' ? 'selected' : ''}>TO - Tocantins</option>
        </select>

        <h3>Carga</h3>

        <label for="descricaoCarga">Descrição da Carga:</label>
        <input type="text" id="descricaoCarga" name="descricaoCarga" class="form-control"
               value="${frete != null ? frete.descricaoCarga : ''}" />

        <label for="pesoKg">Peso Bruto (kg):</label>
		 <input type="text" id="pesoKg" name="pesoKg" class="form-control"
		       oninput="formatarMoeda(this)"
		       placeholder="0,00"
		       value="${frete != null ? frete.pesoKg : ''}" />

        <label for="volumes">Volumes:</label>
		<input type="text" id="volumes" name="volumes" class="form-control"
		       maxlength="10"
		       oninput="this.value = this.value.replace(/[^0-9]/g, '');"
		       value="${frete != null ? frete.volumes : ''}" />

        <h3>Valores</h3>

        <label for="valorFrete">Valor do Frete (R$):</label>
		<input type="text" id="valorFrete" name="valorFrete" class="form-control"
		       oninput="formatarMoeda(this); calcularValores();"
		       placeholder="0,00"
		       value="${frete != null ? frete.valorFrete : ''}"/>

        <label for="aliquotaIcms">Alíquota ICMS (%):</label>
		<input type="text" id="aliquotaIcms" name="aliquotaIcms" class="form-control"
		       oninput="formatarMoeda(this); calcularValores();"
		       placeholder="0,00"
		       value="${frete != null ? frete.aliquotaIcms : '12'}" />

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