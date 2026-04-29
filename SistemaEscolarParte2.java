import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

// Exceção personalizada
class ExcecaoDeAlunoJaExistente extends Exception {
    public ExcecaoDeAlunoJaExistente(String mensagem) {
        super(mensagem);
    }
}

class Aluno {
    String nome, cpf, endereco;
    LocalDate dataNascimento;

    public Aluno(String nome, String cpf, String endereco, String dataNascimento) {
        this.nome = nome;
        this.cpf = cpf;
        this.endereco = endereco;
        this.dataNascimento = LocalDate.parse(dataNascimento, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public int getIdade() {
        return Period.between(this.dataNascimento, LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        return nome + " - " + getIdade() + " anos (" + cpf + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Aluno)) return false;
        Aluno outro = (Aluno) obj;
        return this.nome.equalsIgnoreCase(outro.nome) && this.dataNascimento.equals(outro.dataNascimento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome.toLowerCase(), dataNascimento);
    }
}

class ListaDeAlunos {
    private LinkedList<Aluno> alunos = new LinkedList<>();
    private Map<String, Aluno> alunosPorCpf = new HashMap<>();

    public void incluirNoInicio(Aluno aluno) throws ExcecaoDeAlunoJaExistente {
        if (alunos.contains(aluno)) {
            throw new ExcecaoDeAlunoJaExistente("Aluno já existe na lista!");
        }
        alunos.addFirst(aluno);
        alunosPorCpf.put(aluno.cpf, aluno);
    }

    public void incluirNoFim(Aluno aluno) throws ExcecaoDeAlunoJaExistente {
        if (alunos.contains(aluno)) {
            throw new ExcecaoDeAlunoJaExistente("Aluno já existe na lista!");
        }
        alunos.addLast(aluno);
        alunosPorCpf.put(aluno.cpf, aluno);
    }

    public void ordenar() {
        alunos.sort(Comparator.comparing(a -> a.nome));
    }

    public Aluno removerDoFim() {
        if (alunos.isEmpty()) return null;
        Aluno aluno = alunos.removeLast();
        alunosPorCpf.remove(aluno.cpf);
        return aluno;
    }

    public int tamanho() {
        return alunos.size();
    }

    public Aluno buscarPorCpf(String cpf) {
        return alunosPorCpf.get(cpf);
    }

    public void listarAlunosOrdenados() {
        ordenar();
        for (Aluno aluno : alunos) {
            System.out.println(aluno);
        }
    }
}

class Turma {
    String codigo, etapaEnsino;
    int ano, limiteVagas;
    List<Aluno> matriculados = new ArrayList<>();

    public Turma(String codigo, String etapaEnsino, int ano, int limiteVagas) {
        this.codigo = codigo;
        this.etapaEnsino = etapaEnsino;
        this.ano = ano;
        this.limiteVagas = limiteVagas;
    }

    public boolean matricularAluno(Aluno aluno) {
        int idade = aluno.getIdade();
        String etapa = etapaEnsino.toLowerCase();

        boolean idadeValida = switch (etapa) {
            case "infantil" -> idade >= 0 && idade <= 5;
            case "fundamental anos iniciais" -> idade >= 6 && idade <= 10;
            case "fundamental anos finais" -> idade >= 11 && idade <= 14;
            case "médio" -> idade >= 15 && idade <= 18;
            default -> {
                System.out.println("Etapa de ensino não reconhecida!");
                yield false;
            }
        };

        if (!idadeValida) {
            System.out.println("A idade do aluno (" + idade + " anos) não é compatível com a etapa de ensino: " + etapaEnsino);
            return false;
        }

        if (matriculados.size() >= limiteVagas) {
            System.out.println("Turma cheia! Limite de vagas atingido.");
            return false;
        }

        matriculados.add(aluno);
        return true;
    }

    public void listarMatriculados() {
        for (Aluno aluno : matriculados) {
            System.out.println(aluno);
        }
    }

    @Override
    public String toString() {
        return "Turma " + codigo + " - " + etapaEnsino + " - " + ano + " (" + matriculados.size() + "/" + limiteVagas + " matriculados)";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Turma)) return false;
        Turma outra = (Turma) obj;
        return this.codigo.equalsIgnoreCase(outra.codigo) &&
                this.ano == outra.ano &&
                this.etapaEnsino.equalsIgnoreCase(outra.etapaEnsino);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo.toLowerCase(), ano, etapaEnsino.toLowerCase());
    }
}

