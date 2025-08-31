package modelo;

/**
 *
 * @author João Ricardo
 */

public abstract class Pessoa {

    //Atributos
    private String nome;
    private String cpf;
    private String RG;
    private Genero genero;
    private String dataNascimento;
    private String telefone;
    private String email;
    private Endereco endereco;

    //Construtor
    public Pessoa(String nome, String cpf, String Rg, Genero genero, String dataNascimento, String telefone, String email, Endereco endereco) {
        this.nome = nome;
        this.cpf = cpf;
        this.RG = Rg;
        this.genero = genero;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    public String getRG() {
        return RG;
    }
    public void setRG(String RG) {
        this.RG = RG;
    }
    public Genero getGenero() {
        return genero;
    }
    public void setGenero(Genero genero) {
        this.genero = genero;
    }
    public String getDataNascimento() {
        return dataNascimento;
    }
    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
    public String getTelefone() {
        return telefone;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Endereco getEndereco() {
        return endereco;
    }
    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

}
