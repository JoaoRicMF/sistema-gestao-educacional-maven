package dao;

import database.Conexao;
import modelo.Aluno;
import modelo.Endereco;
import modelo.Genero;
import modelo.NivelAcademico;
import modelo.StatusAluno;
import modelo.TipoCurso;
import modelo.UF;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {

    public void salvar(Aluno aluno) {
        String sql = "INSERT INTO aluno (matricula, nome, cpf, rg, genero, data_nascimento, telefone, email, curso, tipo_curso, semestre, nivel_academico, status_aluno, rua, numero, complemento, bairro, cidade, uf, cep) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, aluno.getMatricula());
            pstmt.setString(2, aluno.getNome());
            pstmt.setString(3, aluno.getCpf());
            pstmt.setString(4, aluno.getRG());
            pstmt.setString(5, aluno.getGenero().name());
            pstmt.setString(6, aluno.getDataNascimento());
            pstmt.setString(7, aluno.getTelefone());
            pstmt.setString(8, aluno.getEmail());
            pstmt.setString(9, aluno.getCurso());
            pstmt.setString(10, aluno.getTipoCurso().name());
            pstmt.setInt(11, aluno.getSemestre());
            pstmt.setString(12, aluno.getNivelAcademico().name());
            pstmt.setString(13, aluno.getStatusAluno().name());
            pstmt.setString(14, aluno.getEndereco().getRua());
            pstmt.setString(15, aluno.getEndereco().getNumero());
            pstmt.setString(16, aluno.getEndereco().getComplemento());
            pstmt.setString(17, aluno.getEndereco().getBairro());
            pstmt.setString(18, aluno.getEndereco().getCidade());
            pstmt.setString(19, aluno.getEndereco().getUf().name());
            pstmt.setString(20, aluno.getEndereco().getCep());

            pstmt.executeUpdate();
            System.out.println("Aluno salvo com sucesso NO BANCO DE DADOS!");

        } catch (SQLException e) {
            System.err.println("Erro ao salvar o aluno no banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Aluno> listarTodos() {
        String sql = "SELECT * FROM aluno";
        List<Aluno> alunos = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

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
            System.err.println("Erro ao listar alunos: " + e.getMessage());
        }
        return alunos;
    }

    public void atualizar(Aluno aluno) {
        String sql = "UPDATE aluno SET nome = ?, cpf = ?, rg = ?, genero = ?, data_nascimento = ?, telefone = ?, email = ?, curso = ?, tipo_curso = ?, semestre = ?, nivel_academico = ?, status_aluno = ?, rua = ?, numero = ?, complemento = ?, bairro = ?, cidade = ?, uf = ?, cep = ? WHERE matricula = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, aluno.getNome());
            pstmt.setString(2, aluno.getCpf());
            pstmt.setString(3, aluno.getRG());
            pstmt.setString(4, aluno.getGenero().name());
            pstmt.setString(5, aluno.getDataNascimento());
            pstmt.setString(6, aluno.getTelefone());
            pstmt.setString(7, aluno.getEmail());
            pstmt.setString(8, aluno.getCurso());
            pstmt.setString(9, aluno.getTipoCurso().name());
            pstmt.setInt(10, aluno.getSemestre());
            pstmt.setString(11, aluno.getNivelAcademico().name());
            pstmt.setString(12, aluno.getStatusAluno().name());
            pstmt.setString(13, aluno.getEndereco().getRua());
            pstmt.setString(14, aluno.getEndereco().getNumero());
            pstmt.setString(15, aluno.getEndereco().getComplemento());
            pstmt.setString(16, aluno.getEndereco().getBairro());
            pstmt.setString(17, aluno.getEndereco().getCidade());
            pstmt.setString(18, aluno.getEndereco().getUf().name());
            pstmt.setString(19, aluno.getEndereco().getCep());
            pstmt.setString(20, aluno.getMatricula());

            pstmt.executeUpdate();
            System.out.println("Aluno atualizado com sucesso NO BANCO DE DADOS!");

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar o aluno no banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void excluir(String matricula) {
        String sql = "DELETE FROM aluno WHERE matricula = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, matricula);
            pstmt.executeUpdate();
            System.out.println("Aluno excluído com sucesso DO BANCO DE DADOS!");

        } catch (SQLException e) {
            System.err.println("Erro ao excluir o aluno do banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Aluno buscarPorMatricula(String matricula) {
        String sql = "SELECT * FROM aluno WHERE matricula = ?";
        Aluno aluno = null;

        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, matricula);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Endereco endereco = new Endereco(
                        rs.getString("rua"), rs.getString("numero"), rs.getString("complemento"),
                        rs.getString("bairro"), rs.getString("cidade"), UF.valueOf(rs.getString("uf")),
                        rs.getString("cep")
                );
                aluno = new Aluno(
                        rs.getString("nome"), rs.getString("cpf"), rs.getString("rg"),
                        Genero.valueOf(rs.getString("genero")), rs.getString("data_nascimento"),
                        rs.getString("telefone"), rs.getString("email"), endereco,
                        rs.getString("matricula"), rs.getString("curso"),
                        TipoCurso.valueOf(rs.getString("tipo_curso")), rs.getInt("semestre"),
                        NivelAcademico.valueOf(rs.getString("nivel_academico")),
                        StatusAluno.valueOf(rs.getString("status_aluno")), 0, 0, 0
                );
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar aluno: " + e.getMessage());
            e.printStackTrace();
        }
        return aluno;
    }

    public Aluno buscarPorCpf(String cpf) {
        String sql = "SELECT * FROM aluno WHERE cpf = ?";
        Aluno aluno = null;

        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cpf);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                aluno = new Aluno(rs.getString("nome"), rs.getString("cpf"), null, null, null, null, null, null, rs.getString("matricula"), null, null, 0, null, null, 0, 0, 0);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar aluno por CPF: " + e.getMessage());
            e.printStackTrace();
        }
        return aluno;
    }
}