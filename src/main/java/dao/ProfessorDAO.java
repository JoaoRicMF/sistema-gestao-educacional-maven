package dao;

// Imports do projeto (gui, modelo, service, dao)
import modelo.*;

//Imports do java
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

//Imports do DAO
import database.Conexao;

/**
 *
 * @author Rhwan
 */

public class ProfessorDAO {

    public void salvar(Professor professor) {
        String sql = "INSERT INTO professor (matricula, nome, cpf, rg, genero, data_nascimento, telefone, email, rua, numero, complemento, bairro, cidade, uf, cep) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, professor.getMatricula());
            pstmt.setString(2, professor.getNome());
            pstmt.setString(3, professor.getCpf());
            pstmt.setString(4, professor.getRG());
            pstmt.setString(5, professor.getGenero().name());
            pstmt.setString(6, professor.getDataNascimento());
            pstmt.setString(7, professor.getTelefone());
            pstmt.setString(8, professor.getEmail());
            pstmt.setString(9, professor.getEndereco().getRua());
            pstmt.setString(10, professor.getEndereco().getNumero());
            pstmt.setString(11, professor.getEndereco().getComplemento());
            pstmt.setString(12, professor.getEndereco().getBairro());
            pstmt.setString(13, professor.getEndereco().getCidade());
            pstmt.setString(14, professor.getEndereco().getUf().name());
            pstmt.setString(15, professor.getEndereco().getCep());
            pstmt.executeUpdate();

            for (Disciplina d : professor.getDisciplinas()) {
                adicionarDisciplinaAoProfessor(professor.getMatricula(), d.getId());
            }

            System.out.println("Professor salvo com sucesso no banco de dados!");
        } catch (SQLException e) {
            System.err.println("Erro ao salvar professor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Professor> listarTodos() {
        String sqlProfessores = "SELECT * FROM professor";
        String sqlDisciplinas = "SELECT pd.professor_matricula, d.* FROM disciplina d JOIN professor_disciplina pd ON d.id = pd.disciplina_id";

        Map<String, Professor> professoresMap = new HashMap<>();
        Map<String, List<Disciplina>> disciplinasPorProfessor = new HashMap<>();

        try (Connection conn = Conexao.getConexao(); Statement stmt = conn.createStatement()) {
            ResultSet rsDisciplinas = stmt.executeQuery(sqlDisciplinas);
            while(rsDisciplinas.next()){
                String profMatricula = rsDisciplinas.getString("professor_matricula");
                Disciplina disciplina = criarDisciplinaDeResultSet(rsDisciplinas);
                disciplinasPorProfessor.computeIfAbsent(profMatricula, k -> new ArrayList<>()).add(disciplina);
            }

            ResultSet rsProfessores = stmt.executeQuery(sqlProfessores);
            while(rsProfessores.next()){
                Professor professor = criarProfessorDeResultSet(rsProfessores);
                String profMatricula = professor.getMatricula();

                professor.setDisciplinas(disciplinasPorProfessor.getOrDefault(profMatricula, new ArrayList<>()));
                professoresMap.put(profMatricula, professor);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar professores: " + e.getMessage());
            e.printStackTrace();
        }
        return new ArrayList<>(professoresMap.values());
    }

    private Professor criarProfessorDeResultSet(ResultSet rs) throws SQLException {
        Endereco endereco = new Endereco(
                rs.getString("rua"), rs.getString("numero"), rs.getString("complemento"),
                rs.getString("bairro"), rs.getString("cidade"), UF.valueOf(rs.getString("uf")),
                rs.getString("cep")
        );
        return new Professor(
                rs.getString("nome"), rs.getString("cpf"), rs.getString("rg"),
                Genero.valueOf(rs.getString("genero")), rs.getString("data_nascimento"),
                rs.getString("telefone"), rs.getString("email"), endereco,
                rs.getString("matricula")
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

    public void atualizar(Professor professor) {
        String sql = "UPDATE professor SET nome=?, cpf=?, rg=?, genero=?, data_nascimento=?, telefone=?, email=?, rua=?, numero=?, complemento=?, bairro=?, cidade=?, uf=?, cep=? WHERE matricula=?";
        try (Connection conn = Conexao.getConexao(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, professor.getNome());
            pstmt.setString(2, professor.getCpf());
            pstmt.setString(3, professor.getRG());
            pstmt.setString(4, professor.getGenero().name());
            pstmt.setString(5, professor.getDataNascimento());
            pstmt.setString(6, professor.getTelefone());
            pstmt.setString(7, professor.getEmail());
            pstmt.setString(8, professor.getEndereco().getRua());
            pstmt.setString(9, professor.getEndereco().getNumero());
            pstmt.setString(10, professor.getEndereco().getComplemento());
            pstmt.setString(11, professor.getEndereco().getBairro());
            pstmt.setString(12, professor.getEndereco().getCidade());
            pstmt.setString(13, professor.getEndereco().getUf().name());
            pstmt.setString(14, professor.getEndereco().getCep());
            pstmt.setString(15, professor.getMatricula());
            pstmt.executeUpdate();

            removerTodasDisciplinasDoProfessor(professor.getMatricula());
            for (Disciplina d : professor.getDisciplinas()) {
                adicionarDisciplinaAoProfessor(professor.getMatricula(), d.getId());
            }

            System.out.println("Professor atualizado com sucesso no banco de dados!");
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar professor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void excluir(String matricula) {
        String sql = "DELETE FROM professor WHERE matricula = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, matricula);
            pstmt.executeUpdate();
            System.out.println("Professor excluído com sucesso do banco de dados!");
        } catch (SQLException e) {
            System.err.println("Erro ao excluir professor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Professor buscarPorCpf(String cpf) {
        String sql = "SELECT * FROM professor WHERE cpf = ?";
        Professor professor = null;
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cpf);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                professor = new Professor(rs.getString("nome"), rs.getString("cpf"), null, null, null, null, null, null, rs.getString("matricula"));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar professor por CPF: " + e.getMessage());
            e.printStackTrace();
        }
        return professor;
    }
    private List<Disciplina> listarDisciplinasDoProfessor(String matricula) {
        String sql = "SELECT d.* FROM disciplina d " +
                "JOIN professor_disciplina pd ON d.id = pd.disciplina_id " +
                "WHERE pd.professor_matricula = ?";
        List<Disciplina> disciplinas = new ArrayList<>();
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, matricula);
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
            System.err.println("Erro ao listar disciplinas do professor: " + e.getMessage());
        }
        return disciplinas;
    }

    private void adicionarDisciplinaAoProfessor(String matricula, int disciplinaId) {
        String sql = "INSERT INTO professor_disciplina (professor_matricula, disciplina_id) VALUES (?, ?)";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, matricula);
            pstmt.setInt(2, disciplinaId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            if (!e.getSQLState().equals("23505")) {
                System.err.println("Erro ao adicionar disciplina ao professor: " + e.getMessage());
            }
        }
    }

    private void removerTodasDisciplinasDoProfessor(String matricula) {
        String sql = "DELETE FROM professor_disciplina WHERE professor_matricula = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, matricula);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao remover disciplinas do professor: " + e.getMessage());
        }
    }
}