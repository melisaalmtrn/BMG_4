package controller.user.pages;

import controller.UserSessionController;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import model.DataSource;

/**
 * Bu class kullanıcı anaa sayfasını yönetir
 */
public class UserHomeController {

    public Label carsCount;
    public Label rentalsCount;

    /**
     * Kullanıcı için araç sayısını alır
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
     * This method gets the orders count for the user dashboard and sets it to the ordersCount label.
     * @since                   1.0.0
     */
    public void getDashboardRentalsCount() {
        Task<Integer> getDashRentalCount = new Task<Integer>() {
            @Override
            protected Integer call() {
                return DataSource.getInstance().countUserRentals(UserSessionController.getUserId());
            }
        };

        getDashRentalCount.setOnSucceeded(e -> {
            rentalsCount.setText(String.valueOf(getDashRentalCount.valueProperty().getValue()));
        });

        new Thread(getDashRentalCount).start();
    }

}
