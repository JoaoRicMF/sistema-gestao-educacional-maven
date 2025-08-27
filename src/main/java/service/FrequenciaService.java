package service;

import excecoes.ValidacaoExcecoes;
import modelo.Aluno;
import modelo.Disciplina;
import modelo.Frequencia;
import modelo.Turma;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author João Ricardo
 */
public class FrequenciaService {
    private final List<Frequencia> registrosFrequencia = new ArrayList<>(); // Simula um banco de dados

    public void lancarFrequencia(Turma turma, Disciplina disciplina, LocalDate data, List<Aluno> alunosPresentes) throws ValidacaoExcecoes {
        if (turma == null || disciplina == null || data == null || alunosPresentes == null) {
            throw new ValidacaoExcecoes("Turma, disciplina, data e lista de alunos são obrigatórios.");
        }

        for (Aluno aluno : turma.getAlunos()) {
            boolean presente = alunosPresentes.contains(aluno);
            registrosFrequencia.add(new Frequencia(aluno, disciplina, data, presente));
        }
        System.out.println("LOG: Frequência para a turma '" + turma.getNomeTurma() + "' na data " + data + " foi salva.");
    }

    public double calcularFrequencia(Aluno aluno, Disciplina disciplina) {
        long totalAulas = registrosFrequencia.stream()
                .filter(f -> f.getDisciplina().equals(disciplina))
                .map(Frequencia::getData)
                .distinct()
                .count();

        if (totalAulas == 0) return 100.0; // Evita divisão por zero

        long aulasPresente = registrosFrequencia.stream()
                .filter(f -> f.getAluno().equals(aluno) && f.getDisciplina().equals(disciplina) && f.isPresente())
                .count();

        return ((double) aulasPresente / totalAulas) * 100.0;
    }
}