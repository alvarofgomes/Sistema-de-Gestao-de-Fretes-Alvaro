<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Detalhe do Frete - Sistema de Fretes</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>

<body>

<c:set var="paginaAtualMenu" value="fretes" scope="request"/>
<jsp:include page="/includes/header.jsp"/>

<div class="container">

    <div class="page-header">
        <h1>Frete ${frete.numero}</h1>
    </div>

    <div class="topo-botoes">
        <a href="${pageContext.request.contextPath}/fretes" class="btn btn-secondary">Voltar</a>

        <%-- Confirmar Saída — só aparece se EMITIDO --%>
        <c:if test="${frete.status == 'EMITIDO'}">
            <a href="${pageContext.request.contextPath}/fretes?acao=confirmarSaida&id=${frete.id}"
               class="btn btn-primary"
               onclick="return confirm('Confirmar saída do frete ${frete.numero}?');">
                Confirmar Saída
            </a>
        </c:if>

        <%-- Cancelar — só aparece se EMITIDO --%>
        <c:if test="${frete.status == 'EMITIDO'}">
            <a href="${pageContext.request.contextPath}/fretes?acao=cancelar&id=${frete.id}"
               class="btn btn-danger"
               onclick="return confirm('Tem certeza que deseja cancelar este frete?');">
                Cancelar Frete
            </a>
        </c:if>
    </div>

    <c:if test="${not empty sucesso}">
        <div class="msg-sucesso">${sucesso}</div>
    </c:if>

    <c:if test="${not empty erro}">
        <div class="erro">${erro}</div>
    </c:if>

    <%-- ── DADOS DO FRETE ── --%>
    <h3>Dados Gerais</h3>
    <table class="table">
        <tr><th>Número</th><td>${frete.numero}</td><th>Status</th>
            <td>
                <c:choose>
                    <c:when test="${frete.status == 'EMITIDO'}">
                        <span class="status-info">EMITIDO</span>
                    </c:when>
                    <c:when test="${frete.status == 'SAIDA_CONFIRMADA'}">
                        <span class="status-alerta">SAÍDA CONFIRMADA</span>
                    </c:when>
                    <c:when test="${frete.status == 'EM_TRANSITO'}">
                        <span class="status-transito">EM TRÂNSITO</span>
                    </c:when>
                    <c:when test="${frete.status == 'ENTREGUE'}">
                        <span class="status-ativo">ENTREGUE</span>
                    </c:when>
                    <c:when test="${frete.status == 'NAO_ENTREGUE'}">
                        <span class="status-nao-entregue">NÃO ENTREGUE</span>
                    </c:when>
                    <c:when test="${frete.status == 'CANCELADO'}">
                        <span class="status-inativo">CANCELADO</span>
                    </c:when>
                </c:choose>
            </td>
        </tr>
        <tr><th>Remetente</th><td>${frete.remetente.razaoSocial}</td>
            <th>Destinatário</th><td>${frete.destinatario.razaoSocial}</td></tr>
        <tr><th>Motorista</th><td>${frete.motorista.nome} — CPF: ${frete.motorista.cpf}</td>
            <th>Veículo</th><td>${frete.veiculo.placa}</td></tr>
        <tr><th>Origem</th><td>${frete.municipioOrigem}/${frete.ufOrigem}</td>
            <th>Destino</th><td>${frete.municipioDestino}/${frete.ufDestino}</td></tr>
        <tr><th>Emissão</th><td>${frete.dataEmissao}</td>
            <th>Previsão Entrega</th><td>${frete.dataPrevisaoEntrega}</td></tr>
        <tr><th>Saída</th><td>${frete.dataSaida != null ? frete.dataSaida : '—'}</td>
            <th>Entrega</th><td>${frete.dataEntrega != null ? frete.dataEntrega : '—'}</td></tr>
    </table>

    <h3>Carga e Valores</h3>
    <table class="table">
        <tr><th>Descrição</th><td colspan="3">${frete.descricaoCarga}</td></tr>
        <tr><th>Peso (kg)</th><td>${frete.pesoKg}</td>
            <th>Volumes</th><td>${frete.volumes}</td></tr>
        <tr><th>Valor Frete</th><td>R$ ${frete.valorFrete}</td>
            <th>Alíquota ICMS</th><td>${frete.aliquotaIcms}%</td></tr>
        <tr><th>Valor ICMS</th><td>R$ ${frete.valorIcms}</td>
            <th>Valor Total</th><td><strong>R$ ${frete.valorTotal}</strong></td></tr>
    </table>

    <%-- ── OCORRÊNCIAS ── --%>
    <h3>Histórico de Ocorrências</h3>

    <c:choose>
        <c:when test="${not empty frete.ocorrencias}">
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
                    <c:forEach var="oc" items="${frete.ocorrencias}">
                        <tr>
                            <td>${oc.dataHora}</td>
                            <td>${oc.tipo}</td>
                            <td>${oc.municipio}/${oc.uf}</td>
                            <td>${not empty oc.descricao ? oc.descricao : '—'}</td>
                            <td>${not empty oc.nomeRecebedor ? oc.nomeRecebedor : '—'}</td>
                            <td>${not empty oc.documentoRecebedor ? oc.documentoRecebedor : '—'}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <p class="mensagem-vazia">Nenhuma ocorrência registrada.</p>
        </c:otherwise>
    </c:choose>

    <%-- ── FORM NOVA OCORRÊNCIA — só aparece se o status permitir ── --%>
    <c:if test="${frete.status != 'ENTREGUE' && frete.status != 'NAO_ENTREGUE' && frete.status != 'CANCELADO'}">

        <h3>Registrar Ocorrência</h3>

        <form action="${pageContext.request.contextPath}/ocorrencias" method="post">
            <input type="hidden" name="idFrete" value="${frete.id}" />

            <label for="tipo">Tipo:</label>
            <select id="tipo" name="tipo" class="form-control" onchange="mostrarCampos(this.value)">
                <option value="">Selecione</option>
                <option value="SAIDA_DO_PATIO">Saída do Pátio</option>
                <option value="EM_ROTA">Em Rota</option>
                <option value="TENTATIVA_ENTREGA">Tentativa de Entrega</option>
                <option value="ENTREGA_REALIZADA">Entrega Realizada</option>
                <option value="AVARIA">Avaria</option>
                <option value="EXTRAVIO">Extravio</option>
                <option value="OUTROS">Outros</option>
            </select>

            <label for="dataHora">Data e Hora:</label>
            <input type="datetime-local" id="dataHora" name="dataHora" class="form-control" />

            <label for="municipio">Município:</label>
            <input type="text" id="municipio" name="municipio" class="form-control" />

            <label for="uf">UF:</label>
            <input type="text" id="uf" name="uf" class="form-control" maxlength="2" style="width:80px" />

            <div id="campoDescricao">
                <label for="descricao">Descrição:</label>
                <input type="text" id="descricao" name="descricao" class="form-control" />
            </div>

            <div id="campoRecebedor" style="display:none;">
                <label for="nomeRecebedor">Nome do Recebedor:</label>
                <input type="text" id="nomeRecebedor" name="nomeRecebedor" class="form-control" />

                <label for="documentoRecebedor">Documento do Recebedor:</label>
                <input type="text" id="documentoRecebedor" name="documentoRecebedor" class="form-control" />
            </div>

            <div class="acoes-form" style="margin-top: 16px;">
                <button type="submit" class="btn btn-primary">Registrar Ocorrência</button>
            </div>
        </form>

        <script>
            function mostrarCampos(tipo) {
                var campoDesc     = document.getElementById('campoDescricao');
                var campoReceb    = document.getElementById('campoRecebedor');
                var tiposComDesc  = ['AVARIA', 'EXTRAVIO', 'OUTROS'];
                campoReceb.style.display = (tipo === 'ENTREGA_REALIZADA') ? 'block' : 'none';
                campoDesc.style.display  = (tiposComDesc.indexOf(tipo) >= 0 || tipo === 'ENTREGA_REALIZADA') ? 'block' : 'none';
            }
        </script>

    </c:if>

</div>

</body>
</html>