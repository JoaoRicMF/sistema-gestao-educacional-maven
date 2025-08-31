package excecoes;

/**
 *
 * @author João Ricardo
 */

public class ValidacaoExcecoes extends Exception{
    public ValidacaoExcecoes(String mensagem) {
        super(mensagem); // Passa a mensagem de erro
    }
}
