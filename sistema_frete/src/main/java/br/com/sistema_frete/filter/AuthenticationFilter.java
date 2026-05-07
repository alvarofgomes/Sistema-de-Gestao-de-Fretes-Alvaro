package br.com.sistema_frete.filter;

import br.com.sistema_frete.enums.usuario.PerfilUsuario;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    private static final List<String> ROTAS_PUBLICAS = Arrays.asList(
            "/login", "/login.jsp", "/erro.jsp", "/cadastro"
    );

    private static final List<String> ROTAS_ADMIN = Arrays.asList(
            "/usuarios"
    );

    private static final List<String> ROTAS_BLOQUEADAS_CLIENTE = Arrays.asList(
    	    "/clientes", "/motoristas", "/veiculos", "/fretes",
    	    "/usuarios", "/home", "/ocorrencias", "/relatorios"
    	);

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest  req  = (HttpServletRequest)  servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        String ctx = req.getContextPath();
        String uri = req.getRequestURI();

        // recursos estáticos — liberar sempre
        if (uri.startsWith(ctx + "/assets/") ||
            uri.startsWith(ctx + "/css/")    ||
            uri.startsWith(ctx + "/js/")     ||
            uri.startsWith(ctx + "/images/")) {
            chain.doFilter(servletRequest, servletResponse);
            return;
        }

        // rotas públicas — liberar sem autenticação
        String path = uri.substring(ctx.length());
        for (String rota : ROTAS_PUBLICAS) {
            if (path.equals(rota) || path.startsWith(rota)) {
                chain.doFilter(servletRequest, servletResponse);
                return;
            }
        }

        // verificar autenticação
        HttpSession session   = req.getSession(false);
        String perfilStr      = session != null ? (String) session.getAttribute("perfilUsuario") : null;
        boolean autenticado   = session != null && session.getAttribute("usuarioLogado") != null;

        if (!autenticado) {
            resp.sendRedirect(ctx + "/login");
            return;
        }

        PerfilUsuario perfil = PerfilUsuario.valueOf(perfilStr);

        // CLIENTE só acessa portal-cliente
        if (perfil == PerfilUsuario.CLIENTE) {
            boolean bloqueado = false;
            for (String rota : ROTAS_BLOQUEADAS_CLIENTE) {
                if (path.startsWith(rota)) {
                    bloqueado = true;
                    break;
                }
            }
            if (bloqueado) {
                resp.sendRedirect(ctx + "/portal-cliente");
                return;
            }
        }

        // rotas exclusivas de ADMIN
        if (perfil != PerfilUsuario.ADMIN) {
            for (String rota : ROTAS_ADMIN) {
                if (path.startsWith(rota)) {
                    req.setAttribute("mensagemErro",
                            "Acesso negado. Esta área é restrita ao administrador.");
                    req.setAttribute("voltarUrl", ctx + "/home");
                    req.getRequestDispatcher("/erro.jsp").forward(req, resp);
                    return;
                }
            }
        }

        // portal-cliente acessível apenas para CLIENTE
        if (path.startsWith("/portal-cliente") && perfil != PerfilUsuario.CLIENTE) {
            resp.sendRedirect(ctx + "/home");
            return;
        }

        chain.doFilter(servletRequest, servletResponse);
    }
}