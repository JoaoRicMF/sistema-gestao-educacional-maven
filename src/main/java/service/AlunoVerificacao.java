package service;

import modelo.*;
import excecoes.ValidacaoExcecoes;

//Verificao de data
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 *
 * @author João Ricardo
 */

public class AlunoVerificacao {
    private final PessoaVerificacao pessoaVerificacao = new PessoaVerificacao();

    public void validarAluno(Aluno aluno) throws ValidacaoExcecoes {
        if (aluno == null) {
            throw new ValidacaoExcecoes("Erro: O objeto Aluno não pode ser nulo.");
        }
        pessoaVerificacao.validarPessoa(aluno);
        // Validações de campos específicos do Aluno
        validarFormatoMatricula(aluno.getMatricula());
        validarCurso(aluno.getCurso());
        validarTipoCurso(aluno.getTipoCurso());
        validarSemestre(aluno.getSemestre());
        validarNivelAcademico(aluno.getNivelAcademico());
        validarStatusAluno(aluno.getStatusAluno());
    }

    private void validarFormatoMatricula(String matricula) throws ValidacaoExcecoes {
        if (matricula == null || matricula.trim().isEmpty()) {
            throw new ValidacaoExcecoes("Erro de validação: A matrícula é obrigatória.");
        }
        if (matricula.length() != 9) {
            throw new ValidacaoExcecoes("Erro de formato: A matrícula deve conter exatamente 9 dígitos.");
        }
        for(char c : matricula.toCharArray()){
            if(!Character.isLetterOrDigit(c)){
                throw new ValidacaoExcecoes("A matrícula deve conter apenas letras e números.");
            }
        }
    }
    private void validarCurso(String curso) throws ValidacaoExcecoes {
        if (curso == null || curso.trim().isEmpty()) {
            throw new ValidacaoExcecoes("Erro de validação: O curso é obrigatorio");
        }
        if (curso.length()<3){
            throw new ValidacaoExcecoes("Erro de validação: O nome do curso deve conter mais de 3 letras");
        }
        for (char c: curso.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && !Character.isWhitespace(c)) {
                throw new ValidacaoExcecoes("Erro de validaçao: o nome do curso nao pode conter caracteres especiais");
            }
        }
    }
    private void validarTipoCurso(TipoCurso tipoCurso) throws ValidacaoExcecoes {
        if (tipoCurso == null){
            throw new ValidacaoExcecoes("Erro de validação: O tipo de curso é obrigatorio");
        }
    }
    private void validarSemestre(Integer semestre) throws ValidacaoExcecoes {
        if(semestre == null||semestre == 0){
            throw new ValidacaoExcecoes("Erro de validação: O semestre é obrigatorio");
        }
        if(semestre > 20 || semestre < 1){
            throw new ValidacaoExcecoes("Erro de validação: O semestre deve estar entre 1 e 20");
        }
    }
    private void validarNivelAcademico(NivelAcademico nivelAcademico) throws ValidacaoExcecoes {
        if (nivelAcademico == null){
            throw new ValidacaoExcecoes("Erro de validação: O nivel academico é obrigatorio");
        }
    }
    private void validarStatusAluno(StatusAluno statusAluno) throws ValidacaoExcecoes {
        if (statusAluno == null){
            throw new ValidacaoExcecoes("Erro de validação: O status do aluno é obrigatorio");
        }
    }
}




