package br.com.sistema_frete.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String contextPath = request.getContextPath();
        String uri = request.getRequestURI();

        boolean recursoPublico =
                uri.equals(contextPath + "/login") ||
                uri.equals(contextPath + "/login.jsp") ||
                uri.equals(contextPath + "/erro.jsp") ||
                uri.startsWith(contextPath + "/css/") ||
                uri.startsWith(contextPath + "/js/") ||
                uri.startsWith(contextPath + "/images/");

        HttpSession session = request.getSession(false);
        boolean autenticado = session != null && session.getAttribute("usuarioLogado") != null;

        if (recursoPublico || autenticado) {
            chain.doFilter(servletRequest, servletResponse);
            return;
        }

        response.sendRedirect(contextPath + "/login");
    }
}