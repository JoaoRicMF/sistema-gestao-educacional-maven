package modelo;

import java.time.LocalDate;

/**
 *
 * @author João Ricardo
 */

public class Nota {
    //Atributos
    private int id;
    private Aluno aluno;
    private Disciplina disciplina;
    private double valor;
    private TipoAvaliacao tipoAvaliacao;
    private LocalDate data; // data da Prova

    //Construtor
    public Nota(Aluno aluno, Disciplina disciplina, double valor, TipoAvaliacao tipoAvaliacao, LocalDate data) {
        this.aluno = aluno;
        this.disciplina = disciplina;
        this.valor = valor;
        this.tipoAvaliacao = tipoAvaliacao;
        this.data = data;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Aluno getAluno() {
        return aluno;
    }
    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }
    public Disciplina getDisciplina() {
        return disciplina;
    }
    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }
    public double getValor() {
        return valor;
    }
    public void setValor(double valor) {
        this.valor = valor;
    }
    public TipoAvaliacao getTipoAvaliacao() {
        return tipoAvaliacao;
    }
    public void setTipoAvaliacao(TipoAvaliacao tipoAvaliacao) {
        this.tipoAvaliacao = tipoAvaliacao;
    }
    public LocalDate getData() {
        return data;
    }
    public void setData(LocalDate data) {
        this.data = data;
    }
}