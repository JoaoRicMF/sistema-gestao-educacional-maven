package dao;

// Imports do projeto (gui, modelo, service, dao)
import modelo.Aluno;
import modelo.Disciplina;
import modelo.Nota;
import modelo.TipoAvaliacao;

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

public class NotaDAO {

    //Salva uma nova nota no banco de dados e atualiza o objeto com o ID gerado.
    public void salvar(Nota nota) {
        String sql = "INSERT INTO nota (aluno_matricula, disciplina_id, valor_nota, tipo_avaliacao, data_avaliacao) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, nota.getAluno().getMatricula());
            pstmt.setInt(2, nota.getDisciplina().getId());
            pstmt.setDouble(3, nota.getValor());
            pstmt.setString(4, nota.getTipoAvaliacao().name());
            pstmt.setDate(5, Date.valueOf(nota.getData()));
            pstmt.executeUpdate();

            // Pega o ID gerado pelo banco e atribui ao objeto
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                nota.setId(generatedKeys.getInt(1));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao salvar nota: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Atualiza uma nota existente no banco de dados.
    public void atualizar(Nota nota) {
        String sql = "UPDATE nota SET valor_nota = ?, tipo_avaliacao = ?, data_avaliacao = ? WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, nota.getValor());
            pstmt.setString(2, nota.getTipoAvaliacao().name());
            pstmt.setDate(3, Date.valueOf(nota.getData()));
            pstmt.setInt(4, nota.getId());
            pstmt.executeUpdate();
            System.out.println("Nota atualizada com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar nota: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Exclui uma nota do banco de dados pelo seu ID.
    public void excluir(int id) {
        String sql = "DELETE FROM nota WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Nota excluída com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao excluir nota: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Nota> listarPorAluno(Aluno aluno) {
        String sql = "SELECT n.*, d.nome_disciplina, d.carga_horaria, d.serie_semestre " +
                "FROM nota n " +
                "JOIN disciplina d ON n.disciplina_id = d.id " +
                "WHERE n.aluno_matricula = ?";

        List<Nota> notas = new ArrayList<>();
        if (aluno == null) return notas;

        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, aluno.getMatricula());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Disciplina disciplina = new Disciplina(
                        rs.getString("nome_disciplina"),
                        rs.getInt("carga_horaria"),
                        rs.getInt("serie_semestre")
                );
                disciplina.setId(rs.getInt("disciplina_id"));

                Nota nota = new Nota(
                        aluno,
                        disciplina,
                        rs.getDouble("valor_nota"),
                        TipoAvaliacao.valueOf(rs.getString("tipo_avaliacao")),
                        rs.getDate("data_avaliacao").toLocalDate()
                );
                nota.setId(rs.getInt("id"));
                notas.add(nota);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar notas do aluno: " + e.getMessage());
            e.printStackTrace();
        }
        return notas;
    }
}