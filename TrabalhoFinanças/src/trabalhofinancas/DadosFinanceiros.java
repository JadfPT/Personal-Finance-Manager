package trabalhofinancas;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class DadosFinanceiros {

    private static final String FICHEIRO = "src/trabalhofinancas/transacoes.txt";// Caminho do ficheiro de texto

    public static List<Transacao> transacoes = new ArrayList<>();
    public static final Map<String, Double> limitesPorCategoria = new HashMap<>();

    static {// Para carregar os dados
        carregar();
    }

    public static void adicionarTransacao(Transacao t) {//Adiciona na lista e guarda no ficheiro
        transacoes.add(t);
        guardar();
    }

    public static void removerTransacao(Transacao t) {//Remove da lista e guarda no ficheiro
        transacoes.remove(t);
        guardar();
    }

    public static void guardar() {// Usado para guardar no ficheiro criado
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FICHEIRO))) {
            for (Transacao t : transacoes) {
                String linha = t.getTipo() + ";" +
                               t.getCategoria() + ";" +
                               t.getValor() + ";" +
                               t.getData() + ";" +
                               t.getDescricao().replace(";", ","); // Evita conflitos no ficheiro
                writer.write(linha);
                writer.newLine();
            }
        } catch (IOException e) {
        }
    }

    public static void carregar() {// Funçao para carregar os dados do ficheiro
        transacoes.clear();
        File ficheiro = new File(FICHEIRO);

        if (!ficheiro.exists()) return;// Verifica se tem algum ficheiro 

        try (BufferedReader reader = new BufferedReader(new FileReader(ficheiro))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(";", 5); // Carrega a linha do ficheiro com o ; a dividir os 5 dados
                if (partes.length == 5) {
                    String tipo = partes[0];
                    String categoria = partes[1];
                    double valor = Double.parseDouble(partes[2]);
                    LocalDate data = LocalDate.parse(partes[3]);
                    String descricao = partes[4];
                    transacoes.add(new Transacao(tipo, categoria, valor, data, descricao));// Coloca no historico
                }
            }
        } catch (IOException | NumberFormatException e) {
        }
    }

    public static double totalPorCategoria(String categoria) {// Total gasto em despesas nas categorias
        return transacoes.stream()
                .filter(t -> t.getCategoria().equals(categoria) && t.getTipo().equals("Despesa"))
                .mapToDouble(Transacao::getValor)
                .sum();
    }
}
