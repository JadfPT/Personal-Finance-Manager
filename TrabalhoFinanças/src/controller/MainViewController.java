package trabalhofinancas.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import trabalhofinancas.DadosFinanceiros;
import trabalhofinancas.Transacao;

public class MainViewController implements Initializable {

    @FXML private Label lblSaldoContas;
    @FXML private Label lblGastosHoje;
    @FXML private Label lblReceitasMes;
    @FXML private Label lblDespesasMes;
    @FXML private Label lblPoupancaMes;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        atualizarTotais();
    }

    public void atualizarTotais() {
        double saldo = 0, receitas = 0, despesas = 0, hoje = 0;
        LocalDate hojeData = LocalDate.now();

        for (Transacao t : DadosFinanceiros.transacoes) {
            if (t.getTipo().equals("Receita")) {
                saldo += t.getValor();
                if (t.getData().getMonth() == hojeData.getMonth() && t.getData().getYear() == hojeData.getYear()) {
                    receitas += t.getValor();
                }
            } else {
                saldo -= t.getValor();
                if (t.getData().getMonth() == hojeData.getMonth() && t.getData().getYear() == hojeData.getYear()) {
                    despesas += t.getValor();
                }
                if (t.getData().equals(hojeData)) {
                    hoje += t.getValor();
                }
            }
        }

        lblSaldoContas.setText(String.format("€ %.2f", saldo));
        lblReceitasMes.setText(String.format("€ %.2f", receitas));
        lblDespesasMes.setText(String.format("€ %.2f", despesas));
        lblPoupancaMes.setText(String.format("€ %.2f", receitas - despesas));
        lblGastosHoje.setText(String.format("€ %.2f", hoje));
    }
}
