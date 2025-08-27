package modelo;

/**
 *
 * @author João Ricardo
 */

public enum NivelAcademico {
    ENSINO_MEDIO_COMPLETO("Ensino médio completo"),
    GRADUACAO_EM_ANDAMENTO("Graduação em andamento"),
    GRADUACAO_COMPLETA("Graducação completa"),
    POS_GRADUACAO_EM_ANDAMENTO("Pós graduação em andamento"),
    POS_GRADUACAO_COMPLETA("Pós graduação completa"),
    MESTRADO("Mestrado"),
    DOUTORADO("Doutorado");
    //Descrição da variavel para ter uma melhor vizualização na tela

    //Atributo para guardar a descrição
    private final String descricaoNivelAcademico;

    //Construtor para receber a descrição
    NivelAcademico(String descricaoNivelAcademico) {
        this.descricaoNivelAcademico = descricaoNivelAcademico;
    }

    //Metodo para pegar a descrição
    public String getDescricaoNivelAcademico() {
        return descricaoNivelAcademico;
    }
    @Override
    public String toString() {
        return this.descricaoNivelAcademico;
    }
}
