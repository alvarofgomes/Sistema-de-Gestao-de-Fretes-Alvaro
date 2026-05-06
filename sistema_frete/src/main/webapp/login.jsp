<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<title>Login - Sistema de Gestão de Fretes</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
        <style>
        body {
            background:
                linear-gradient(
                    rgba(6, 20, 50, .10),
                    rgba(6, 20, 50, .22)
                ),
                url('${pageContext.request.contextPath}/assets/images/OptimusPrimeNoPrime.png');

            background-size: cover;
            background-position: center center;
            background-repeat: no-repeat;
            background-attachment: fixed;
        }

        .container-home {
            background: rgba(255, 255, 255, .96);
            backdrop-filter: blur(8px);
        }
        
* { box-sizing: border-box; margin: 0; padding: 0; }

body {
    font-family: 'Barlow', sans-serif; /* ou qualquer fonte que estiver usando */
    min-height: 100vh;
    display: flex;
    align-items: center;
    justify-content: center;
}

.login-box {
    background: rgba(255, 255, 255, 0.97);
    backdrop-filter: blur(10px);
    border-radius: 18px;
    padding: 44px 48px 40px;
    width: 420px;
    max-width: 95vw;
    box-shadow: 0 20px 60px rgba(0, 0, 0, 0.25);
}


.logo {
    display: flex;
    align-items: center;
    gap: 14px;
    margin-bottom: 36px;
}

.logo-icon {
    width: 64px;
    height: 64px;
    flex-shrink: 0;   /* <-- ESSENCIAL: impede o SVG de crescer */
}

.logo-text {
    display: flex;
    flex-direction: column;
    line-height: 1.1;
}

.logo-text .top {
    font-size: 13px;
    font-weight: 600;
    color: #1a4fa8;
    text-transform: uppercase;
    letter-spacing: 0.5px;
}

.logo-text .bottom {
    font-size: 22px;
    font-weight: 800;
    color: #1a2a4a;
    text-transform: uppercase;
}

.form-group { margin-bottom: 20px; }

.field-label {
    display: flex;
    align-items: center;
    gap: 7px;
    font-size: 14px;
    font-weight: 600;
    color: #1a2a4a;
    margin-bottom: 8px;
}

.field-label svg { width: 17px; height: 17px; }

input[type="text"],
input[type="password"] {
    width: 100%;
    padding: 13px 16px;
    border: 1.5px solid #dde3ef;
    border-radius: 10px;
    font-size: 15px;
    color: #1a2a4a;
    background: #fff;
    outline: none;
    transition: border-color .2s;
}

input:focus { border-color: #1a4fa8; }

.input-wrapper { position: relative; }
.input-wrapper input { padding-right: 46px; }

.toggle-pwd {
    position: absolute;
    right: 13px;
    top: 50%;
    transform: translateY(-50%);
    background: none;
    border: none;
    cursor: pointer;
    color: #8899bb;
    display: flex;
    align-items: center;
    padding: 0;
}

button[type="submit"] {
    width: 100%;
    margin-top: 10px;
    padding: 15px 20px;
    background: #1a4fa8;
    color: #fff;
    font-size: 16px;
    font-weight: 700;
    border: none;
    border-radius: 10px;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 10px;
    transition: background .2s, transform .1s;
}

button[type="submit"]:hover {
    background: #153e8a;
    transform: translateY(-1px);
}

button[type="submit"] svg { width: 20px; height: 20px; }


.erro {
    margin-top: 16px;
    background: #fff0f0;
    color: #c0392b;
    border: 1px solid #f5c6c6;
    border-radius: 8px;
    padding: 10px 14px;
    font-size: 14px;
    font-weight: 600;
}
    </style>
</head>

<body>

<div class="login-box">

    <div class="logo">
        <svg class="logo-icon" viewBox="0 0 64 64" fill="none" xmlns="http://www.w3.org/2000/svg">
            <rect x="4" y="22" width="34" height="22" rx="3" fill="#1a4fa8"/>
            <path d="M38 28 L38 44 L58 44 L58 34 L50 28 Z" fill="#1a4fa8"/>
            <path d="M42 30 L42 36 L54 36 L54 34 L48 30 Z" fill="#7eb3f5"/>
            <circle cx="14" cy="46" r="6" fill="#1a2a4a"/>
            <circle cx="14" cy="46" r="3" fill="#d0d5e0"/>
            <circle cx="46" cy="46" r="6" fill="#1a2a4a"/>
            <circle cx="46" cy="46" r="3" fill="#d0d5e0"/>
            <path d="M8 22 Q12 16 16 22 Q20 16 24 22" stroke="#c0392b" stroke-width="2" fill="none" stroke-linecap="round"/>
            <path d="M20 22 Q24 15 28 22 Q32 15 36 22" stroke="#c0392b" stroke-width="2" fill="none" stroke-linecap="round"/>
        </svg>

        <div class="logo-text">
            <span class="top">Sistema de</span>
            <span class="bottom">Gestão de<br>Fretes</span>
        </div>
    </div>

    <form action="${pageContext.request.contextPath}/login" method="post">

        <div class="form-group">
            <div class="field-label">
                <svg viewBox="0 0 24 24" fill="none" stroke="#1a4fa8" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
                    <circle cx="12" cy="7" r="4"/>
                </svg>
                Usuário
            </div>

            <input type="text" name="usuario" maxlength="50" placeholder="Digite seu usuário" required>
        </div>

        <div class="form-group">
            <div class="field-label">
                <svg viewBox="0 0 24 24" fill="none" stroke="#1a4fa8" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/>
                    <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
                </svg>
                Senha
            </div>

            <div class="input-wrapper">
                <input type="password" name="senha" maxlength="50" placeholder="Digite sua senha" id="senhaInput" required>

                <button type="button" class="toggle-pwd" onclick="toggleSenha()" title="Mostrar/ocultar senha">
                    <svg id="eyeIcon" viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
                        <circle cx="12" cy="12" r="3"/>
                    </svg>
                </button>
            </div>
        </div>

        <button type="submit">
            Entrar
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                <line x1="5" y1="12" x2="19" y2="12"/>
                <polyline points="12 5 19 12 12 19"/>
            </svg>
        </button>

    </form>

		<c:if test="${not empty sessionScope.sucessoCadastro}">
			<div
				style="margin-top: 16px; background: #d1e7dd; color: #0f5132; border-radius: 8px; padding: 10px 14px; font-size: 14px; font-weight: 600;">
				${sessionScope.sucessoCadastro}</div>
			<%
			session.removeAttribute("sucessoCadastro");
			%>
		</c:if>

		<div
			style="text-align: center; margin-top: 20px; font-size: 13px; color: #6b7e8f;">
			Ainda não tem conta? <a
				href="${pageContext.request.contextPath}/cadastro"
				style="color: #1a4fa8; font-weight: 700; text-decoration: none;">
				Criar conta </a>
		</div>

		<c:if test="${not empty mensagemErro}">
        <div class="erro">${mensagemErro}</div>
    </c:if>

</div>

<script>
function toggleSenha() {
    var input = document.getElementById("senhaInput");

    if (input.type === "password") {
        input.type = "text";
    } else {
        input.type = "password";
    }
}
</script>

</body>
</html>