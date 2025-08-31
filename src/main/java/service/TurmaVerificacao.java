package service;

import excecoes.ValidacaoExcecoes;
import modelo.Turma;

/**
 *
 * @author João Ricardo
 * Camada de verificação para gerenciar as regras relacionadas a Turma.
 */

public class TurmaVerificacao {
    public void validarTurma(Turma turma) throws ValidacaoExcecoes {
        if (turma == null) {
            throw new ValidacaoExcecoes("Erro: O objeto Turma não pode ser nulo.");
        }

        if (turma.getNomeTurma() == null || turma.getNomeTurma().trim().isEmpty()) {
            throw new ValidacaoExcecoes("Erro: O nome da turma é obrigatório.");
        }

        if (turma.getSemestre() <= 0) {
            throw new ValidacaoExcecoes("Erro: O semestre da turma deve ser um valor positivo.");
        }
        if (turma.getTurno() == null) {
            throw new ValidacaoExcecoes("Erro: O turno da turma é obrigatório.");
        }
    }
}