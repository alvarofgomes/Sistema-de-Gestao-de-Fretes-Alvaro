<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Criar Conta - Sistema de Fretes</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
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
<body class="body-cadastro-publico">

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
                    <label>Endereço</label>
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
					<input type="text" name="cidade"
					       value="${not empty cidade ? cidade : ''}" />
                </div>
				
				<div class="form-group">
				    <label>UF</label>
				    <select name="uf" required>
				        <option value="">Selecione</option>
				        <option value="AC" ${uf == 'AC' ? 'selected' : ''}>AC - Acre</option>
				        <option value="AL" ${uf == 'AL' ? 'selected' : ''}>AL - Alagoas</option>
				        <option value="AP" ${uf == 'AP' ? 'selected' : ''}>AP - Amapá</option>
				        <option value="AM" ${uf == 'AM' ? 'selected' : ''}>AM - Amazonas</option>
				        <option value="BA" ${uf == 'BA' ? 'selected' : ''}>BA - Bahia</option>
				        <option value="CE" ${uf == 'CE' ? 'selected' : ''}>CE - Ceará</option>
				        <option value="DF" ${uf == 'DF' ? 'selected' : ''}>DF - Distrito Federal</option>
				        <option value="ES" ${uf == 'ES' ? 'selected' : ''}>ES - Espírito Santo</option>
				        <option value="GO" ${uf == 'GO' ? 'selected' : ''}>GO - Goiás</option>
				        <option value="MA" ${uf == 'MA' ? 'selected' : ''}>MA - Maranhão</option>
				        <option value="MT" ${uf == 'MT' ? 'selected' : ''}>MT - Mato Grosso</option>
				        <option value="MS" ${uf == 'MS' ? 'selected' : ''}>MS - Mato Grosso do Sul</option>
				        <option value="MG" ${uf == 'MG' ? 'selected' : ''}>MG - Minas Gerais</option>
				        <option value="PA" ${uf == 'PA' ? 'selected' : ''}>PA - Pará</option>
				        <option value="PB" ${uf == 'PB' ? 'selected' : ''}>PB - Paraíba</option>
				        <option value="PR" ${uf == 'PR' ? 'selected' : ''}>PR - Paraná</option>
				        <option value="PE" ${uf == 'PE' ? 'selected' : ''}>PE - Pernambuco</option>
				        <option value="PI" ${uf == 'PI' ? 'selected' : ''}>PI - Piauí</option>
				        <option value="RJ" ${uf == 'RJ' ? 'selected' : ''}>RJ - Rio de Janeiro</option>
				        <option value="RN" ${uf == 'RN' ? 'selected' : ''}>RN - Rio Grande do Norte</option>
				        <option value="RS" ${uf == 'RS' ? 'selected' : ''}>RS - Rio Grande do Sul</option>
				        <option value="RO" ${uf == 'RO' ? 'selected' : ''}>RO - Rondônia</option>
				        <option value="RR" ${uf == 'RR' ? 'selected' : ''}>RR - Roraima</option>
				        <option value="SC" ${uf == 'SC' ? 'selected' : ''}>SC - Santa Catarina</option>
				        <option value="SP" ${uf == 'SP' ? 'selected' : ''}>SP - São Paulo</option>
				        <option value="SE" ${uf == 'SE' ? 'selected' : ''}>SE - Sergipe</option>
				        <option value="TO" ${uf == 'TO' ? 'selected' : ''}>TO - Tocantins</option>
				    </select>
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