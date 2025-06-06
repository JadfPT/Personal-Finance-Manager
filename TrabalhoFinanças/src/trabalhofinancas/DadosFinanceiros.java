package trabalhofinancas;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class DadosFinanceiros {

    private static final String FICHEIRO = "src/trabalhofinancas/transacoes.txt";

    public static List<Transacao> transacoes = new ArrayList<>();
    public static final Map<String, Double> limitesPorCategoria = new HashMap<>();

    static {
        carregar();
    }

    public static void adicionarTransacao(Transacao t) {
        transacoes.add(t);
        guardar();
    }

    public static void removerTransacao(Transacao t) {
        transacoes.remove(t);
        guardar();
    }

    public static void guardar() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FICHEIRO))) {
            for (Transacao t : transacoes) {
                String linha = t.getTipo() + ";" +
                               t.getCategoria() + ";" +
                               t.getValor() + ";" +
                               t.getData() + ";" +
                               t.getDescricao().replace(";", ","); // evitar conflito no split
                writer.write(linha);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void carregar() {
        transacoes.clear();
        File ficheiro = new File(FICHEIRO);

        if (!ficheiro.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(ficheiro))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(";", 5); // garantir que descrição pode conter ';'
                if (partes.length == 5) {
                    String tipo = partes[0];
                    String categoria = partes[1];
                    double valor = Double.parseDouble(partes[2]);
                    LocalDate data = LocalDate.parse(partes[3]);
                    String descricao = partes[4];
                    transacoes.add(new Transacao(tipo, categoria, valor, data, descricao));
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public static double totalPorCategoria(String categoria) {
        return transacoes.stream()
                .filter(t -> t.getCategoria().equals(categoria) && t.getTipo().equals("Despesa"))
                .mapToDouble(Transacao::getValor)
                .sum();
    }
}
