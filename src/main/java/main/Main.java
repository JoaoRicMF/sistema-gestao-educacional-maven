package main;

import database.TesteConexao;
import gui.PrincipalFX;

public class Main {

    public static void main(String[] args) {
        //Teste banco de dados
        TesteConexao teste = new TesteConexao();
        teste.testarBDB();
        // Inicia a aplicação JavaFX chamando o metodo launch da classe MainFX
        PrincipalFX.launch(PrincipalFX.class, args);
    }
}