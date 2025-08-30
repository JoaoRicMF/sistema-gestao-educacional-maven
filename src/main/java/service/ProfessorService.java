package service;

import dao.ProfessorDAO;
import excecoes.ValidacaoExcecoes;
import modelo.Disciplina;
import modelo.Professor;
import modelo.Endereco;

import java.util.List;
import java.util.Optional;

public class ProfessorService {

    private final ProfessorVerificacao professorVerificacao = new ProfessorVerificacao();
    private final ProfessorDAO professorDAO = new ProfessorDAO();

    public Optional<Professor> buscarProfessorPorMatricula(String matricula) {
        // Implementar a busca por matrícula no DAO se necessário
        return professorDAO.listarTodos().stream()
                .filter(p -> p.getMatricula().equals(matricula))
                .findFirst();
    }

    public void cadastrarNovoProfessor(Professor novoProfessor) throws ValidacaoExcecoes {
        try {
            professorVerificacao.validar(novoProfessor);
            professorDAO.salvar(novoProfessor);
            System.out.println("LOG: Professor '" + novoProfessor.getNome() + "' cadastrado com sucesso!");
        } catch (ValidacaoExcecoes e) {
            System.out.println("Falha no cadastro: " + e.getMessage());
            throw e;
        }
    }

    public void atualizarDadosProfessor(Professor professor) throws ValidacaoExcecoes {
        try {
            professorVerificacao.validar(professor);
            professorDAO.atualizar(professor);
            System.out.println("LOG: Dados do professor '" + professor.getNome() + "' atualizados com sucesso!");
        } catch (ValidacaoExcecoes e) {
            System.out.println("Falha na atualização: " + e.getMessage());
            throw e;
        }
    }

    public void excluirProfessor(Professor professor) throws ValidacaoExcecoes {
        try {
            professorVerificacao.validar(professor);
            professorDAO.excluir(professor.getMatricula());
            System.out.println("LOG: Professor '" + professor.getNome() + "' excluído do sistema.");
        } catch (ValidacaoExcecoes e) {
            System.out.println("Falha ao excluir professor: " + e.getMessage());
            throw e;
        }
    }
}