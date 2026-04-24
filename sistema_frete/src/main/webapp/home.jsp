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
            width: 900px;
            margin: 50px auto;
            background: #ffffff;
            padding: 32px;
            border-radius: 10px;
            box-shadow: 0 2px 12px rgba(0,0,0,0.12);
        }

        .topo {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 24px;
            border-bottom: 1px solid #ddd;
            padding-bottom: 16px;
        }

        h1 {
            margin: 0;
            color: #222;
        }

        .usuario {
            color: #555;
            font-size: 14px;
        }

        .cards {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 18px;
            margin-top: 20px;
        }

        .card {
            display: block;
            padding: 22px;
            border-radius: 8px;
            background: #f8f9fa;
            border: 1px solid #ddd;
            text-decoration: none;
            color: #222;
            transition: 0.2s;
        }

        .card:hover {
            background: #e9f2ff;
            border-color: #0d6efd;
            transform: translateY(-2px);
        }

        .card h2 {
            margin: 0 0 10px 0;
            font-size: 20px;
            color: #0d6efd;
        }

        .card p {
            margin: 0;
            color: #555;
            font-size: 14px;
            line-height: 1.4;
        }

        .card.desabilitado {
            opacity: 0.55;
            cursor: not-allowed;
            background: #f1f1f1;
        }

        .card.desabilitado:hover {
            transform: none;
            border-color: #ddd;
            background: #f1f1f1;
        }

        .sair {
            margin-top: 28px;
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

    <div class="topo">
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

    <div class="cards">
        <a href="${pageContext.request.contextPath}/clientes" class="card">
            <h2>Clientes</h2>
            <p>Cadastro, edição, listagem e controle de clientes da transportadora.</p>
        </a>

        <a href="${pageContext.request.contextPath}/motoristas" class="card">
            <h2>Motoristas</h2>
            <p>Gerencie motoristas, CNH, vínculo e status operacional.</p>
        </a>

        <a href="${pageContext.request.contextPath}/veiculos" class="card">
            <h2>Veículos</h2>
            <p>Controle frota, capacidade, status e disponibilidade dos veículos.</p>
        </a>

        <a href="#" class="card desabilitado">
            <h2>Fretes</h2>
            <p>Módulo em desenvolvimento para emissão e acompanhamento de fretes.</p>
        </a>

        <a href="#" class="card desabilitado">
            <h2>Ocorrências</h2>
            <p>Registro futuro de eventos e ocorrências durante o transporte.</p>
        </a>

        <a href="#" class="card desabilitado">
            <h2>Relatórios</h2>
            <p>Área futura para relatórios operacionais com JasperReports.</p>
        </a>
    </div>

</div>

</body>
</html>