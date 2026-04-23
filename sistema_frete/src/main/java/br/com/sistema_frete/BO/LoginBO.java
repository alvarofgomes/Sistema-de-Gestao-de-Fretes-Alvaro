package br.com.sistema_frete.BO;

import br.com.sistema_frete.exception.*;

public class LoginBO {

    public void autenticar(String usuario, String senha) throws NegocioException {
        if (usuario == null || usuario.trim().isEmpty()) {
            throw new NegocioException("O usuario deve ser informado.");
        }

        if (senha == null || senha.trim().isEmpty()) {
            throw new NegocioException("A senha deve ser informada.");
        }

        /*
         * Semana 1:
         * Autenticacao simples sem banco, apenas para validar o mecanismo de sessao e filtro.
         */
        if (!"admin".equals(usuario) || !"123".equals(senha)) {
            throw new NegocioException("Usuario ou senha invalidos.");
        }
    }
}