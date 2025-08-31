package service;

import excecoes.ValidacaoExcecoes;
import modelo.Mural;
import modelo.Professor;
import modelo.Turma;
import dao.MuralDAO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author João Ricardo
 * Camada de serviço para gerenciar as regras relacionadas a Mural.
 */

public class MuralService {

    private final MuralDAO muralDAO = new MuralDAO();

    public void postarNoMural(String titulo, String conteudo, Professor autor, Turma turma) throws ValidacaoExcecoes {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new ValidacaoExcecoes("O título do aviso é obrigatório.");
        }
        if (conteudo == null || conteudo.trim().isEmpty()) {
            throw new ValidacaoExcecoes("O conteúdo do aviso é obrigatório.");
        }
        if (autor == null || turma == null) {
            throw new ValidacaoExcecoes("Autor e turma de destino são obrigatórios.");
        }

        Mural novaPostagem = new Mural(titulo, conteudo, autor, turma, LocalDateTime.now());

        // Salva a postagem no banco de dados
        muralDAO.salvar(novaPostagem);

        System.out.println("LOG: Nova postagem salva no banco para a turma " + turma.getNomeTurma());
    }

    public List<Mural> verPostagensDaTurma(Turma turma) {
        if (turma == null) {
            return new ArrayList<>();
        }
        return muralDAO.listarPorTurma(turma);
    }
}