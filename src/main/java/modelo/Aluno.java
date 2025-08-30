package modelo;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 *
 * @author João Ricardo
 */

public class Aluno extends Pessoa {
    private String matricula;//Matricula aluno na istituição
    private String curso;
    private TipoCurso tipoCurso;
    private int semestre;
    private NivelAcademico nivelAcademico; //Enum
    private StatusAluno statusAluno; //Enum
    private float nota;
    private float frequencia;
    private float media;
    private final BooleanProperty presente = new SimpleBooleanProperty();

    //Construtor
    public Aluno(String nome, String cpf,String RG,Genero genero, String dataNascimento, String telefone, String email, Endereco endereco, String matricula, String curso,TipoCurso tipoCurso, int semestre, NivelAcademico grauDeIntrucao, StatusAluno statusAluno, float nota, float frequencia, float media) {
        super(nome, cpf,RG,genero,dataNascimento,telefone,email,endereco);
        this.matricula = matricula;
        this.curso = curso;
        this.tipoCurso = tipoCurso;
        this.semestre = semestre;
        this.nivelAcademico = grauDeIntrucao;
        this.statusAluno = statusAluno;
        this.nota = nota;
        this.frequencia = frequencia;
        this.media = media;
    }
    public String getMatricula() {
        return matricula;
    }
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
    public String getCurso() {
        return curso;
    }
    public void setCurso(String curso) {
        this.curso = curso;
    }
    public TipoCurso getTipoCurso() {
        return tipoCurso;
    }
    public void setTipoCurso(TipoCurso tipoCurso) {
        this.tipoCurso = tipoCurso;
    }
    public int getSemestre() {
        return semestre;
    }
    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }
    public NivelAcademico getNivelAcademico() {
        return nivelAcademico;
    }
    public void setNivelAcademico(NivelAcademico nivelAcademico) {
        this.nivelAcademico = nivelAcademico;
    }
    public StatusAluno getStatusAluno() {
        return statusAluno;
    }
    public void setStatusAluno(StatusAluno statusAluno) {
        this.statusAluno = statusAluno;
    }
    public float getNota() {
        return nota;
    }
    public void setNota(float nota) {
        this.nota = nota;
    }
    public float getFrequencia() {
        return frequencia;
    }
    public void setFrequencia(float frequencia) {
        this.frequencia = frequencia;
    }
    public float getMedia() {
        return media;
    }
    public void setMedia(float media) {
        this.media = media;
    }
    public final BooleanProperty presenteProperty() { return this.presente; }
    public final boolean isPresente() { return this.presenteProperty().get(); }
    public final void setPresente(final boolean presente) { this.presenteProperty().set(presente); }
    @Override
    public String toString() {
        return this.getNome();
    }
}
