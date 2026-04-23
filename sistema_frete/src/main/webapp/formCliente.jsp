<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Cadastro de Cliente</title>
    <style>
        body { font-family: Arial; background: #f5f5f5; }
        .container {
            width: 600px;
            margin: 40px auto;
            background: white;
            padding: 20px;
            border-radius: 6px;
        }
        input, select {
            width: 100%;
            margin-bottom: 10px;
            padding: 6px;
        }
        button {
            padding: 10px;
            background: #0d6efd;
            color: white;
            border: none;
            cursor: pointer;
        }
        .erro {
            background: #f8d7da;
            color: #842029;
            padding: 10px;
            margin-bottom: 15px;
        }
    </style>
</head>
<body>

<div class="container">

    <h2>Cadastro de Cliente</h2>

    <!-- ERRO -->
    <c:if test="${not empty erro}">
        <div class="erro">
            ${erro}
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/clientes" method="post">

        <input type="hidden" name="id" value="${cliente.id}" />

        <label>Razão Social:</label>
        <input type="text" name="razaoSocial" value="${cliente.razaoSocial}" required />

        <label>Nome Fantasia:</label>
        <input type="text" name="nomeFantasia" value="${cliente.nomeFantasia}" />

        <label>CNPJ:</label>
        <input type="text" name="cnpj" value="${cliente.cnpj}" required />

        <label>Inscrição Estadual:</label>
        <input type="text" name="inscricaoEstadual" value="${cliente.inscricaoEstadual}" />

        <label>Tipo:</label>
        <select name="tipo">
            <option value="">Selecione</option>
            <option value="REMETENTE" ${cliente.tipo == 'REMETENTE' ? 'selected' : ''}>Remetente</option>
            <option value="DESTINATARIO" ${cliente.tipo == 'DESTINATARIO' ? 'selected' : ''}>Destinatário</option>
            <option value="AMBOS" ${cliente.tipo == 'AMBOS' ? 'selected' : ''}>Ambos</option>
        </select>

        <label>Logradouro:</label>
        <input type="text" name="logradouro" value="${cliente.logradouro}" />

        <label>Número:</label>
        <input type="text" name="numero" value="${cliente.numero}" />

        <label>Bairro:</label>
        <input type="text" name="bairro" value="${cliente.bairro}" />

        <label>Município:</label>
        <input type="text" name="municipio" value="${cliente.municipio}" />

        <label>UF:</label>
        <input type="text" name="uf" value="${cliente.uf}" />

        <label>CEP:</label>
        <input type="text" name="cep" value="${cliente.cep}" />

        <label>Telefone:</label>
        <input type="text" name="telefone" value="${cliente.telefone}" />

        <label>Email:</label>
        <input type="email" name="email" value="${cliente.email}" />

        <label>Status:</label>
        <select name="status">
            <option value="ATIVO" ${cliente.status == 'ATIVO' ? 'selected' : ''}>Ativo</option>
            <option value="INATIVO" ${cliente.status == 'INATIVO' ? 'selected' : ''}>Inativo</option>
        </select>

        <button type="submit">Salvar</button>
    </form>

    <br>

    <a href="${pageContext.request.contextPath}/clientes">← Voltar para listagem</a>

</div>

</body>
</html>