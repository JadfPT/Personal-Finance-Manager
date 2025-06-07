package trabalhofinancas;

import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javafx.scene.control.Tooltip;

public class EstatisticasController {

    @FXML private PieChart pieChart;
    @FXML private BarChart<String, Number> barChart;
    @FXML private LineChart<String, Number> lineChart;
    @FXML private ComboBox<String> cbAno;

    @FXML
    public void initialize() {

        aplicarEstiloGlobal(); // O CSS dos graficos
        preencherAnosDisponiveis(); 
        cbAno.setValue(cbAno.getItems().get(0)); // Para colocar o ano atual como ano de valor inicial

        // Gera os três gráficos
        preencherPieChart();
        preencherBarChart();
        preencherLineChart();
    }

    private void aplicarEstiloGlobal() {// Estilo dos gráficos
        String css = getClass().getResource("estilo.css").toExternalForm();
        pieChart.getStylesheets().add(css);
        barChart.getStylesheets().add(css);
        lineChart.getStylesheets().add(css);
    }

    
    private void preencherAnosDisponiveis() {// Como diz o nome preenche com os anos que estao nas transiçoes
        Set<String> anos = new TreeSet<>();

        for (Transacao t : DadosFinanceiros.transacoes) {
            anos.add(String.valueOf(t.getData().getYear()));
        }

        cbAno.getItems().setAll(anos); // atualiza anos
    }

    private void preencherPieChart() {
        Map<String, Double> somaPorCategoria = new HashMap<>();

        // Soma os valores das despesas por categoria
        for (Transacao t : DadosFinanceiros.transacoes) {
            if ("Despesa".equals(t.getTipo())) {
                somaPorCategoria.merge(t.getCategoria(), t.getValor(), Double::sum);
            }
        }

        // Total geral das despesas (para calcular percentagens)
        double total = somaPorCategoria.values().stream()
            .mapToDouble(Double::doubleValue)
            .sum();

        // Limpa o gráfico e adiciona os dados
        pieChart.getData().clear();
        for (var entry : somaPorCategoria.entrySet()) {
            PieChart.Data data = new PieChart.Data(entry.getKey(), entry.getValue());
            pieChart.getData().add(data);
        }

        // Necessário para garantir que os nós gráficos estão disponíveis
        pieChart.applyCss();
        pieChart.layout();

        // Aplica tooltips com valores + percentagens
        for (PieChart.Data data : pieChart.getData()) {
            double valor = data.getPieValue();
            double percentagem = (valor / total) * 100;

            Tooltip tooltip = new Tooltip(
                String.format("%s: %.2f € (%.1f%%)", data.getName(), valor, percentagem)
            );

            Tooltip.install(data.getNode(), tooltip);
        }
    }


    private void preencherBarChart() { // Gera o gráfico de barras
        Map<String, Double> totalPorMes = new TreeMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        for (Transacao t : DadosFinanceiros.transacoes) {
            String mes = t.getData().format(formatter);
            totalPorMes.merge(mes, t.getValor(), Double::sum);
        }

        XYChart.Series<String, Number> serie = new XYChart.Series<>(); // Para a legenda do grafico das barras
        serie.setName("Total");

        for (var entry : totalPorMes.entrySet()) {
            XYChart.Data<String, Number> data = new XYChart.Data<>(entry.getKey(), entry.getValue());
            serie.getData().add(data);
        }

        barChart.getData().clear();
        barChart.setCategoryGap(40); // Espaço entre colunas
        barChart.setBarGap(8); // Espaço entre barras
        barChart.getData().add(serie);

        barChart.applyCss(); // Para ver o valor exato das barras
        barChart.layout();  
        for (XYChart.Data<String, Number> data : serie.getData()) {
            Tooltip tooltip = new Tooltip(String.format("%.2f €", data.getYValue().doubleValue()));
            Tooltip.install(data.getNode(), tooltip);
        }
    }

    private void preencherLineChart() { // Gera o gráfico de linha 
        String anoSelecionado = cbAno.getValue();

        List<Transacao> filtradas = new ArrayList<>(DadosFinanceiros.transacoes); // Para ver a lista das transaçoes do ano escolhido

        if (!"Todos".equals(anoSelecionado)) {
            int ano = Integer.parseInt(anoSelecionado);
            filtradas.removeIf(t -> t.getData().getYear() != ano);
        }

        // Ordena as transações por data
        filtradas.sort(Comparator.comparing(Transacao::getData));

        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Evolução do Saldo");

        double saldo = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
        LinkedHashMap<String, Double> saldoPorData = new LinkedHashMap<>();

        for (Transacao t : filtradas) { // Ve todo o saldo das datas
            saldo += t.getTipo().equals("Receita") ? t.getValor() : -t.getValor();
            String data = t.getData().format(formatter);
            saldoPorData.put(data, saldo);
        }

        for (Map.Entry<String, Double> entry : saldoPorData.entrySet()) { // Cria os pontinhos da linha
            serie.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        lineChart.getData().clear();
        lineChart.getData().add(serie);

        lineChart.getXAxis().setTickLabelRotation(0); // Mantém a data na horizontal
    }

    @FXML
    private void atualizarGraficoPorAno() { // Como o nome diz atualiza o grafico por ano
        preencherLineChart();
    }
}
