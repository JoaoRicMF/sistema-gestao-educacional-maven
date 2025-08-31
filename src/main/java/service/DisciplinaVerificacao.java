package service;

import excecoes.ValidacaoExcecoes;
import modelo.Disciplina;

/**
 *
 * @author João Ricardo
 * Camada de verificação para gerenciar as regras relacionadas a Disciplina.
 */

public class DisciplinaVerificacao {
    public void validar(Disciplina disciplina) throws ValidacaoExcecoes {
        if (disciplina == null) {
            throw new ValidacaoExcecoes("Erro: O objeto Disciplina não pode ser nulo.");
        }

        if (disciplina.getNomeDisciplina() == null || disciplina.getNomeDisciplina().trim().isEmpty()) {
            throw new ValidacaoExcecoes("Erro: O nome da disciplina é obrigatório.");
        }

        if (disciplina.getCargaHoraria() <= 0) {
            throw new ValidacaoExcecoes("Erro: A carga horária da disciplina deve ser um valor positivo.");
        }

        // Adicionada validação para o semestre
        if (disciplina.getSerieSemestre() <= 0) {
            throw new ValidacaoExcecoes("Erro: A Série / Semestre deve ser um valor positivo.");
        }
    }
}