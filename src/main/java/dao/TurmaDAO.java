package dao;

import database.Conexao;
import modelo.Aluno;
import modelo.Endereco;
import modelo.Genero;
import modelo.NivelAcademico;
import modelo.StatusAluno;
import modelo.TipoCurso;
import modelo.Turma;
import modelo.Turno;
import modelo.UF;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TurmaDAO {
    public void salvar(Turma turma) {
        String sql = "INSERT INTO turma (nome_turma, semestre, turno) VALUES (?, ?, ?)";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, turma.getNomeTurma());
            pstmt.setInt(2, turma.getSemestre());
            pstmt.setString(3, turma.getTurno().name());
            pstmt.executeUpdate();

            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                turma.setId(generatedKeys.getInt(1));
            }
            System.out.println("Turma salva com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao salvar turma: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * CORREÇÃO: Este método agora também carrega a lista de alunos de cada turma.
     */
    public List<Turma> listarTodas() {
        String sql = "SELECT * FROM turma";
        List<Turma> turmas = new ArrayList<>();
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Turma turma = new Turma(
                        rs.getString("nome_turma"),
                        rs.getInt("semestre"),
                        Turno.valueOf(rs.getString("turno"))
                );
                turma.setId(rs.getInt("id"));

                // **NOVA LÓGICA**: Após criar a turma, busca e anexa os seus alunos
                turma.setAlunos(listarAlunosDaTurma(turma.getId()));

                turmas.add(turma);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar turmas: " + e.getMessage());
            e.printStackTrace();
        }
        return turmas;
    }

    /**
     * MÉTODO NOVO: Busca no banco todos os alunos matriculados numa turma específica.
     * @param turmaId O ID da turma.
     * @return Uma lista de objetos Aluno.
     */
    private List<Aluno> listarAlunosDaTurma(int turmaId) {
        String sql = "SELECT a.* FROM aluno a " +
                "JOIN turma_aluno ta ON a.matricula = ta.aluno_matricula " +
                "WHERE ta.turma_id = ?";
        List<Aluno> alunos = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, turmaId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Endereco endereco = new Endereco(
                        rs.getString("rua"), rs.getString("numero"), rs.getString("complemento"),
                        rs.getString("bairro"), rs.getString("cidade"), UF.valueOf(rs.getString("uf")),
                        rs.getString("cep")
                );
                Aluno aluno = new Aluno(
                        rs.getString("nome"), rs.getString("cpf"), rs.getString("rg"),
                        Genero.valueOf(rs.getString("genero")), rs.getString("data_nascimento"),
                        rs.getString("telefone"), rs.getString("email"), endereco,
                        rs.getString("matricula"), rs.getString("curso"),
                        TipoCurso.valueOf(rs.getString("tipo_curso")), rs.getInt("semestre"),
                        NivelAcademico.valueOf(rs.getString("nivel_academico")),
                        StatusAluno.valueOf(rs.getString("status_aluno")), 0, 0, 0
                );
                alunos.add(aluno);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar alunos da turma: " + e.getMessage());
            e.printStackTrace();
        }
        return alunos;
    }

    public void matricularAluno(int turmaId, String alunoMatricula) throws SQLException {
        String sql = "INSERT INTO turma_aluno (turma_id, aluno_matricula) VALUES (?, ?)";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, turmaId);
            pstmt.setString(2, alunoMatricula);
            pstmt.executeUpdate();
            System.out.println("Aluno matriculado na turma com sucesso no banco de dados!");
        }
    }

    public boolean alunoEstaMatriculado(int turmaId, String alunoMatricula) {
        String sql = "SELECT COUNT(*) FROM turma_aluno WHERE turma_id = ? AND aluno_matricula = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, turmaId);
            pstmt.setString(2, alunoMatricula);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao verificar matrícula: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}