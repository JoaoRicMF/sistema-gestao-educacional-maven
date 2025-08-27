package service;

import modelo.Aluno;
import modelo.Disciplina;
import modelo.Professor;
import modelo.Turma;

import java.util.List;

/**
 *
 * @author João Ricardo
 */

public class RelatorioService {
    public void gerarRelatorioAlunosPorTurma(Turma turma) {
        if (turma == null) {
            System.out.println("Erro: A turma não pode ser nula.");
            return;
        }

        System.out.println("=============================================");
        System.out.println(" Relatório de Alunos da Turma: " + turma.getNomeTurma());
        System.out.println("=============================================");

        List<Aluno> alunos = turma.getAlunos();
        if (alunos.isEmpty()) {
            System.out.println("A turma não possui alunos matriculados.");
        } else {
            for (Aluno aluno : alunos) {
                System.out.println(" - Matrícula: " + aluno.getMatricula() + ", Nome: " + aluno.getNome());
            }
        }
        System.out.println("=============================================\n");
    }

    public void gerarRelatorioDisciplinasPorTurma(Turma turma) {
        if (turma == null) {
            System.out.println("Erro: A turma não pode ser nula.");
            return;
        }

        System.out.println("=============================================");
        System.out.println(" Relatório de Disciplinas da Turma: " + turma.getNomeTurma());
        System.out.println("=============================================");

        List<Disciplina> disciplinas = turma.getDisciplinas();
        if (disciplinas.isEmpty()) {
            System.out.println("A turma não possui disciplinas cadastradas.");
        } else {
            for (Disciplina disciplina : disciplinas) {
                System.out.println(" - Disciplina: " + disciplina.getNomeDisciplina() + ", Carga Horária: " + disciplina.getCargaHoraria() + "h");
            }
        }
        System.out.println("=============================================\n");
    }

    public void gerarRelatorioGeralDeProfessores(List<Professor> professores) {
        if (professores == null || professores.isEmpty()) {
            System.out.println("Não há professores cadastrados no sistema.");
            return;
        }

        System.out.println("=============================================");
        System.out.println("     Relatório Geral de Professores");
        System.out.println("=============================================");

        for (Professor professor : professores) {
            System.out.println(" - Matrícula: " + professor.getMatricula() + ", Nome: " + professor.getNome() + ", Email: " + professor.getEmail());
        }
        System.out.println("=============================================\n");
    }

    public void gerarRelatorioGeralDeAlunos(List<Aluno> alunos) {
        if (alunos == null || alunos.isEmpty()) {
            System.out.println("Não há alunos cadastrados no sistema.");
            return;
        }

        System.out.println("=============================================");
        System.out.println("       Relatório Geral de Alunos");
        System.out.println("=============================================");

        for (Aluno aluno : alunos) {
            System.out.println(" - Matrícula: " + aluno.getMatricula() + ", Nome: " + aluno.getNome() + ", Curso: " + aluno.getCurso());
        }
        System.out.println("=============================================\n");
    }
}