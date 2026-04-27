package br.com.sistema_frete.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import br.com.sistema_frete.enums.veiculo.StatusVeiculo;
import br.com.sistema_frete.enums.veiculo.TipoVeiculo;
import br.com.sistema_frete.model.Veiculo;
import br.com.sistema_frete.util.ConnectionFactory;

public class VeiculoDAO {

	public List<Veiculo> buscarComPaginacao(String filtro, String statusFiltro, int offset, int limit) {
	    List<Veiculo> veiculos = new ArrayList<Veiculo>();

	    String sql = "SELECT id, placa, rntrc, ano_fabricacao, tipo, tara_kg, capacidade_kg, volume_m3, status " +
	                 "FROM veiculo " +
	                 "WHERE (placa ILIKE ? OR rntrc ILIKE ?) " +
	                 "AND (? IS NULL OR ? = '' OR status = ?) " +
	                 "ORDER BY placa ASC " +
	                 "LIMIT ? OFFSET ?";

	    try (Connection conn = ConnectionFactory.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        String filtroTratado = filtro == null ? "" : filtro.trim();
	        String filtroLike = "%" + filtroTratado + "%";

	        ps.setString(1, filtroLike);
	        ps.setString(2, filtroLike);
	        ps.setString(3, statusFiltro);
	        ps.setString(4, statusFiltro);
	        ps.setString(5, statusFiltro);
	        ps.setInt(6, limit);
	        ps.setInt(7, offset);

	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                veiculos.add(mapearVeiculo(rs));
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("Erro ao buscar veículos com paginação.", e);
	    }

	    return veiculos;
	}

	public int contarTotal(String filtro, String statusFiltro) {
	    String sql = "SELECT COUNT(*) AS total " +
	                 "FROM veiculo " +
	                 "WHERE (placa ILIKE ? OR rntrc ILIKE ?) " +
	                 "AND (? IS NULL OR ? = '' OR status = ?)";

	    try (Connection conn = ConnectionFactory.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        String filtroTratado = filtro == null ? "" : filtro.trim();
	        String filtroLike = "%" + filtroTratado + "%";

	        ps.setString(1, filtroLike);
	        ps.setString(2, filtroLike);
	        ps.setString(3, statusFiltro);
	        ps.setString(4, statusFiltro);
	        ps.setString(5, statusFiltro);

	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt("total");
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("Erro ao contar total de veículos.", e);
	    }

	    return 0;
	}

    public Veiculo buscarPorId(Integer id) {
        String sql = "SELECT id, placa, rntrc, ano_fabricacao, tipo, tara_kg, capacidade_kg, volume_m3, status " +
                     "FROM veiculo WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearVeiculo(rs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar veículo por ID.", e);
        }

        return null;
    }

    public Veiculo buscarPorPlaca(String placa) {
        String sql = "SELECT id, placa, rntrc, ano_fabricacao, tipo, tara_kg, capacidade_kg, volume_m3, status " +
                     "FROM veiculo WHERE placa = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, placa);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearVeiculo(rs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar veículo por placa.", e);
        }

        return null;
    }

    public void salvar(Veiculo veiculo) {
        if (veiculo.getId() == null) {
            inserir(veiculo);
        } else {
            atualizar(veiculo);
        }
    }

    private void inserir(Veiculo veiculo) {
        String sql = "INSERT INTO veiculo " +
                     "(placa, rntrc, ano_fabricacao, tipo, tara_kg, capacidade_kg, volume_m3, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            preencherPreparedStatement(veiculo, ps);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao inserir veículo.", e);
        }
    }

    private void atualizar(Veiculo veiculo) {
        String sql = "UPDATE veiculo SET " +
                     "placa = ?, rntrc = ?, ano_fabricacao = ?, tipo = ?, tara_kg = ?, capacidade_kg = ?, volume_m3 = ?, status = ? " +
                     "WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            preencherPreparedStatement(veiculo, ps);
            ps.setInt(9, veiculo.getId());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar veículo.", e);
        }
    }

    private void preencherPreparedStatement(Veiculo veiculo, PreparedStatement ps) throws Exception {
        ps.setString(1, veiculo.getPlaca());
        ps.setString(2, veiculo.getRntrc());
        ps.setInt(3, veiculo.getAnoFabricacao());
        ps.setString(4, veiculo.getTipo() != null ? veiculo.getTipo().name() : null);
        ps.setBigDecimal(5, veiculo.getTaraKg());
        ps.setBigDecimal(6, veiculo.getCapacidadeKg());
        ps.setBigDecimal(7, veiculo.getVolumeM3());
        ps.setString(8, veiculo.getStatus() != null ? veiculo.getStatus().name() : null);
    }

    private Veiculo mapearVeiculo(ResultSet rs) throws Exception {
        Veiculo veiculo = new Veiculo();

        veiculo.setId(rs.getInt("id"));
        veiculo.setPlaca(rs.getString("placa"));
        veiculo.setRntrc(rs.getString("rntrc"));
        veiculo.setAnoFabricacao(rs.getInt("ano_fabricacao"));
        veiculo.setTaraKg(rs.getBigDecimal("tara_kg"));
        veiculo.setCapacidadeKg(rs.getBigDecimal("capacidade_kg"));
        veiculo.setVolumeM3(rs.getBigDecimal("volume_m3"));

        String tipo = rs.getString("tipo");
        if (tipo != null && !tipo.trim().isEmpty()) {
            veiculo.setTipo(TipoVeiculo.valueOf(tipo));
        }

        String status = rs.getString("status");
        if (status != null && !status.trim().isEmpty()) {
            veiculo.setStatus(StatusVeiculo.valueOf(status));
        }

        return veiculo;
    }
    
    public void inativar(Integer id) {
        String sql = "UPDATE veiculo SET status = 'INATIVO' WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao inativar veículo.", e);
        }
    }
    
}