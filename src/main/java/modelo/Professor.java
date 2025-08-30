package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author João Ricardo
 */

public class Professor extends Pessoa {
    private String matricula;
    private List<Disciplina> disciplinas;


    public Professor(String nome, String cpf, String RG, Genero genero, String dataNascimento, String telefone, String email, Endereco endereco, String matricula) {
        super(nome, cpf, RG, genero, dataNascimento, telefone, email, endereco);
        this.matricula = matricula;
        this.disciplinas = new ArrayList<>();
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public void adicionarDisciplina(Disciplina disciplina) {
        this.disciplinas.add(disciplina);
    }

    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public void setDisciplinas(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }

    @Override
    public String toString() {
        return this.getNome();
    }
}
