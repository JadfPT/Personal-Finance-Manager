package trabalhofinancas.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MenuViewController {

    @FXML
    private void handleVerSaldo(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/View/MainView.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Saldo Atual");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAdicionarTransacao(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/View/AdicionarTransacao.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Adicionar Transação");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleVerHistorico(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/View/Historico.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Histórico de Transações");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
