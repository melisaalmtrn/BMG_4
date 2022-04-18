package controller.user.pages;

import controller.UserSessionController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import model.DataSource;
import model.Rental;

import java.awt.event.ActionEvent;

/**
 * Bu class kullanıcıların kiralama sayfasını görüntüler
 */
public class UserRentalsController {
    public TableView tableRentalsPage;
    @FXML
    public void listRentals() {

        Task<ObservableList<Rental>> getAllRentalsTask = new Task<ObservableList<Rental>>() {
            @Override
            protected ObservableList<Rental> call() {
                return FXCollections.observableArrayList(DataSource.getInstance().getAllUserRentals(DataSource.ORDER_BY_NONE, UserSessionController.getUserId()));
            }
        };

        tableRentalsPage.itemsProperty().bind(getAllRentalsTask.valueProperty());
        new Thread(getAllRentalsTask).start();

    }


    public void btnRentalsSearchOnAction(ActionEvent actionEvent) {
        // TODO
        // Kiralık arama işlevi ekleyin.
        System.out.println("TODO: Add orders search functionality.");
    }

}
