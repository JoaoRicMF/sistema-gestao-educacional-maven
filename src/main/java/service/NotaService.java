package service;

import excecoes.ValidacaoExcecoes;
import modelo.Aluno;
import modelo.Disciplina;
import modelo.Nota;
import modelo.TipoAvaliacao;
import dao.NotaDAO; // Importar o DAO
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author João Ricardo
 * Camada de serviço para gerenciar as regras relacionadas a Nota.
 */

public class NotaService {

    private final NotaVerificacao notaVerificacao = new NotaVerificacao();
    private final NotaDAO notaDAO = new NotaDAO();

    public double lancarNotasEObterMedia(Aluno aluno, Disciplina disciplina, double valorNota1, double valorNota2) throws ValidacaoExcecoes {
        if (aluno == null || disciplina == null) {
            throw new ValidacaoExcecoes("Selecione um aluno e uma disciplina.");
        }

        Nota nota1 = new Nota(aluno, disciplina, valorNota1, TipoAvaliacao.PROVA, LocalDate.now());
        Nota nota2 = new Nota(aluno, disciplina, valorNota2, TipoAvaliacao.TRABALHO, LocalDate.now());

        // Valida as notas antes de salvar
        notaVerificacao.validar(nota1);
        notaVerificacao.validar(nota2);

        // Salva as notas no banco de dados
        notaDAO.salvar(nota1);
        notaDAO.salvar(nota2);

        System.out.println("LOG: Notas salvas no banco de dados para o aluno " + aluno.getNome());


        List<Nota> notasDoAluno = new ArrayList<>();
        notasDoAluno.add(nota1);
        notasDoAluno.add(nota2);
        return calcularMedia(notasDoAluno);
    }

    public List<Nota> buscarNotasDoAluno(Aluno aluno) {
        if (aluno == null) return new ArrayList<>();
        return notaDAO.listarPorAluno(aluno);
    }

    public double calcularMedia(List<Nota> notas) throws ValidacaoExcecoes {
        if (notas == null || notas.isEmpty()) {
            return 0.0;
        }
        double somaTotal = 0.0;
        for (Nota nota : notas) {
            somaTotal += nota.getValor();
        }
        return somaTotal / notas.size();
    }

    public String verificarStatusAprovacao(double mediaFinal) {
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