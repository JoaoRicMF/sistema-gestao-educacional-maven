package modelo;

/**
 *
 * @author João Ricardo
 */

public enum Turno {
    MANHA("Manhã"),
    TARDE("Tarde"),
    NOITE("Noite"),
    INTEGRAL("Integral");
    //Descrição da variavel para ter uma melhor vizualização na tela

    //Atributo para guardar a descrição
    private final String descricao;

    //Construtor para receber a descrição
    Turno(String descricao) {
        this.descricao = descricao;
    }

    //Metodo para pegar a descrição
    public String getDescricao() {
        return descricao;
    }
}
