package service;

import modelo.Aluno;
import excecoes.ValidacaoExcecoes;
import modelo.Endereco;
import modelo.StatusAluno;
import modelo.Genero;
import modelo.NivelAcademico;
import modelo.TipoCurso;
import modelo.UF;
import java.util.Optional;

/**
 *
 * @author João Ricardo
 */

public class AlunoService {

    private final AlunoVerificacao alunoVerificacao = new AlunoVerificacao();

    public Optional<Aluno> buscarAlunoPorMatricula(String matricula) {
        if ("2025A001".equals(matricula)) {
            Endereco enderecoJoao = new Endereco("Rua A", "123", null, "Centro", "Cidade Exemplo", UF.SP, "12345-678");
            Aluno aluno = new Aluno("João da Silva", "123.456.789-00", "11223344", Genero.MASCULINO, "10/05/2005",
                    "(11) 9999-8888", "joao@email.com", enderecoJoao, "2025A001",
                    "Ciência da Computação", TipoCurso.PRESENCIAL, 3,
                    NivelAcademico.GRADUACAO_EM_ANDAMENTO, StatusAluno.ATIVO, 0, 0, 0);
            return Optional.of(aluno);
        }
        return Optional.empty();
    }

    public void cadastrarNovoAluno(Aluno aluno) throws ValidacaoExcecoes {
        try {
            alunoVerificacao.validarAluno(aluno);
            // O objeto aluno sera enviado para o Banco de Dados
            System.out.println("LOG: Aluno '" + aluno.getNome() + "' cadastrado com sucesso!");
        } catch (ValidacaoExcecoes e) {
            System.out.println("Falha no cadastro: " + e.getMessage());
            throw e;
        }
    }
    public void atualizarDadosAluno(Aluno aluno, String novoTelefone, String novoEmail, Endereco novoEndereco) throws ValidacaoExcecoes {
        try {
            alunoVerificacao.validarAluno(aluno);
            if (novoTelefone == null || novoTelefone.trim().isEmpty() ||
                    novoEmail == null || novoEmail.trim().isEmpty() ||
                    novoEndereco == null) {
                throw new ValidacaoExcecoes("Os novos dados (telefone, email, endereço) não podem ser vazios.");
            }

            aluno.setTelefone(novoTelefone);
            aluno.setEmail(novoEmail);
            aluno.setEndereco(novoEndereco);

            // Banco de dados para a alteração
            System.out.println("LOG: Dados do aluno '" + aluno.getNome() + "' atualizados com sucesso!");
        } catch (ValidacaoExcecoes e) {
            System.out.println("Falha na atualização: " + e.getMessage());
            //GUI
            throw e;
        }
    }
    public void excluirAluno(Aluno aluno) throws ValidacaoExcecoes {
        try {
            alunoVerificacao.validarAluno(aluno);
            aluno.setStatusAluno(StatusAluno.INATIVO);

            // Banco de Dados atualizar o status do aluno
            System.out.println("LOG: Aluno '" + aluno.getNome() + "' desativado com sucesso.");
        } catch (ValidacaoExcecoes e) {
            System.out.println("Falha ao excluir aluno: " + e.getMessage());
            throw e;
        }
    }
    public void passarAlunoParaProximoSemestre(Aluno aluno) throws ValidacaoExcecoes {
        try {
            alunoVerificacao.validarAluno(aluno);
            int semestreAtual = aluno.getSemestre();
            aluno.setSemestre(semestreAtual + 1);
            System.out.println("LOG: Aluno " + aluno.getNome() + " passou para o semestre " + aluno.getSemestre());
        } catch (ValidacaoExcecoes e) {
            System.out.println("Operação falhou: " + e.getMessage());
            throw e;
        }
    }
    public void trancarMatricula(Aluno aluno) throws ValidacaoExcecoes {
        try {
            alunoVerificacao.validarAluno(aluno);
            if (aluno.getStatusAluno() != StatusAluno.ATIVO) {
                throw new ValidacaoExcecoes("Apenas alunos ATIVOS podem trancar a matrícula.");
            }
            aluno.setStatusAluno(StatusAluno.TRANCADO);
            // banco de dados
            System.out.println("LOG: Matrícula do aluno '" + aluno.getNome() + "' foi trancada.");
        } catch (ValidacaoExcecoes e) {
            System.out.println("Falha ao trancar a matrícula: " + e.getMessage());
            throw e;
        }
    }

}