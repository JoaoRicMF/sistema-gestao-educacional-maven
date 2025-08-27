package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author João Ricardo
 */

public class Disciplina {
    private String nomeDisciplina;
    private int cargaHoraria;
    private int serieSemestre; // Adicionado
    private List<Disciplina> preRequisitos;

    public Disciplina(String nomeDisciplina, int cargaHoraria, int serieSemestre) { // Adicionado
        this.nomeDisciplina = nomeDisciplina;
        this.cargaHoraria = cargaHoraria;
        this.serieSemestre = serieSemestre; // Adicionado
        this.preRequisitos = new ArrayList<>();
    }

    public String getNomeDisciplina() {
        return nomeDisciplina;
    }
    public void setNomeDisciplina(String nome) {
        this.nomeDisciplina = nome;
    }
    public int getCargaHoraria() {
        return cargaHoraria;
    }
    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    // Getter e Setter para o novo atributo
    public int getSerieSemestre() {
        return serieSemestre;
    }

    public void setSerieSemestre(int serieSemestre) {
        this.serieSemestre = serieSemestre;
    }

    public List<Disciplina> getPreRequisitos() {
        return preRequisitos;
    }
    public void adicionarPreRequisito(Disciplina disciplina) {
        this.preRequisitos.add(disciplina);
    }
}