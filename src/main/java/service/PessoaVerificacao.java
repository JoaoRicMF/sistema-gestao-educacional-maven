package service;

import excecoes.ValidacaoExcecoes;
import modelo.Genero;
import modelo.Pessoa;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 *
 * @author João Ricardo
 * Camada de verificação para gerenciar as regras relacionadas a Pessoa.
 */

public class PessoaVerificacao {
    private final EnderecoVerificacao enderecoVerificacao = new EnderecoVerificacao();
    public void validarPessoa(Pessoa pessoa) throws ValidacaoExcecoes {
        if (pessoa == null) {
            throw new ValidacaoExcecoes("O objeto Pessoa não pode ser nulo.");
        }
        validarNome(pessoa.getNome());
        validarCpf(pessoa.getCpf());
        validarRg(pessoa.getRG());
        validarGenero(pessoa.getGenero());
        validarDataNascimento(pessoa.getDataNascimento());
        validarTelefone(pessoa.getTelefone());
        validarEmail(pessoa.getEmail());
        if (pessoa.getEndereco() == null) {
            throw new ValidacaoExcecoes("O endereço é obrigatório.");
        }
        enderecoVerificacao.validarEndereco(pessoa.getEndereco());
    }
    private void validarNome(String nome) throws ValidacaoExcecoes {
        if (nome == null || nome.trim().isEmpty()) {
            throw new ValidacaoExcecoes("Erro de validação: O nome do aluno é obrigatório.");
        }
        if (!nome.equals(nome.trim())) {
            throw new ValidacaoExcecoes("Erro de formato: O nome não pode conter espaços no começo ou no fim.");
        }
        if (nome.length() < 2 || nome.length() > 50) {
            throw new ValidacaoExcecoes("Erro de tamanho: O nome deve ter entre 2 e 50 caracteres.");
        }
        for (char c : nome.toCharArray()) {
            if (!Character.isLetter(c) && !Character.isWhitespace(c)) {
                throw new ValidacaoExcecoes("Erro de formato: O nome deve conter apenas letras e espaços.");
            }
        }
    }
    private void validarCpf(String cpf) throws ValidacaoExcecoes {
        if (cpf == null || cpf.trim().isEmpty()){
            throw new ValidacaoExcecoes("O CPF é obrigatório.");
        }

        String cpfLimpo = cpf.replace(".", "").replace("-", "");
        if (cpfLimpo.length() != 11) {
            throw new ValidacaoExcecoes("O CPF deve ter exatamente 11 dígitos.");
        }
        for (char c : cpfLimpo.toCharArray()) {
            if (!Character.isDigit(c)) {
                throw new ValidacaoExcecoes("O CPF deve conter apenas dígitos numéricos.");
            }
        }
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += (Character.getNumericValue(cpfLimpo.charAt(i)) * (10 - i));
        }
        int resto = soma % 11;
        int primeiroDigitoVerificador = (resto < 2) ? 0 : (11 - resto);

