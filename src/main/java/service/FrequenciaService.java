package service;

import excecoes.ValidacaoExcecoes;
import modelo.Aluno;
import modelo.Disciplina;
import modelo.Frequencia;
import modelo.Turma;
import dao.FrequenciaDAO; // Importar o DAO
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FrequenciaService {

    private final FrequenciaDAO frequenciaDAO = new FrequenciaDAO(); // Instanciar o DAO

    public void lancarFrequencia(Turma turma, Disciplina disciplina, LocalDate data, List<Aluno> alunosPresentes) throws ValidacaoExcecoes {
        if (turma == null || disciplina == null || data == null || alunosPresentes == null) {
            throw new ValidacaoExcecoes("Turma, disciplina, data e lista de alunos são obrigatórios.");
        }

        List<Frequencia> registrosParaSalvar = new ArrayList<>();
        for (Aluno aluno : turma.getAlunos()) {
            boolean presente = alunosPresentes.contains(aluno);
            registrosParaSalvar.add(new Frequencia(aluno, disciplina, data, presente));
        }

        // Salva a lista de frequências no banco de dados
        frequenciaDAO.salvar(registrosParaSalvar);

        System.out.println("LOG: Frequência para a turma '" + turma.getNomeTurma() + "' na data " + data + " foi salva no banco de dados.");
    }
    public int[] consultarFaltasEPresencas(Aluno aluno, Disciplina disciplina) {
        if (aluno == null || disciplina == null) return new int[]{0, 0};
        return frequenciaDAO.calcularTotalAulasEPresencas(aluno, disciplina);
    }
}