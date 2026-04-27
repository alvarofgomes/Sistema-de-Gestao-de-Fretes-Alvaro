package br.com.sistema_frete.DAO;

import br.com.sistema_frete.enums.motorista.CategoriaCNH;
import br.com.sistema_frete.enums.motorista.StatusMotorista;
import br.com.sistema_frete.enums.motorista.TipoVinculo;
import br.com.sistema_frete.model.Motorista;
import br.com.sistema_frete.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MotoristaDAO {

    public Motorista buscarPorId(Integer id) {
        String sql = "SELECT id, nome, cpf, data_nascimento, telefone, cnh_numero, " +
                     "cnh_categoria, cnh_validade, tipo_vinculo, status " +
                     "FROM motorista WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearMotorista(rs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar motorista por ID.", e);
        }

        return null;
    }

    public Motorista buscarPorCpf(String cpf) {
        String sql = "SELECT id, nome, cpf, data_nascimento, telefone, cnh_numero, " +
                     "cnh_categoria, cnh_validade, tipo_vinculo, status " +
                     "FROM motorista WHERE cpf = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cpf);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearMotorista(rs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar motorista por CPF.", e);
        }

        return null;
    }

    public List<Motorista> buscarComPaginacao(String filtro, int offset, int limit) {
        List<Motorista> motoristas = new ArrayList<Motorista>();

        String sql = "SELECT id, nome, cpf, telefone, cnh_numero, cnh_categoria, cnh_validade, tipo_vinculo, status " +
                     "FROM motorista " +
                     "WHERE nome ILIKE ? " +
                     "ORDER BY nome ASC " +
                     "LIMIT ? OFFSET ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String filtroTratado = (filtro == null) ? "" : filtro.trim();
            String filtroLike = "%" + filtroTratado + "%";

            ps.setString(1, filtroLike);
            ps.setInt(2, limit);
            ps.setInt(3, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    motoristas.add(mapearMotorista(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar motoristas com paginação.", e);
        }

        return motoristas;
    }

    public int contarTotal(String filtro) {
        String sql = "SELECT COUNT(*) AS total FROM motorista WHERE nome ILIKE ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String filtroTratado = (filtro == null) ? "" : filtro.trim();
            String filtroLike = "%" + filtroTratado + "%";

            ps.setString(1, filtroLike);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao contar total de motoristas.", e);
        }

        return 0;
    }

    public void salvar(Motorista motorista) {
        if (motorista.getId() == null) {
            inserir(motorista);
        } else {
            atualizar(motorista);
        }
    }

    private void inserir(Motorista motorista) {
        String sql = "INSERT INTO motorista " +
                     "(nome, cpf, data_nascimento, telefone, cnh_numero, cnh_categoria, cnh_validade, tipo_vinculo, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            preencherPreparedStatement(motorista, ps);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao inserir motorista.", e);
        }
    }

    private void atualizar(Motorista motorista) {
        String sql = "UPDATE motorista SET " +
                     "nome = ?, cpf = ?, data_nascimento = ?, telefone = ?, cnh_numero = ?, " +
                     "cnh_categoria = ?, cnh_validade = ?, tipo_vinculo = ?, status = ? " +
                     "WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            preencherPreparedStatement(motorista, ps);
            ps.setInt(10, motorista.getId());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar motorista.", e);
        }
    }

    private void preencherPreparedStatement(Motorista motorista, PreparedStatement ps) throws Exception {
        ps.setString(1, motorista.getNome());
        ps.setString(2, motorista.getCpf());
        ps.setDate(3, motorista.getDataNascimento() != null ? Date.valueOf(motorista.getDataNascimento()) : null);
        ps.setString(4, motorista.getTelefone());
        ps.setString(5, motorista.getCnhNumero());
        ps.setString(6, motorista.getCnhCategoria() != null ? motorista.getCnhCategoria().name() : null);
        ps.setDate(7, motorista.getCnhValidade() != null ? Date.valueOf(motorista.getCnhValidade()) : null);
        ps.setString(8, motorista.getTipoVinculo() != null ? motorista.getTipoVinculo().name() : null);
        ps.setString(9, motorista.getStatus() != null ? motorista.getStatus().name() : null);
    }

    private Motorista mapearMotorista(ResultSet rs) throws Exception {
        Motorista motorista = new Motorista();

        motorista.setId(rs.getInt("id"));
        motorista.setNome(rs.getString("nome"));
        motorista.setCpf(rs.getString("cpf"));
        motorista.setTelefone(rs.getString("telefone"));
        motorista.setCnhNumero(rs.getString("cnh_numero"));

        Date dataNascimento = null;
        try {
            dataNascimento = rs.getDate("data_nascimento");
        } catch (Exception e) {
        }
        if (dataNascimento != null) {
            motorista.setDataNascimento(dataNascimento.toLocalDate());
        }

        String categoria = rs.getString("cnh_categoria");
        if (categoria != null && !categoria.trim().isEmpty()) {
            motorista.setCnhCategoria(CategoriaCNH.valueOf(categoria));
        }

        Date cnhValidade = rs.getDate("cnh_validade");
        if (cnhValidade != null) {
            motorista.setCnhValidade(cnhValidade.toLocalDate());
        }

        String tipoVinculo = rs.getString("tipo_vinculo");
        if (tipoVinculo != null && !tipoVinculo.trim().isEmpty()) {
            motorista.setTipoVinculo(TipoVinculo.valueOf(tipoVinculo));
        }

        String status = rs.getString("status");
        if (status != null && !status.trim().isEmpty()) {
            motorista.setStatus(StatusMotorista.valueOf(status));
        }

        return motorista;
    }
    
    public void inativar(Long id) {
        String sql = "UPDATE motorista SET status = 'INATIVO' WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao inativar motorista.", e);
        }
    }
}