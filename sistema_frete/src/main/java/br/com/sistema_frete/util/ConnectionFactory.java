package br.com.sistema_frete.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String URL =
        "jdbc:postgresql://localhost:5432/sistema_gestao_frete";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1234";

    public Connection recuperarConexao() {

        try {
            // forçando carregamento do driver
            Class.forName("org.postgresql.Driver");

            System.out.println("Conexão ok.");
            return DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver do PostgreSQL não encontrado", e);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar no banco", e);
        }
    }
}