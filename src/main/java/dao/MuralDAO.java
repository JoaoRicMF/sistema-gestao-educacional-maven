package dao;

import database.Conexao;
import modelo.Mural;
import modelo.Professor;
import modelo.Turma;
import modelo.Turno;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MuralDAO {

    //Salva uma nova postagem no mural e atualiza o objeto com o ID gerado.
    public void salvar(Mural postagem) {
        String sql = "INSERT INTO mural (titulo, conteudo, autor_matricula, turma_id, data_postagem) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, postagem.getTitulo());
            pstmt.setString(2, postagem.getConteudo());
            pstmt.setString(3, postagem.getAutor().getMatricula());
            pstmt.setInt(4, postagem.getTurmaDestino().getId());
            pstmt.setTimestamp(5, Timestamp.valueOf(postagem.getDataPostagem()));
            pstmt.executeUpdate();

            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                postagem.setId(generatedKeys.getInt(1));
            }
            System.out.println("Postagem do mural salva com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao salvar postagem no mural: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Atualiza uma postagem existente no mural.
    public void atualizar(Mural postagem) {
        String sql = "UPDATE mural SET titulo = ?, conteudo = ? WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, postagem.getTitulo());
            pstmt.setString(2, postagem.getConteudo());
            pstmt.setInt(3, postagem.getId());
            pstmt.executeUpdate();
            System.out.println("Postagem do mural atualizada com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar postagem do mural: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Exclui uma postagem do mural pelo seu ID.
    public void excluir(int id) {
        String sql = "DELETE FROM mural WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Postagem do mural excluída com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao excluir postagem do mural: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Lista todas as postagens de uma turma específica, ordenadas da mais recente para a mais antiga.
    public List<Mural> listarPorTurma(Turma turma) {
        String sql = "SELECT m.*, p.nome as nome_professor, t.nome_turma, t.semestre, t.turno " +
                "FROM mural m " +
                "JOIN professor p ON m.autor_matricula = p.matricula " +
                "JOIN turma t ON m.turma_id = t.id " +
                "WHERE m.turma_id = ? " +
                "ORDER BY m.data_postagem DESC";

        List<Mural> postagens = new ArrayList<>();
        if (turma == null) return postagens;

        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, turma.getId());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Professor autor = new Professor(rs.getString("nome_professor"), null, null, null, null, null, null, null, rs.getString("autor_matricula"));

                Turma turmaDestino = new Turma(
                        rs.getString("nome_turma"),
                        rs.getInt("semestre"),
                        Turno.valueOf(rs.getString("turno"))
                );
                turmaDestino.setId(rs.getInt("turma_id"));

                Mural postagem = new Mural(
                        rs.getString("titulo"),
                        rs.getString("conteudo"),
                        autor,
                        turmaDestino,
                        rs.getTimestamp("data_postagem").toLocalDateTime()
                );
                postagem.setId(rs.getInt("id"));
                postagens.add(postagem);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar postagens do mural: " + e.getMessage());
            e.printStackTrace();
        }
        return postagens;
    }
}