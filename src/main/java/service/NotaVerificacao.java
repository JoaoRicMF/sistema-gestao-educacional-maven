package service;

import excecoes.ValidacaoExcecoes;
import modelo.Nota;

/**
 *
 * @author João Ricardo
 * Camada de verificação para gerenciar as regras relacionadas a nota.
 */

public class NotaVerificacao {
    public void validar(Nota nota) throws ValidacaoExcecoes {
        if (nota == null) {
            throw new ValidacaoExcecoes("Erro: O objeto Nota não pode ser nulo.");
        }

        if (nota.getAluno() == null) {
            throw new ValidacaoExcecoes("Erro de validação: A nota deve estar associada a um aluno.");
        }

        if (nota.getDisciplina() == null) {
            throw new ValidacaoExcecoes("Erro de validação: A nota deve estar associada a uma disciplina.");
        }

        // Valida se o valor da nota está entre 0 e 10
        if (nota.getValor() < 0 || nota.getValor() > 10) {
            throw new ValidacaoExcecoes("Erro de validação: O valor da nota deve estar entre 0 e 10.");
        }

        if (nota.getTipoAvaliacao() == null) {
            throw new ValidacaoExcecoes("Erro de validação: O tipo de avaliação da nota é obrigatório.");
        }

        if (nota.getData() == null) {
            throw new ValidacaoExcecoes("Erro de validação: A data da nota é obrigatória.");
        }
    }
}