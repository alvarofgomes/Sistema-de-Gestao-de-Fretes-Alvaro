<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Cadastro de Cliente</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <script>
        function aplicarMascaraCNPJ(campo) {
            let v = campo.value.replace(/\D/g, "");
            v = v.replace(/^(\d{2})(\d)/, "$1.$2");
            v = v.replace(/^(\d{2})\.(\d{3})(\d)/, "$1.$2.$3");
            v = v.replace(/\.(\d{3})(\d)/, ".$1/$2");
            v = v.replace(/(\d{4})(\d)/, "$1-$2");
            campo.value = v;
        }

        function aplicarMascaraCEP(campo) {
            let v = campo.value.replace(/\D/g, "");
            v = v.replace(/^(\d{5})(\d)/, "$1-$2");
            campo.value = v;
        }

        function aplicarMascaraTelefone(campo) {
            let v = campo.value.replace(/\D/g, "");
            if (v.length <= 10) {
                v = v.replace(/^(\d{2})(\d)/g, "($1) $2");
                v = v.replace(/(\d{4})(\d)/, "$1-$2");
            } else {
                v = v.replace(/^(\d{2})(\d)/g, "($1) $2");
                v = v.replace(/(\d{5})(\d)/, "$1-$2");
            }
            campo.value = v;
        }
    </script>
</head>
<body>

<div class="container container-form">
    <h2>${cliente.id != null ? 'Editar Cliente' : 'Cadastro de Cliente'}</h2>

    <c:if test="${not empty erro}">
        <div class="erro">
            ${erro}
        </div>
    </c:if>

	<form action="${pageContext.request.contextPath}/clientes" method="post" class="form-cadastro">
        <input type="hidden" name="id" value="${cliente.id}" />
        <input type="hidden" name="filtroRetorno" value="${param.filtro != null ? param.filtro : filtro}" />
        <input type="hidden" name="paginaRetorno" value="${param.pagina != null ? param.pagina : paginaAtual}" />
        <input type="hidden" name="registrosPorPaginaRetorno" value="${param.registrosPorPagina != null ? param.registrosPorPagina : registrosPorPagina}" />

        <label>Razão Social:</label>
        <input type="text" name="razaoSocial" value="${cliente.razaoSocial}" required />

        <label>Nome Fantasia:</label>
        <input type="text" name="nomeFantasia" value="${cliente.nomeFantasia}" />

        <label>CNPJ:</label>
        <input type="text" name="cnpj" value="${cliente.cnpj}" onkeyup="aplicarMascaraCNPJ(this)" maxlength="18" required />

        <label>Inscrição Estadual:</label>
        <input type="text" name="inscricaoEstadual" value="${cliente.inscricaoEstadual}" />

        <label>Logradouro:</label>
        <input type="text" name="logradouro" value="${cliente.logradouro}" />

        <label>Número:</label>
        <input type="text" name="numero" value="${cliente.numero}" />

        <label>Complemento:</label>
        <input type="text" name="complemento" value="${cliente.complemento}" />

        <label>Bairro:</label>
        <input type="text" name="bairro" value="${cliente.bairro}" />

        <label>Município:</label>
        <input type="text" name="municipio" value="${cliente.municipio}" />

        <label>UF:</label>
        <input type="text" name="uf" value="${cliente.uf}" maxlength="2" />

        <label>CEP:</label>
        <input type="text" name="cep" value="${cliente.cep}" onkeyup="aplicarMascaraCEP(this)" maxlength="9" />

        <label>Telefone:</label>
        <input type="text" name="telefone" value="${cliente.telefone}" onkeyup="aplicarMascaraTelefone(this)" maxlength="15" />

        <label>Email:</label>
        <input type="email" name="email" value="${cliente.email}" />

        <label>Status:</label>
        <select name="status">
            <option value="ATIVO" ${cliente.status == 'ATIVO' ? 'selected' : ''}>Ativo</option>
            <option value="INATIVO" ${cliente.status == 'INATIVO' ? 'selected' : ''}>Inativo</option>
        </select>

        <div class="acoes-form">
            <button type="submit" class="btn btn-salvar">Salvar</button>
            <a class="btn btn-cancelar" 
               href="${pageContext.request.contextPath}/clientes?filtro=${param.filtro != null ? param.filtro : filtro}&pagina=${param.pagina != null ? param.pagina : paginaAtual}&registrosPorPagina=${param.registrosPorPagina != null ? param.registrosPorPagina : registrosPorPagina}">
                Cancelar
            </a>
        </div>
    </form>
</div>

</body>
</html>