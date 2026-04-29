<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Home - Sistema de Gestão de Fretes</title>
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
        }
    </style>
</head>

<body>

<div class="container container-home">

    <div class="home-topo">
        <div>
            <h1>Sistema de Gestão de Fretes</h1>
            <p class="usuario">Usuário logado: <strong>${sessionScope.usuarioLogado}</strong></p>
        </div>
        <div class="sair">
            <a href="${pageContext.request.contextPath}/logout">Sair</a>
        </div>
    </div>

    <%-- KPIs --%>
    <div class="kpis">
        <div class="kpi-card">
            <div class="kpi-label">Fretes Emitidos</div>
            <div class="kpi-valor">${fretesEmAberto}</div>
            <div class="kpi-sub">aguardando saída</div>
        </div>
        <div class="kpi-card kpi-transito">
            <div class="kpi-label">Em Trânsito</div>
            <div class="kpi-valor">${fretesEmTransito}</div>
            <div class="kpi-sub">em rota agora</div>
        </div>
        <div class="kpi-card kpi-disponivel">
            <div class="kpi-label">Veículos Disponíveis</div>
            <div class="kpi-valor">${veiculosDisponiveis}</div>
            <div class="kpi-sub">prontos para uso</div>
        </div>
        <div class="kpi-card kpi-atraso">
            <div class="kpi-label">Fretes em Atraso</div>
            <div class="kpi-valor">${fretesAtrasados}</div>
            <div class="kpi-sub">prazo vencido</div>
        </div>
    </div>

    <%-- Cards de módulo --%>
    <div class="cards-home">
        <a href="${pageContext.request.contextPath}/clientes" class="card-home">
            <h2>Clientes</h2>
            <p>Cadastro, edição, listagem e controle de tomadores de serviço.</p>
        </a>

        <a href="${pageContext.request.contextPath}/motoristas" class="card-home">
            <h2>Motoristas</h2>
            <p>Gerencie motoristas, CNH, vínculo e status operacional.</p>
        </a>

        <a href="${pageContext.request.contextPath}/veiculos" class="card-home">
            <h2>Veículos</h2>
            <p>Controle de frota, capacidade, status e disponibilidade.</p>
        </a>

        <a href="${pageContext.request.contextPath}/fretes" class="card-home">
            <div class="card-icone">📦</div>
            <h2>Fretes</h2>
            <p>Emissão, acompanhamento e controle do ciclo completo de fretes.</p>
        </a>

        <a href="#" class="card-home desabilitado">
            <h2>Relatórios</h2>
            <p>Relatórios operacionais com JasperReports. Em breve.</p>
        </a>
    </div>

</div>

</body>
</html>