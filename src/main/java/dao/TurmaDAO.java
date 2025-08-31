package dao;

// Imports do projeto (gui, modelo, service, dao)
import modelo.*;

//Imports do java
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Imports do DAO
import database.Conexao;

/**
 *
 * @author Rhwan
 */

public class TurmaDAO {
    public void salvar(Turma turma) {
        String sql = "INSERT INTO turma (nome_turma, semestre, turno, professor_matricula) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, turma.getNomeTurma());
            pstmt.setInt(2, turma.getSemestre());
            pstmt.setString(3, turma.getTurno().name());
            if (turma.getProfessor() != null) {
                pstmt.setString(4, turma.getProfessor().getMatricula());
            } else {
                pstmt.setNull(4, Types.VARCHAR);
            }
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

    public List<Turma> listarTodas() {
        String sqlTurmas = "SELECT t.*, p.nome as nome_professor, p.matricula as matricula_professor FROM turma t " +
                "LEFT JOIN professor p ON t.professor_matricula = p.matricula";
        String sqlAlunos = "SELECT ta.turma_id, a.* FROM aluno a JOIN turma_aluno ta ON a.matricula = ta.aluno_matricula";
        String sqlDisciplinas = "SELECT td.turma_id, d.* FROM disciplina d JOIN turma_disciplina td ON d.id = td.disciplina_id";

        Map<Integer, Turma> turmasMap = new HashMap<>();
        Map<Integer, List<Aluno>> alunosPorTurma = new HashMap<>();
        Map<Integer, List<Disciplina>> disciplinasPorTurma = new HashMap<>();

        try (Connection conn = Conexao.getConexao(); Statement stmt = conn.createStatement()) {

            ResultSet rsAlunos = stmt.executeQuery(sqlAlunos);
            while(rsAlunos.next()) {
                int turmaId = rsAlunos.getInt("turma_id");
                Aluno aluno = criarAlunoDeResultSet(rsAlunos);
                alunosPorTurma.computeIfAbsent(turmaId, k -> new ArrayList<>()).add(aluno);
            }

            ResultSet rsDisciplinas = stmt.executeQuery(sqlDisciplinas);
            while(rsDisciplinas.next()) {
                int turmaId = rsDisciplinas.getInt("turma_id");
                Disciplina disciplina = criarDisciplinaDeResultSet(rsDisciplinas);
                disciplinasPorTurma.computeIfAbsent(turmaId, k -> new ArrayList<>()).add(disciplina);
            }

            ResultSet rsTurmas = stmt.executeQuery(sqlTurmas);
            while(rsTurmas.next()) {
                Turma turma = criarTurmaDeResultSet(rsTurmas);
                int turmaId = turma.getId();

                turma.setAlunos(alunosPorTurma.getOrDefault(turmaId, new ArrayList<>()));
                turma.setDisciplinas(disciplinasPorTurma.getOrDefault(turmaId, new ArrayList<>()));
                turmasMap.put(turmaId, turma);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar turmas de forma otimizada: " + e.getMessage());
            e.printStackTrace();
        }
        return new ArrayList<>(turmasMap.values());
    }

    private Turma criarTurmaDeResultSet(ResultSet rs) throws SQLException {
        Turma turma = new Turma(
                rs.getString("nome_turma"),
                rs.getInt("semestre"),
                Turno.valueOf(rs.getString("turno"))
        );
        turma.setId(rs.getInt("id"));
        String professorMatricula = rs.getString("matricula_professor");
        if (professorMatricula != null) {
            Professor professor = new Professor(rs.getString("nome_professor"), null, null, null, null, null, null, null, professorMatricula);
            turma.setProfessor(professor);
        }
        return turma;
    }
    private Aluno criarAlunoDeResultSet(ResultSet rs) throws SQLException {
        return new Aluno(
                rs.getString("nome"), rs.getString("cpf"), rs.getString("rg"),
                Genero.valueOf(rs.getString("genero")), rs.getString("data_nascimento"),
                rs.getString("telefone"), rs.getString("email"), null,
                rs.getString("matricula"), rs.getString("curso"),
                TipoCurso.valueOf(rs.getString("tipo_curso")), rs.getInt("semestre"),
                NivelAcademico.valueOf(rs.getString("nivel_academico")),
                StatusAluno.valueOf(rs.getString("status_aluno")), 0, 0, 0
        );
    }

    private Disciplina criarDisciplinaDeResultSet(ResultSet rs) throws SQLException {
        Disciplina disciplina = new Disciplina(
                rs.getString("nome_disciplina"),
                rs.getInt("carga_horaria"),
                rs.getInt("serie_semestre")
        );
        disciplina.setId(rs.getInt("id"));
        return disciplina;
    }

    public void atualizar(Turma turma) {
        String sql = "UPDATE turma SET nome_turma = ?, semestre = ?, turno = ?, professor_matricula = ? WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, turma.getNomeTurma());
            pstmt.setInt(2, turma.getSemestre());
            pstmt.setString(3, turma.getTurno().name());
            if (turma.getProfessor() != null) {
                pstmt.setString(4, turma.getProfessor().getMatricula());
            } else {
                pstmt.setNull(4, Types.VARCHAR);
            }
            pstmt.setInt(5, turma.getId());
            pstmt.executeUpdate();
            System.out.println("Turma atualizada com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar turma: " + e.getMessage());
            e.printStackTrace();
        }
    }

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

    private List<Disciplina> listarDisciplinasDaTurma(int turmaId) {
        String sql = "SELECT d.* FROM disciplina d " +
                "JOIN turma_disciplina td ON d.id = td.disciplina_id " +
                "WHERE td.turma_id = ?";
        List<Disciplina> disciplinas = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, turmaId);
            ResultSet rs = pstmt.executeQuery();

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
            System.err.println("Erro ao listar disciplinas da turma: " + e.getMessage());
            e.printStackTrace();
        }
        return disciplinas;
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

    public void adicionarDisciplinaNaTurma(int turmaId, int disciplinaId) {
        String sql = "INSERT INTO turma_disciplina (turma_id, disciplina_id) VALUES (?, ?)";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, turmaId);
            pstmt.setInt(2, disciplinaId);
            pstmt.executeUpdate();
            System.out.println("Disciplina adicionada à turma no banco de dados!");
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar disciplina à turma: " + e.getMessage());
        }
    }

    public void removerAlunoDaTurma(int turmaId, String alunoMatricula) {
        String sql = "DELETE FROM turma_aluno WHERE turma_id = ? AND aluno_matricula = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, turmaId);
            pstmt.setString(2, alunoMatricula);
            pstmt.executeUpdate();
            System.out.println("Aluno removido da turma no banco de dados!");
        } catch (SQLException e) {
            System.err.println("Erro ao remover aluno da turma: " + e.getMessage());
        }
    }
}