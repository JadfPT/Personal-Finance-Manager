package trabalhofinancas;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MenuViewController {

    @FXML
    private void handleVerSaldo(ActionEvent event) { // Carrega o mainview ao clicar no botao ver saldo
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/trabalhofinancas/MainView.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Saldo Atual"); // Coloca o titulo na janela da pagina como Saldo Atual
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
        }
    }

    @FXML
    private void handleAdicionarTransacao(ActionEvent event) { // Carrega a parte da transacao ao clicar no botao Adicionar transaçao
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/trabalhofinancas/AdicionarTransacao.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Adicionar Transação"); // Coloca o titulo na janela da pagina como Adicionar Transação
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
        }
    }

    @FXML
    private void handleVerHistorico(ActionEvent event) { // Carrega o Historico ao clicar no botao ver historico
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/trabalhofinancas/Historico.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Histórico de Transações"); // Coloca o titulo na janela da pagina como Histórico de Transações
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
        }
    }

    @FXML
    private void abrirEstatisticas() { // Carrega as estatisticas ao clicar no botao ver estatisticas
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/trabalhofinancas/Estatisticas.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Estatísticas Financeiras"); // Coloca o titulo na janela da pagina como Estatísticas Financeiras
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
        }
    }

    @FXML
    private void abrirDefinirLimites() { // Carrega a definiçao dos limites ao clicar no botao definir limites
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/trabalhofinancas/Limites.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Definir Limites"); // Coloca o titulo na janela da pagina como Definir Limites
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
        }
    }
}
