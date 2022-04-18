package controller.admin.pages;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import model.DataSource;
import model.Rental;

public class RentalsController {

    @FXML
    private TableView<Rental> tableRentalsPage;


    @FXML
    public void listRentals() {
        Task<ObservableList<Rental>> getAllRentalsTask = new Task<ObservableList<Rental>>() {
            @Override
            protected ObservableList<Rental> call() {
                return FXCollections.observableArrayList(DataSource.getInstance().getAllRentals(DataSource.ORDER_BY_NONE));
            }
        };
        tableRentalsPage.itemsProperty().bind(getAllRentalsTask.valueProperty());
        new Thread(getAllRentalsTask).start();
    }

    public void btnRentalsSearchOnAction(ActionEvent actionEvent) {
        // TODO
        //  Add orders search functionality.
        System.out.println("TODO: Add orders search functionality.");
    }

}
