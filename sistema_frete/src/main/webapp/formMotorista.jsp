<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Cadastro de Motorista</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f5f5f5;
        }

        .container {
            width: 600px;
            margin: 40px auto;
            background: #fff;
            padding: 20px;
            border-radius: 6px;
        }

        input, select {
            width: 100%;
            margin-bottom: 12px;
            padding: 8px;
            box-sizing: border-box;
        }

        button {
            padding: 10px;
            background: #0d6efd;
            color: white;
            border: none;
            cursor: pointer;
            width: 100%;
        }

        .erro {
            background: #f8d7da;
            color: #842029;
            padding: 10px;
            margin-bottom: 15px;
            border-radius: 4px;
        }

        .aviso {
            background: #fff3cd;
            color: #856404;
            padding: 10px;
            margin-bottom: 15px;
            border-radius: 4px;
        }

        a {
            display: inline-block;
            margin-top: 10px;
            text-decoration: none;
        }
    </style>
</head>
<body>

<div class="container">
    <h2>Cadastro de Motorista</h2>

    <c:if test="${not empty erro}">
        <div class="erro">
            ${erro}
        </div>
    </c:if>

    <c:if test="${not empty motorista.cnhValidade && motorista.cnhValidade lt now}">
        <div class="aviso">
            ⚠ CNH vencida. Este motorista não poderá ser utilizado para novos fretes.
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/motoristas" method="post">

        <input type="hidden" name="id" value="${motorista.id}" />

        <label>Nome:</label>
        <input type="text" name="nome" value="${motorista.nome}" required />

        <label>CPF:</label>
        <input type="text" name="cpf" value="${motorista.cpf}" required />

        <label>Data de Nascimento:</label>
        <input type="date" name="dataNascimento" value="${motorista.dataNascimento}" />

        <label>Telefone:</label>
        <input type="text" name="telefone" value="${motorista.telefone}" />

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

        <button type="submit">Salvar</button>
    </form>

    <a href="${pageContext.request.contextPath}/motoristas">← Voltar para listagem</a>
</div>

</body>
</html>