        if (primeiroDigitoVerificador != Character.getNumericValue(cpfLimpo.charAt(9))) {
            throw new ValidacaoExcecoes("O CPF informado é inválido (dígito verificador não confere).");
        }
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += (Character.getNumericValue(cpfLimpo.charAt(i)) * (11 - i));
        }
        resto = soma % 11;
        int segundoDigitoVerificador = (resto < 2) ? 0 : (11 - resto);

        if (segundoDigitoVerificador != Character.getNumericValue(cpfLimpo.charAt(10))) {
            throw new ValidacaoExcecoes("O CPF informado é inválido (dígito verificador não confere).");
        }
    }
    private void validarRg(String rg) throws ValidacaoExcecoes {
        if (rg == null || rg.trim().isEmpty()) {
            throw new ValidacaoExcecoes("Erro de validação: O RG é obrigatório.");
        }

        if (!rg.equals(rg.trim())) {
            throw new ValidacaoExcecoes("Erro de formato: O RG não pode conter espaços no começo ou no fim.");
        }

        String rgLimpo = rg.replace(".", "").replace("-", "");

        if (rgLimpo.length() < 5 || rgLimpo.length() > 14) {
            throw new ValidacaoExcecoes("Erro de tamanho: O RG deve ter entre 5 e 14 caracteres (sem formatação).");
        }

        boolean encontrouDigito = false;
        for (char c : rgLimpo.toCharArray()) {
            if (Character.isDigit(c)) {
                encontrouDigito = true;
            }
            else if (Character.isLetter(c)) {
                if (encontrouDigito) {
                    throw new ValidacaoExcecoes("Erro de formato: Letras no RG são permitidas apenas no início.");
                }
            }
            else {
                throw new ValidacaoExcecoes("Erro de formato: O RG contém caracteres especiais inválidos.");
            }
        }
    }
    private void validarGenero(Genero genero) throws ValidacaoExcecoes {
        if (genero == null) {
            throw new ValidacaoExcecoes("Erro de validação: O gênero é obrigatório.");
        }
    }
    private void validarDataNascimento(String dataNascimentoStr) throws ValidacaoExcecoes {
        if (dataNascimentoStr == null || dataNascimentoStr.trim().isEmpty()) {
            throw new ValidacaoExcecoes("Erro de validação: A data de nascimento é obrigatória.");
        }
        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataNascimento;

        try {
            dataNascimento = LocalDate.parse(dataNascimentoStr, formatoData);
        } catch (DateTimeParseException e) {
            throw new ValidacaoExcecoes("Erro de formato: A data de nascimento deve estar no formato dd/MM/yyyy e ser uma data válida.");
        }
        LocalDate hoje = LocalDate.now();
        if (dataNascimento.isAfter(hoje)) {
            throw new ValidacaoExcecoes("Erro de validação: A data de nascimento não pode ser uma data futura.");
        }
        int idade = Period.between(dataNascimento, hoje).getYears();
        if (idade > 125) {
            throw new ValidacaoExcecoes("Erro de validação: Idade máxima de 125 anos excedida.");
        }
        if (dataNascimento.getYear() < 1900) {
            throw new ValidacaoExcecoes("Erro de validação: O ano de nascimento parece ser inválido (anterior a 1900).");
        }
    }
    private void validarTelefone(String telefone) throws ValidacaoExcecoes {
        if (telefone == null || telefone.trim().isEmpty()) {
            throw new ValidacaoExcecoes("Erro de validação: O telefone é obrigatório.");
        }
        if (!telefone.equals(telefone.trim())) {
            throw new ValidacaoExcecoes("Erro de formato: O telefone não pode conter espaços no começo ou no fim.");
        }
        String telefoneLimpo = telefone.replace("(", "").replace(")", "").replace("-", "").replace(" ", "");
        for (char c : telefoneLimpo.toCharArray()) {
            if (!Character.isDigit(c)) {
                throw new ValidacaoExcecoes("Erro de formato: O telefone deve conter apenas números e formatação básica (parênteses, hífen, espaços).");
            }
        }
        if (telefoneLimpo.length() < 10 || telefoneLimpo.length() > 11) {
            throw new ValidacaoExcecoes("Erro de formato: O telefone deve ter 10 (fixo) ou 11 (celular) dígitos, incluindo o DDD.");
        }
        char primeiroDigito = telefoneLimpo.charAt(0);
        boolean todosRepetidos = true;
        for (int i = 1; i < telefoneLimpo.length(); i++) {
            if (telefoneLimpo.charAt(i) != primeiroDigito) {
                todosRepetidos = false;
                break;
            }
        }
        if (todosRepetidos) {
            throw new ValidacaoExcecoes("Erro de validação: O número de telefone parece ser inválido (todos os dígitos são iguais).");
        }
        int ddd = Integer.parseInt(telefoneLimpo.substring(0, 2));
        if (ddd < 11 || ddd > 99) {
            throw new ValidacaoExcecoes("Erro de validação: O DDD do telefone é inválido.");
        }
        if (telefoneLimpo.length() == 11) {
            if (telefoneLimpo.charAt(2) != '9') {
                throw new ValidacaoExcecoes("Erro de validação: O prefixo para números de celular (11 dígitos) deve ser 9.");
            }
        }
    }
    private void validarEmail(String email) throws ValidacaoExcecoes {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidacaoExcecoes("Erro de validação: O e-mail é obrigatório.");
        }
        if (email.contains(" ")) {
            throw new ValidacaoExcecoes("Erro de formato: O e-mail não pode conter espaços em branco.");
        }

        int indiceArroba = email.indexOf('@');
        int ultimoIndiceArroba = email.lastIndexOf('@');

        if (indiceArroba == -1 || indiceArroba != ultimoIndiceArroba) {
            throw new ValidacaoExcecoes("Erro de formato: O e-mail deve conter exatamente um caractere '@'.");
        }
        if (indiceArroba == 0 || indiceArroba == email.length() - 1) {
            throw new ValidacaoExcecoes("Erro de formato: O caractere '@' não pode estar no início ou no fim do e-mail.");
        }

        int indicePonto = email.indexOf('.', indiceArroba);

        if (indicePonto == -1) {
            throw new ValidacaoExcecoes("Erro de formato: O e-mail deve conter pelo menos um ponto '.' após o '@'.");
        }
        if (indicePonto == email.length() - 1) {
            throw new ValidacaoExcecoes("Erro de formato: O e-mail não pode terminar com um ponto '.'.");
        }
    }
}
