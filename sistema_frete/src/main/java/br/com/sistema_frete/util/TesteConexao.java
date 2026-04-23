package br.com.sistema_frete.util;

import java.sql.Connection;

public class TesteConexao {

    public static void main(String[] args) {

        ConnectionFactory factory = new ConnectionFactory();
        Connection conn = factory.recuperarConexao();

        if (conn != null) {
            System.out.println("Conectou com sucesso!");
        } else {
            System.out.println("Falha na conexão!");
        }
    }
}