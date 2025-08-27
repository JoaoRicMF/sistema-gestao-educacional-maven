package modelo;

/**
 *
 * @author João Ricardo
 */

public enum Genero {
    MASCULINO("Masculino"),
    FEMININO("Feminino"),
    OUTRO("Outro"),
    NAO_INFORMAR("Não Informar");
    //Descrição da variavel para ter uma melhor vizualização na tela

    //Atributo para guardar a descrição
    private final String descricaoGenero;

    //Construtor para receber a descrição
    Genero(String descricaoGenero) {
        this.descricaoGenero = descricaoGenero;
    }

    //Metodo para pegar a descrição
    public String getDescricaoGenero() {
        return descricaoGenero;
    }
}