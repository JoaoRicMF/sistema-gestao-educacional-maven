package modelo;

/**
 *
 * @author João Ricardo
 */

public class BoletimDisciplina {
    private final Disciplina disciplina;
    private final double nota1;
    private final double nota2;
    private final double mediaFinal;
    private final int totalAulas;
    private final int numeroFaltas;
    private final double frequencia;
    private final String status;

    public BoletimDisciplina(Disciplina disciplina, double nota1, double nota2, double mediaFinal, int totalAulas, int numeroFaltas, String status) {
        this.disciplina = disciplina;
        this.nota1 = nota1;
        this.nota2 = nota2;
        this.mediaFinal = mediaFinal;
        this.totalAulas = totalAulas;
        this.numeroFaltas = numeroFaltas;
        this.frequencia = ((double) (totalAulas - numeroFaltas) / totalAulas) * 100.0;
        this.status = status;
    }

    // Getters para todos os campos
    public Disciplina getDisciplina() { return disciplina; }
    public double getNota1() { return nota1; }
    public double getNota2() { return nota2; }
    public double getMediaFinal() { return mediaFinal; }
    public int getTotalAulas() { return totalAulas; }
    public int getNumeroFaltas() { return numeroFaltas; }
    public double getFrequencia() { return frequencia; }
    public String getStatus() { return status; }
}
