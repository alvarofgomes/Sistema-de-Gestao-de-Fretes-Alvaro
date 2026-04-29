<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<style>
    .app-header {
        background: #ffffff;
        border-bottom: 1px solid #ddd;
        padding: 14px 30px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        box-shadow: 0 1px 6px rgba(0,0,0,0.06);
    }

    .app-header .brand {
        font-weight: bold;
        color: #222;
        text-decoration: none;
        font-size: 18px;
    }

    .app-nav a {
        margin-left: 14px;
        text-decoration: none;
        color: #0d6efd;
        font-weight: bold;
        font-size: 14px;
    }

    .app-nav a.sair {
        color: #c0392b;
    }
</style>

<header class="app-header">
    <a class="brand" href="${pageContext.request.contextPath}/home.jsp">
        Sistema de Gestão de Fretes
    </a>

    <nav class="app-nav">
        <c:if test="${requestScope.paginaAtualMenu ne 'clientes'}">
            <a href="${pageContext.request.contextPath}/clientes">Clientes</a>
        </c:if>

        <c:if test="${requestScope.paginaAtualMenu ne 'motoristas'}">
            <a href="${pageContext.request.contextPath}/motoristas">Motoristas</a>
        </c:if>

        <c:if test="${requestScope.paginaAtualMenu ne 'veiculos'}">
            <a href="${pageContext.request.contextPath}/veiculos">Veículos</a>
        </c:if>

        <c:if test="${requestScope.paginaAtualMenu ne 'fretes'}">
            <a href="${pageContext.request.contextPath}/fretes">Fretes</a>
        </c:if>

        <a href="${pageContext.request.contextPath}/logout" class="sair">Sair</a>
    </nav>
</header>