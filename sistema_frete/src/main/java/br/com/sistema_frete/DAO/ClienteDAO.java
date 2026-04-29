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
        List<Cliente> clientes = new ArrayList<>();

        String sql = "SELECT id, razao_social, nome_fantasia, cnpj, status " +
                     "FROM cliente " +
                     "WHERE razao_social ILIKE ? OR nome_fantasia ILIKE ? " +
                     "ORDER BY razao_social ASC " +
                     "LIMIT ? OFFSET ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String filtroLike = "%" + (filtro == null ? "" : filtro.trim()) + "%";
            ps.setString(1, filtroLike);
            ps.setString(2, filtroLike);
            ps.setInt(3, limit);
            ps.setInt(4, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cliente c = new Cliente();
                    c.setId(rs.getInt("id"));
                    c.setRazaoSocial(rs.getString("razao_social"));
                    c.setNomeFantasia(rs.getString("nome_fantasia"));
                    c.setCnpj(rs.getString("cnpj"));
                    String status = rs.getString("status");
                    if (status != null) c.setStatus(StatusCliente.valueOf(status));
                    clientes.add(c);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar clientes com paginação.", e);
        }

        return clientes;
    }

    public int contarTotal(String filtro) {
        String sql = "SELECT COUNT(*) AS total FROM cliente " +
                     "WHERE razao_social ILIKE ? OR nome_fantasia ILIKE ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String filtroLike = "%" + (filtro == null ? "" : filtro.trim()) + "%";
            ps.setString(1, filtroLike);
            ps.setString(2, filtroLike);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("total");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao contar clientes.", e);
        }

        return 0;
    }

    public Cliente buscarPorCnpj(String cnpj) {
        String sql = "SELECT id, razao_social, nome_fantasia, cnpj, status " +
                     "FROM cliente WHERE cnpj = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cnpj);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Cliente c = new Cliente();
                    c.setId(rs.getInt("id"));
                    c.setRazaoSocial(rs.getString("razao_social"));
                    c.setNomeFantasia(rs.getString("nome_fantasia"));
                    c.setCnpj(rs.getString("cnpj"));
                    String status = rs.getString("status");
                    if (status != null) c.setStatus(StatusCliente.valueOf(status));
                    return c;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar cliente por CNPJ.", e);
        }

        return null;
    }

    public Cliente buscarPorId(Integer id) {
        String sql = "SELECT id, razao_social, nome_fantasia, cnpj, inscricao_estadual, " +
                     "logradouro, numero, complemento, bairro, cidade, uf, cep, " +
                     "telefone, email, status FROM cliente WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Cliente c = new Cliente();
                    c.setId(rs.getInt("id"));
                    c.setRazaoSocial(rs.getString("razao_social"));
                    c.setNomeFantasia(rs.getString("nome_fantasia"));
                    c.setCnpj(rs.getString("cnpj"));
                    c.setInscricaoEstadual(rs.getString("inscricao_estadual"));
                    c.setLogradouro(rs.getString("logradouro"));
                    c.setNumero(rs.getString("numero"));
                    c.setComplemento(rs.getString("complemento"));
                    c.setBairro(rs.getString("bairro"));
                    c.setCidade(rs.getString("cidade"));
                    c.setUf(rs.getString("uf"));
                    c.setCep(rs.getString("cep"));
                    c.setTelefone(rs.getString("telefone"));
                    c.setEmail(rs.getString("email"));
                    String status = rs.getString("status");
                    if (status != null) c.setStatus(StatusCliente.valueOf(status));
                    return c;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar cliente por ID.", e);
        }

        return null;
    }

    //Método extra útil para o FreteBO (buscar todos para popular o select)
    public List<Cliente> buscarTodos() {
        List<Cliente> clientes = new ArrayList<>();

        String sql = "SELECT id, razao_social, nome_fantasia, cnpj, status " +
                     "FROM cliente WHERE status = 'ATIVO' ORDER BY razao_social ASC";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cliente c = new Cliente();
                    c.setId(rs.getInt("id"));
                    c.setRazaoSocial(rs.getString("razao_social"));
                    c.setNomeFantasia(rs.getString("nome_fantasia"));
                    c.setCnpj(rs.getString("cnpj"));
                    String status = rs.getString("status");
                    if (status != null) c.setStatus(StatusCliente.valueOf(status));
                    clientes.add(c);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar todos os clientes.", e);
        }

        return clientes;
    }

    public void salvar(Cliente cliente) {
        if (cliente.getId() == null) inserir(cliente);
        else atualizar(cliente);
    }

    public void excluir(Long id) {
        String sql = "DELETE FROM cliente WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
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
                     "(razao_social, nome_fantasia, cnpj, inscricao_estadual, logradouro, " +
                     "numero, complemento, bairro, cidade, uf, cep, telefone, email, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            preencherPs(cliente, ps);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao inserir cliente.", e);
        }
    }

    private void atualizar(Cliente cliente) {
        String sql = "UPDATE cliente SET " +
                     "razao_social=?, nome_fantasia=?, cnpj=?, inscricao_estadual=?, logradouro=?, " +
                     "numero=?, complemento=?, bairro=?, cidade=?, uf=?, cep=?, " +
                     "telefone=?, email=?, status=? WHERE id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            preencherPs(cliente, ps);
            ps.setInt(15, cliente.getId());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar cliente.", e);
        }
    }

    private void preencherPs(Cliente c, PreparedStatement ps) throws Exception {
        ps.setString(1, c.getRazaoSocial());
        ps.setString(2, c.getNomeFantasia());
        ps.setString(3, c.getCnpj());
        ps.setString(4, c.getInscricaoEstadual());
        ps.setString(5, c.getLogradouro());
        ps.setString(6, c.getNumero());
        ps.setString(7, c.getComplemento());
        ps.setString(8, c.getBairro());
        ps.setString(9, c.getCidade());
        ps.setString(10, c.getUf());
        ps.setString(11, c.getCep());
        ps.setString(12, c.getTelefone());
        ps.setString(13, c.getEmail());
        ps.setString(14, c.getStatus() != null ? c.getStatus().name() : null);
    }
}