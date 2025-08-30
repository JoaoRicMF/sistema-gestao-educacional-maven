package service;

import dao.DisciplinaDAO;
import excecoes.ValidacaoExcecoes;
import modelo.Disciplina;
import java.util.Optional;

public class DisciplinaService {
    private final DisciplinaVerificacao disciplinaVerificacao = new DisciplinaVerificacao();
    private final DisciplinaDAO disciplinaDAO = new DisciplinaDAO();

    public Disciplina cadastrarNovaDisciplina(String nome, int cargaHoraria, int serieSemestre) throws ValidacaoExcecoes {
        Disciplina novaDisciplina = new Disciplina(nome, cargaHoraria, serieSemestre);
        try {
            disciplinaVerificacao.validar(novaDisciplina);
            disciplinaDAO.salvar(novaDisciplina);
            System.out.println("LOG: Disciplina '" + novaDisciplina.getNomeDisciplina() + "' cadastrada com sucesso!");
            return novaDisciplina;
        } catch (ValidacaoExcecoes e) {
            System.out.println("Falha no cadastro da disciplina: " + e.getMessage());
            throw e;
        }
    }

    public void atualizarDadosDisciplina(Disciplina disciplina) throws ValidacaoExcecoes {
        try {
            disciplinaVerificacao.validar(disciplina);
            disciplinaDAO.atualizar(disciplina);
            System.out.println("LOG: Disciplina '" + disciplina.getNomeDisciplina() + "' atualizada com sucesso!");
        } catch (ValidacaoExcecoes e) {
            System.out.println("Falha na atualização da disciplina: " + e.getMessage());
            throw e;
        }
    }

    public void excluirDisciplina(Disciplina disciplina) throws ValidacaoExcecoes {
        try {
            disciplinaVerificacao.validar(disciplina);
            disciplinaDAO.excluir(disciplina.getId());
            System.out.println("LOG: Disciplina '" + disciplina.getNomeDisciplina() + "' excluída do sistema.");
        } catch (ValidacaoExcecoes e) {
            System.out.println("Falha ao excluir disciplina: " + e.getMessage());
            throw e;
        }
    }
}