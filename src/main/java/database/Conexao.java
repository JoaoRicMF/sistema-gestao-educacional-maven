package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    private static final String HOST = "db.xxvuzcrbsnzdhogyihkm.supabase.co";
    private static final String DATABASE = "postgres";
    private static final String PORT = "5432";
    private static final String USER = "postgres";
    private static final String PASSWORD = "u5EJrosqbjuz2YOm";

    private static final String URL = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DATABASE;

    private static Connection conexao = null;

    //Obtém uma conexão com o banco de dados.Se a conexão ainda não foi estabelecida, uma nova será criada.
    public static Connection getConexao() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
