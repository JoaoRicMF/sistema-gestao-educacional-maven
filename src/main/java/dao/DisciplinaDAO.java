package dao;

// Imports do projeto (gui, modelo, service, dao)
import modelo.Disciplina;

//Imports do java
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//Imports do DAO
import database.Conexao;

/**
 *
 * @author Rhwan
 */
public class DisciplinaDAO {
    public void salvar(Disciplina disciplina) {
        String sql = "INSERT INTO disciplina (nome_disciplina, carga_horaria, serie_semestre) VALUES (?, ?, ?)";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, disciplina.getNomeDisciplina());
            pstmt.setInt(2, disciplina.getCargaHoraria());
            pstmt.setInt(3, disciplina.getSerieSemestre());
            pstmt.executeUpdate();

            // Pega o ID gerado pelo banco e atribui ao objeto
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                disciplina.setId(generatedKeys.getInt(1));
            }
            System.out.println("Disciplina salva com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao salvar disciplina: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Disciplina> listarTodas() {
        String sql = "SELECT * FROM disciplina";
        List<Disciplina> disciplinas = new ArrayList<>();
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Disciplina disciplina = new Disciplina(
                        rs.getString("nome_disciplina"),
                        rs.getInt("carga_horaria"),
                        rs.getInt("serie_semestre")
                );
                disciplina.setId(rs.getInt("id"));
                disciplinas.add(disciplina);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar disciplinas: " + e.getMessage());
            e.printStackTrace();
        }
        return disciplinas;
    }

    public void atualizar(Disciplina disciplina) {
        String sql = "UPDATE disciplina SET nome_disciplina = ?, carga_horaria = ?, serie_semestre = ? WHERE id = ?";
        try (Connection conn = Conexao.getConexao(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, disciplina.getNomeDisciplina());
            pstmt.setInt(2, disciplina.getCargaHoraria());
            pstmt.setInt(3, disciplina.getSerieSemestre());
            pstmt.setInt(4, disciplina.getId());
            pstmt.executeUpdate();
            System.out.println("Disciplina atualizada com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar disciplina: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM disciplina WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Disciplina excluída com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao excluir disciplina: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
