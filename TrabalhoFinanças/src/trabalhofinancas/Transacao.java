package trabalhofinancas;

import java.time.LocalDate;

public class Transacao {

    private String tipo;        // "Receita" ou "Despesa"
    private String categoria;   // Categoria da transação
    private double valor;       // Valor em euros
    private LocalDate data;     // Data da transação
    private String descricao;   // Descrição textual

    public Transacao(String tipo, String categoria, double valor, LocalDate data, String descricao) {
        this.tipo = tipo;
        this.categoria = categoria;
        this.valor = valor;
        this.data = data;
        this.descricao = descricao;
    }

    // Getters
    public String getTipo() { return tipo; }
    public String getCategoria() { return categoria; }
    public double getValor() { return valor; }
    public LocalDate getData() { return data; }
    public String getDescricao() { return descricao; }

    // Converte para formato linha de ficheiro
    public String toLinhaTxt() {
        return tipo + ";" + categoria + ";" + valor + ";" + data + ";" + descricao.replace(";", ",");
    }

    // Constrói a partir de uma linha do ficheiro
    public static Transacao fromLinhaTxt(String linha) {
        String[] partes = linha.split(";", 5); // split em no máx. 5 partes
        if (partes.length < 5) return null;

        String tipo = partes[0];
        String categoria = partes[1];
        double valor = Double.parseDouble(partes[2]);
        LocalDate data = LocalDate.parse(partes[3]);
        String descricao = partes[4];
        return new Transacao(tipo, categoria, valor, data, descricao);
    }

    @Override
    public String toString() {
        return String.format("%s | %s | %s | %s: %.2f €", 
            data, tipo, categoria, descricao, valor);
    }
}
