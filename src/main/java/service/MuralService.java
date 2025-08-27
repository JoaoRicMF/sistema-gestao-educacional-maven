package service;

import excecoes.ValidacaoExcecoes;
import modelo.Mural;
import modelo.Professor;
import modelo.Turma;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author João Ricardo
 */
public class MuralService {
    // Simula um "banco de dados" para as postagens do mural
    private final List<Mural> postagens = new ArrayList<>();

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
        postagens.add(novaPostagem);
        System.out.println("LOG: Nova postagem no mural para a turma " + turma.getNomeTurma());
    }

    public List<Mural> verPostagensDaTurma(Turma turma) {
        if (turma == null) {
            return new ArrayList<>();
        }
        return postagens.stream()
                .filter(p -> p.getTurmaDestino().equals(turma))
                .sorted((p1, p2) -> p2.getDataPostagem().compareTo(p1.getDataPostagem())) // Ordena do mais novo para o mais antigo
                .collect(Collectors.toList());
    }
}