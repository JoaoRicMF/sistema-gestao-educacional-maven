package modelo;

/**
 *
 * @author João Ricardo
 */

public enum StatusAluno {
    ATIVO("Ativo"),
    INATIVO("Inativo"),
    TRANSFERIDO( "Transferido"),
    FORMADO("Formado"),
    TRANCADO("Trancado");
    //Descrição da variavel para ter uma melhor vizualização na tela

    //Atributo para guardar a descrição
    private final String descricaoStatusAluno;

    //Construtor para receber a descrição
    StatusAluno(String descricaoStatusAluno) {
        this.descricaoStatusAluno = descricaoStatusAluno;
    }

    //Metodo para pegar a descrição
    public String getDescricaoStatusAluno() {
        return descricaoStatusAluno;
    }
    @Override
    public String toString() {
        return this.descricaoStatusAluno;
    }
}
