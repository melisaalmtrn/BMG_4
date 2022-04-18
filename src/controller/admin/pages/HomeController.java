package controller.admin.pages;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.DataSource;

/**
 * Bu sınıf admin ana sayfasını kontrol eder
 */
public class HomeController {

    @FXML
    public Label carsCount;
    @FXML
    public Label customersCount;

    /**
     * Bu metot araç sayısını alır
     */
    public void getDashboardCarCount() {
        Task<Integer> getDashCarCount = new Task<Integer>() {
            @Override
            protected Integer call() {
                return DataSource.getInstance().countAllCars();
            }
        };

        getDashCarCount.setOnSucceeded(e -> {
            carsCount.setText(String.valueOf(getDashCarCount.valueProperty().getValue()));
        });

        new Thread(getDashCarCount).start();
    }

    /**
     * Bu metot müşterilerin sayısını alır
     */
    public void getDashboardCustomerCount() {
        Task<Integer> getDashboardCustomerCount = new Task<Integer>() {
            @Override
            protected Integer call() {
                return DataSource.getInstance().countAllCustomers();
            }
        };

        getDashboardCustomerCount.setOnSucceeded(e -> {
            customersCount.setText(String.valueOf(getDashboardCustomerCount.valueProperty().getValue()));
        });

        new Thread(getDashboardCustomerCount).start();
    }

}
