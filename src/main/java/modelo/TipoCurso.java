package modelo;

/**
 *
 * @author João Ricardo
 */

public enum TipoCurso {
    PRESENCIAL("Presencial"),
    ONLINE("Online");
    //Descrição da variavel para ter uma melhor vizualização na tela

    //Atributo para guardar a descrição
    private final String descricaoTipoCurso;

    //Construtor para receber a descrição
    TipoCurso(String descricaoTipoCurso) {
        this.descricaoTipoCurso = descricaoTipoCurso;
    }

    //Metodo para pegar a descrição
    public String getDescricaoTipoCurso() {
        return descricaoTipoCurso;
    }
    @Override
    public String toString() {
        return this.descricaoTipoCurso; // Retorna a descrição para ser exibida na JComboBox
    }

}
