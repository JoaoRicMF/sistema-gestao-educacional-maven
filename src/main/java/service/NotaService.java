package service;

import excecoes.ValidacaoExcecoes;
import modelo.Aluno;
import modelo.Disciplina;
import modelo.Nota;
import modelo.TipoAvaliacao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author João Ricardo
 */

public class NotaService {

    private final NotaVerificacao notaVerificacao = new NotaVerificacao();

    /**
     * Orquestra a criação, validação e cálculo da média de um conjunto de notas.
     * @return A média calculada.
     */
    public double lancarNotasEObterMedia(Aluno aluno, Disciplina disciplina, double valorNota1, double valorNota2) throws ValidacaoExcecoes {
        if (aluno == null || disciplina == null) {
            throw new ValidacaoExcecoes("Selecione um aluno e uma disciplina.");
        }

        Nota nota1 = new Nota(aluno, disciplina, valorNota1, TipoAvaliacao.PROVA, LocalDate.now());
        Nota nota2 = new Nota(aluno, disciplina, valorNota2, TipoAvaliacao.TRABALHO, LocalDate.now());

        List<Nota> notasDoAluno = new ArrayList<>();
        notasDoAluno.add(nota1);
        notasDoAluno.add(nota2);

        return calcularMedia(notasDoAluno);
    }

    public double calcularMedia(List<Nota> notas) throws ValidacaoExcecoes {
        if (notas == null || notas.isEmpty()) {
            return 0.0;
        }

        double somaTotal = 0.0;
        try {
            for (Nota nota : notas) {
                notaVerificacao.validar(nota);
                somaTotal += nota.getValor();
            }
        } catch (ValidacaoExcecoes e) {
            System.out.println("Falha no cálculo da média: " + e.getMessage());
            // Lança a exceção para a camada da GUI
            throw e;
        }

        return somaTotal / notas.size();
    }

    public String verificarStatusAprovacao(double mediaFinal) {
        // Definição das notas de corte
        final double NOTA_APROVACAO = 6.0;
        final double NOTA_RECUPERACAO = 5.0;

        if (mediaFinal >= NOTA_APROVACAO) {
            return "APROVADO";
        } else if (mediaFinal >= NOTA_RECUPERACAO) {
            return "EM RECUPERAÇÃO";
        } else {
            return "REPROVADO";
        }
    }
}