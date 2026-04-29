<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Home - Sistema de Gestão de Fretes</title>

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
    </style>
</head>

<body>

<div class="container container-home">

    <div class="home-topo">
        <div>
            <h1>Sistema de Gestão de Fretes</h1>
            <p class="usuario">
                Usuário logado: <strong>${sessionScope.usuarioLogado}</strong>
            </p>
        </div>

        <div class="sair">
            <a href="${pageContext.request.contextPath}/logout">Sair</a>
        </div>
    </div>

    <div class="cards-home">

        <a href="${pageContext.request.contextPath}/clientes" class="card-home">
            <h2>Clientes</h2>
            <p>Cadastro, edição, listagem e controle de clientes da transportadora.</p>
        </a>

        <a href="${pageContext.request.contextPath}/motoristas" class="card-home">
            <h2>Motoristas</h2>
            <p>Gerencie motoristas, CNH, vínculo e status operacional.</p>
        </a>

        <a href="${pageContext.request.contextPath}/veiculos" class="card-home">
            <h2>Veículos</h2>
            <p>Controle frota, capacidade, status e disponibilidade dos veículos.</p>
        </a>

        <a href="${pageContext.request.contextPath}/fretes" class="card-home">
            <h2>Fretes</h2>
            <p>Emissão, acompanhamento e controle do ciclo completo de fretes.</p>
        </a>

        <a href="#" class="card-home desabilitado">
            <h2>Relatórios</h2>
            <p>Área futura para relatórios operacionais com JasperReports.</p>
        </a>

    </div>

</div>

</body>
</html>