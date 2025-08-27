package modelo;

import java.time.LocalDateTime;

/**
 *
 * @author João Ricardo
 */

public class Mural {
    private String titulo;
    private String conteudo;
    private Professor autor;
    private Turma turmaDestino;
    private LocalDateTime dataPostagem;

    public Mural(String titulo, String conteudo, Professor autor, Turma turmaDestino, LocalDateTime dataPostagem) {
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.autor = autor;
        this.turmaDestino = turmaDestino;
        this.dataPostagem = dataPostagem;
    }

    // Getters
    public String getTitulo() { return titulo; }
    public String getConteudo() { return conteudo; }
    public Professor getAutor() { return autor; }
    public Turma getTurmaDestino() { return turmaDestino; }
    public LocalDateTime getDataPostagem() { return dataPostagem; }
}