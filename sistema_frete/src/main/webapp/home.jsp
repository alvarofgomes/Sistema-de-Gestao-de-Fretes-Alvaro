<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Home - Sistema de Gestão de Fretes</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f4f6f8;
            margin: 0;
            padding: 0;
        }
        .container {
            width: 700px;
            margin: 40px auto;
            background: #ffffff;
            padding: 24px;
            border-radius: 8px;
            box-shadow: 0 2px 12px rgba(0,0,0,0.12);
        }
        a {
            color: #0d6efd;
            text-decoration: none;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Sistema de Gestão de Fretes</h1>
    <p>Login realizado com sucesso.</p>
    <p>Usuário logado: <strong>${sessionScope.usuarioLogado}</strong></p>
    <p>Esta é uma página protegida pelo filtro de autenticação.</p>

    <a href="${pageContext.request.contextPath}/logout">Sair</a>
</div>
</body>
</html>