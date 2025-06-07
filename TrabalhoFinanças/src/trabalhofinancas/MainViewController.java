package trabalhofinancas;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

public class MainViewController implements Initializable {

    @FXML private Label lblSaldoContas;
    @FXML private Label lblGastosHoje;
    @FXML private Label lblReceitasMes;
    @FXML private Label lblDespesasMes;
    @FXML private ComboBox<String> cbMesResumo;
    @FXML private ComboBox<String> cbAno; // Adicionado

    private final String[] meses = {
        "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
        "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
    };

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Preencher meses
        cbMesResumo.getItems().addAll(meses);
        cbMesResumo.setValue(meses[LocalDate.now().getMonthValue() - 1]);

        // Preencher anos únicos a partir das transações
        Set<String> anos = new TreeSet<>();
        for (Transacao t : DadosFinanceiros.transacoes) {
            anos.add(String.valueOf(t.getData().getYear()));
        }
        cbAno.getItems().addAll(anos);
        cbAno.setValue(String.valueOf(LocalDate.now().getYear()));

        // Listeners
        cbMesResumo.setOnAction(e -> atualizarResumoMensal());
        cbAno.setOnAction(e -> atualizarResumoMensal());

        atualizarTotais();
        atualizarResumoMensal();
    }

    public void atualizarTotais() {
        double saldo = 0, hoje = 0;
        LocalDate hojeData = LocalDate.now();

        for (Transacao t : DadosFinanceiros.transacoes) {
            if (t.getTipo().equals("Receita")) {
                saldo += t.getValor();
            } else {
                saldo -= t.getValor();
                if (t.getData().equals(hojeData)) {
                    hoje += t.getValor();
                }
            }
        }

        lblSaldoContas.setText(String.format("€ %.2f", saldo));
        lblGastosHoje.setText(String.format("€ %.2f", hoje));
    }

    private void atualizarResumoMensal() {
        String mesSelecionado = cbMesResumo.getValue();
        String anoSelecionado = cbAno.getValue();

        // Verificação extra de segurança
        if (mesSelecionado == null || anoSelecionado == null) return;

        int mesIndex = cbMesResumo.getItems().indexOf(mesSelecionado) + 1;
        int ano = Integer.parseInt(anoSelecionado);

        double receitas = 0, despesas = 0;

        for (Transacao t : DadosFinanceiros.transacoes) {
            if (t.getData().getMonthValue() == mesIndex &&
                t.getData().getYear() == ano) {

                if (t.getTipo().equals("Receita")) {
                    receitas += t.getValor();
                } else {
                    despesas += t.getValor();
                }
            }
        }

        lblReceitasMes.setText(String.format("€ %.2f", receitas));
        lblDespesasMes.setText(String.format("€ %.2f", despesas));
    }
}