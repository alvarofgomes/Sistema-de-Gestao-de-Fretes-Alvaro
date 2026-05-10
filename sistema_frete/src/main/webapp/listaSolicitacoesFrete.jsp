<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Solicitações de Frete</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <style>
        .modal-overlay {
            display:none; position:fixed; top:0; left:0;
            width:100%; height:100%; background:rgba(0,0,0,.45); z-index:999;
        }
        .modal-box {
            background:#fff; border-radius:10px; padding:28px 32px;
            width:460px; max-width:96vw; margin:140px auto;
            box-shadow:0 8px 32px rgba(0,0,0,.25);
        }
        .modal-box h3 { color:var(--primary); margin-bottom:16px; }
    </style>
</head>
<body>

<c:choose>
    <c:when test="${perfilAtual == 'CLIENTE'}">
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
    </c:when>
    <c:otherwise>
        <c:set var="paginaAtualMenu" value="solicitacoes" scope="request"/>
        <jsp:include page="/includes/header.jsp"/>
    </c:otherwise>
</c:choose>

<div class="container">

    <div class="page-header">
        <h1>
            <c:choose>
                <c:when test="${perfilAtual == 'CLIENTE'}">Minhas Solicitações</c:when>
                <c:otherwise>Solicitações de Frete</c:otherwise>
            </c:choose>
        </h1>
    </div>

    <div class="topo-botoes">
        <c:choose>
            <c:when test="${perfilAtual == 'CLIENTE'}">
                <a href="${pageContext.request.contextPath}/portal-cliente"
                   class="btn btn-secondary">Voltar</a>
                <a href="${pageContext.request.contextPath}/solicitacoes-frete?acao=nova"
                   class="btn btn-success">Nova Solicitação</a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/home"
                   class="btn btn-secondary">Voltar</a>
            </c:otherwise>
        </c:choose>
    </div>

    <c:if test="${not empty sucesso}">
        <div class="msg-sucesso">${sucesso}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/solicitacoes-frete"
          method="get" class="filtro-form">
        <label for="filtro">Buscar:</label>
        <input type="text" id="filtro" name="filtro" value="${filtro}"
               placeholder="Destino ou carga..." />
        <label for="statusFiltro">Status:</label>
        <select id="statusFiltro" name="statusFiltro">
            <option value="">Todos</option>
            <option value="PENDENTE"  ${statusFiltro=='PENDENTE'  ?'selected':''}>Pendente</option>
            <option value="APROVADA"  ${statusFiltro=='APROVADA'  ?'selected':''}>Aprovada</option>
            <option value="RECUSADA"  ${statusFiltro=='RECUSADA'  ?'selected':''}>Recusada</option>
            <option value="CANCELADA" ${statusFiltro=='CANCELADA' ?'selected':''}>Cancelada</option>
        </select>
        <input type="hidden" name="pagina" value="1" />
        <button type="submit" class="btn btn-primary">Buscar</button>
    </form>

    <div class="table-container">
        <table class="table">
            <thead>
                <tr>
                    <c:if test="${perfilAtual != 'CLIENTE'}">
                        <th>Cliente</th>
                    </c:if>
                    <th>Origem</th>
                    <th>Destino</th>
                    <th>Carga</th>
                    <th>Peso (kg)</th>
                    <th>Volumes</th>
                    <th>Data</th>
                    <th>Status</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty listaSolicitacoes}">
                        <c:forEach var="s" items="${listaSolicitacoes}">
                            <tr>
                                <c:if test="${perfilAtual != 'CLIENTE'}">
                                    <td>${s.cliente.razaoSocial}</td>
                                </c:if>
                                <td>${s.cidadeOrigem}/${s.ufOrigem}</td>
                                <td>${s.cidadeDestino}/${s.ufDestino}</td>
                                <td>${s.descricaoCarga}</td>
                                <td>${s.pesoKg}</td>
                                <td>${s.volumes}</td>
                                <td>${s.dataSolicitacao}</td>
                                <td>
									<c:choose>
									    <c:when test="${s.status == 'PENDENTE'}">
									        <span class="badge status-saida">PENDENTE</span>
									    </c:when>
									    <c:when test="${s.status == 'APROVADA'}">
									        <span class="badge status-emitido">APROVADA</span>
									    </c:when>
									    <c:when test="${s.status == 'CONVERTIDA'}">
									        <span class="badge status-entregue">CONVERTIDA</span>
									    </c:when>
									    <c:when test="${s.status == 'RECUSADA'}">
									        <span class="badge status-nao-entregue">RECUSADA</span>
									    </c:when>
									    <c:otherwise>
									        <span class="badge status-cancelado">CANCELADA</span>
									    </c:otherwise>
									</c:choose>
                                </td>
                                <td class="acoes">
                                    <%-- CLIENTE: cancelar se PENDENTE --%>
                                    <c:if test="${perfilAtual == 'CLIENTE' && s.status == 'PENDENTE'}">
                                        <a class="link-inativar"
                                           href="${pageContext.request.contextPath}/solicitacoes-frete?acao=cancelar&id=${s.id}"
                                           onclick="return confirm('Cancelar esta solicitação?');">
                                            Cancelar
                                        </a>
                                    </c:if>

                                    <%-- ADMIN: aprovar/recusar se PENDENTE --%>
									<c:if test="${perfilAtual == 'ADMIN' && s.status == 'PENDENTE'}">
									    <a class="link-editar"
									       href="${pageContext.request.contextPath}/solicitacoes-frete?acao=aprovar&id=${s.id}"
									       onclick="return confirm('Aprovar esta solicitação?');">
									        Aprovar
									    </a>
									
									    <a class="link-inativar"
									       href="javascript:void(0)"
									       onclick="abrirModalRecusa(${s.id})">
									        Recusar
									    </a>
									</c:if>
									
									<c:if test="${perfilAtual != 'CLIENTE' && s.status == 'APROVADA'}">
									    <a class="link-editar"
									       href="${pageContext.request.contextPath}/fretes?acao=novo&solicitacaoId=${s.id}">
									        Gerar Frete
									    </a>
									</c:if>

                                    <%-- mostra motivo se recusada --%>
                                    <c:if test="${s.status == 'RECUSADA' && not empty s.motivoRecusa}">
                                        <span title="${s.motivoRecusa}"
                                              style="cursor:help; color:var(--text-muted); font-size:11px;">
                                            motivo
                                        </span>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="9" class="mensagem-vazia">
                                Nenhuma solicitação encontrada.
                            </td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>

    <div class="paginacao">
        <c:if test="${paginaAtual > 1}">
            <a href="${pageContext.request.contextPath}/solicitacoes-frete?filtro=${filtro}&statusFiltro=${statusFiltro}&pagina=${paginaAtual - 1}">← Anterior</a>
        </c:if>
        <span>Página ${paginaAtual} de ${totalPaginas}</span>
        <c:if test="${paginaAtual < totalPaginas}">
            <a href="${pageContext.request.contextPath}/solicitacoes-frete?filtro=${filtro}&statusFiltro=${statusFiltro}&pagina=${paginaAtual + 1}">Próximo →</a>
        </c:if>
    </div>

