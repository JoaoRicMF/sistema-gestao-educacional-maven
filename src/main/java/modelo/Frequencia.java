package modelo;

import java.time.LocalDate;

/**
 *
 * @author João Ricardo
 */

public class Frequencia {

    //Atributos
    private int id;
    private Aluno aluno;
    private Disciplina disciplina;
    private LocalDate data;
    private boolean presente;

    //Construtor
    public Frequencia(Aluno aluno, Disciplina disciplina, LocalDate data, boolean presente) {
        this.aluno = aluno;
        this.disciplina = disciplina;
        this.data = data;
        this.presente = presente;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Aluno getAluno() { return aluno; }
    public void setAluno(Aluno aluno) { this.aluno = aluno; }
    public Disciplina getDisciplina() { return disciplina; }
    public void setDisciplina(Disciplina disciplina) { this.disciplina = disciplina; }
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
    public boolean isPresente() { return presente; }
    public void setPresente(boolean presente) { this.presente = presente; }
}