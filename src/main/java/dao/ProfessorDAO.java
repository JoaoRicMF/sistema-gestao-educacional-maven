package dao;

import database.Conexao;
import modelo.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author João Ricardo
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
            System.out.println("Professor salvo com sucesso no banco de dados!");
        } catch (SQLException e) {
            System.err.println("Erro ao salvar professor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Professor> listarTodos() {
        String sql = "SELECT * FROM professor";
        List<Professor> professores = new ArrayList<>();
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Endereco endereco = new Endereco(
                        rs.getString("rua"), rs.getString("numero"), rs.getString("complemento"),
                        rs.getString("bairro"), rs.getString("cidade"), UF.valueOf(rs.getString("uf")),
                        rs.getString("cep")
                );
                Professor professor = new Professor(
                        rs.getString("nome"), rs.getString("cpf"), rs.getString("rg"),
                        Genero.valueOf(rs.getString("genero")), rs.getString("data_nascimento"),
                        rs.getString("telefone"), rs.getString("email"), endereco,
                        rs.getString("matricula")
                );
                professores.add(professor);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar professores: " + e.getMessage());
            e.printStackTrace();
        }
        return professores;
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
}
