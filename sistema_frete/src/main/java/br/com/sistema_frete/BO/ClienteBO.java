package br.com.sistema_frete.BO;

import br.com.sistema_frete.DAO.ClienteDAO;
import br.com.sistema_frete.DAO.FreteDAO;
import br.com.sistema_frete.exception.CadastroException;
import br.com.sistema_frete.exception.NegocioException;
import br.com.sistema_frete.model.Cliente;
import br.com.sistema_frete.util.ValidadorCNPJ;

import java.util.List;

public class ClienteBO {

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final FreteDAO freteDAO = new FreteDAO();

    public List<Cliente> listarTodos() throws NegocioException {
        try {
            return clienteDAO.buscarTodos();
        } catch (Exception e) {
            System.err.println("Erro ao listar todos os clientes:");
            e.printStackTrace();
            throw new NegocioException("Não foi possível listar os clientes.", e);
        }
    }
    
    public List<Cliente> listarComPaginacao(String filtro, int pagina, int registrosPorPagina) throws NegocioException {
        try {
            int offset = (pagina - 1) * registrosPorPagina;
            int limit = registrosPorPagina;

            return clienteDAO.buscarComPaginacao(filtro, offset, limit);
        } catch (Exception e) {
            System.err.println("Erro ao listar clientes com paginação:");
            e.printStackTrace();
            throw new NegocioException("Não foi possível listar os clientes.", e);
        }
    }

    public int contarTotal(String filtro) throws NegocioException {
        try {
            return clienteDAO.contarTotal(filtro);
        } catch (Exception e) {
            System.err.println("Erro ao contar total de clientes:");
            e.printStackTrace();
            throw new NegocioException("Não foi possível obter o total de clientes.", e);
        }
    }
    
    public Cliente buscarPorId(Integer id) throws NegocioException {
        try {
            if (id == null) {
                throw new CadastroException("ID do cliente não informado.");
            }

            Cliente cliente = clienteDAO.buscarPorId(id);

            if (cliente == null) {
                throw new CadastroException("Cliente não encontrado.");
            }

            return cliente;

        } catch (CadastroException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Erro ao buscar cliente por ID:");
            e.printStackTrace();
            throw new NegocioException("Não foi possível buscar o cliente.", e);
        }
    }

    public void salvar(Cliente cliente) throws NegocioException {
        try {
            if (cliente == null) {
                throw new CadastroException("Cliente não informado.");
            }

            if (cliente.getCnpj() == null || cliente.getCnpj().trim().isEmpty()) {
                throw new CadastroException("CNPJ inválido");
            }

            if (!ValidadorCNPJ.isValid(cliente.getCnpj())) {
                throw new CadastroException("CNPJ inválido");
            }

            if (cliente.getId() != null) {
                Cliente clienteExistente = clienteDAO.buscarPorId(cliente.getId());

                if (clienteExistente == null) {
                    throw new CadastroException("Cliente não encontrado para edição.");
                }

                Cliente clienteMesmoCnpj = clienteDAO.buscarPorCnpj(cliente.getCnpj());
                if (clienteMesmoCnpj != null && !clienteMesmoCnpj.getId().equals(cliente.getId())) {
                    throw new CadastroException("Já existe cliente cadastrado com este CNPJ.");
                }
            } else {
                Cliente clienteDuplicado = clienteDAO.buscarPorCnpj(cliente.getCnpj());
                if (clienteDuplicado != null) {
                    throw new CadastroException("Já existe cliente cadastrado com este CNPJ.");
                }
            }

            clienteDAO.salvar(cliente);

        } catch (CadastroException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Erro ao salvar cliente:");
            e.printStackTrace();
            throw new NegocioException("Não foi possível salvar o cliente.", e);
        }
    }

    public void excluir(Long id) throws NegocioException {
        try {
            if (id == null) {
                throw new CadastroException("ID do cliente não informado.");
            }

            int totalFretes = freteDAO.contarFretesPorCliente(id);

            if (totalFretes > 0) {
                throw new CadastroException("Não é possível excluir: Cliente possui fretes vinculados.");
            }

            clienteDAO.excluir(id);

        } catch (CadastroException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Erro ao excluir cliente:");
            e.printStackTrace();
            throw new NegocioException("Não foi possível excluir o cliente.", e);
        }
    }
}