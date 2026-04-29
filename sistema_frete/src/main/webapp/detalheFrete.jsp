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

    <div class="topo-botoes">
        <a href="${pageContext.request.contextPath}/fretes" class="btn btn-secondary">Voltar</a>

        <c:if test="${frete.status == 'EMITIDO'}">
            <a href="${pageContext.request.contextPath}/fretes?acao=confirmarSaida&id=${frete.id}"
               class="btn btn-primary"
               onclick="return confirm('Confirmar saída do frete ${frete.numero}?');">
                Confirmar Saída
            </a>
            <a href="${pageContext.request.contextPath}/fretes?acao=cancelar&id=${frete.id}"
               class="btn btn-danger"
               onclick="return confirm('Cancelar este frete?');">
                Cancelar Frete
            </a>
        </c:if>
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
                <th>Origem</th><td>${frete.municipioOrigem}/${frete.ufOrigem}</td>
                <th>Destino</th><td>${frete.municipioDestino}/${frete.ufDestino}</td>
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
            <c:when test="${not empty frete.ocorrencias}">
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
                            <c:forEach var="oc" items="${frete.ocorrencias}">
                                <tr>
                                    <td>${oc.dataHora}</td>
                                    <td><span class="badge status-info">${oc.tipo}</span></td>
                                    <td>${oc.municipio}/${oc.uf}</td>
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

    <%--REGISTRAR OCORRÊNCIA--%>
    <c:if test="${frete.status != 'ENTREGUE' && frete.status != 'NAO_ENTREGUE' && frete.status != 'CANCELADO'}">
        <div class="form-section">
            <div class="form-section-title">Registrar Ocorrência</div>

            <form action="${pageContext.request.contextPath}/ocorrencias" method="post">
                <input type="hidden" name="idFrete" value="${frete.id}" />

                <div class="form-grid">
                    <div class="form-group">
                        <label>Tipo *</label>
                        <select name="tipo" onchange="mostrarCampos(this.value)">
                            <option value="">Selecione</option>
                            <option value="SAIDA_DO_PATIO">Saída do Pátio</option>
                            <option value="EM_ROTA">Em Rota</option>
                            <option value="TENTATIVA_ENTREGA">Tentativa de Entrega</option>
                            <option value="ENTREGA_REALIZADA">Entrega Realizada</option>
                            <option value="AVARIA">Avaria</option>
                            <option value="EXTRAVIO">Extravio</option>
                            <option value="OUTROS">Outros</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Data e Hora *</label>
                        <input type="datetime-local" name="dataHora" />
                    </div>
                    <div class="form-group">
                        <label>Município *</label>
                        <input type="text" name="municipio" />
                    </div>
                    <div class="form-group">
                        <label>UF *</label>
                        <input type="text" name="uf" maxlength="2" />
                    </div>

                    <div class="form-group col-full" id="campoDescricao" style="display:none;">
                        <label>Descrição</label>
                        <input type="text" name="descricao" />
                    </div>

                    <div class="form-group" id="campoNomeRecebedor" style="display:none;">
                        <label>Nome do Recebedor *</label>
                        <input type="text" name="nomeRecebedor" />
                    </div>
                    <div class="form-group" id="campoDocumentoRecebedor" style="display:none;">
                        <label>Documento do Recebedor *</label>
                        <input type="text" name="documentoRecebedor" />
                    </div>
                </div>

                <div class="acoes-form">
                    <button type="submit" class="btn btn-primary">Registrar Ocorrência</button>
                </div>
            </form>

            <script>
                function mostrarCampos(tipo) {
                    var tiposDesc = ['AVARIA','EXTRAVIO','OUTROS','ENTREGA_REALIZADA'];
                    document.getElementById('campoDescricao').style.display =
                        tiposDesc.indexOf(tipo) >= 0 ? 'block' : 'none';
                    var isEntrega = tipo === 'ENTREGA_REALIZADA';
                    document.getElementById('campoNomeRecebedor').style.display =
                        isEntrega ? 'block' : 'none';
                    document.getElementById('campoDocumentoRecebedor').style.display =
                        isEntrega ? 'block' : 'none';
                }
            </script>
        </div>
    </c:if>

</div>
</body>
</html>