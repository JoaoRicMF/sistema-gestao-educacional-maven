package service;

import excecoes.ValidacaoExcecoes;
import modelo.Aluno;
import modelo.StatusAluno;
import modelo.Turma;

/**
 *
 * @author João Ricardo
 */

public class MatriculaService {
    private final TurmaVerificacao turmaVerificacao = new TurmaVerificacao();
    private final AlunoVerificacao alunoVerificacao = new AlunoVerificacao();
    private final TurmaService turmaService = new TurmaService();

    public void matricularAluno(Aluno aluno, Turma turma) throws ValidacaoExcecoes {
        try {
            alunoVerificacao.validarAluno(aluno);
            turmaVerificacao.validarTurma(turma);
            turmaService.verificarDisponibilidadeDeVagas(turma);

            if (aluno.getStatusAluno() != StatusAluno.ATIVO) {
                throw new ValidacaoExcecoes("O aluno precisa estar ATIVO para ser matriculado.");
            }

            turmaService.verificarDisponibilidadeDeVagas(turma);

            for (Aluno alunoExistente : turma.getAlunos()) {
                if (alunoExistente.getMatricula().equals(aluno.getMatricula())) {
                    throw new ValidacaoExcecoes("Erro de negócio: A matrícula '" + aluno.getMatricula() + "' já existe nesta turma.");
                }
            }

            turma.adicionarAluno(aluno);
            System.out.println("LOG: Aluno '" + aluno.getNome() + "' adicionado à turma '" + turma.getNomeTurma() + "' em memória.");

        } catch (ValidacaoExcecoes e) {

            throw e;
        }
    }
}