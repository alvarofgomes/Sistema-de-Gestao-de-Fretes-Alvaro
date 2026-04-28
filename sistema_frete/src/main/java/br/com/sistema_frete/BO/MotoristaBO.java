package br.com.sistema_frete.BO;

import br.com.sistema_frete.DAO.FreteDAO;
import br.com.sistema_frete.DAO.MotoristaDAO;
import br.com.sistema_frete.exception.CadastroException;
import br.com.sistema_frete.exception.NegocioException;
import br.com.sistema_frete.model.Motorista;
import br.com.sistema_frete.util.ValidadorCPF;

import java.time.LocalDate;
import java.util.List;

public class MotoristaBO {

    private final MotoristaDAO motoristaDAO = new MotoristaDAO();
    private final FreteDAO freteDAO = new FreteDAO();

    public void salvar(Motorista motorista) throws NegocioException {
        try {
            if (motorista == null) {
                throw new CadastroException("Motorista não informado.");
            }

            if (motorista.getCpf() == null || motorista.getCpf().trim().isEmpty()) {
                throw new CadastroException("CPF inválido");
            }

            if (!ValidadorCPF.isValid(motorista.getCpf())) {
                throw new CadastroException("CPF inválido");
            }

            if (motorista.getId() != null) {
                Motorista motoristaExistente = motoristaDAO.buscarPorId(motorista.getId());

                if (motoristaExistente == null) {
                    throw new CadastroException("Motorista não encontrado para edição.");
                }

                Motorista motoristaMesmoCpf = motoristaDAO.buscarPorCpf(motorista.getCpf());
                if (motoristaMesmoCpf != null && !motoristaMesmoCpf.getId().equals(motorista.getId())) {
                    throw new CadastroException("Já existe motorista cadastrado com este CPF.");
                }
            } else {
                Motorista motoristaDuplicado = motoristaDAO.buscarPorCpf(motorista.getCpf());
                if (motoristaDuplicado != null) {
                    throw new CadastroException("Já existe motorista cadastrado com este CPF.");
                }
            }

            motoristaDAO.salvar(motorista);

        } catch (CadastroException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Erro ao salvar motorista:");
            e.printStackTrace();
            throw new NegocioException("Não foi possível salvar o motorista.", e);
        }
    }

    public Motorista buscarPorId(Integer id) throws NegocioException {
        try {
            if (id == null) {
                throw new CadastroException("ID do motorista não informado.");
            }

            Motorista motorista = motoristaDAO.buscarPorId(id);

            if (motorista == null) {
                throw new CadastroException("Motorista não encontrado.");
            }

            return motorista;

        } catch (CadastroException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Erro ao buscar motorista por ID:");
            e.printStackTrace();
            throw new NegocioException("Não foi possível buscar o motorista.", e);
        }
    }
    
    public boolean isMotoristaAptoParaFrete(Motorista motorista) {
        if (motorista == null) {
            return false;
        }

        if (motorista.getCnhValidade() == null) {
            return false;
        }

        return !isCnhVencida(motorista);
    }

    private boolean isCnhVencida(Motorista motorista) {
        LocalDate validade = motorista.getCnhValidade();
        return validade != null && validade.isBefore(LocalDate.now());
    }
    
    public List<Motorista> listarAtivos() throws NegocioException {
        try {
            return motoristaDAO.buscarAtivos();
        } catch (Exception e) {
            System.err.println("Erro ao listar motoristas ativos:");
            e.printStackTrace();
            throw new NegocioException("Não foi possível listar os motoristas.", e);
        }
    }

    public List<Motorista> listarComPaginacao(String filtro, int pagina, int registrosPorPagina) throws NegocioException {
        try {
            int offset = (pagina - 1) * registrosPorPagina;
            int limit = registrosPorPagina;

            return motoristaDAO.buscarComPaginacao(filtro, offset, limit);
        } catch (Exception e) {
            System.err.println("Erro ao listar motoristas:");
            e.printStackTrace();
            throw new NegocioException("Não foi possível listar os motoristas.", e);
        }
    }

    public int contarTotal(String filtro) throws NegocioException {
        try {
            return motoristaDAO.contarTotal(filtro);
        } catch (Exception e) {
            System.err.println("Erro ao contar total de motoristas:");
            e.printStackTrace();
            throw new NegocioException("Não foi possível obter o total de motoristas.", e);
        }
    }

    public void inativar(Long id) throws NegocioException {
        try {
            if (id == null) {
                throw new CadastroException("ID do motorista não informado.");
            }

            boolean possuiFreteAtivo = freteDAO.buscarFretesAtivosPorMotorista(id);

            if (possuiFreteAtivo) {
                throw new CadastroException("Motorista não pode ser inativado: possui fretes ativos.");
            }

            motoristaDAO.inativar(id);

        } catch (CadastroException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Erro ao inativar motorista:");
            e.printStackTrace();
            throw new NegocioException("Não foi possível inativar o motorista.", e);
        }
    }
}