</div>

<%-- Modal de recusa --%>
<div class="modal-overlay" id="modalRecusa">
    <div class="modal-box">
        <h3>Recusar Solicitação</h3>
        <form action="${pageContext.request.contextPath}/solicitacoes-frete" method="get">
            <input type="hidden" name="acao" value="recusar" />
            <input type="hidden" name="id"   id="modalIdSolicitacao" value="" />
            <div class="form-group" style="margin-bottom:16px;">
                <label style="font-size:12px;font-weight:700;color:var(--text-muted);">
                    MOTIVO DA RECUSA *
                </label>
                <input type="text" name="motivo" style="width:100%;padding:9px 12px;
                       border:1px solid var(--border);border-radius:4px;font-size:14px;"
                       placeholder="Informe o motivo" required />
            </div>
            <div style="display:flex;gap:10px;">
                <button type="submit" class="btn btn-danger">Confirmar Recusa</button>
                <button type="button" class="btn btn-secondary"
                        onclick="fecharModalRecusa()">Cancelar</button>
            </div>
        </form>
    </div>
</div>

<script>
function abrirModalRecusa(id) {
    document.getElementById('modalIdSolicitacao').value = id;
    document.getElementById('modalRecusa').style.display = 'block';
}
function fecharModalRecusa() {
    document.getElementById('modalRecusa').style.display = 'none';
}
</script>

</body>
</html>