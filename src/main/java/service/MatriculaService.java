package service;

import dao.TurmaDAO;
import excecoes.ValidacaoExcecoes;
import modelo.Aluno;
import modelo.StatusAluno;
import modelo.Turma;

import java.sql.SQLException;

public class MatriculaService {
    private final AlunoVerificacao alunoVerificacao = new AlunoVerificacao();
    private final TurmaDAO turmaDAO = new TurmaDAO();

    public void matricularAluno(Aluno aluno, Turma turma) throws ValidacaoExcecoes {
        try {
            alunoVerificacao.validarAluno(aluno);

            if (aluno.getStatusAluno() != StatusAluno.ATIVO) {
                throw new ValidacaoExcecoes("O aluno precisa estar ATIVO para ser matriculado.");
            }
            if (turmaDAO.alunoEstaMatriculado(turma.getId(), aluno.getMatricula())) {
                throw new ValidacaoExcecoes("Este aluno já está matriculado nesta turma.");
            }
            turma.adicionarAluno(aluno); // Adiciona na lista em memória para a UI
            turmaDAO.matricularAluno(turma.getId(), aluno.getMatricula()); // Salva a relação no banco

            System.out.println("LOG: Aluno '" + aluno.getNome() + "' matriculado na turma '" + turma.getNomeTurma() + "'.");

        } catch (ValidacaoExcecoes e) {
            throw e;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}