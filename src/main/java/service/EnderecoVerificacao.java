package service;

import excecoes.ValidacaoExcecoes;
import modelo.Endereco;
import modelo.UF;

/**
 *
 * @author João Ricardo
 */

public class EnderecoVerificacao {
    public void validarEndereco(Endereco endereco) throws ValidacaoExcecoes {
        if (endereco == null) {
            throw new ValidacaoExcecoes("Erro: O objeto Endereço não pode ser nulo.");
        }
        validarRua(endereco.getRua());
        validarNumero(endereco.getNumero());
        validarComplemento(endereco.getComplemento());
        validarBairro(endereco.getBairro());
        validarCidade(endereco.getCidade());
        validarUf(endereco.getUf());
        validarCep(endereco.getCep());
        }
    private void validarRua(String Rua) throws ValidacaoExcecoes {
        if (Rua == null || Rua.trim().isEmpty()) {
            throw new ValidacaoExcecoes("A Rua  é obrigatório.");
        }
        if (Rua.length() < 3) {
            throw new ValidacaoExcecoes("A Rua deve ter no mínimo 3 caracteres.");
        }
        String caracteresPermitidos = ".,-º/";
        for (char c : Rua.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && !Character.isWhitespace(c) && caracteresPermitidos.indexOf(c) == -1) {
                throw new ValidacaoExcecoes("A Rua contém caracteres especiais inválidos.");
            }
        }
    }
    private void validarNumero(String numero) throws ValidacaoExcecoes {
        if (numero == null || numero.trim().isEmpty()) {
            throw new ValidacaoExcecoes("O número do endereço é obrigatório.");
        }
        for (char c : numero.toCharArray()) {
            if (!Character.isDigit(c)) {
                throw new ValidacaoExcecoes("O número do endereço deve conter apenas dígitos.");
            }
        }
    }

    private void validarComplemento(String complemento) throws ValidacaoExcecoes {
        if (complemento != null && complemento.length() > 50) {
            throw new ValidacaoExcecoes("O complemento deve ter no máximo 50 caracteres.");
        }
    }

    private void validarBairro(String bairro) throws ValidacaoExcecoes {
        if (bairro == null || bairro.trim().isEmpty()) {
            throw new ValidacaoExcecoes("O bairro é obrigatório.");
        }
        if (bairro.length() < 2) {
            throw new ValidacaoExcecoes("O bairro deve ter no mínimo 2 caracteres.");
        }
        for (char c : bairro.toCharArray()) {
            if (!Character.isLetter(c) && !Character.isWhitespace(c)) {
                throw new ValidacaoExcecoes("O bairro deve conter apenas letras e espaços.");
            }
        }
    }
    private void validarCidade(String cidade) throws ValidacaoExcecoes {
        if (cidade == null || cidade.trim().isEmpty()) {
            throw new ValidacaoExcecoes("A cidade é obrigatória.");
        }
        for (char c : cidade.toCharArray()) {
            if (!Character.isLetter(c) && !Character.isWhitespace(c)) {
                throw new ValidacaoExcecoes("A cidade deve conter apenas letras e espaços.");
            }
        }
    }
    private void validarUf(UF uf) throws ValidacaoExcecoes {
        if (uf == null) {
            throw new ValidacaoExcecoes("A UF (Estado) é obrigatória.");
        }
    }
    private void validarCep(String cep) throws ValidacaoExcecoes {
        if (cep == null || cep.trim().isEmpty()) {
            throw new ValidacaoExcecoes("O CEP é obrigatório.");
        }
        String cepLimpo = cep.replace("-", "").trim();

        if (cepLimpo.length() != 8) {
            throw new ValidacaoExcecoes("O CEP deve ter exatamente 8 dígitos.");
        }
        for (char c : cepLimpo.toCharArray()) {
            if (!Character.isDigit(c)) {
                throw new ValidacaoExcecoes("O CEP deve conter apenas dígitos numéricos.");
            }
        }
        char primeiroDigito = cepLimpo.charAt(0);
        boolean todosIguais = true;
        for (int i = 1; i < cepLimpo.length(); i++) {
            if (cepLimpo.charAt(i) != primeiroDigito) {
                todosIguais = false;
                break;
            }
        }
        if (todosIguais) {
            throw new ValidacaoExcecoes("O CEP não pode ter todos os dígitos repetidos.");
        }
    }
}