public class SistemaEscolar {
    static ListaDeAlunos listaAlunos = new ListaDeAlunos();
    static Map<String, Turma> turmas = new HashMap<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== MENU SISTEMA ESCOLAR ===");
            System.out.println("1. Cadastrar Aluno");
            System.out.println("2. Cadastrar Turma");
            System.out.println("3. Listar Alunos");
            System.out.println("4. Listar Turmas");
            System.out.println("5. Matricular Aluno em Turma");
            System.out.println("6. Listar alunos de uma turma");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt(); scanner.nextLine();
            switch (opcao) {
                case 1 -> cadastrarAluno();
                case 2 -> cadastrarTurma();
                case 3 -> listaAlunos.listarAlunosOrdenados();
                case 4 -> listarTurmas();
                case 5 -> matricularAluno();
                case 6 -> listarAlunosTurma();
                case 0 -> {
                    System.out.println("Saindo do sistema...");
                    System.exit(0);
                }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private static void cadastrarAluno() {
        try {
            System.out.print("Nome: ");
            String nome = scanner.nextLine();

            System.out.print("CPF: ");
            String cpf = scanner.nextLine().replaceAll("[^\\d]", "");

            if (!validaCpf(cpf)) {
                System.out.println("CPF inválido! Cadastro cancelado.");
                return;
            }

            System.out.print("Endereço: ");
            String endereco = scanner.nextLine();

            System.out.print("Data de Nascimento (dd/MM/yyyy): ");
            String dataNascimento = scanner.nextLine();

            Aluno aluno = new Aluno(nome, cpf, endereco, dataNascimento);
            listaAlunos.incluirNoFim(aluno);
            System.out.println("Aluno cadastrado com sucesso!");

        } catch (ExcecaoDeAlunoJaExistente e) {
            System.out.println("Erro ao cadastrar: " + e.getMessage());
        }
    }

    private static void cadastrarTurma() {
        System.out.print("Código da Turma: ");
        String codigo = scanner.nextLine();
        System.out.print("Etapa de Ensino: ");
        String etapa = scanner.nextLine();
        System.out.print("Ano: ");
        int ano = scanner.nextInt();
        System.out.print("Limite de Vagas: ");
        int limiteVagas = scanner.nextInt(); scanner.nextLine();

        turmas.put(codigo, new Turma(codigo, etapa, ano, limiteVagas));
    }

    private static void listarTurmas() {
        if (turmas.isEmpty()) {
            System.out.println("Nenhuma turma cadastrada.");
            return;
        }

        for (Turma turma : turmas.values()) {
            System.out.println(turma);
        }
    }

    private static void matricularAluno() {
        System.out.print("CPF do aluno: ");
        String cpf = scanner.nextLine().replaceAll("[^\\d]", "");

        Aluno aluno = listaAlunos.buscarPorCpf(cpf);
        if (aluno == null) {
            System.out.println("Aluno não encontrado!");
            return;
        }

        System.out.print("Código da turma: ");
        String codigoTurma = scanner.nextLine();

        Turma turma = turmas.get(codigoTurma);
        if (turma == null) {
            System.out.println("Turma não encontrada!");
            return;
        }

        if (turma.matricularAluno(aluno)) {
            System.out.println("Aluno matriculado com sucesso!");
        }
    }

    private static void listarAlunosTurma() {
        System.out.print("Código da turma: ");
        String codigoTurma = scanner.nextLine();

        Turma turma = turmas.get(codigoTurma);
        if (turma != null) {
            turma.listarMatriculados();
        } else {
            System.out.println("Turma não encontrada!");
        }
    }

    public static boolean validaCpf(String cpf) {
        cpf = cpf.replaceAll("[^\\d]", "");
        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            int soma = 0;
            for (int i = 0; i < 9; i++) {
                soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
            }
            int digito1 = 11 - (soma % 11);
            if (digito1 >= 10) digito1 = 0;

            soma = 0;
            for (int i = 0; i < 10; i++) {
                soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
            }
            int digito2 = 11 - (soma % 11);
            if (digito2 >= 10) digito2 = 0;

            return digito1 == Character.getNumericValue(cpf.charAt(9)) &&
                    digito2 == Character.getNumericValue(cpf.charAt(10));
        } catch (Exception e) {
            return false;
        }
    }
}