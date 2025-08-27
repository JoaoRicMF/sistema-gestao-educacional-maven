package service;

import excecoes.ValidacaoExcecoes;
import modelo.Disciplina;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author João Ricardo
 */

public class DisciplinaService {
    private final DisciplinaVerificacao disciplinaVerificacao = new DisciplinaVerificacao();

    public Optional<Disciplina> buscarDisciplinaPorNome(String nome, List<Disciplina> disciplinas) {
        if (disciplinas != null) {
            for (Disciplina d : disciplinas) {
                if (d.getNomeDisciplina().equalsIgnoreCase(nome.trim())) {
                    return Optional.of(d);
                }
            }
        }
        return Optional.empty();
    }

    public Disciplina cadastrarNovaDisciplina(String nome, int cargaHoraria, int serieSemestre) throws ValidacaoExcecoes {
        Disciplina novaDisciplina = new Disciplina(nome, cargaHoraria, serieSemestre);
        try {
            disciplinaVerificacao.validar(novaDisciplina);
            // 2. A disciplina sera salva no banco de dados
            System.out.println("LOG: Disciplina '" + novaDisciplina.getNomeDisciplina() + "' cadastrada com sucesso!");
            return novaDisciplina;
        } catch (ValidacaoExcecoes e) {
            System.out.println("Falha no cadastro da disciplina: " + e.getMessage());
            throw e;
        }
    }

    public void atualizarDadosDisciplina(Disciplina disciplina, String novoNome, int novaCargaHoraria, int novoSemestre) throws ValidacaoExcecoes {
        try {
            // Valida o objeto original para garantir que ele seja um objeto válido antes de tentar atualizar
            disciplinaVerificacao.validar(disciplina);

            // Cria um objeto temporário com os novos dados para validação
            Disciplina dadosParaValidar = new Disciplina(novoNome, novaCargaHoraria, novoSemestre);
            disciplinaVerificacao.validar(dadosParaValidar);

            // Se a validação passar, atualiza o objeto original
            disciplina.setNomeDisciplina(novoNome);
            disciplina.setCargaHoraria(novaCargaHoraria);
            disciplina.setSerieSemestre(novoSemestre);

            System.out.println("LOG: Disciplina '" + disciplina.getNomeDisciplina() + "' atualizada com sucesso!");

        } catch (ValidacaoExcecoes e) {
            System.out.println("Falha na atualização da disciplina: " + e.getMessage());
            throw e;
        }
    }

    public void excluirDisciplina(Disciplina disciplina) throws ValidacaoExcecoes {
        try {
            disciplinaVerificacao.validar(disciplina);
            // Lógica para excluir do banco de dados
            System.out.println("LOG: Disciplina '" + disciplina.getNomeDisciplina() + "' excluída do sistema.");
        } catch (ValidacaoExcecoes e) {
            System.out.println("Falha ao excluir disciplina: " + e.getMessage());
            throw e;
        }
    }
}