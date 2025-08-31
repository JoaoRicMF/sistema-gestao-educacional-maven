package service;

import dao.AlunoDAO;
import modelo.Aluno;
import excecoes.ValidacaoExcecoes;
import modelo.Endereco;
import modelo.StatusAluno;
import java.util.Optional;

/**
 *
 * @author João Ricardo
 * Camada de serviço para gerenciar as regras relacionadas a Alunos.
 */
public class AlunoService {

    private final AlunoVerificacao alunoVerificacao = new AlunoVerificacao();
    private final AlunoDAO alunoDAO = new AlunoDAO();

    public Optional<Aluno> buscarAlunoPorMatricula(String matricula) {
        return Optional.ofNullable(alunoDAO.buscarPorMatricula(matricula));
    }

    public void cadastrarNovoAluno(Aluno aluno) throws ValidacaoExcecoes {
        try {
            alunoVerificacao.validarAluno(aluno);

            if (alunoDAO.buscarPorCpf(aluno.getCpf()) != null) {
                throw new ValidacaoExcecoes("O CPF informado já está cadastrado no sistema.");
            }

            alunoDAO.salvar(aluno);
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

            alunoDAO.atualizar(aluno);
            System.out.println("LOG: Dados do aluno '" + aluno.getNome() + "' atualizados com sucesso!");
        } catch (ValidacaoExcecoes e) {
            System.out.println("Falha na atualização: " + e.getMessage());
            throw e;
        }
    }

    public void desativarAluno(Aluno aluno) throws ValidacaoExcecoes {
        try {
            alunoVerificacao.validarAluno(aluno);
            aluno.setStatusAluno(StatusAluno.INATIVO);
            alunoDAO.atualizar(aluno);
            System.out.println("LOG: Aluno '" + aluno.getNome() + "' desativado com sucesso.");
        } catch (ValidacaoExcecoes e) {
            System.out.println("Falha ao desativar aluno: " + e.getMessage());
            throw e;
        }
    }

    public void passarAlunoParaProximoSemestre(Aluno aluno) throws ValidacaoExcecoes {
        try {
            alunoVerificacao.validarAluno(aluno);
            int semestreAtual = aluno.getSemestre();
            aluno.setSemestre(semestreAtual + 1);
            alunoDAO.atualizar(aluno);
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
            alunoDAO.atualizar(aluno);
            System.out.println("LOG: Matrícula do aluno '" + aluno.getNome() + "' foi trancada.");
        } catch (ValidacaoExcecoes e) {
            System.out.println("Falha ao trancar a matrícula: " + e.getMessage());
            throw e;
        }
    }
}