package br.com.sistema_frete.BO;

import br.com.sistema_frete.DAO.ClienteDAO;
import br.com.sistema_frete.DAO.UsuarioDAO;
import br.com.sistema_frete.enums.cliente.StatusCliente;
import br.com.sistema_frete.enums.usuario.PerfilUsuario;
import br.com.sistema_frete.enums.usuario.StatusUsuario;
import br.com.sistema_frete.exception.CadastroException;
import br.com.sistema_frete.exception.NegocioException;
import br.com.sistema_frete.model.Cliente;
import br.com.sistema_frete.model.Usuario;
import br.com.sistema_frete.util.ConnectionFactory;
import br.com.sistema_frete.util.HashUtil;
import br.com.sistema_frete.util.ValidadorCNPJ;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CadastroPublicoBO {

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public void cadastrar(Cliente cliente, Usuario usuario,
                          String senha, String confirmaSenha) throws NegocioException {
        Connection conn = null;
        try {

            if (cliente.getRazaoSocial() == null || cliente.getRazaoSocial().trim().isEmpty())
                throw new CadastroException("A razão social é obrigatória.");

            if (cliente.getCnpj() == null || cliente.getCnpj().trim().isEmpty())
                throw new CadastroException("O CNPJ é obrigatório.");

            if (!ValidadorCNPJ.isValid(cliente.getCnpj()))
                throw new CadastroException("O CNPJ informado é inválido.");

            if (clienteDAO.buscarPorCnpj(cliente.getCnpj()) != null)
                throw new CadastroException("Já existe uma empresa cadastrada com este CNPJ.");

            if (usuario.getNome() == null || usuario.getNome().trim().isEmpty())
                throw new CadastroException("O nome do responsável é obrigatório.");

            if (usuario.getLogin() == null || usuario.getLogin().trim().isEmpty())
                throw new CadastroException("O login é obrigatório.");

            if (usuario.getLogin().length() < 3)
                throw new CadastroException("O login deve ter no mínimo 3 caracteres.");

            if (usuarioDAO.buscarPorLoginExcluindoId(usuario.getLogin(), null) != null)
                throw new CadastroException("Este login já está em uso. Escolha outro.");

            if (senha == null || senha.trim().isEmpty())
                throw new CadastroException("A senha é obrigatória.");

            if (senha.length() < 6)
                throw new CadastroException("A senha deve ter no mínimo 6 caracteres.");

            if (!senha.equals(confirmaSenha))
                throw new CadastroException("A senha e a confirmação não conferem.");

            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            // 1. insere o cliente e obtém o ID gerado
            cliente.setStatus(StatusCliente.ATIVO);
            Integer clienteId = inserirClienteRetornandoId(cliente, conn);

            // 2. configura e insere o usuário vinculado ao cliente
            usuario.setPerfil(PerfilUsuario.CLIENTE);
            usuario.setStatus(StatusUsuario.ATIVO);
            usuario.setClienteId(clienteId);
            usuario.setSenha(HashUtil.hash(senha));
            inserirUsuario(usuario, conn);

            conn.commit();

        } catch (CadastroException e) {
            rollback(conn);
            throw e;
        } catch (Exception e) {
            rollback(conn);
            System.err.println("Erro ao realizar cadastro público:");
            e.printStackTrace();
            throw new NegocioException("Não foi possível concluir o cadastro. Tente novamente.", e);
        } finally {
            fechar(conn);
        }
    }

    private Integer inserirClienteRetornandoId(Cliente c, Connection conn) throws SQLException {
        String sql = "INSERT INTO cliente " +
                     "(razao_social, nome_fantasia, cnpj, inscricao_estadual, logradouro, " +
                     "numero, complemento, bairro, municipio, uf, cep, telefone, email, status) " +
                     "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?) RETURNING id";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,  c.getRazaoSocial());
            ps.setString(2,  c.getNomeFantasia());
            ps.setString(3,  c.getCnpj());
            ps.setString(4,  c.getInscricaoEstadual());
            ps.setString(5,  c.getLogradouro());
            ps.setString(6,  c.getNumero());
            ps.setString(7,  c.getComplemento());
            ps.setString(8,  c.getBairro());
            ps.setString(9,  c.getCidade());
            ps.setString(10, c.getUf());
            ps.setString(11, c.getCep());
            ps.setString(12, c.getTelefone());
            ps.setString(13, c.getEmail());
            ps.setString(14, c.getStatus().name());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
                throw new SQLException("Não foi possível obter o ID do cliente inserido.");
            }
        }
    }

    private void inserirUsuario(Usuario u, Connection conn) throws SQLException {
        String sql = "INSERT INTO usuario (nome, login, senha, perfil, status, cliente_id) " +
                     "VALUES (?,?,?,?,?,?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getNome());
            ps.setString(2, u.getLogin());
            ps.setString(3, u.getSenha());
            ps.setString(4, u.getPerfil().name());
            ps.setString(5, u.getStatus().name());
            ps.setInt(6,    u.getClienteId());
            ps.executeUpdate();
        }
    }

    private void rollback(Connection conn) {
        if (conn != null) try { conn.rollback(); } catch (Exception e) { e.printStackTrace(); }
    }

    private void fechar(Connection conn) {
        if (conn != null) try { conn.close(); } catch (Exception e) { e.printStackTrace(); }
    }
}