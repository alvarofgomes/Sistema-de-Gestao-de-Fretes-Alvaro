package br.com.sistema_frete.BO;

import br.com.sistema_frete.DAO.UsuarioDAO;
import br.com.sistema_frete.enums.usuario.StatusUsuario;
import br.com.sistema_frete.exception.CadastroException;
import br.com.sistema_frete.exception.NegocioException;
import br.com.sistema_frete.model.Usuario;

public class LoginBO {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public Usuario autenticar(String login, String senha) throws NegocioException {
        try {
            if (login == null || login.trim().isEmpty()) {
                throw new CadastroException("Informe o usuário.");
            }

            if (senha == null || senha.trim().isEmpty()) {
                throw new CadastroException("Informe a senha.");
            }

            Usuario usuario = usuarioDAO.buscarPorLogin(login);

            if (usuario == null) {
                throw new CadastroException("Usuário ou senha inválidos.1");
            }

            if (usuario.getStatus() != StatusUsuario.ATIVO) {
                throw new CadastroException("Usuário inativo. Entre em contato com o administrador.");
            }

            return usuario;

        } catch (CadastroException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new NegocioException("Não foi possível realizar o login.", e);
        }
    }
}