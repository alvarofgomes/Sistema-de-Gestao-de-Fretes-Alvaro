package br.com.sistema_frete.DAO;

import br.com.sistema_frete.enums.cliente.StatusCliente;
import br.com.sistema_frete.model.Cliente;
import br.com.sistema_frete.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public List<Cliente> buscarComPaginacao(String filtro, int offset, int limit) {
        List<Cliente> clientes = new ArrayList<Cliente>();

        String sql = "SELECT id, razao_social, nome_fantasia, cnpj, status " +
                     "FROM cliente " +
                     "WHERE razao_social ILIKE ? OR nome_fantasia ILIKE ? " +
                     "ORDER BY razao_social ASC " +
                     "LIMIT ? OFFSET ?";

        try (Connection conn = new ConnectionFactory().recuperarConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String filtroTratado = (filtro == null) ? "" : filtro.trim();
            String filtroLike = "%" + filtroTratado + "%";

            ps.setString(1, filtroLike);
            ps.setString(2, filtroLike);
            ps.setInt(3, limit);
            ps.setInt(4, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cliente cliente = new Cliente();

                    cliente.setId(rs.getInt("id"));
                    cliente.setRazaoSocial(rs.getString("razao_social"));
                    cliente.setNomeFantasia(rs.getString("nome_fantasia"));
                    cliente.setCnpj(rs.getString("cnpj"));

                    String status = rs.getString("status");
                    if (status != null && !status.trim().isEmpty()) {
                        cliente.setStatus(StatusCliente.valueOf(status));
                    }

                    clientes.add(cliente);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar clientes com paginação.", e);
        }

        return clientes;
    }

    public int contarTotal(String filtro) {
        String sql = "SELECT COUNT(*) AS total " +
                     "FROM cliente " +
                     "WHERE razao_social ILIKE ? OR nome_fantasia ILIKE ?";

        try (Connection conn = new ConnectionFactory().recuperarConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String filtroTratado = (filtro == null) ? "" : filtro.trim();
            String filtroLike = "%" + filtroTratado + "%";

            ps.setString(1, filtroLike);
            ps.setString(2, filtroLike);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao contar total de clientes.", e);
        }

        return 0;
    }

    public Cliente buscarPorCnpj(String cnpj) {
        String sql = "SELECT id, razao_social, nome_fantasia, cnpj, status " +
                     "FROM cliente " +
                     "WHERE cnpj = ?";

        try (Connection conn = new ConnectionFactory().recuperarConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cnpj);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Cliente cliente = new Cliente();
                    cliente.setId(rs.getInt("id"));
                    cliente.setRazaoSocial(rs.getString("razao_social"));
                    cliente.setNomeFantasia(rs.getString("nome_fantasia"));
                    cliente.setCnpj(rs.getString("cnpj"));

                    String status = rs.getString("status");
                    if (status != null && !status.trim().isEmpty()) {
                        cliente.setStatus(StatusCliente.valueOf(status));
                    }

                    return cliente;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar cliente por CNPJ.", e);
        }

        return null;
    }

    public Cliente buscarPorId(Integer id) {
        String sql = "SELECT id, razao_social, nome_fantasia, cnpj, status " +
                     "FROM cliente " +
                     "WHERE id = ?";

        try (Connection conn = new ConnectionFactory().recuperarConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Cliente cliente = new Cliente();
                    cliente.setId(rs.getInt("id"));
                    cliente.setRazaoSocial(rs.getString("razao_social"));
                    cliente.setNomeFantasia(rs.getString("nome_fantasia"));
                    cliente.setCnpj(rs.getString("cnpj"));

                    String status = rs.getString("status");
                    if (status != null && !status.trim().isEmpty()) {
                        cliente.setStatus(StatusCliente.valueOf(status));
                    }

                    return cliente;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar cliente por ID.", e);
        }

        return null;
    }

    public void salvar(Cliente cliente) {
        if (cliente.getId() == null) {
            inserir(cliente);
        } else {
            atualizar(cliente);
        }
    }
    
    public void excluir(Long id) {
        String sql = "DELETE FROM cliente WHERE id = ?";

        try (Connection conn = new ConnectionFactory().recuperarConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao excluir cliente.", e);
        }
    }

    private void inserir(Cliente cliente) {
        String sql = "INSERT INTO cliente " +
                     "(razao_social, nome_fantasia, cnpj, inscricao_estadual, tipo, logradouro, numero, complemento, bairro, municipio, uf, cep, telefone, email, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = new ConnectionFactory().recuperarConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            preencherPreparedStatement(cliente, ps);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao inserir cliente.", e);
        }
    }

    private void atualizar(Cliente cliente) {
        String sql = "UPDATE cliente SET " +
                     "razao_social = ?, nome_fantasia = ?, cnpj = ?, inscricao_estadual = ?, tipo = ?, " +
                     "logradouro = ?, numero = ?, complemento = ?, bairro = ?, municipio = ?, uf = ?, cep = ?, " +
                     "telefone = ?, email = ?, status = ? " +
                     "WHERE id = ?";

        try (Connection conn = new ConnectionFactory().recuperarConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            preencherPreparedStatement(cliente, ps);
            ps.setInt(16, cliente.getId());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar cliente.", e);
        }
    }

    private void preencherPreparedStatement(Cliente cliente, PreparedStatement ps) throws Exception {
        ps.setString(1, cliente.getRazaoSocial());
        ps.setString(2, cliente.getNomeFantasia());
        ps.setString(3, cliente.getCnpj());
        ps.setString(4, cliente.getInscricaoEstadual());
        ps.setString(5, cliente.getTipo() != null ? cliente.getTipo().name() : null);
        ps.setString(6, cliente.getLogradouro());
        ps.setString(7, cliente.getNumero());
        ps.setString(8, cliente.getComplemento());
        ps.setString(9, cliente.getBairro());
        ps.setString(10, cliente.getMunicipio());
        ps.setString(11, cliente.getUf());
        ps.setString(12, cliente.getCep());
        ps.setString(13, cliente.getTelefone());
        ps.setString(14, cliente.getEmail());
        ps.setString(15, cliente.getStatus() != null ? cliente.getStatus().name() : null);
    }
}