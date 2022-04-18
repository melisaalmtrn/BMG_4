package controller.admin.pages.cars;

import app.utils.HelperMethods;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.StringConverter;
import model.Car;
import model.DataSource;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class CarsController {

    @FXML
    public TextField fieldCarsSearch;
    @FXML
    public Text viewCarResponse;
    @FXML
    private StackPane carsContent;
    @FXML

    private TableView<Car> tableCarsPage;


    @FXML
    public void listCars() {

        Task<ObservableList<Car>> getAllCarsTask = new Task<ObservableList<Car>>() {
            @Override
            protected ObservableList<Car> call() {
                return FXCollections.observableArrayList(DataSource.getInstance().getAllCars(DataSource.ORDER_BY_NONE));
            }
        };

        tableCarsPage.itemsProperty().bind(getAllCarsTask.valueProperty());
        addActionsButtonsToTable();
        new Thread(getAllCarsTask).start();
    }

    @FXML
    private void addActionsButtonsToTable() {
        TableColumn colBtnEdit = new TableColumn("Actions");
        Callback<TableColumn<Car, Void>, TableCell<Car, Void>> cellFactory = new Callback<TableColumn<Car, Void>, TableCell<Car, Void>>() {
            @Override
            public TableCell<Car, Void> call(final TableColumn<Car, Void> param) {
                return new TableCell<Car, Void>() {


                    private final Button viewButton = new Button("View");

                    {
                        viewButton.getStyleClass().add("button");
                        viewButton.getStyleClass().add("xs");
                        viewButton.getStyleClass().add("info");
                        viewButton.setOnAction((ActionEvent event) -> {
                            Car carData = getTableView().getItems().get(getIndex());
                            btnViewCar(carData.getId());
                            /**
                             * Düzenlenecek
                             */

                        });
                    }

                    private final Button editButton = new Button("Edit");

                    {
                        editButton.getStyleClass().add("button");
                        editButton.getStyleClass().add("xs");
                        editButton.getStyleClass().add("primary");
                        editButton.setOnAction((ActionEvent event) -> {
                            Car carData = getTableView().getItems().get(getIndex());
                            btnEditCar(carData.getId());
                        });
                    }

                    private final Button deleteButton = new Button("Delete");

                    {
                        deleteButton.getStyleClass().add("button");
                        deleteButton.getStyleClass().add("xs");
                        deleteButton.getStyleClass().add("danger");
                        deleteButton.setOnAction((ActionEvent event) -> {
                            Car carData = getTableView().getItems().get(getIndex());

                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setHeaderText("Are you sure that you want to delete " + carData.getBrand_name() + " " + carData.getModel_name() + " ?");
                            alert.setTitle("Delete " + carData.getModel_name() + " ?");
                            Optional<ButtonType> deleteConfirmation = alert.showAndWait();

                            if (deleteConfirmation.get() == ButtonType.OK){
                                System.out.println("Delete Car");
                                System.out.println("car id: " + carData.getId());
                                System.out.println("car name: " + carData.getModel_name());
                                if (DataSource.getInstance().deleteSingleCar(carData.getId())) {
                                    getTableView().getItems().remove(getIndex());
                                }
                            }
                        });
                    }

                    private final HBox buttonsPane = new HBox();

                    {
                        buttonsPane.setSpacing(10);
                        buttonsPane.getChildren().add(viewButton);
                        buttonsPane.getChildren().add(editButton);
                        buttonsPane.getChildren().add(deleteButton);
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

        colBtnEdit.setCellFactory(cellFactory);
        tableCarsPage.getColumns().add(colBtnEdit);

    }


    @FXML
    private void btnAddCarOnClick () {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            fxmlLoader.load(getClass().getResource("/view/admin/pages/cars/add-car.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        AnchorPane root = fxmlLoader.getRoot();
        carsContent.getChildren().clear();
        carsContent.getChildren().add(root);

    }


    /**
     * Bu metot metin girişlerini doğrulamak için
     *
     * @return TextFormatter
     */
    public static TextFormatter<Double> formatDoubleField () {
//        Pattern validEditingState = Pattern.compile("^[0-9]+(|\\.)[0-9]+$");
        Pattern validEditingState = Pattern.compile("-?(([1-9][0-9]*)|0)?(\\.[0-9]*)?");
        UnaryOperator<TextFormatter.Change> filter = c -> {
            String text = c.getControlNewText();
            if (validEditingState.matcher(text).matches()) {
                return c;
            } else {
                return null;
            }
        };
        StringConverter<Double> converter = new StringConverter<Double>() {
            @Override
            public Double fromString(String s) {
                if (s.isEmpty() || "-".equals(s) || ".".equals(s) || "-.".equals(s)) {
                    return 0.0;
                } else {
                    return Double.valueOf(s);
                }
            }

            @Override
            public String toString(Double d) {
                return d.toString();
            }
        };

        return new TextFormatter<>(converter, 0.0, filter);
    }


    @FXML
    private void btnViewCar ( int car_id){
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            fxmlLoader.load(getClass().getResource("/view/admin/pages/cars/view-car.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        AnchorPane root = fxmlLoader.getRoot();
        carsContent.getChildren().clear();
        carsContent.getChildren().add(root);

        ViewCarController controller = fxmlLoader.getController();
        controller.fillViewingCarFields(car_id);
    }

    private void btnEditCar(int car_id){
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            fxmlLoader.load(getClass().getResource("/view/admin/pages/cars/edit-car.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        AnchorPane root = fxmlLoader.getRoot();
        carsContent.getChildren().clear();
        carsContent.getChildren().add(root);

        EditCarController controller = fxmlLoader.getController();
        controller.fillEditingCarFields(car_id);
    }

    /**
     * Bu metot int alanları doğrulamak için
     *
     * @return TextFormatter
     */
    public static TextFormatter<Integer> formatIntField () {
//        Pattern validEditingState = Pattern.compile("-?(0|[1-9]\\d*)");
        Pattern validEditingState = Pattern.compile("^[0-9]+$");
        UnaryOperator<TextFormatter.Change> filter = c -> {
            String text = c.getControlNewText();
            if (validEditingState.matcher(text).matches()) {
                return c;
            } else {
                return null;
            }
        };
        StringConverter<Integer> converter = new StringConverter<Integer>() {
            @Override
            public Integer fromString(String s) {
                if (s.isEmpty() || "-".equals(s) || ".".equals(s) || "-.".equals(s)) {
                    return 0;
                } else {
                    return Integer.valueOf(s);
                }
            }

            @Override
            public String toString(Integer d) {
                return d.toString();
            }
        };

        return new TextFormatter<>(converter, 0, filter);
    }

    @FXML
    boolean areCarInputsValid ( int carBrandId, int carColorId, String fieldAddModelName, String
            fieldAddModelYear, String fieldAddCarDailyPrice, String fieldAddCarDescription){

        System.out.println("TODO: Better validate inputs.");
        String errorMessage = "";

        if (carBrandId == 0) {
            errorMessage += "Not valid brand id!\n";
        }
        if (carColorId == 0) {
            errorMessage += "Not valid color id!\n";
        }
        if (fieldAddModelName == null || fieldAddModelName.length() < 2) {
            errorMessage += "Model Name is not valid!\n";
        }
        if (!HelperMethods.validateCarPrice(fieldAddCarDailyPrice)) {
            errorMessage += "Daily price is not valid!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Hata mesajı gösterir
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }


    }


}
