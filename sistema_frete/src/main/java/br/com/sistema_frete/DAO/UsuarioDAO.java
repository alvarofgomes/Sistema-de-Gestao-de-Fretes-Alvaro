package br.com.sistema_frete.DAO;

import br.com.sistema_frete.enums.usuario.PerfilUsuario;
import br.com.sistema_frete.enums.usuario.StatusUsuario;
import br.com.sistema_frete.model.Usuario;
import br.com.sistema_frete.util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public Usuario buscarPorLogin(String login) {
        String sql = "SELECT u.id, u.nome, u.login, u.senha, u.perfil, u.status, " +
                     "u.cliente_id, c.razao_social AS nome_cliente " +
                     "FROM usuario u " +
                     "LEFT JOIN cliente c ON c.id = u.cliente_id " +
                     "WHERE u.login = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, login);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar usuário por login.", e);
        }
        return null;
    }

    public Usuario buscarPorId(Integer id) {
        String sql = "SELECT u.id, u.nome, u.login, u.senha, u.perfil, u.status, " +
                     "u.cliente_id, c.razao_social AS nome_cliente " +
                     "FROM usuario u " +
                     "LEFT JOIN cliente c ON c.id = u.cliente_id " +
                     "WHERE u.id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar usuário por ID.", e);
        }
        return null;
    }

    public Usuario buscarPorLoginExcluindoId(String login, Integer idExcluir) {
        String sql = "SELECT id FROM usuario WHERE login = ? AND id != ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setInt(2, idExcluir != null ? idExcluir : -1);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("id"));
                    return u;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao verificar login duplicado.", e);
        }
        return null;
    }

    public List<Usuario> buscarComPaginacao(String filtro, int offset, int limit) {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT u.id, u.nome, u.login, u.senha, u.perfil, u.status, " +
                     "u.cliente_id, c.razao_social AS nome_cliente " +
                     "FROM usuario u " +
                     "LEFT JOIN cliente c ON c.id = u.cliente_id " +
                     "WHERE u.nome ILIKE ? OR u.login ILIKE ? " +
                     "ORDER BY u.nome ASC LIMIT ? OFFSET ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String like = "%" + (filtro == null ? "" : filtro.trim()) + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setInt(3, limit);
            ps.setInt(4, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao listar usuários.", e);
        }
        return lista;
    }

    public int contarTotal(String filtro) {
        String sql = "SELECT COUNT(*) FROM usuario u " +
                     "WHERE u.nome ILIKE ? OR u.login ILIKE ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String like = "%" + (filtro == null ? "" : filtro.trim()) + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao contar usuários.", e);
        }
    }

    public void salvar(Usuario usuario) {
        if (usuario.getId() == null) inserir(usuario);
        else                         atualizar(usuario);
    }

    private void inserir(Usuario usuario) {
        String sql = "INSERT INTO usuario (nome, login, senha, perfil, status, cliente_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        System.out.println("sql usuario" + sql); // Debug: Verificar a consulta SQL
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            preencherPs(usuario, ps);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao inserir usuário.", e);
        }
    }

    private void atualizar(Usuario usuario) {
        String sql = "UPDATE usuario SET nome=?, login=?, senha=?, perfil=?, status=?, " +
                     "cliente_id=? WHERE id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            preencherPs(usuario, ps);
            ps.setInt(7, usuario.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar usuário.", e);
        }
    }

    private void preencherPs(Usuario u, PreparedStatement ps) throws SQLException {
        ps.setString(1, u.getNome());
        ps.setString(2, u.getLogin());
        ps.setString(3, u.getSenha());
        ps.setString(4, u.getPerfil() != null ? u.getPerfil().name() : null);
        ps.setString(5, u.getStatus() != null ? u.getStatus().name() : null);
        if (u.getClienteId() != null) ps.setInt(6, u.getClienteId());
        else                          ps.setNull(6, Types.INTEGER);
    }

    public void inativar(Integer id) {
        String sql = "UPDATE usuario SET status = 'INATIVO' WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao inativar usuário.", e);
        }
    }

    public boolean clientePossuiUsuario(Integer clienteId) {
        String sql = "SELECT 1 FROM usuario WHERE cliente_id = ? LIMIT 1";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao verificar usuário do cliente.", e);
        }
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id"));
        u.setNome(rs.getString("nome"));
        u.setLogin(rs.getString("login"));
        u.setSenha(rs.getString("senha"));

        String perfil = rs.getString("perfil");
        if (perfil != null) u.setPerfil(PerfilUsuario.valueOf(perfil));

        String status = rs.getString("status");
        if (status != null) u.setStatus(StatusUsuario.valueOf(status));

        int clienteId = rs.getInt("cliente_id");
        if (!rs.wasNull()) u.setClienteId(clienteId);

        u.setNomeCliente(rs.getString("nome_cliente"));
        return u;
    }
}