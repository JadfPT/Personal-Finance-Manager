package trabalhofinancas;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.Scene;

public class LimitesController {

    @FXML private ComboBox<String> cbCategoriaLimite;
    @FXML private TextField txtLimiteValor;

    @FXML
    private void initialize() {
        cbCategoriaLimite.getItems().addAll( // Obter as 6 categorias 
            "Alimentação", "Transporte", "Lazer", "Saúde", "Educação", "Outro"
        );
    }

    @FXML
    private void handleGuardarLimite() {
        String categoria = cbCategoriaLimite.getValue();
        String valorStr = txtLimiteValor.getText();

        if (categoria == null || valorStr == null || valorStr.isBlank()) { // Verificar se os campos estao vazios
            mostrarErro("Preenche todos os campos!");
            return;
        }

        try {
            double limite = Double.parseDouble(valorStr);

            // Guarda o limite na estrutura global
            DadosFinanceiros.limitesPorCategoria.put(categoria, limite);

            mostrarSucesso("Limite definido com sucesso para " + categoria + ": €" + String.format("%.2f", limite));
        } catch (NumberFormatException e) {
            mostrarErro("Valor inválido!");
        }
    }
    
    @FXML
    private void handleRemoverLimite() { // Como diz o nome para remover o limite
        String categoria = cbCategoriaLimite.getValue();

        if (categoria == null) {
            mostrarErro("Seleciona uma categoria para remover o limite.");
            return;
        }

        if (DadosFinanceiros.limitesPorCategoria.containsKey(categoria)) { // Escolher a categoria que queres remover
            DadosFinanceiros.limitesPorCategoria.remove(categoria); // Remove o limite da categoria
            mostrarSucesso("Limite removido com sucesso para " + categoria + ".");
        } else {
            mostrarErro("Não existe limite definido para a categoria " + categoria + ".");
        }
    }

    public void mostrarSucesso(String mensagem) { // CSS da cor e formato da mensagem de sucesso
        Stage dialog = new Stage();
        dialog.setTitle("Sucesso");

        Label msg = new Label(mensagem);
        msg.setStyle("-fx-font-size: 14px; -fx-text-fill: #2e7d32;");

        Button okButton = new Button("OK");
        okButton.setStyle("-fx-background-color: #2e7d32; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 6 20;");
        okButton.setOnAction(e -> dialog.close());

        VBox root = new VBox(20, msg, okButton);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("estilo.css").toExternalForm());
        root.getStyleClass().add("dialog-success");

        dialog.setScene(scene);
        dialog.setResizable(false);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();
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
