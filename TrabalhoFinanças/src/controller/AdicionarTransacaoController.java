package trabalhofinancas.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import trabalhofinancas.DadosFinanceiros;
import trabalhofinancas.Transacao;

public class AdicionarTransacaoController {

    @FXML private ComboBox<String> cbTipo;
    @FXML private TextField txtCategoria;
    @FXML private TextField txtValor;
    @FXML private DatePicker dpData;

    private MainViewController mainController;

    public void setMainController(MainViewController controller) {
        this.mainController = controller;
    }

    @FXML
    private void handleGuardar() {
        try {
            String tipo = cbTipo.getValue();
            String categoria = txtCategoria.getText();
            double valor = Double.parseDouble(txtValor.getText());
            var data = dpData.getValue();

            if (tipo == null || categoria.isEmpty() || data == null) {
                mostrarErro("Preenche todos os campos.");
                return;
            }

            Transacao t = new Transacao(tipo, categoria, valor, data);
            DadosFinanceiros.transacoes.add(t);

            if (mainController != null)
                mainController.atualizarTotais();

            ((Stage) cbTipo.getScene().getWindow()).close();

        } catch (Exception e) {
            mostrarErro("Erro ao guardar. Verifica os dados.");
        }
    }

    private void mostrarErro(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }
}
