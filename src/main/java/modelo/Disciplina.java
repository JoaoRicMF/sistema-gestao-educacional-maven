package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author João Ricardo
 */

public class Disciplina {
    private int id;
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

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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
    @Override
    public String toString() {
        return this.getNomeDisciplina();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Disciplina that = (Disciplina) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}