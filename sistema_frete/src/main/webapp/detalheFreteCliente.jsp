<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Frete ${frete.numero} - Sistema de Fretes</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>

<c:set var="paginaAtualMenu" value="fretes" scope="request"/>
<jsp:include page="/includes/header.jsp"/>

<div class="container">

    <div class="page-header">
        <h1>Frete ${frete.numero}</h1>
    </div>

    <c:if test="${not empty sucesso}"><div class="msg-sucesso">${sucesso}</div></c:if>
    <c:if test="${not empty erro}"><div class="msg-erro">${erro}</div></c:if>

    <div class="form-section">
        <div class="form-section-title">Dados Gerais</div>
        <table class="tabela-detalhe">
            <tr>
                <th>Número</th><td>${frete.numero}</td>
                <th>Status</th>
                <td>
                    <c:choose>
                        <c:when test="${frete.status == 'EMITIDO'}">
                            <span class="badge status-emitido">EMITIDO</span>
                        </c:when>
                        <c:when test="${frete.status == 'SAIDA_CONFIRMADA'}">
                            <span class="badge status-saida">SAÍDA CONFIRMADA</span>
                        </c:when>
                        <c:when test="${frete.status == 'EM_TRANSITO'}">
                            <span class="badge status-transito">EM TRÂNSITO</span>
                        </c:when>
                        <c:when test="${frete.status == 'ENTREGUE'}">
                            <span class="badge status-entregue">ENTREGUE</span>
                        </c:when>
                        <c:when test="${frete.status == 'NAO_ENTREGUE'}">
                            <span class="badge status-nao-entregue">NÃO ENTREGUE</span>
                        </c:when>
                        <c:when test="${frete.status == 'CANCELADO'}">
                            <span class="badge status-cancelado">CANCELADO</span>
                        </c:when>
                    </c:choose>
                </td>
            </tr>
            <tr>
                <th>Remetente</th><td>${frete.remetente.razaoSocial}</td>
                <th>Destinatário</th><td>${frete.destinatario.razaoSocial}</td>
            </tr>
            <tr>
                <th>Motorista</th><td>${frete.motorista.nome} — CPF: ${frete.motorista.cpf}</td>
                <th>Veículo</th><td>${frete.veiculo.placa}</td>
            </tr>
            <tr>
                <th>Origem</th><td>${frete.cidadeOrigem}/${frete.ufOrigem}</td>
                <th>Destino</th><td>${frete.cidadeDestino}/${frete.ufDestino}</td>
            </tr>
            <tr>
                <th>Emissão</th><td>${frete.dataEmissao}</td>
                <th>Previsão Entrega</th><td>${frete.dataPrevisaoEntrega}</td>
            </tr>
            <tr>
                <th>Saída</th><td>${not empty frete.dataSaida ? frete.dataSaida : '—'}</td>
                <th>Entrega</th><td>${not empty frete.dataEntrega ? frete.dataEntrega : '—'}</td>
            </tr>
        </table>
    </div>

    <div class="form-section">
        <div class="form-section-title">Carga e Valores</div>
        <table class="tabela-detalhe">
            <tr>
                <th>Descrição da Carga</th><td colspan="3">${frete.descricaoCarga}</td>
            </tr>
            <tr>
                <th>Peso (kg)</th><td>${frete.pesoKg}</td>
                <th>Volumes</th><td>${frete.volumes}</td>
            </tr>
            <tr>
                <th>Valor do Frete</th><td>R$ ${frete.valorFrete}</td>
                <th>Alíquota ICMS</th><td>${frete.aliquotaIcms}%</td>
            </tr>
            <tr>
                <th>Valor ICMS</th><td>R$ ${frete.valorIcms}</td>
                <th>Valor Total</th><td><strong>R$ ${frete.valorTotal}</strong></td>
            </tr>
        </table>
    </div>

    <div class="form-section">
        <div class="form-section-title">Histórico de Ocorrências</div>
        <c:choose>
            <c:when test="${not empty ocorrencias}">
                <div class="table-container">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>Data/Hora</th>
                                <th>Tipo</th>
                                <th>Município/UF</th>
                                <th>Descrição</th>
                                <th>Recebedor</th>
                                <th>Documento</th>
                            </tr>
                        </thead>
                        <tbody>
                           <c:forEach var="oc" items="${ocorrencias}">
                                <tr>
                                    <td>${oc.dataHora}</td>
                                    <td><span class="badge status-info">${oc.tipo}</span></td>
                                    <td>${oc.cidade}/${oc.uf}</td>
                                    <td>${not empty oc.descricao ? oc.descricao : '—'}</td>
                                    <td>${not empty oc.nomeRecebedor ? oc.nomeRecebedor : '—'}</td>
                                    <td>${not empty oc.documentoRecebedor ? oc.documentoRecebedor : '—'}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <p class="mensagem-vazia">Nenhuma ocorrência registrada.</p>
            </c:otherwise>
        </c:choose>
    </div>

</div>
</body>
</html>