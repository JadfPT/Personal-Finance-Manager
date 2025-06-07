package trabalhofinancas;

import java.time.LocalDate;

public class Transacao {

    private String tipo;
    private String categoria;
    private double valor;
    private LocalDate data;
    private String descricao;

    public Transacao(String tipo, String categoria, double valor, LocalDate data, String descricao) {
        this.tipo = tipo;
        this.categoria = categoria;
        this.valor = valor;
        this.data = data;
        this.descricao = descricao;
    }

    public String getTipo() { return tipo; }
    public String getCategoria() { return categoria; }
    public double getValor() { return valor; }
    public LocalDate getData() { return data; }
    public String getDescricao() { return descricao; }

    public String toLinhaTxt() {// Para converter as transaçoes em txt
        return tipo + ";" + categoria + ";" + valor + ";" + data + ";" + descricao.replace(";", ",");
    }

        public static Transacao fromLinhaTxt(String linha) {// Para carregar o ficheiro txt do historico
            String[] partes = linha.split(";", 5); // Verificar se tem as 5 variaveis
            if (partes.length < 5) return null;// Se nao tiver da null

            String tipo = partes[0];// Transforma em string
            String categoria = partes[1];// Transforma em string
            double valor = Double.parseDouble(partes[2]);// Transforma em double
            LocalDate data = LocalDate.parse(partes[3]);// Transforma em LocalDate
            String descricao = partes[4];// Transforma em string
            return new Transacao(tipo, categoria, valor, data, descricao);
        }

    @Override
    public String toString() {
        return String.format("%s | %s | %s | %s: %.2f €", 
            data, tipo, categoria, descricao, valor);
    }
}
