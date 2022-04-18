package controller.user.pages;

import app.utils.HelperMethods;
import controller.UserSessionController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import model.Car;
import model.DataSource;
import model.Rental;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Optional;

public class UserCarsController {

    @FXML
    public TextField fieldCarsSearch;

    @FXML
    private TableView<Car> tableCarsPage;

    @FXML
    public void listCar() {
        Task<ObservableList<Car>> getAllCarsTask = new Task<ObservableList<Car>>() {
            @Override
            protected ObservableList<Car> call() {
                return FXCollections.observableArrayList(DataSource.getInstance().getAllCars(DataSource.ORDER_BY_NONE));
            }
        };

        tableCarsPage.itemsProperty().bind(getAllCarsTask.valueProperty());
        addActionButtonsToTable();
        new Thread(getAllCarsTask).start();
    }

    /**
     * Tabloya ekler
     */
    @FXML
    private void addActionButtonsToTable() {
        TableColumn colBtnRental = new TableColumn("Actions");

        Callback<TableColumn<Car, Void>, TableCell<Car, Void>> cellFactory = new Callback<TableColumn<Car, Void>, TableCell<Car, Void>>() {
            @Override
            public TableCell<Car, Void> call(final TableColumn<Car, Void> param) {
                return new TableCell<Car, Void>() {

                    private final Button rentalButton = new Button("Rental");

                    {
                        rentalButton.getStyleClass().add("button");
                        rentalButton.getStyleClass().add("xs");
                        rentalButton.getStyleClass().add("success");
                        rentalButton.setOnAction((ActionEvent event) -> {
                            Car carData = getTableView().getItems().get(getIndex());
//                            if (carData.getDaily_price() <= 0) {
//                                HelperMethods.alertBox("You can't buy this product because there is no stock!", "", "No Stock");
//                            } else {
                                btnRentalCar(carData.getId(), carData.getBrand_name(), carData.getModel_name());
                                System.out.println("Rental Car");
                                System.out.println("car id: " + carData.getId());
                                System.out.println("brand name: " + carData.getBrand_name());
                                System.out.println("model :" + carData.getModel_name());
                            //}
                            //btnRental
                        });
                    }

                    private final HBox buttonsPane = new HBox();

                    {
                        buttonsPane.setSpacing(10);
                        buttonsPane.getChildren().add(rentalButton);
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(buttonsPane);
                        }
                    }
                };

            }
        };
        colBtnRental.setCellFactory(cellFactory);

        tableCarsPage.getColumns().add(colBtnRental);
    }

    private void btnCarsSearchOnAction() {
        Task<ObservableList<Car>> searchCarsTask = new Task<ObservableList<Car>>() {
            @Override
            protected ObservableList<Car> call() {
                return FXCollections.observableArrayList(
                        DataSource.getInstance().searchCars(fieldCarsSearch.getText().toLowerCase(), DataSource.ORDER_BY_NONE));
            }
        };
        tableCarsPage.itemsProperty().bind(searchCarsTask.valueProperty());

        new Thread(searchCarsTask).start();
    }

    private void btnRentalCar(int car_id, String brand_name, String model_name) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("You are about to rental " + brand_name + " " + model_name);
        alert.setTitle("Rental car ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == ButtonType.OK) {
                int user_id = UserSessionController.getUserId();
                String rent_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());


                Task<Boolean> addProductTask = new Task<Boolean>() {
                    @Override
                    protected Boolean call() {
                        return DataSource.getInstance().insertNewRental(car_id, user_id, rent_date );
                    }
                };

                addProductTask.setOnSucceeded(e -> {
                    if (addProductTask.valueProperty().get()) {
                        DataSource.getInstance().decreaseStock(car_id);
                        System.out.println("Rental Successful!");
                    }
                });

                new Thread(addProductTask).start();
                System.out.println(car_id);
            }
        }

    }
}


