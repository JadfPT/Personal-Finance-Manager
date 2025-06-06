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
        aplicarEstiloGlobal();
        preencherAnosDisponiveis();
        cbAno.setValue(cbAno.getItems().get(0)); // valor padrão
        preencherPieChart();
        preencherBarChart();
        preencherLineChart();
    }

    private void aplicarEstiloGlobal() {
        String css = getClass().getResource("estilo.css").toExternalForm();
        pieChart.getStylesheets().add(css);
        barChart.getStylesheets().add(css);
        lineChart.getStylesheets().add(css);
    }

    private void preencherAnosDisponiveis() {
        Set<String> anos = new TreeSet<>();

        for (Transacao t : DadosFinanceiros.transacoes) {
            anos.add(String.valueOf(t.getData().getYear()));
        }

        cbAno.getItems().setAll(anos);
    }

    private void preencherPieChart() {
        Map<String, Double> somaPorCategoria = new HashMap<>();

        for (Transacao t : DadosFinanceiros.transacoes) {
            if ("Despesa".equals(t.getTipo())) {
                somaPorCategoria.merge(t.getCategoria(), t.getValor(), Double::sum);
            }
        }

        pieChart.getData().clear();
        for (var entry : somaPorCategoria.entrySet()) {
            pieChart.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
    }

    private void preencherBarChart() {
        Map<String, Double> totalPorMes = new TreeMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        for (Transacao t : DadosFinanceiros.transacoes) {
            String mes = t.getData().format(formatter);
            totalPorMes.merge(mes, t.getValor(), Double::sum);
        }

        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Total");

        for (var entry : totalPorMes.entrySet()) {
            XYChart.Data<String, Number> data = new XYChart.Data<>(entry.getKey(), entry.getValue());
            serie.getData().add(data);
        }

        barChart.getData().clear();
        barChart.setCategoryGap(40);
        barChart.setBarGap(8);
        barChart.getData().add(serie);

        // ✅ Tooltips nas barras
        barChart.applyCss(); // necessário para garantir que os nós foram criados
        barChart.layout();
        for (XYChart.Data<String, Number> data : serie.getData()) {
            Tooltip tooltip = new Tooltip(String.format("%.2f €", data.getYValue().doubleValue()));
            Tooltip.install(data.getNode(), tooltip);
        }
    }

    private void preencherLineChart() {
        String anoSelecionado = cbAno.getValue();
        List<Transacao> filtradas = new ArrayList<>(DadosFinanceiros.transacoes);

        if (!"Todos".equals(anoSelecionado)) {
            int ano = Integer.parseInt(anoSelecionado);
            filtradas.removeIf(t -> t.getData().getYear() != ano);
        }

        filtradas.sort(Comparator.comparing(Transacao::getData));

        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Evolução do Saldo");

        double saldo = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");

        LinkedHashMap<String, Double> saldoPorData = new LinkedHashMap<>();
        for (Transacao t : filtradas) {
            saldo += t.getTipo().equals("Receita") ? t.getValor() : -t.getValor();
            String data = t.getData().format(formatter);
            saldoPorData.put(data, saldo);
        }

        for (Map.Entry<String, Double> entry : saldoPorData.entrySet()) {
            serie.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        lineChart.getData().clear();
        lineChart.getData().add(serie);
        lineChart.getXAxis().setTickLabelRotation(0);
    }

    @FXML
    private void atualizarGraficoPorAno() {
        preencherLineChart();
    }
}
