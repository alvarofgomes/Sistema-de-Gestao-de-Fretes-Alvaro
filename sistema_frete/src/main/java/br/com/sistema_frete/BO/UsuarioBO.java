package br.com.sistema_frete.BO;

import br.com.sistema_frete.DAO.ClienteDAO;
import br.com.sistema_frete.DAO.UsuarioDAO;
import br.com.sistema_frete.enums.usuario.PerfilUsuario;
import br.com.sistema_frete.enums.usuario.StatusUsuario;
import br.com.sistema_frete.exception.CadastroException;
import br.com.sistema_frete.exception.NegocioException;
import br.com.sistema_frete.model.Usuario;
import br.com.sistema_frete.util.HashUtil;

import java.util.List;

public class UsuarioBO {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();

    public Usuario autenticar(String login, String senha) throws NegocioException {
        try {
            if (login == null || login.trim().isEmpty())
                throw new CadastroException("Informe o usuário.");

            if (senha == null || senha.trim().isEmpty())
                throw new CadastroException("Informe a senha.");

            Usuario usuario = usuarioDAO.buscarPorLogin(login.trim());

            if (usuario == null)
                throw new CadastroException("senha vazia ou usuário inválidos.");

            if (!usuario.getSenha().equals(HashUtil.hash(senha)))
                throw new CadastroException("Sua senha deve conter no mínimo 6 caracteres ou o usuário informado é inválido.");

            if (usuario.getStatus() != StatusUsuario.ATIVO)
                throw new CadastroException("Usuário inativo. Entre em contato com o administrador.");

            return usuario;

        } catch (CadastroException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao autenticar:");
            e.printStackTrace();
            throw new NegocioException("Não foi possível realizar o login.", e);
        }
    }

    public List<Usuario> listarComPaginacao(String filtro, int pagina,
                                            int registrosPorPagina) throws NegocioException {
        try {
            int offset = (pagina - 1) * registrosPorPagina;
            return usuarioDAO.buscarComPaginacao(filtro, offset, registrosPorPagina);
        } catch (Exception e) {
            System.err.println("Erro ao listar usuários:");
            e.printStackTrace();
            throw new NegocioException("Não foi possível listar os usuários.", e);
        }
    }

    public int contarTotal(String filtro) throws NegocioException {
        try {
            return usuarioDAO.contarTotal(filtro);
        } catch (Exception e) {
            System.err.println("Erro ao contar usuários:");
            e.printStackTrace();
            throw new NegocioException("Não foi possível contar os usuários.", e);
        }
    }

    public Usuario buscarPorId(Integer id) throws NegocioException {
        try {
            if (id == null)
                throw new CadastroException("ID do usuário não informado.");

            Usuario usuario = usuarioDAO.buscarPorId(id);

            if (usuario == null)
                throw new CadastroException("Usuário não encontrado.");

            return usuario;

        } catch (CadastroException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Erro ao buscar usuário:");
            e.printStackTrace();
            throw new NegocioException("Não foi possível buscar o usuário.", e);
        }
    }

    public void salvar(Usuario usuario, String senhaDigitada) throws NegocioException {
        try {
            validarCamposObrigatorios(usuario, senhaDigitada);

            // duplicidade de login
            Usuario duplicado = usuarioDAO.buscarPorLoginExcluindoId(
                    usuario.getLogin(), usuario.getId());
            if (duplicado != null)
                throw new CadastroException("Já existe um usuário cadastrado com este login.");

            // perfil CLIENTE exige cliente vinculado
            if (usuario.getPerfil() == PerfilUsuario.CLIENTE) {
                if (usuario.getClienteId() == null)
                    throw new CadastroException("Usuário com perfil CLIENTE deve estar vinculado a um cliente.");
                if (clienteDAO.buscarPorId(usuario.getClienteId()) == null)
                    throw new CadastroException("Cliente vinculado não encontrado.");
            } else {
                // perfis não-CLIENTE não devem ter cliente vinculado
                usuario.setClienteId(null);
            }

            // senha: obrigatória no cadastro, opcional na edição
            if (usuario.getId() == null) {
                // novo usuário — senha obrigatória
                usuario.setSenha(HashUtil.hash(senhaDigitada));
            } else {
                // edição — se digitou senha nova, atualiza; senão mantém a atual
                if (senhaDigitada != null && !senhaDigitada.trim().isEmpty()) {
                    usuario.setSenha(HashUtil.hash(senhaDigitada));
                } else {
                    Usuario atual = usuarioDAO.buscarPorId(usuario.getId());
                    if (atual == null)
                        throw new CadastroException("Usuário não encontrado para edição.");
                    usuario.setSenha(atual.getSenha());
                }
            }

            usuarioDAO.salvar(usuario);

        } catch (CadastroException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Erro ao salvar usuário:");
            e.printStackTrace();
            throw new NegocioException("Não foi possível salvar o usuário.", e);
        }
    }
    
    public void inativar(Integer id) throws NegocioException {
        try {
            if (id == null)
                throw new CadastroException("ID do usuário não informado.");

            Usuario usuario = usuarioDAO.buscarPorId(id);
            if (usuario == null)
                throw new CadastroException("Usuário não encontrado.");

            if (usuario.getStatus() == StatusUsuario.INATIVO)
                throw new CadastroException("Usuário já está inativo.");

            usuarioDAO.inativar(id);

        } catch (CadastroException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Erro ao inativar usuário:");
            e.printStackTrace();
            throw new NegocioException("Não foi possível inativar o usuário.", e);
        }
    }

    private void validarCamposObrigatorios(Usuario usuario,
                                           String senhaDigitada) throws CadastroException {
        if (usuario == null)
            throw new CadastroException("Usuário não informado.");

        if (usuario.getNome() == null || usuario.getNome().trim().isEmpty())
            throw new CadastroException("O nome é obrigatório.");

        if (usuario.getLogin() == null || usuario.getLogin().trim().isEmpty())
            throw new CadastroException("O login é obrigatório.");

        if (usuario.getLogin().length() < 3)
            throw new CadastroException("O login deve ter no mínimo 3 caracteres.");

        if (usuario.getPerfil() == null)
            throw new CadastroException("O perfil é obrigatório.");

        if (usuario.getStatus() == null)
            throw new CadastroException("O status é obrigatório.");

        // senha obrigatória apenas no cadastro
        if (usuario.getId() == null) {
            if (senhaDigitada == null || senhaDigitada.trim().isEmpty())
                throw new CadastroException("A senha é obrigatória no cadastro.");
            if (senhaDigitada.length() < 6)
                throw new CadastroException("A senha deve ter no mínimo 6 caracteres.");
        } else {
            // na edição, se informou senha, valida o tamanho
            if (senhaDigitada != null && !senhaDigitada.trim().isEmpty()
                    && senhaDigitada.length() < 6)
                throw new CadastroException("A nova senha deve ter no mínimo 6 caracteres.");
        }
    }
}