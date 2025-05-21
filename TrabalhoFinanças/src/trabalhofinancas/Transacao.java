package trabalhofinancas;

import java.time.LocalDate;

public class Transacao {
    private String tipo; // "Receita" ou "Despesa"
    private String categoria;
    private double valor;
    private LocalDate data;

    public Transacao(String tipo, String categoria, double valor, LocalDate data) {
        this.tipo = tipo;
        this.categoria = categoria;
        this.valor = valor;
        this.data = data;
    }

    public String getTipo() { return tipo; }
    public String getCategoria() { return categoria; }
    public double getValor() { return valor; }
    public LocalDate getData() { return data; }
}
