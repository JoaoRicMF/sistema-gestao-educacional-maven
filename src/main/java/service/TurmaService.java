package service;

import dao.TurmaDAO;
import excecoes.ValidacaoExcecoes;
import modelo.Turma;
import modelo.Turno;

public class TurmaService {

    private final TurmaVerificacao turmaVerificacao = new TurmaVerificacao();
    private final TurmaDAO turmaDAO = new TurmaDAO();

    public Turma cadastrarNovaTurma(String nomeTurma, int serieAno, Turno turno) throws ValidacaoExcecoes {
        Turma novaTurma = new Turma(nomeTurma, serieAno, turno);
        turmaVerificacao.validarTurma(novaTurma);
        turmaDAO.salvar(novaTurma);
        System.out.println("LOG: Turma '" + novaTurma.getNomeTurma() + "' cadastrada com sucesso!");
        return novaTurma;
    }
}