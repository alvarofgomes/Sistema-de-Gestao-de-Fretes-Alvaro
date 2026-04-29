<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>${cliente.id != null ? 'Editar' : 'Novo'} Cliente - Sistema de Fretes</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <script>
        function aplicarMascaraCNPJ(c) {
            let v = c.value.replace(/\D/g,"");
            v = v.replace(/^(\d{2})(\d)/,"$1.$2");
            v = v.replace(/^(\d{2})\.(\d{3})(\d)/,"$1.$2.$3");
            v = v.replace(/\.(\d{3})(\d)/,".$1/$2");
            v = v.replace(/(\d{4})(\d)/,"$1-$2");
            c.value = v;
        }
        function aplicarMascaraCEP(c) {
            let v = c.value.replace(/\D/g,"");
            v = v.replace(/^(\d{5})(\d)/,"$1-$2");
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

<c:set var="paginaAtualMenu" value="clientes" scope="request"/>
<jsp:include page="/includes/header.jsp"/>

<div class="container container-form">

    <div class="page-header">
        <h1>${cliente.id != null ? 'Editar Cliente' : 'Novo Cliente'}</h1>
    </div>

    <c:if test="${not empty erro}">
        <div class="msg-erro">${erro}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/clientes" method="post">
        <input type="hidden" name="id"                        value="${cliente.id}" />
        <input type="hidden" name="filtroRetorno"             value="${param.filtro != null ? param.filtro : filtro}" />
        <input type="hidden" name="paginaRetorno"             value="${param.pagina != null ? param.pagina : paginaAtual}" />
        <input type="hidden" name="registrosPorPaginaRetorno" value="${param.registrosPorPagina != null ? param.registrosPorPagina : registrosPorPagina}" />

        <div class="form-section">
            <div class="form-section-title">Dados da Empresa</div>
            <div class="form-grid">
                <div class="form-group col-full">
                    <label>Razão Social *</label>
                    <input type="text" name="razaoSocial" value="${cliente.razaoSocial}" required />
                </div>
                <div class="form-group col-full">
                    <label>Nome Fantasia</label>
                    <input type="text" name="nomeFantasia" value="${cliente.nomeFantasia}" />
                </div>
                <div class="form-group">
                    <label>CNPJ *</label>
                    <input type="text" name="cnpj" value="${cliente.cnpj}"
                           onkeyup="aplicarMascaraCNPJ(this)" maxlength="18" required />
                </div>
                <div class="form-group">
                    <label>Inscrição Estadual</label>
                    <input type="text" name="inscricaoEstadual" value="${cliente.inscricaoEstadual}" />
                </div>
                <div class="form-group">
                    <label>Status</label>
                    <select name="status">
                        <option value="ATIVO"   ${cliente.status == 'ATIVO'   ? 'selected' : ''}>Ativo</option>
                        <option value="INATIVO" ${cliente.status == 'INATIVO' ? 'selected' : ''}>Inativo</option>
                    </select>
                </div>
            </div>
        </div>

        <div class="form-section">
            <div class="form-section-title">Endereço</div>
            <div class="form-grid">
                <div class="form-group col-full">
                    <label>Logradouro</label>
                    <input type="text" name="logradouro" value="${cliente.logradouro}" />
                </div>
                <div class="form-group">
                    <label>Número</label>
                    <input type="text" name="numero" value="${cliente.numero}" />
                </div>
                <div class="form-group">
                    <label>Complemento</label>
                    <input type="text" name="complemento" value="${cliente.complemento}" />
                </div>
                <div class="form-group">
                    <label>Bairro</label>
                    <input type="text" name="bairro" value="${cliente.bairro}" />
                </div>
                <div class="form-group">
                    <label>Município</label>
                    <input type="text" name="municipio" value="${cliente.municipio}" />
                </div>
                <div class="form-group">
                    <label>UF</label>
                    <input type="text" name="uf" value="${cliente.uf}" maxlength="2" />
                </div>
                <div class="form-group">
                    <label>CEP</label>
                    <input type="text" name="cep" value="${cliente.cep}"
                           onkeyup="aplicarMascaraCEP(this)" maxlength="9" />
                </div>
            </div>
        </div>

        <%-- SEÇÃO 3: Contato --%>
        <div class="form-section">
            <div class="form-section-title">Contato</div>
            <div class="form-grid">
                <div class="form-group">
                    <label>Telefone</label>
                    <input type="text" name="telefone" value="${cliente.telefone}"
                           onkeyup="aplicarMascaraTelefone(this)" maxlength="15" />
                </div>
                <div class="form-group">
                    <label>E-mail</label>
                    <input type="email" name="email" value="${cliente.email}" />
                </div>
            </div>
        </div>

        <div class="acoes-form">
            <button type="submit" class="btn btn-primary">Salvar</button>
            <a class="btn btn-secondary"
               href="${pageContext.request.contextPath}/clientes?filtro=${param.filtro != null ? param.filtro : filtro}&pagina=${param.pagina != null ? param.pagina : paginaAtual}&registrosPorPagina=${param.registrosPorPagina != null ? param.registrosPorPagina : registrosPorPagina}">
                Cancelar
            </a>
        </div>
    </form>
</div>

</body>
</html>