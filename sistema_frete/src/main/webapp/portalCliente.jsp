<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Portal do Cliente</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <style>
        body {
            background:
                linear-gradient(rgba(6,20,50,.15), rgba(6,20,50,.30)),
                url('${pageContext.request.contextPath}/assets/images/OptimusPrimeNoPrime.png');
            background-size:cover; background-position:center;
            background-repeat:no-repeat; background-attachment:fixed;
        }
    </style>
</head>
<body>

<header class="app-header">
    <a class="brand" href="${pageContext.request.contextPath}/portal-cliente">
        Sistema de Gestão de Fretes
    </a>
    <nav class="app-nav">
        <a href="${pageContext.request.contextPath}/portal-cliente">Meus Fretes</a>
        <a href="${pageContext.request.contextPath}/solicitacoes-frete?acao=nova">Solicitar Frete</a>
        <a href="${pageContext.request.contextPath}/solicitacoes-frete">Minhas Solicitações</a>
        <a href="javascript:void(0)" onclick="document.getElementById('formRelatorio').style.display='block'">
            Relatório
        </a>
        <a href="${pageContext.request.contextPath}/logout" class="sair">Sair</a>
    </nav>
</header>

<div class="container" style="margin-top:28px;">

    <div class="page-header">
        <h1>Olá, ${sessionScope.usuarioLogado}!</h1>
    </div>

    <c:if test="${not empty sucesso}">
        <div class="msg-sucesso">${sucesso}</div>
    </c:if>

    <%-- KPIs --%>
    <div class="kpis" style="margin-bottom:28px;">
        <div class="kpi-card">
            <div class="kpi-label">Fretes em Aberto</div>
            <div class="kpi-valor">${kpiEmAberto}</div>
            <div class="kpi-sub">emitidos ou com saída confirmada</div>
        </div>
        <div class="kpi-card kpi-transito">
            <div class="kpi-label">Em Trânsito</div>
            <div class="kpi-valor">${kpiEmTransito}</div>
            <div class="kpi-sub">em rota agora</div>
        </div>
        <div class="kpi-card kpi-disponivel">
            <div class="kpi-label">Entregues</div>
            <div class="kpi-valor">${kpiEntregues}</div>
            <div class="kpi-sub">concluídos com sucesso</div>
        </div>
        <div class="kpi-card kpi-atraso">
            <div class="kpi-label">Em Atraso</div>
            <div class="kpi-valor">${kpiEmAtraso}</div>
            <div class="kpi-sub">prazo vencido</div>
        </div>
        <div class="kpi-card" style="border-left-color:var(--accent);">
            <div class="kpi-label">Solicitações Pendentes</div>
            <div class="kpi-valor" style="color:var(--accent);">${kpiSolicitacoesPendentes}</div>
            <div class="kpi-sub">aguardando análise</div>
        </div>
    </div>

    <%-- Filtro de fretes --%>
    <form action="${pageContext.request.contextPath}/portal-cliente"
          method="get" class="filtro-form">
        <label for="filtro">Número do Frete:</label>
        <input type="text" id="filtro" name="filtro" value="${filtro}"
               placeholder="Buscar..." />
        <label for="statusFiltro">Status:</label>
        <select id="statusFiltro" name="statusFiltro">
            <option value="">Todos</option>
            <option value="EMITIDO"          ${statusFiltro=='EMITIDO'          ?'selected':''}>Emitido</option>
            <option value="SAIDA_CONFIRMADA" ${statusFiltro=='SAIDA_CONFIRMADA' ?'selected':''}>Saída Confirmada</option>
            <option value="EM_TRANSITO"      ${statusFiltro=='EM_TRANSITO'      ?'selected':''}>Em Trânsito</option>
            <option value="ENTREGUE"         ${statusFiltro=='ENTREGUE'         ?'selected':''}>Entregue</option>
            <option value="NAO_ENTREGUE"     ${statusFiltro=='NAO_ENTREGUE'     ?'selected':''}>Não Entregue</option>
            <option value="CANCELADO"        ${statusFiltro=='CANCELADO'        ?'selected':''}>Cancelado</option>
        </select>
        <input type="hidden" name="pagina" value="1" />
        <button type="submit" class="btn btn-primary">Buscar</button>
    </form>

	<div class="table-container">
	    <table class="table table-fretes">
	        <thead>
	            <tr>
	                <th>Número</th>
	                <th>Remetente</th>
	                <th>Destinatário</th>
	                <th>Destino</th>
	                <th>Previsão</th>
	                <th>Status</th>
	                <th>Ações</th>
	            </tr>
	        </thead>
	        <tbody>
	            <c:choose>
	                <c:when test="${not empty listaFretes}">
	                    <c:forEach var="frete" items="${listaFretes}">
	                        <tr>
	                            <td><strong>${frete.numero}</strong></td>
	                            <td>${frete.remetente.razaoSocial}</td>
	                            <td>${frete.destinatario.razaoSocial}</td>
	                            <td>${frete.cidadeDestino}/${frete.ufDestino}</td>
	                            <td>${frete.dataPrevisaoEntrega}</td>
	                            <td>
	                                <c:choose>
	                                    <c:when test="${frete.status == 'EMITIDO'}"><span class="badge status-emitido">EMITIDO</span></c:when>
	                                    <c:when test="${frete.status == 'SAIDA_CONFIRMADA'}"><span class="badge status-saida">SAÍDA CONF.</span></c:when>
	                                    <c:when test="${frete.status == 'EM_TRANSITO'}"><span class="badge status-transito">EM TRÂNSITO</span></c:when>
	                                    <c:when test="${frete.status == 'ENTREGUE'}"><span class="badge status-entregue">ENTREGUE</span></c:when>
	                                    <c:when test="${frete.status == 'NAO_ENTREGUE'}"><span class="badge status-nao-entregue">NÃO ENTREGUE</span></c:when>
	                                    <c:when test="${frete.status == 'CANCELADO'}"><span class="badge status-cancelado">CANCELADO</span></c:when>
	                                </c:choose>
	                            </td>
	                            <td class="acoes">
	                                <a class="link-editar"
	                                   href="${pageContext.request.contextPath}/portal-cliente/frete?id=${frete.id}">
	                                    Detalhes
	                                </a>
	                            </td>
	                        </tr>
	                    </c:forEach>
	                </c:when>
	                <c:otherwise>
	                    <tr><td colspan="7" class="mensagem-vazia">Nenhum frete encontrado.</td></tr>
	                </c:otherwise>
	            </c:choose>
	        </tbody>
	    </table>
	</div>

    <div class="paginacao">
        <c:if test="${paginaAtual > 1}">
            <a href="${pageContext.request.contextPath}/portal-cliente?filtro=${filtro}&statusFiltro=${statusFiltro}&pagina=${paginaAtual - 1}">← Anterior</a>
        </c:if>
        <span>Página ${paginaAtual} de ${totalPaginas}</span>
        <c:if test="${paginaAtual < totalPaginas}">
            <a href="${pageContext.request.contextPath}/portal-cliente?filtro=${filtro}&statusFiltro=${statusFiltro}&pagina=${paginaAtual + 1}">Próximo →</a>
        </c:if>
    </div>

