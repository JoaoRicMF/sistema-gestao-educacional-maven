<div align="center">
  <h1>Sistema de Gestão Educacional (SGE)</h1>
  <p>
    <img src="https://img.shields.io/badge/status-em--desenvolvimento-yellow" alt="Status do Projeto: Em Desenvolvimento">
  </p>
</div>

<hr>

<h3>
  <a id="colaboradores"></a>
  👥 Colaboradores
</h3>

<p>
  Este é um projeto acadêmico desenvolvido para a disciplina de <b>Programação Orientada a Objetos</b>.
</p>
<p>
  <b>Desenvolvido por:</b>
</p>
<ul>
  <li>João Ricardo (<a href="https://github.com/JoaoRicMF">JoaoRicMF</a>)</li>
  <li>Rhwan (<a href="https://github.com/rhwann">rhwann</a>)</li>
  <li>Josemar (<a href="https://github.com/JosemarOky">JosemarOky</a>)</li>
</ul>
<h2>📁 Estrutura do Projeto</h2>

<pre>
src/main
├── <strong>java/</strong>
│   ├── <strong>dao/</strong>            ← Classes que fazem a comunicação com o banco de dados
│   ├── <strong>database/</strong>       ← Classe de conexão com o MySQL
│   ├── <strong>excecoes/</strong>       ← Exceções personalizadas do sistema
│   ├── <strong>gui/</strong>            ← Controladores da interface gráfica (arquivos .java)
│   │   ├── <strong>aluno/</strong>
│   │   ├── <strong>disciplina/</strong>
│   │   ├── <strong>frequencia/</strong>
│   │   ├── <strong>matricula/</strong>
│   │   ├── <strong>mural/</strong>
│   │   ├── <strong>notas/</strong>
│   │   ├── <strong>professor/</strong>
│   │   ├── <strong>telainicial/</strong>
│   │   ├── <strong>turma/</strong>
│   │   ├── <strong>util/</strong>
│   │   └── <strong>visaoaluno/</strong>
│   ├── <strong>main/</strong>           ← Classe principal (Main.java)
│   ├── <strong>modelo/</strong>         ← Classes de modelo (Aluno, Professor, etc.)
│   └── <strong>service/</strong>        ← Lógica de negócio e validações
├── <strong>META-INF/</strong>         ← Metadados do projeto
└── <strong>resources/</strong>
    ├── <strong>Imagens/</strong>        ← Imagens e ícones da aplicação
    └── <strong>gui/</strong>            ← Arquivos da interface gráfica (arquivos .fxml e .css)
        ├── <strong>aluno/</strong>
        ├── <strong>css/</strong>
        ├── <strong>disciplina/</strong>
        ├── <strong>frequencia/</strong>
        ├── <strong>matricula/</strong>
        ├── <strong>mural/</strong>
        ├── <strong>notas/</strong>
        ├── <strong>professor/</strong>
        ├── <strong>telainicial/</strong>
        ├── <strong>turma/</strong>
        ├── <strong>util/</strong>
        └── <strong>visaoaluno/</strong>
</pre>
