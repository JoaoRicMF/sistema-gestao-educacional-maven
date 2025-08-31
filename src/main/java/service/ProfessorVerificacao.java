package service;

import excecoes.ValidacaoExcecoes;
import modelo.Professor;

/**
 *
 * @author João Ricardo
 * Camada de verificação para gerenciar as regras relacionadas a Professor.
 */

public class ProfessorVerificacao {
    private final PessoaVerificacao pessoaVerificacao = new PessoaVerificacao();

    public void validar(Professor professor) throws ValidacaoExcecoes{
        if (professor == null) {
            throw new ValidacaoExcecoes("Erro: O objeto Professor não pode ser nulo.");
        }
        pessoaVerificacao.validarPessoa(professor);
        // Validações de campos específicos de Professor
        validarFormatoMatricula(professor.getMatricula());

    }
    private void validarFormatoMatricula(String matricula) throws ValidacaoExcecoes {
        if (matricula == null || matricula.trim().isEmpty()) {
            throw new ValidacaoExcecoes("Erro de validação: A matrícula é obrigatória.");
        }
        if (matricula.length() != 9) {
            throw new ValidacaoExcecoes("Erro de formato: A matrícula deve conter exatamente 9 dígitos.");
        }
        for (char c : matricula.toCharArray()) {
            if (!Character.isDigit(c)) {
                throw new ValidacaoExcecoes("Erro de formato: A matrícula deve conter apenas números.");
            }
        }
    }
}