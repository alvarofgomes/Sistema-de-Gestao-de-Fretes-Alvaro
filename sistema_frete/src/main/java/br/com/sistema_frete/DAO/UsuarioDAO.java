package br.com.sistema_frete.DAO;

import br.com.sistema_frete.enums.usuario.StatusUsuario;
import br.com.sistema_frete.model.Usuario;
import br.com.sistema_frete.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsuarioDAO {

    public Usuario buscarPorLogin(String login) {
        String sql = "SELECT id, nome, login, senha, perfil, status " +
                     "FROM usuario " +
                     "WHERE login = ?";

        try (Connection conn = new ConnectionFactory().recuperarConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, login);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario();

                    usuario.setId(rs.getInt("id"));
                    usuario.setNome(rs.getString("nome"));
                    usuario.setLogin(rs.getString("login"));
                    usuario.setSenha(rs.getString("senha"));
                    usuario.setPerfil(rs.getString("perfil"));

                    String status = rs.getString("status");
                    if (status != null && !status.trim().isEmpty()) {
                        usuario.setStatus(StatusUsuario.valueOf(status));
                    }

                    return usuario;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar usuário por login.", e);
        }

        return null;
    }
}