</div>

<%-- Modal de relatório --%>
<div id="formRelatorio" style="display:none; position:fixed; top:0; left:0;
     width:100%; height:100%; background:rgba(0,0,0,.45); z-index:999;">
    <div style="background:#fff; border-radius:10px; padding:28px 32px;
                width:440px; max-width:96vw; margin:140px auto;
                box-shadow:0 8px 32px rgba(0,0,0,.25);">
        <h3 style="color:var(--primary); margin-bottom:20px;">Gerar Relatório de Fretes</h3>
        <form action="${pageContext.request.contextPath}/portal-cliente/relatorio"
              method="get" target="_blank">
            <div class="form-group" style="margin-bottom:14px;">
                <label style="font-size:12px;font-weight:700;color:var(--text-muted);">DATA INICIAL *</label>
                <input type="date" name="dataInicio" style="width:100%;padding:9px 12px;
                       border:1px solid var(--border);border-radius:4px;" required />
            </div>
            <div class="form-group" style="margin-bottom:20px;">
                <label style="font-size:12px;font-weight:700;color:var(--text-muted);">DATA FINAL *</label>
                <input type="date" name="dataFim" style="width:100%;padding:9px 12px;
                       border:1px solid var(--border);border-radius:4px;" required />
            </div>
            <div style="display:flex;gap:10px;">
                <button type="submit" class="btn btn-primary">Gerar PDF</button>
                <button type="button" class="btn btn-secondary"
                        onclick="document.getElementById('formRelatorio').style.display='none'">
                    Cancelar
                </button>
            </div>
        </form>
    </div>
</div>

</body>
</html>