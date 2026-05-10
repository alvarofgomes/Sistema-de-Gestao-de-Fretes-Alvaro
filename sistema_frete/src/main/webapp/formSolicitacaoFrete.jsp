<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Solicitar Frete</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>

<header class="app-header">
    <a class="brand" href="${pageContext.request.contextPath}/portal-cliente">
        Sistema de Gestão de Fretes
    </a>
    <nav class="app-nav">
        <a href="${pageContext.request.contextPath}/portal-cliente">Meus Fretes</a>
        <a href="${pageContext.request.contextPath}/solicitacoes-frete">Minhas Solicitações</a>
        <a href="${pageContext.request.contextPath}/logout" class="sair">Sair</a>
    </nav>
</header>

<div class="container container-form">

    <div class="page-header">
        <h1>Solicitar Frete</h1>
    </div>

    <div class="topo-botoes">
        <a href="${pageContext.request.contextPath}/portal-cliente" class="btn btn-secondary">Voltar</a>
    </div>

    <c:if test="${not empty erro}">
        <div class="msg-erro">${erro}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/solicitacoes-frete" method="post">

        <div class="form-section">
            <div class="form-section-title">Origem</div>
            <div class="form-grid">
                <div class="form-group">
                    <label>Cidade de Origem *</label>
                    <input type="text" name="cidadeOrigem"
                           value="${solicitacao.cidadeOrigem}" required />
                </div>
                <div class="form-group">
                    <label>UF de Origem *</label>
						<select name="ufOrigem" required>
						    <option value="">Selecione</option>
						    <option value="AC" ${solicitacao.ufOrigem == 'AC' ? 'selected' : ''}>AC</option>
						    <option value="AL" ${solicitacao.ufOrigem == 'AL' ? 'selected' : ''}>AL</option>
						    <option value="AP" ${solicitacao.ufOrigem == 'AP' ? 'selected' : ''}>AP</option>
						    <option value="AM" ${solicitacao.ufOrigem == 'AM' ? 'selected' : ''}>AM</option>
						    <option value="BA" ${solicitacao.ufOrigem == 'BA' ? 'selected' : ''}>BA</option>
						    <option value="CE" ${solicitacao.ufOrigem == 'CE' ? 'selected' : ''}>CE</option>
						    <option value="DF" ${solicitacao.ufOrigem == 'DF' ? 'selected' : ''}>DF</option>
						    <option value="ES" ${solicitacao.ufOrigem == 'ES' ? 'selected' : ''}>ES</option>
						    <option value="GO" ${solicitacao.ufOrigem == 'GO' ? 'selected' : ''}>GO</option>
						    <option value="MA" ${solicitacao.ufOrigem == 'MA' ? 'selected' : ''}>MA</option>
						    <option value="MT" ${solicitacao.ufOrigem == 'MT' ? 'selected' : ''}>MT</option>
						    <option value="MS" ${solicitacao.ufOrigem == 'MS' ? 'selected' : ''}>MS</option>
						    <option value="MG" ${solicitacao.ufOrigem == 'MG' ? 'selected' : ''}>MG</option>
						    <option value="PA" ${solicitacao.ufOrigem == 'PA' ? 'selected' : ''}>PA</option>
						    <option value="PB" ${solicitacao.ufOrigem == 'PB' ? 'selected' : ''}>PB</option>
						    <option value="PR" ${solicitacao.ufOrigem == 'PR' ? 'selected' : ''}>PR</option>
						    <option value="PE" ${solicitacao.ufOrigem == 'PE' ? 'selected' : ''}>PE</option>
						    <option value="PI" ${solicitacao.ufOrigem == 'PI' ? 'selected' : ''}>PI</option>
						    <option value="RJ" ${solicitacao.ufOrigem == 'RJ' ? 'selected' : ''}>RJ</option>
						    <option value="RN" ${solicitacao.ufOrigem == 'RN' ? 'selected' : ''}>RN</option>
						    <option value="RS" ${solicitacao.ufOrigem == 'RS' ? 'selected' : ''}>RS</option>
						    <option value="RO" ${solicitacao.ufOrigem == 'RO' ? 'selected' : ''}>RO</option>
						    <option value="RR" ${solicitacao.ufOrigem == 'RR' ? 'selected' : ''}>RR</option>
						    <option value="SC" ${solicitacao.ufOrigem == 'SC' ? 'selected' : ''}>SC</option>
						    <option value="SP" ${solicitacao.ufOrigem == 'SP' ? 'selected' : ''}>SP</option>
						    <option value="SE" ${solicitacao.ufOrigem == 'SE' ? 'selected' : ''}>SE</option>
						    <option value="TO" ${solicitacao.ufOrigem == 'TO' ? 'selected' : ''}>TO</option>
						</select>
                </div>
            </div>
        </div>

        <div class="form-section">
            <div class="form-section-title">Destino</div>
            <div class="form-grid">
                <div class="form-group">
                    <label>Cidade de Destino *</label>
                    <input type="text" name="cidadeDestino"
                           value="${solicitacao.cidadeDestino}" required />
                </div>
                <div class="form-group">
                    <label>UF de Destino *</label>
						<select name="ufDestino" required>
						    <option value="">Selecione</option>
						    <option value="AC" ${solicitacao.ufDestino == 'AC' ? 'selected' : ''}>AC</option>
						    <option value="AL" ${solicitacao.ufDestino == 'AL' ? 'selected' : ''}>AL</option>
						    <option value="AP" ${solicitacao.ufDestino == 'AP' ? 'selected' : ''}>AP</option>
						    <option value="AM" ${solicitacao.ufDestino == 'AM' ? 'selected' : ''}>AM</option>
						    <option value="BA" ${solicitacao.ufDestino == 'BA' ? 'selected' : ''}>BA</option>
						    <option value="CE" ${solicitacao.ufDestino == 'CE' ? 'selected' : ''}>CE</option>
						    <option value="DF" ${solicitacao.ufDestino == 'DF' ? 'selected' : ''}>DF</option>
						    <option value="ES" ${solicitacao.ufDestino == 'ES' ? 'selected' : ''}>ES</option>
						    <option value="GO" ${solicitacao.ufDestino == 'GO' ? 'selected' : ''}>GO</option>
						    <option value="MA" ${solicitacao.ufDestino == 'MA' ? 'selected' : ''}>MA</option>
						    <option value="MT" ${solicitacao.ufDestino == 'MT' ? 'selected' : ''}>MT</option>
						    <option value="MS" ${solicitacao.ufDestino == 'MS' ? 'selected' : ''}>MS</option>
						    <option value="MG" ${solicitacao.ufDestino == 'MG' ? 'selected' : ''}>MG</option>
						    <option value="PA" ${solicitacao.ufDestino == 'PA' ? 'selected' : ''}>PA</option>
						    <option value="PB" ${solicitacao.ufDestino == 'PB' ? 'selected' : ''}>PB</option>
						    <option value="PR" ${solicitacao.ufDestino == 'PR' ? 'selected' : ''}>PR</option>
						    <option value="PE" ${solicitacao.ufDestino == 'PE' ? 'selected' : ''}>PE</option>
						    <option value="PI" ${solicitacao.ufDestino == 'PI' ? 'selected' : ''}>PI</option>
						    <option value="RJ" ${solicitacao.ufDestino == 'RJ' ? 'selected' : ''}>RJ</option>
						    <option value="RN" ${solicitacao.ufDestino == 'RN' ? 'selected' : ''}>RN</option>
						    <option value="RS" ${solicitacao.ufDestino == 'RS' ? 'selected' : ''}>RS</option>
						    <option value="RO" ${solicitacao.ufDestino == 'RO' ? 'selected' : ''}>RO</option>
						    <option value="RR" ${solicitacao.ufDestino == 'RR' ? 'selected' : ''}>RR</option>
						    <option value="SC" ${solicitacao.ufDestino == 'SC' ? 'selected' : ''}>SC</option>
						    <option value="SP" ${solicitacao.ufDestino == 'SP' ? 'selected' : ''}>SP</option>
						    <option value="SE" ${solicitacao.ufDestino == 'SE' ? 'selected' : ''}>SE</option>
						    <option value="TO" ${solicitacao.ufDestino == 'TO' ? 'selected' : ''}>TO</option>
						</select>
                </div>
            </div>
        </div>

        <div class="form-section">
            <div class="form-section-title">Dados da Carga</div>
            <div class="form-grid">
                <div class="form-group col-full">
                    <label>Descrição da Carga *</label>
                    <input type="text" name="descricaoCarga"
                           value="${solicitacao.descricaoCarga}" required />
                </div>
                <div class="form-group">
                    <label>Peso (kg) *</label>
                    <input type="number" name="pesoKg" step="0.01" min="0.01"
                           value="${solicitacao.pesoKg}" required />
                </div>
                <div class="form-group">
                    <label>Volumes *</label>
                    <input type="number" name="volumes" min="1"
                           value="${solicitacao.volumes}" required />
                </div>
                <div class="form-group col-full">
                    <label>Observação</label>
                    <input type="text" name="observacao"
                           value="${solicitacao.observacao}" />
                </div>
            </div>
        </div>

        <div class="acoes-form">
            <button type="submit" class="btn btn-primary">Enviar Solicitação</button>
            <a href="${pageContext.request.contextPath}/portal-cliente"
               class="btn btn-secondary">Cancelar</a>
        </div>
    </form>
</div>
</body>
</html>