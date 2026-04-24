<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

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

    .login-box {
        width: 420px;
        margin: 80px auto;
        background: #ffffff;
        padding: 32px;
        border-radius: 10px;
        box-shadow: 0 2px 12px rgba(0,0,0,0.12);
    }

    .logo {
        text-align: center;
        margin-bottom: 26px;
    }

    .logo h1 {
        margin: 0;
        color: #222;
        font-size: 28px;
    }

    .logo p {
        margin-top: 8px;
        color: #555;
        font-size: 14px;
    }

    .form-group {
        margin-bottom: 18px;
    }

    label {
        display: block;
        margin-bottom: 6px;
        font-weight: bold;
        color: #222;
    }

    input {
        width: 100%;
        padding: 10px;
        box-sizing: border-box;
        border: 1px solid #ccc;
        border-radius: 4px;
        font-size: 15px;
    }

    input:focus {
        outline: none;
        border-color: #0d6efd;
    }

    button {
        width: 100%;
        margin-top: 10px;
        padding: 12px;
        border: none;
        border-radius: 6px;
        background: #0d6efd;
        color: #ffffff;
        font-size: 15px;
        font-weight: bold;
        cursor: pointer;
    }

    button:hover {
        background: #0b5ed7;
    }

    .erro {
        margin-top: 15px;
        background: #f8d7da;
        color: #842029;
        padding: 10px;
        border-radius: 4px;
        text-align: center;
    }

    .subtitulo {
        margin-top: 18px;
        text-align: center;
        color: #555;
        font-size: 13px;
    }
</style>
</head>

<body>

<div class="login-box">

    <div class="logo">
        <h1>Sistema de Gestão de Fretes</h1>
        <p>Acesso ao sistema</p>
    </div>

    <form action="${pageContext.request.contextPath}/login" method="post">

        <div class="form-group">
            <label>Usuário</label>
            <input
                type="text"
                name="usuario"
                maxlength="50"
                placeholder="Digite seu usuário"
                required>
        </div>

        <div class="form-group">
            <label>Senha</label>
            <input
                type="password"
                name="senha"
                maxlength="50"
                placeholder="Digite sua senha"
                required>
        </div>

        <button type="submit">
            Entrar
        </button>

    </form>

    <c:if test="${not empty mensagemErro}">
        <div class="erro">
            ${mensagemErro}
        </div>
    </c:if>

    <div class="subtitulo">
        Sistema de Gestão de Fretes
    </div>

</div>

</body>
</html>	