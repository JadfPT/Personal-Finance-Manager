package trabalhofinancas;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class HistoricoController implements Initializable { // Lables para colocar no historico

    @FXML private TableView<Transacao> tabelaTransacoes;
    @FXML private TableColumn<Transacao, String> colTipo;
    @FXML private TableColumn<Transacao, String> colCategoria;
    @FXML private TableColumn<Transacao, String> colDescricao;
    @FXML private TableColumn<Transacao, Double> colValor;
    @FXML private TableColumn<Transacao, LocalDate> colData;
    @FXML private ComboBox<String> cbMes;
    @FXML private ComboBox<String> cbCategoria;

    @Override
    public void initialize(URL url, ResourceBundle rb) { // Para coletar os dados para adicionar na tabela
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colData.setCellValueFactory(new PropertyValueFactory<>("data"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        // Adicionado para conseguir ver a descricao toda quando passar o rato por cima
        colDescricao.setCellFactory(column -> {
        return new TableCell<Transacao, String>() {
            private final Tooltip tooltip = new Tooltip();

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) { // Se não tiver nada limpa o texto e deixa como ta
                    setText(null);
                    setTooltip(null);
                } else { // Verifica se a descriçao tem mais de 30 caracters e se tiver coloca dps dos 30 "..."
                    setText(item.length() > 30 ? item.substring(0, 30) + "..." : item); 
                    tooltip.setText(item);
                    setTooltip(tooltip);
                }
            }
        };
        });
        
        // Guarda as informaçoes de qual mes, guarda tudo e coloca
        cbMes.getItems().add("Todos"); // Apresenta e verifica todos os meses
        for (int i = 1; i <= 12; i++)
            cbMes.getItems().add(String.format("%02d", i));
        cbMes.setValue("Todos");

        cbCategoria.getItems().add("Todas"); // Opção para aparecer todos, pois n queremos que seja especificamente so um
        for (Transacao t : DadosFinanceiros.transacoes) { // Verifica todas as transaçoes feitas nos meses
            if (!cbCategoria.getItems().contains(t.getCategoria()))
                cbCategoria.getItems().add(t.getCategoria());
        }
        cbCategoria.setValue("Todas");

        tabelaTransacoes.setItems(FXCollections.observableArrayList(DadosFinanceiros.transacoes));
    }

    @FXML // Como diz o nome, para aplicar os filtros, neste caso em categorias e mes, para os poder escolher
    private void aplicarFiltros() {
        String mesSelecionado = cbMes.getValue();
        String categoriaSelecionada = cbCategoria.getValue();

        List<Transacao> filtradas = new ArrayList<>();

        for (Transacao t : DadosFinanceiros.transacoes) { // Verifica todas as transaçoes feitas nos meses
            boolean mesOK = mesSelecionado.equals("Todos") ||
                    String.format("%02d", t.getData().getMonthValue()).equals(mesSelecionado); // Verifica se a transaçao e do mes selecionado 

            boolean catOK = categoriaSelecionada.equals("Todas") || 
                    t.getCategoria().equals(categoriaSelecionada); // Verifica se a transaçao e da categoria selecionada

            if (mesOK && catOK)
                filtradas.add(t); // Filtra se tiver tudo bem
        }

        tabelaTransacoes.setItems(FXCollections.observableArrayList(filtradas));
    }

    @FXML
    private void exportarParaTxt() { // Para criar um ficheiro Txt
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Histórico");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Ficheiros de Texto", "*.txt"));
        fileChooser.setInitialFileName("historico.txt");

        File ficheiro = fileChooser.showSaveDialog(new Stage());
        if (ficheiro != null) {
            try (FileWriter writer = new FileWriter(ficheiro)) {
                for (Transacao t : tabelaTransacoes.getItems()) {
                    writer.write(t.toString() + "\n");
                }

                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("Exportação");
                alerta.setHeaderText(null);
                alerta.setContentText("Histórico exportado com sucesso!");
                alerta.showAndWait();
            } catch (Exception e) {
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Erro");
                alerta.setHeaderText(null);
                alerta.setContentText("Erro ao guardar o ficheiro.");
                alerta.showAndWait();
            }
        }
    }
}
