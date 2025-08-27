package service;

import excecoes.ValidacaoExcecoes;
import modelo.Disciplina;
import modelo.Turma;
import modelo.Aluno;
import modelo.Genero;
import modelo.NivelAcademico;
import modelo.StatusAluno;
import modelo.TipoCurso;
import modelo.Turno;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author João Ricardo
 */

public class TurmaService {

    private final DisciplinaVerificacao disciplinaVerificacao = new DisciplinaVerificacao();
    private final TurmaVerificacao turmaVerificacao = new TurmaVerificacao();
    private final AlunoVerificacao  alunoVerificacao = new AlunoVerificacao();

    public Optional<Turma> buscarTurma(String nome, List<Disciplina> todasAsDisciplinas) {
        if ("Turma A".equalsIgnoreCase(nome) && todasAsDisciplinas != null && !todasAsDisciplinas.isEmpty()) {
            Turma turma = new Turma(nome, 1, Turno.MANHA);
            turma.adicionarDisciplina(todasAsDisciplinas.get(0));
            // Simula um aluno matriculado
            Aluno alunoTeste = new Aluno("Aluno Teste", "12312312312", "1234567", Genero.NAO_INFORMAR, "10/10/2000", "11988887777", "aluno@escola.com", null, "ALN123456", "Ciência da Computação", TipoCurso.PRESENCIAL, 1, NivelAcademico.GRADUACAO_EM_ANDAMENTO, StatusAluno.ATIVO, 0, 0, 0);
            turma.adicionarAluno(alunoTeste);
            return Optional.of(turma);
        }
        return Optional.empty();
    }

    public Turma cadastrarNovaTurma(String nomeTurma, int serieAno, Turno turno) throws ValidacaoExcecoes {
        Turma novaTurma = new Turma(nomeTurma, serieAno, turno);
        turmaVerificacao.validarTurma(novaTurma);
        // Lógica para salvar no banco de dados
        System.out.println("LOG: Turma '" + novaTurma.getNomeTurma() + "' cadastrada com sucesso!");
        return novaTurma;
    }

    public void atualizarTurma(Turma turma, String novoNome, int novaSerieAno, Turno novoTurno) throws ValidacaoExcecoes {
        turmaVerificacao.validarTurma(turma); // Valida o objeto existente

        // Cria um objeto temporário para validar os novos dados
        Turma dadosParaValidar = new Turma(novoNome, novaSerieAno, novoTurno);
        turmaVerificacao.validarTurma(dadosParaValidar);

        // Se a validação for bem-sucedida, atualiza o objeto original
        turma.setNomeTurma(novoNome);
        turma.setSemestre(novaSerieAno);
        turma.setTurno(novoTurno);

        // Lógica para atualizar no banco de dados
        System.out.println("LOG: Turma '" + turma.getNomeTurma() + "' atualizada com sucesso!");
    }


    public void adicionarDisciplinaNaGrade(Disciplina disciplina, Turma turma) throws ValidacaoExcecoes {

        try {
            disciplinaVerificacao.validar(disciplina);
            turmaVerificacao.validarTurma(turma);

            turma.adicionarDisciplina(disciplina);
            System.out.println("LOG: Disciplina '" + disciplina.getNomeDisciplina() + "' adicionada à grade da turma '" + turma.getNomeTurma() + "'.");

        } catch (ValidacaoExcecoes e) {
            System.out.println("Falha na operação: " + e.getMessage());
            throw e;
        }
    }
    public void verificarDisponibilidadeDeVagas(Turma turma) throws ValidacaoExcecoes {
        final int CAPACIDADE_MAXIMA = 50;

        try {
            turmaVerificacao.validarTurma(turma);

            if (turma.getAlunos().size() >= CAPACIDADE_MAXIMA) {
                throw new ValidacaoExcecoes("A turma '" + turma.getNomeTurma() + "' está lotada.");
            }

            System.out.println("LOG: A turma '" + turma.getNomeTurma() + "' ainda possui vagas.");

        } catch (ValidacaoExcecoes e) {
            System.out.println("Falha na verificação: " + e.getMessage());
            throw e;
        }
    }
    public void removerAlunoDaTurma(Aluno aluno, Turma turma) throws ValidacaoExcecoes {
        try {
            alunoVerificacao.validarAluno(aluno);
            turmaVerificacao.validarTurma(turma);

            if (!turma.getAlunos().contains(aluno)) {
                throw new ValidacaoExcecoes("O aluno '" + aluno.getNome() + "' não está matriculado nesta turma.");
            }

            turma.removerAluno(aluno);
            System.out.println("LOG: Aluno '" + aluno.getNome() + "' removido da turma '" + turma.getNomeTurma() + "'.");

        } catch (ValidacaoExcecoes e) {
            System.out.println("Falha ao remover aluno: " + e.getMessage());
            throw e;
        }
    }
}