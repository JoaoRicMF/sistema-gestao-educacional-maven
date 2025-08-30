package database;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author João Ricardo
 */
public class TesteConexao {
    public void testarBDB(){
        // Tenta obter uma conexão do banco de dados
        Connection conn = Conexao.getConexao();

        // Verifica se a conexão foi estabelecida
        if (conn != null) {
            System.out.println("Teste de Conexão bem-sucedido!");
            System.out.println("O programa conseguiu se conectar ao banco de dados.");

            try {
                conn.close();
                System.out.println("Conexão de teste fechada.");
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão de teste:");
                e.printStackTrace();
            }

        } else {
            System.err.println("Falha no teste de Conexão.");
            System.err.println("Verifique suas credenciais no arquivo Conexao.java");
            System.err.println("e certifique-se de que o IP da sua máquina está liberado no Supabase, se necessário.");
        }
    }
}
