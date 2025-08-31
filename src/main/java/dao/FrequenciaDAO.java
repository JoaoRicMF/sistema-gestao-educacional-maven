package dao;

// Imports do projeto (gui, modelo, service, dao)
import modelo.Aluno;
import modelo.Disciplina;
import modelo.Frequencia;

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

public class FrequenciaDAO {
    public void salvar(List<Frequencia> registros) {
        String sql = "INSERT INTO frequencia (aluno_matricula, disciplina_id, data_aula, presente) VALUES (?, ?, ?, ?) " +
                "ON CONFLICT (aluno_matricula, disciplina_id, data_aula) DO UPDATE SET presente = EXCLUDED.presente";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (Frequencia f : registros) {
                pstmt.setString(1, f.getAluno().getMatricula());
                pstmt.setInt(2, f.getDisciplina().getId());
                pstmt.setDate(3, Date.valueOf(f.getData()));
                pstmt.setBoolean(4, f.isPresente());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            System.out.println("Lote de frequências salvo/atualizado com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao salvar lote de frequências: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Busca todos os registros de frequência de um aluno em uma disciplina específica.
    public List<Frequencia> listarPorAlunoEDisciplina(Aluno aluno, Disciplina disciplina) {
        String sql = "SELECT * FROM frequencia WHERE aluno_matricula = ? AND disciplina_id = ?";
        List<Frequencia> registros = new ArrayList<>();

        if (aluno == null || disciplina == null) return registros;

        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, aluno.getMatricula());
            pstmt.setInt(2, disciplina.getId());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Frequencia freq = new Frequencia(
                        aluno,
                        disciplina,
                        rs.getDate("data_aula").toLocalDate(),
                        rs.getBoolean("presente")
                );
                freq.setId(rs.getInt("id"));
                registros.add(freq);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar frequências: " + e.getMessage());
            e.printStackTrace();
        }
        return registros;
    }

    // Calcula o total de aulas e o número de presenças de um aluno em uma disciplina.
    public int[] calcularTotalAulasEPresencas(Aluno aluno, Disciplina disciplina) {
        String sql = "SELECT " +
                "  COUNT(*) AS total_aulas, " +
                "  SUM(CASE WHEN presente = true THEN 1 ELSE 0 END) AS total_presencas " +
                "FROM frequencia " +
                "WHERE aluno_matricula = ? AND disciplina_id = ?";

        int[] resultado = new int[]{0, 0};

        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, aluno.getMatricula());
            pstmt.setInt(2, disciplina.getId());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                resultado[0] = rs.getInt("total_aulas");
                resultado[1] = rs.getInt("total_presencas");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao calcular frequência: " + e.getMessage());
            e.printStackTrace();
        }
        return resultado;
    }
}