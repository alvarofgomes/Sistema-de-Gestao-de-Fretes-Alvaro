<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="false" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Erro - Sistema de Gestão de Fretes</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #fff5f5;
            margin: 0;
            padding: 0;
        }
        .container {
            width: 520px;
            margin: 80px auto;
            background: #ffffff;
            padding: 24px;
            border-radius: 8px;
            box-shadow: 0 2px 12px rgba(0,0,0,0.12);
            text-align: center;
        }
        h1 {
            color: #b02a37;
        }
        p {
            color: #444;
        }
        a {
            display: inline-block;
            margin-top: 16px;
            text-decoration: none;
            color: #0d6efd;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Ocorreu um erro</h1>
    <p>
        ${mensagemErro != null ? mensagemErro : 'Não foi possível concluir a operação no momento.'}
    </p>
    <p>Tente novamente mais tarde ou entre em contato com o suporte.</p>
    <a href="${pageContext.request.contextPath}/login">Voltar para o login</a>
</div>
</body>
</html>