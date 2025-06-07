package trabalhofinancas;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.stage.StageStyle;
import javafx.stage.Modality;
import javafx.scene.Scene;

public class AdicionarTransacaoController {

    @FXML private ComboBox<String> cbTipo;
    @FXML private ComboBox<String> cbCategoria;
    @FXML private TextField txtDescricao;
    @FXML private TextField txtValor;
    @FXML private DatePicker dpData;

    private MainViewController mainController;

    public void setMainController(MainViewController controller) {
        this.mainController = controller;
    }

    @FXML
    private void initialize() { // Para aparecer a categoria do que queremos escolher
        cbTipo.getItems().addAll("Receita", "Despesa");
        cbCategoria.getItems().addAll(
            "Alimentação", "Transporte", "Lazer", "Saúde", "Educação", "Outro"
        );
    }

    @FXML
    private void handleGuardar() {
        try {
            String tipo = cbTipo.getValue();
            String categoria = cbCategoria.getValue();
            String valorStr = txtValor.getText();
            var data = dpData.getValue();
            String descricao = txtDescricao.getText();

            if (tipo == null || categoria == null || valorStr.isEmpty() || data == null) { // Para confirmar se os campos estão preenchidos
                mostrarErro("Preenche todos os campos.");
                return;
            }

            double valor = Double.parseDouble(valorStr); // Para confirmar que o valor que é adicionado n é menor que 0 ou 0
            if (valor <= 0) {
                mostrarErro("O valor deve ser maior que zero.");
                return;
            }

            if (tipo.equals("Despesa")) {// Verificação do limite
                double totalAtual = DadosFinanceiros.totalPorCategoria(categoria);
                Double limite = DadosFinanceiros.limitesPorCategoria.get(categoria);

                if (limite != null && totalAtual + valor > limite) { // Verifica se o valor acumulado de despesas é maior q o limite, se for bloqueia
                    mostrarErro("Ultrapassaste o limite definido para " + categoria + "!");
                    return;
                }
            }

            Transacao t = new Transacao(tipo, categoria, valor, data, descricao);
            DadosFinanceiros.adicionarTransacao(t);

            if (mainController != null)
                mainController.atualizarTotais();

            ((Stage) cbTipo.getScene().getWindow()).close();

        } catch (NumberFormatException e) {
            mostrarErro("Valor inválido.");
        } catch (Exception e) {
            mostrarErro("Erro ao guardar. Verifica os dados.");
        }
    }

    public void mostrarErro(String mensagem) { // CSS da cor e formato da mensagem de erro
        Stage dialog = new Stage();
        dialog.setTitle("Erro");

        Label msg = new Label(mensagem);
        msg.setStyle("-fx-font-size: 14px; -fx-text-fill: #c62828;");

        Button okButton = new Button("OK");
        okButton.setStyle("-fx-background-color: #c62828; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 6 20;");
        okButton.setOnAction(e -> dialog.close());

        VBox root = new VBox(20, msg, okButton);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("estilo.css").toExternalForm());
        root.getStyleClass().add("dialog-erro");

        dialog.setScene(scene);
        dialog.setResizable(false);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();
    }
}
