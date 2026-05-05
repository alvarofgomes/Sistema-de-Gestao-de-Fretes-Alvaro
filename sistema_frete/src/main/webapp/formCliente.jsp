<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>${cliente.id != null ? 'Editar' : 'Novo'} Cliente - Sistema de Fretes</title>
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
<body>

<c:set var="paginaAtualMenu" value="clientes" scope="request"/>
<jsp:include page="/includes/header.jsp"/>

<div class="container container-form">

    <div class="page-header">
        <h1>${cliente.id != null ? 'Editar Cliente' : 'Novo Cliente'}</h1>
    </div>

    <c:if test="${not empty erro}">
        <div class="msg-erro">${erro}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/clientes" method="post">
        <input type="hidden" name="id"                        value="${cliente.id}" />
        <input type="hidden" name="filtroRetorno"             value="${param.filtro != null ? param.filtro : filtro}" />
        <input type="hidden" name="paginaRetorno"             value="${param.pagina != null ? param.pagina : paginaAtual}" />
        <input type="hidden" name="registrosPorPaginaRetorno" value="${param.registrosPorPagina != null ? param.registrosPorPagina : registrosPorPagina}" />

        <div class="form-section">
            <div class="form-section-title">Dados da Empresa</div>
            <div class="form-grid">
                <div class="form-group col-full">
                    <label>Razão Social *</label>
                    <input type="text" name="razaoSocial" value="${cliente.razaoSocial}" required />
                </div>
                <div class="form-group col-full">
                    <label>Nome Fantasia</label>
                    <input type="text" name="nomeFantasia" value="${cliente.nomeFantasia}" />
                </div>
                <div class="form-group">
                    <label>CNPJ *</label>
                    <input type="text" name="cnpj" value="${cliente.cnpj}"
                           onkeyup="aplicarMascaraCNPJ(this)" maxlength="18" required />
                </div>
                <div class="form-group">
                    <label>Inscrição Estadual</label>
                    <input type="text" name="inscricaoEstadual" value="${cliente.inscricaoEstadual}" />
                </div>
                <div class="form-group">
                    <label>Status</label>
                    <select name="status">
                        <option value="ATIVO"   ${cliente.status == 'ATIVO'   ? 'selected' : ''}>Ativo</option>
                        <option value="INATIVO" ${cliente.status == 'INATIVO' ? 'selected' : ''}>Inativo</option>
                    </select>
                </div>
            </div>
        </div>

        <div class="form-section">
            <div class="form-section-title">Endereço</div>
            <div class="form-grid">
                <div class="form-group col-full">
                    <label>Endereço:</label>
                    <input type="text" name="logradouro" value="${cliente.logradouro}" />
                </div>
                <div class="form-group">
                    <label>Número</label>
                    <input type="text" name="numero" value="${cliente.numero}" />
                </div>
                <div class="form-group">
                    <label>Complemento</label>
                    <input type="text" name="complemento" value="${cliente.complemento}" />
                </div>
                <div class="form-group">
                    <label>Bairro</label>
                    <input type="text" name="bairro" value="${cliente.bairro}" />
                </div>
                <div class="form-group">
                    <label>Cidade:</label>
                    <input type="text" name="cidade" value="${cliente.cidade}" />
                </div>
				<div class="form-group">
				    <label>UF</label>
				    <select name="uf" required>
				        <option value="">Selecione</option>
				
				        <option value="AC" ${cliente.uf == 'AC' ? 'selected' : ''}>AC - Acre</option>
				        <option value="AL" ${cliente.uf == 'AL' ? 'selected' : ''}>AL - Alagoas</option>
				        <option value="AP" ${cliente.uf == 'AP' ? 'selected' : ''}>AP - Amapá</option>
				        <option value="AM" ${cliente.uf == 'AM' ? 'selected' : ''}>AM - Amazonas</option>
				        <option value="BA" ${cliente.uf == 'BA' ? 'selected' : ''}>BA - Bahia</option>
				        <option value="CE" ${cliente.uf == 'CE' ? 'selected' : ''}>CE - Ceará</option>
				        <option value="DF" ${cliente.uf == 'DF' ? 'selected' : ''}>DF - Distrito Federal</option>
				        <option value="ES" ${cliente.uf == 'ES' ? 'selected' : ''}>ES - Espírito Santo</option>
				        <option value="GO" ${cliente.uf == 'GO' ? 'selected' : ''}>GO - Goiás</option>
				        <option value="MA" ${cliente.uf == 'MA' ? 'selected' : ''}>MA - Maranhão</option>
				        <option value="MT" ${cliente.uf == 'MT' ? 'selected' : ''}>MT - Mato Grosso</option>
				        <option value="MS" ${cliente.uf == 'MS' ? 'selected' : ''}>MS - Mato Grosso do Sul</option>
				        <option value="MG" ${cliente.uf == 'MG' ? 'selected' : ''}>MG - Minas Gerais</option>
				        <option value="PA" ${cliente.uf == 'PA' ? 'selected' : ''}>PA - Pará</option>
				        <option value="PB" ${cliente.uf == 'PB' ? 'selected' : ''}>PB - Paraíba</option>
				        <option value="PR" ${cliente.uf == 'PR' ? 'selected' : ''}>PR - Paraná</option>
				        <option value="PE" ${cliente.uf == 'PE' ? 'selected' : ''}>PE - Pernambuco</option>
				        <option value="PI" ${cliente.uf == 'PI' ? 'selected' : ''}>PI - Piauí</option>
				        <option value="RJ" ${cliente.uf == 'RJ' ? 'selected' : ''}>RJ - Rio de Janeiro</option>
				        <option value="RN" ${cliente.uf == 'RN' ? 'selected' : ''}>RN - Rio Grande do Norte</option>
				        <option value="RS" ${cliente.uf == 'RS' ? 'selected' : ''}>RS - Rio Grande do Sul</option>
				        <option value="RO" ${cliente.uf == 'RO' ? 'selected' : ''}>RO - Rondônia</option>
				        <option value="RR" ${cliente.uf == 'RR' ? 'selected' : ''}>RR - Roraima</option>
				        <option value="SC" ${cliente.uf == 'SC' ? 'selected' : ''}>SC - Santa Catarina</option>
				        <option value="SP" ${cliente.uf == 'SP' ? 'selected' : ''}>SP - São Paulo</option>
				        <option value="SE" ${cliente.uf == 'SE' ? 'selected' : ''}>SE - Sergipe</option>
				        <option value="TO" ${cliente.uf == 'TO' ? 'selected' : ''}>TO - Tocantins</option>
				    </select>
				</div>
                <div class="form-group">
                    <label>CEP</label>
                    <input type="text" name="cep" value="${cliente.cep}"
                           onkeyup="aplicarMascaraCEP(this)" maxlength="9" />
                </div>
            </div>
        </div>

        <%-- SEÇÃO 3: Contato --%>
        <div class="form-section">
            <div class="form-section-title">Contato</div>
            <div class="form-grid">
                <div class="form-group">
                    <label>Telefone</label>
                    <input type="text" name="telefone" value="${cliente.telefone}"
                           onkeyup="aplicarMascaraTelefone(this)" maxlength="15" />
                </div>
                <div class="form-group">
                    <label>E-mail</label>
                    <input type="email" name="email" value="${cliente.email}" />
                </div>
            </div>
        </div>

		<%-- SEÇÃO DE ACESSO — aparece só na edição e só se não tiver usuário --%>
		<c:if test="${cliente.id != null && !possuiUsuario}">
		    <div class="form-section">
		        <div class="form-section-title" style="color: var(--accent);">
		            Criar Acesso ao Portal do Cliente
		        </div>
		        <p style="font-size:13px; color:var(--text-muted); margin-bottom:16px;">
		            Este cliente ainda não possui login de acesso. 
		            Preencha abaixo para criar o acesso ao portal.
		        </p>
		        <div class="form-grid">
		            <div class="form-group">
		                <label>Nome do Responsável</label>
		                <input type="text" name="nomeResponsavel"
		                       placeholder="Nome de quem vai acessar" />
		            </div>
		            <div class="form-group">
		                <label>Login</label>
		                <input type="text" name="loginNovo"
		                       placeholder="Login de acesso" />
		            </div>
		            <div class="form-group">
		                <label>Senha (mínimo 6 caracteres)</label>
		                <input type="password" name="senhaNova" />
		            </div>
		            <div class="form-group">
		                <label>Confirmar Senha</label>
		                <input type="password" name="confirmarSenhaNova" />
		            </div>
		        </div>
		        <p style="font-size:12px; color:var(--text-muted); margin-top:4px;">
		            * Deixe em branco se não quiser criar o acesso agora.
		        </p>
		    </div>
		</c:if>
		
		<%-- Se já tem usuário, apenas informa --%>
		<c:if test="${cliente.id != null && possuiUsuario}">
		    <div class="form-section">
		        <div class="form-section-title">Acesso ao Portal</div>
		        <p style="font-size:13px; color:var(--success); font-weight:600;">
		            Este cliente já possui login de acesso ao portal configurado.
		        </p>
		    </div>
		</c:if>
	
        <div class="acoes-form">
            <button type="submit" class="btn btn-primary">Salvar</button>
            <a class="btn btn-secondary"
               href="${pageContext.request.contextPath}/clientes?filtro=${param.filtro != null ? param.filtro : filtro}&pagina=${param.pagina != null ? param.pagina : paginaAtual}&registrosPorPagina=${param.registrosPorPagina != null ? param.registrosPorPagina : registrosPorPagina}">
                Cancelar
            </a>
        </div>
    </form>
</div>

</body>
</html>