<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>${motorista.id != null ? 'Editar' : 'Novo'} Motorista - Sistema de Fretes</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <script>
        function aplicarMascaraCPF(c) {
            let v = c.value.replace(/\D/g,"");
            v = v.replace(/(\d{3})(\d)/,"$1.$2");
            v = v.replace(/(\d{3})(\d)/,"$1.$2");
            v = v.replace(/(\d{3})(\d{1,2})$/,"$1-$2");
            c.value = v;
        }
        function aplicarMascaraTelefone(c) {
            let v = c.value.replace(/\D/g,"");
            v = v.length <= 10
                ? v.replace(/^(\d{2})(\d)/,"($1) $2").replace(/(\d{4})(\d)/,"$1-$2")
                : v.replace(/^(\d{2})(\d)/,"($1) $2").replace(/(\d{5})(\d)/,"$1-$2");
            c.value = v;
        }
    </script>
</head>
<body>

<c:set var="paginaAtualMenu" value="motoristas" scope="request"/>
<jsp:include page="/includes/header.jsp"/>

<div class="container container-form">

    <div class="page-header">
        <h1>${motorista.id != null ? 'Editar Motorista' : 'Novo Motorista'}</h1>
    </div>

    <c:if test="${not empty erro}">
        <div class="msg-erro">${erro}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/motoristas" method="post">
        <input type="hidden" name="id"                        value="${motorista.id}" />
        <input type="hidden" name="filtroRetorno"             value="${param.filtro != null ? param.filtro : filtro}" />
        <input type="hidden" name="paginaRetorno"             value="${param.pagina != null ? param.pagina : paginaAtual}" />
        <input type="hidden" name="registrosPorPaginaRetorno" value="${param.registrosPorPagina != null ? param.registrosPorPagina : registrosPorPagina}" />

        <div class="form-section">
            <div class="form-section-title">Dados Pessoais</div>
            <div class="form-grid">
                <div class="form-group col-full">
                    <label>Nome Completo *</label>
                    <input type="text" name="nome" value="${motorista.nome}" required />
                </div>
                <div class="form-group">
                    <label>CPF *</label>
                    <input type="text" name="cpf" value="${motorista.cpf}"
                           onkeyup="aplicarMascaraCPF(this)" maxlength="14" required />
                </div>
                <div class="form-group">
                    <label>Data de Nascimento</label>
                    <input type="date" name="dataNascimento" value="${motorista.dataNascimento}" />
                </div>
                <div class="form-group">
                    <label>Telefone</label>
                    <input type="text" name="telefone" value="${motorista.telefone}"
                           onkeyup="aplicarMascaraTelefone(this)" maxlength="15" />
                </div>
                <div class="form-group">
                    <label>Tipo de Vínculo</label>
                    <select name="tipoVinculo">
                        <option value="">Selecione</option>
                        <option value="FUNCIONARIO" ${motorista.tipoVinculo == 'FUNCIONARIO' ? 'selected' : ''}>Funcionário</option>
                        <option value="AGREGADO"    ${motorista.tipoVinculo == 'AGREGADO'    ? 'selected' : ''}>Agregado</option>
                        <option value="TERCEIRO"    ${motorista.tipoVinculo == 'TERCEIRO'    ? 'selected' : ''}>Terceiro</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Status</label>
                    <select name="status">
                        <option value="ATIVO"    ${motorista.status == 'ATIVO'    ? 'selected' : ''}>Ativo</option>
                        <option value="INATIVO"  ${motorista.status == 'INATIVO'  ? 'selected' : ''}>Inativo</option>
                        <option value="SUSPENSO" ${motorista.status == 'SUSPENSO' ? 'selected' : ''}>Suspenso</option>
                    </select>
                </div>
            </div>
        </div>

        <div class="form-section">
            <div class="form-section-title">Carteira Nacional de Habilitação (CNH)</div>
            <div class="form-grid cols-3">
                <div class="form-group">
                    <label>Número da CNH</label>
                    <input type="text" name="cnhNumero" value="${motorista.cnhNumero}" />
                </div>
                <div class="form-group">
                    <label>Categoria</label>
                    <select name="cnhCategoria">
                        <option value="">Selecione</option>
                        <option value="A" ${motorista.cnhCategoria == 'A' ? 'selected' : ''}>A</option>
                        <option value="B" ${motorista.cnhCategoria == 'B' ? 'selected' : ''}>B</option>
                        <option value="C" ${motorista.cnhCategoria == 'C' ? 'selected' : ''}>C</option>
                        <option value="D" ${motorista.cnhCategoria == 'D' ? 'selected' : ''}>D</option>
                        <option value="E" ${motorista.cnhCategoria == 'E' ? 'selected' : ''}>E</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Validade da CNH</label>
                    <input type="date" name="cnhValidade" value="${motorista.cnhValidade}" />
                </div>
            </div>
        </div>

        <div class="acoes-form">
            <button type="submit" class="btn btn-primary">Salvar</button>
            <a class="btn btn-secondary"
               href="${pageContext.request.contextPath}/motoristas?filtro=${param.filtro != null ? param.filtro : filtro}&pagina=${param.pagina != null ? param.pagina : paginaAtual}&registrosPorPagina=${param.registrosPorPagina != null ? param.registrosPorPagina : registrosPorPagina}">
                Cancelar
            </a>
        </div>
    </form>
</div>

</body>
</html>