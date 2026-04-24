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
            width: 760px;
            margin: 60px auto;
            background: #ffffff;
            padding: 32px;
            border-radius: 10px;
            box-shadow: 0 2px 12px rgba(0,0,0,0.12);
        }

        h1 {
            margin-top: 0;
            color: #222;
        }

        .info {
            background: #f8f9fa;
            padding: 14px;
            border-radius: 6px;
            margin-bottom: 24px;
            color: #333;
        }

        .menu {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 14px;
            margin-top: 20px;
        }

        .menu a {
            display: block;
            text-align: center;
            padding: 16px;
            border-radius: 6px;
            background: #0d6efd;
            color: #fff;
            text-decoration: none;
            font-weight: bold;
        }

        .menu a:hover {
            background: #0b5ed7;
        }

        .sair {
            margin-top: 22px;
            text-align: right;
        }

        .sair a {
            color: #c0392b;
            text-decoration: none;
            font-weight: bold;
        }

        .sair a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>

<div class="container">
    <h1>Sistema de Gestão de Fretes</h1>

    <div class="info">
        <p>Login realizado com sucesso.</p>
        <p>Usuário logado: <strong>${sessionScope.usuarioLogado}</strong></p>
    </div>

    <div class="menu">
        <a href="${pageContext.request.contextPath}/clientes">Clientes</a>
        <a href="${pageContext.request.contextPath}/motoristas">Motoristas</a>
        <a href="${pageContext.request.contextPath}/veiculos">Veículos</a>
    </div>

    <div class="sair">
        <a href="${pageContext.request.contextPath}/logout">Sair do sistema</a>
    </div>
</div>

</body>
</html>