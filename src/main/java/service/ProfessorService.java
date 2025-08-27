package service;

import excecoes.ValidacaoExcecoes;
import modelo.Disciplina;
import modelo.Professor;
import modelo.Endereco;
import modelo.Genero;
import modelo.UF;

import java.util.List;
import java.util.Optional;

public class ProfessorService {

    private final ProfessorVerificacao professorVerificacao = new ProfessorVerificacao();

    public Optional<Professor> buscarProfessorPorMatricula(String matricula, List<Disciplina> todasAsDisciplinas) {
        if ("PROF001".equals(matricula)) {
            Endereco end = new Endereco("Rua do Saber", "100", "", "Universitário", "Cidade Exemplo", UF.GO, "75000-000");
            Professor professor = new Professor("Dr. Carlos", "111.222.333-44", "556677", Genero.MASCULINO, "15/08/1980", "(62) 98877-6655", "carlos@email.com", end, "PROF001");

            if (todasAsDisciplinas != null && !todasAsDisciplinas.isEmpty()) {
                professor.adicionarDisciplina(todasAsDisciplinas.get(0));
            }
            return Optional.of(professor);
        }
        return Optional.empty();
    }

    public Professor cadastrarNovoProfessor(String nome, String cpf, String rg, Genero genero, String dataNascimento,
                                            String telefone, String email, String matricula, Endereco endereco, List<Disciplina> disciplinas) throws ValidacaoExcecoes {
        Professor novoProfessor = new Professor(nome, cpf, rg, genero, dataNascimento, telefone, email, endereco, matricula);
        novoProfessor.setDisciplinas(disciplinas);

        try {
            professorVerificacao.validar(novoProfessor);
            // Banco de Dados para salvar o novo professor
            System.out.println("LOG: Professor '" + novoProfessor.getNome() + "' cadastrado com sucesso!");
            return novoProfessor;
        } catch (ValidacaoExcecoes e) {
            System.out.println("Falha no cadastro: " + e.getMessage());
            throw e;
        }
    }

    public void atualizarDadosProfessor(Professor professor, String novoTelefone, String novoEmail, Endereco novoEndereco, List<Disciplina> novasDisciplinas) throws ValidacaoExcecoes {
        try {
            professorVerificacao.validar(professor);

            if (novoTelefone == null || novoTelefone.trim().isEmpty() ||
                    novoEmail == null || novoEmail.trim().isEmpty()) {
                throw new ValidacaoExcecoes("Os novos dados (telefone, email) não podem ser vazios.");
            }

            professor.setTelefone(novoTelefone);
            professor.setEmail(novoEmail);
            professor.setEndereco(novoEndereco);
            professor.setDisciplinas(novasDisciplinas);

            System.out.println("LOG: Dados do professor '" + professor.getNome() + "' atualizados com sucesso!");

        } catch (ValidacaoExcecoes e) {
            System.out.println("Falha na atualização: " + e.getMessage());
            throw e;
        }
    }

    public void excluirProfessor(Professor professor) throws ValidacaoExcecoes {
        try {
            professorVerificacao.validar(professor);
            //Banco de dados.
            System.out.println("LOG: Professor '" + professor.getNome() + "' excluído do sistema.");
        } catch (ValidacaoExcecoes e) {
            // Lança a exceção para a GUI saber do erro
            System.out.println("Falha ao excluir professor: " + e.getMessage());
            throw e;
        }
    }
}