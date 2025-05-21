package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import trabalhofinancas.DadosFinanceiros;
import trabalhofinancas.Transacao;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class HistoricoController implements Initializable {

    @FXML private TableView<Transacao> tabelaTransacoes;
    @FXML private TableColumn<Transacao, String> colTipo;
    @FXML private TableColumn<Transacao, String> colCategoria;
    @FXML private TableColumn<Transacao, Double> colValor;
    @FXML private TableColumn<Transacao, LocalDate> colData;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colData.setCellValueFactory(new PropertyValueFactory<>("data"));

        tabelaTransacoes.setItems(javafx.collections.FXCollections.observableArrayList(DadosFinanceiros.transacoes));
    }
}
