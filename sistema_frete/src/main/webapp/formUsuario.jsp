<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>${usuario.id != null ? 'Editar' : 'Novo'} Usuário - Sistema de Fretes</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <script>
        function toggleCampoCliente() {
            var perfil = document.getElementById('perfil').value;
            var div    = document.getElementById('divCliente');
            div.style.display = (perfil === 'CLIENTE') ? 'block' : 'none';
        }
        window.onload = function() { toggleCampoCliente(); };
    </script>
</head>
<body>

<c:set var="paginaAtualMenu" value="usuarios" scope="request"/>
<jsp:include page="/includes/header.jsp"/>

<div class="container container-form">
    <div class="page-header">
        <h1>${usuario.id != null ? 'Editar Usuário' : 'Novo Usuário'}</h1>
    </div>

    <div class="topo-botoes">
        <a href="${pageContext.request.contextPath}/usuarios" class="btn btn-secondary">Voltar</a>
    </div>

    <c:if test="${not empty erro}">
        <div class="msg-erro">${erro}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/usuarios" method="post">
        <input type="hidden" name="id" value="${usuario.id}" />

        <div class="form-section">
            <div class="form-section-title">Dados do Usuário</div>
            <div class="form-grid">
                <div class="form-group col-full">
                    <label>Nome Completo *</label>
                    <input type="text" name="nome" value="${usuario.nome}" required />
                </div>
                <div class="form-group">
                    <label>Login *</label>
                    <input type="text" name="login" value="${usuario.login}" required />
                </div>
                <div class="form-group">
                    <label>
                        ${usuario.id != null ? 'Nova Senha (deixe em branco para manter)' : 'Senha *'}
                    </label>
                    <input type="password" name="senha" autocomplete="new-password"
                           ${usuario.id == null ? 'required' : ''} />
                </div>
                <div class="form-group">
                    <label>Perfil *</label>
                    <select id="perfil" name="perfil" onchange="toggleCampoCliente()" required>
                        <option value="">Selecione</option>
                        <option value="ADMIN"    ${usuario.perfil == 'ADMIN'    ? 'selected' : ''}>Admin</option>
                        <option value="OPERADOR" ${usuario.perfil == 'OPERADOR' ? 'selected' : ''}>Operador</option>
                        <option value="CLIENTE"  ${usuario.perfil == 'CLIENTE'  ? 'selected' : ''}>Cliente</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Status *</label>
                    <select name="status" required>
                        <option value="ATIVO"   ${usuario.status == 'ATIVO'   ? 'selected' : ''}>Ativo</option>
                        <option value="INATIVO" ${usuario.status == 'INATIVO' ? 'selected' : ''}>Inativo</option>
                    </select>
                </div>
            </div>
        </div>

        <div class="form-section" id="divCliente" style="display:none;">
            <div class="form-section-title">Vínculo com Cliente</div>
            <div class="form-grid">
                <div class="form-group col-full">
                    <label>Cliente *</label>
                    <select name="clienteId">
                        <option value="">Selecione o cliente</option>
                        <c:forEach var="c" items="${clientes}">
                            <option value="${c.id}"
                                ${usuario.clienteId == c.id ? 'selected' : ''}>
                                ${c.razaoSocial}
                            </option>
                        </c:forEach>
                    </select>
                </div>
            </div>
        </div>

        <div class="acoes-form">
            <button type="submit" class="btn btn-primary">Salvar</button>
            <a href="${pageContext.request.contextPath}/usuarios" class="btn btn-secondary">Cancelar</a>
        </div>
    </form>
</div>
</body>
</html>