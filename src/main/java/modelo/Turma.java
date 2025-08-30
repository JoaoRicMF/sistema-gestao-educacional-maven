package modelo;

import java.util.ArrayList;
import java.util.List;

public class Turma {
    private int id;
    private String nomeTurma;
    private int semestre;
    private Turno turno;
    private Professor professor;
    private List<Aluno> alunos;
    private List<Disciplina> disciplinas;

    public Turma(String nomeTurma, int semestre, Turno turno) {
        this.nomeTurma = nomeTurma;
        this.semestre = semestre;
        this.turno = turno;
        this.alunos = new ArrayList<>();
        this.disciplinas = new ArrayList<>();
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public void adicionarAluno(Aluno aluno){
        alunos.add(aluno);
    }
    public void adicionarDisciplina(Disciplina disciplina){
        disciplinas.add(disciplina);
    }
    public void listarAlunos(){
        System.out.println("Alunos da turma " + nomeTurma + ":\n ");
        for(Aluno aluno : alunos){
            System.out.println(aluno.getNome());
        }
    }
    public void listarDisciplinas(){
        System.out.println("Disciplinas da turma " + nomeTurma + ":\n");
        for(Disciplina disciplina : disciplinas){
            System.out.println(disciplina.getNomeDisciplina());
        }
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNomeTurma() {
        return nomeTurma;
    }
    public void setNomeTurma(String nomeTurma) {
        this.nomeTurma = nomeTurma;
    }
    public int getSemestre() {
        return semestre;
    }
    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }
    public Turno getTurno() {
        return turno;
    }
    public void setTurno(Turno turno) {
        this.turno = turno;
    }
    public List<Aluno> getAlunos() {
        return alunos;
    }
    public void setAlunos(List<Aluno> alunos) {
        this.alunos = alunos;
    }
    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }
    public void setDisciplinas(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }
    public void removerAluno(Aluno aluno){
        alunos.remove(aluno);
    }
    @Override
    public String toString() {
        return this.getNomeTurma();
    }
}