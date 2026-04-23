<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Login - Sistema de Gestão de Fretes</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f4f6f8;
            margin: 0;
            padding: 0;
        }
        .container {
            width: 380px;
            margin: 80px auto;
            background: #ffffff;
            padding: 24px;
            border-radius: 8px;
            box-shadow: 0 2px 12px rgba(0,0,0,0.12);
        }
        h1 {
            margin-top: 0;
            font-size: 22px;
            text-align: center;
        }
        label {
            display: block;
            margin-top: 12px;
            margin-bottom: 6px;
            font-weight: bold;
        }
        input {
            width: 100%;
            padding: 10px;
            box-sizing: border-box;
        }
        button {
            width: 100%;
            margin-top: 18px;
            padding: 10px;
            border: none;
            background: #0d6efd;
            color: #fff;
            font-size: 15px;
            cursor: pointer;
        }
        .erro {
            background: #f8d7da;
            color: #842029;
            padding: 10px;
            border-radius: 4px;
            margin-top: 12px;
        }
        .info {
            margin-top: 14px;
            font-size: 13px;
            color: #555;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Login</h1>

    <form action="${pageContext.request.contextPath}/login" method="post">
        <label for="usuario">Usuário</label>
        <input type="text" id="usuario" name="usuario" maxlength="50">

        <label for="senha">Senha</label>
        <input type="password" id="senha" name="senha" maxlength="50">

        <button type="submit">Entrar</button>
    </form>

    <div class="erro">
        ${mensagemErro}
    </div>

    <div class="info">
        Usuário de teste: <strong>admin</strong><br>
        Senha de teste: <strong>123</strong>
    </div>
</div>
</body>
</html>