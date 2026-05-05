<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Criar Conta - Sistema de Fretes</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <style>
        body {
            background:
                linear-gradient(rgba(6,20,50,.15), rgba(6,20,50,.30)),
                url('${pageContext.request.contextPath}/assets/images/OptimusPrimeNoPrime.png');
            background-size: cover;
            background-position: center;
            background-repeat: no-repeat;
            background-attachment: fixed;
            min-height: 100vh;
            display: flex;
            align-items: flex-start;
            justify-content: center;
            padding: 40px 16px;
        }

        .cadastro-box {
            background: rgba(255,255,255,0.97);
            backdrop-filter: blur(10px);
            border-radius: 18px;
            padding: 40px 48px;
            width: 680px;
            max-width: 98vw;
            box-shadow: 0 20px 60px rgba(0,0,0,0.25);
        }

        .cadastro-header {
            display: flex;
            align-items: center;
            gap: 14px;
            margin-bottom: 32px;
            padding-bottom: 20px;
            border-bottom: 2px solid var(--primary-light);
        }

        .cadastro-header h1 {
            font-size: 22px;
            font-weight: 700;
            color: var(--primary);
            margin: 0;
        }

        .cadastro-header p {
            font-size: 13px;
            color: var(--text-muted);
            margin: 4px 0 0;
        }

        .ja-tem-conta {
            text-align: center;
            margin-top: 24px;
            font-size: 13px;
            color: var(--text-muted);
        }

        .ja-tem-conta a {
            color: var(--primary);
            font-weight: 700;
            text-decoration: none;
        }

        .ja-tem-conta a:hover { text-decoration: underline; }
    </style>
    <script>
        function aplicarMascaraCNPJ(c) {
            let v = c.value.replace(/\D/g,"");
            v = v.replace(/^(\d{2})(\d)/,"$1.$2");
            v = v.replace(/^(\d{2})\.(\d{3})(\d)/,"$1.$2.$3");
            v = v.replace(/\.(\d{3})(\d)/,".$1/$2");
            v = v.replace(/(\d{4})(\d)/,"$1-$2");
            c.value = v;
        }
        function aplicarMascaraCEP(c) {
            let v = c.value.replace(/\D/g,"");
            v = v.replace(/^(\d{5})(\d)/,"$1-$2");
            c.value = v;
        }
        function aplicarMascaraTelefone(c) {
            let v = c.value.replace(/\D/g,"");
            v = v.length <= 10
                ? v.replace(/^(\d{2})(\d)/,"($1) $2").replace(/(\d{4})(\d)/,"$1-$2")
                : v.replace(/^(\d{2})(\d)/,"($1) $2").replace(/(\d{5})(\d)/,"$1-$2");
            c.value = v;
        }
    </script>
</head>
<body>

<div class="cadastro-box">

    <div class="cadastro-header">
        <div>
            <h1>Criar Conta</h1>
            <p>Preencha os dados da sua empresa e crie seu acesso ao sistema.</p>
        </div>
    </div>

    <c:if test="${not empty erro}">
        <div class="msg-erro">${erro}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/cadastro" method="post">

        <%-- SEÇÃO 1: Dados da Empresa --%>
        <div class="form-section">
            <div class="form-section-title">Dados da Empresa</div>
            <div class="form-grid">
                <div class="form-group col-full">
                    <label>Razão Social *</label>
                    <input type="text" name="razaoSocial"
                           value="${not empty razaoSocial ? razaoSocial : ''}" required />
                </div>
                <div class="form-group col-full">
                    <label>Nome Fantasia</label>
                    <input type="text" name="nomeFantasia"
                           value="${not empty nomeFantasia ? nomeFantasia : ''}" />
                </div>
                <div class="form-group">
                    <label>CNPJ *</label>
                    <input type="text" name="cnpj"
                           value="${not empty cnpj ? cnpj : ''}"
                           onkeyup="aplicarMascaraCNPJ(this)" maxlength="18" required />
                </div>
                <div class="form-group">
                    <label>Telefone</label>
                    <input type="text" name="telefone"
                           value="${not empty telefone ? telefone : ''}"
                           onkeyup="aplicarMascaraTelefone(this)" maxlength="15" />
                </div>
                <div class="form-group col-full">
                    <label>E-mail</label>
                    <input type="email" name="email"
                           value="${not empty email ? email : ''}" />
                </div>
            </div>
        </div>

        <%-- SEÇÃO 2: Endereço --%>
        <div class="form-section">
            <div class="form-section-title">Endereço</div>
            <div class="form-grid">
                <div class="form-group col-full">
                    <label>Logradouro</label>
                    <input type="text" name="logradouro"
                           value="${not empty logradouro ? logradouro : ''}" />
                </div>
                <div class="form-group">
                    <label>Número</label>
                    <input type="text" name="numero"
                           value="${not empty numero ? numero : ''}" />
                </div>
                <div class="form-group">
                    <label>Complemento</label>
                    <input type="text" name="complemento"
                           value="${not empty complemento ? complemento : ''}" />
                </div>
                <div class="form-group">
                    <label>Bairro</label>
                    <input type="text" name="bairro"
                           value="${not empty bairro ? bairro : ''}" />
                </div>
                <div class="form-group">
                    <label>Cidade</label>
                    <input type="text" name="municipio"
                           value="${not empty municipio ? municipio : ''}" />
                </div>
                <div class="form-group">
                    <label>UF</label>
                    <input type="text" name="uf" maxlength="2"
                           value="${not empty uf ? uf : ''}" />
                </div>
                <div class="form-group">
                    <label>CEP</label>
                    <input type="text" name="cep"
                           value="${not empty cep ? cep : ''}"
                           onkeyup="aplicarMascaraCEP(this)" maxlength="9" />
                </div>
            </div>
        </div>

        <%-- SEÇÃO 3: Dados de Acesso --%>
        <div class="form-section">
            <div class="form-section-title">Dados de Acesso</div>
            <div class="form-grid">
                <div class="form-group col-full">
                    <label>Nome do Responsável *</label>
                    <input type="text" name="nomeResponsavel"
                           value="${not empty nomeResponsavel ? nomeResponsavel : ''}" required />
                </div>
                <div class="form-group">
                    <label>Login *</label>
                    <input type="text" name="login"
                           value="${not empty login ? login : ''}" required />
                </div>
                <div class="form-group">
                    <%-- espaçador --%>
                </div>
                <div class="form-group">
                    <label>Senha * (mínimo 6 caracteres)</label>
                    <input type="password" name="senha" required />
                </div>
                <div class="form-group">
                    <label>Confirmar Senha *</label>
                    <input type="password" name="confirmaSenha" required />
                </div>
            </div>
        </div>

        <div class="acoes-form">
            <button type="submit" class="btn btn-primary" style="width:100%;">
                Criar Conta
            </button>
        </div>
    </form>

    <div class="ja-tem-conta">
        Já tem uma conta? <a href="${pageContext.request.contextPath}/login">Fazer login</a>
    </div>

</div>

</body>
</html>