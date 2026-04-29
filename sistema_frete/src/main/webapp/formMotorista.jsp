<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Cadastro de Motorista</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <script>
        function aplicarMascaraCPF(campo) {
            let v = campo.value.replace(/\D/g, "");
            v = v.replace(/(\d{3})(\d)/, "$1.$2");
            v = v.replace(/(\d{3})(\d)/, "$1.$2");
            v = v.replace(/(\d{3})(\d{1,2})$/, "$1-$2");
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
    <h2>${motorista.id != null ? 'Editar Motorista' : 'Cadastro de Motorista'}</h2>

    <c:if test="${not empty erro}">
        <div class="erro">
            ${erro}
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/motoristas" method="post" class="form-cadastro">

        <input type="hidden" name="id" value="${motorista.id}" />
        <input type="hidden" name="filtroRetorno" value="${param.filtro != null ? param.filtro : filtro}" />
        <input type="hidden" name="paginaRetorno" value="${param.pagina != null ? param.pagina : paginaAtual}" />
        <input type="hidden" name="registrosPorPaginaRetorno" value="${param.registrosPorPagina != null ? param.registrosPorPagina : registrosPorPagina}" />

        <label>Nome:</label>
        <input type="text" name="nome" value="${motorista.nome}" required />

        <label>CPF:</label>
        <input type="text" name="cpf" value="${motorista.cpf}" onkeyup="aplicarMascaraCPF(this)" maxlength="14" required />

        <label>Data de Nascimento:</label>
        <input type="date" name="dataNascimento" value="${motorista.dataNascimento}" />

        <label>Telefone:</label>
        <input type="text" name="telefone" value="${motorista.telefone}" onkeyup="aplicarMascaraTelefone(this)" maxlength="15" />

        <label>Número da CNH:</label>
        <input type="text" name="cnhNumero" value="${motorista.cnhNumero}" />

        <label>Categoria CNH:</label>
        <select name="cnhCategoria">
            <option value="">Selecione</option>
            <option value="A" ${motorista.cnhCategoria == 'A' ? 'selected' : ''}>A</option>
            <option value="B" ${motorista.cnhCategoria == 'B' ? 'selected' : ''}>B</option>
            <option value="C" ${motorista.cnhCategoria == 'C' ? 'selected' : ''}>C</option>
            <option value="D" ${motorista.cnhCategoria == 'D' ? 'selected' : ''}>D</option>
            <option value="E" ${motorista.cnhCategoria == 'E' ? 'selected' : ''}>E</option>
        </select>

        <label>Validade da CNH:</label>
        <input type="date" name="cnhValidade" value="${motorista.cnhValidade}" />

        <label>Tipo de Vínculo:</label>
        <select name="tipoVinculo">
            <option value="">Selecione</option>
            <option value="FUNCIONARIO" ${motorista.tipoVinculo == 'FUNCIONARIO' ? 'selected' : ''}>Funcionário</option>
            <option value="AGREGADO" ${motorista.tipoVinculo == 'AGREGADO' ? 'selected' : ''}>Agregado</option>
            <option value="TERCEIRO" ${motorista.tipoVinculo == 'TERCEIRO' ? 'selected' : ''}>Terceiro</option>
        </select>

        <label>Status:</label>
        <select name="status">
            <option value="ATIVO" ${motorista.status == 'ATIVO' ? 'selected' : ''}>Ativo</option>
            <option value="INATIVO" ${motorista.status == 'INATIVO' ? 'selected' : ''}>Inativo</option>
            <option value="SUSPENSO" ${motorista.status == 'SUSPENSO' ? 'selected' : ''}>Suspenso</option>
        </select>

        <div class="acoes-form">
            <button type="submit" class="btn btn-salvar">Salvar</button>

            <a class="btn btn-cancelar"
               href="${pageContext.request.contextPath}/motoristas?filtro=${param.filtro != null ? param.filtro : filtro}&pagina=${param.pagina != null ? param.pagina : paginaAtual}&registrosPorPagina=${param.registrosPorPagina != null ? param.registrosPorPagina : registrosPorPagina}">
                Cancelar
            </a>
        </div>
    </form>
</div>

</body>
</html>