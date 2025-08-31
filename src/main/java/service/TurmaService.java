package service;

import dao.TurmaDAO;
import excecoes.ValidacaoExcecoes;
import modelo.Professor;
import modelo.Turma;
import modelo.Turno;

/**
 *
 * @author João Ricardo
 * Camada de serviço para gerenciar as regras relacionadas a Turma.
 */

public class TurmaService {

    private final TurmaVerificacao turmaVerificacao = new TurmaVerificacao();
    private final TurmaDAO turmaDAO = new TurmaDAO();

    public Turma cadastrarNovaTurma(String nomeTurma, int serieAno, Turno turno, Professor professor) throws ValidacaoExcecoes {
        Turma novaTurma = new Turma(nomeTurma, serieAno, turno);
        novaTurma.setProfessor(professor);
        turmaVerificacao.validarTurma(novaTurma);
        turmaDAO.salvar(novaTurma);
        System.out.println("LOG: Turma '" + novaTurma.getNomeTurma() + "' cadastrada com sucesso!");
        return novaTurma;
    }

    public void atualizarTurma(Turma turma) throws ValidacaoExcecoes {
        turmaVerificacao.validarTurma(turma);
        turmaDAO.atualizar(turma);
        System.out.println("LOG: Turma '" + turma.getNomeTurma() + "' atualizada com sucesso!");
    }
}