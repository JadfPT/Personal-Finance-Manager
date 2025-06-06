package trabalhofinancas;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    @FXML private Label lblSaldoContas;
    @FXML private Label lblGastosHoje;
    @FXML private Label lblReceitasMes;
    @FXML private Label lblDespesasMes;
    @FXML private ComboBox<String> cbMesResumo;

    private final String[] meses = { // Nome dos 12 meses
        "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
        "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
    };

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        cbMesResumo.getItems().addAll(meses); // colocar o nome dos 12 meses
        cbMesResumo.setValue(meses[LocalDate.now().getMonthValue() - 1]); // Colocar o mes atual
        cbMesResumo.setOnAction(e -> atualizarResumoMensal()); // Atualizar mes
        atualizarTotais();
        atualizarResumoMensal();
    }

    public void atualizarTotais() { // Atualizar os dois textos fora do resumo mensal, neste caso o saldo atual e as despesas do dia
        double saldo = 0, hoje = 0;
        LocalDate hojeData = LocalDate.now();

        for (Transacao t : DadosFinanceiros.transacoes) { // Funçao para obter os dados das receitas e despesas mensais
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

    private void atualizarResumoMensal() { // Como diz o nome da funçao, atualizar o resumo mensal 
        String mesSelecionado = cbMesResumo.getValue();
        int mesIndex = cbMesResumo.getItems().indexOf(mesSelecionado) + 1;

        double receitas = 0, despesas = 0;

        for (Transacao t : DadosFinanceiros.transacoes) { // Funçao para obter os dados das receitas e despesas mensais
            if (t.getData().getMonthValue() == mesIndex) {
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
