package controller.admin.pages.cars;

import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.text.Text;
import model.Brands;
import model.Colors;
import model.DataSource;

public class AddCarController extends CarsController {

    @FXML
    public ComboBox<Brands> fieldAddCarBrandId;
    public TextField fieldAddModelName;
    public ComboBox<Colors> fieldAddCarColorId;
    public TextField fieldAddModelYear;
    public TextField fieldAddCarDailyPrice;
    public TextArea fieldAddCarDescription;
    public Text viewCarResponse;


    @FXML
    private void initialize() {
        fieldAddCarBrandId.setItems(FXCollections.observableArrayList(DataSource.getInstance().getCarBrands(DataSource.ORDER_BY_ASC)));
        fieldAddCarColorId.setItems((FXCollections.observableArrayList(DataSource.getInstance().getCarColors(DataSource.ORDER_BY_ASC))));

        TextFormatter<Double> textFormatterDouble = formatDoubleField();
        TextFormatter<Integer> textFormatterInt = formatIntField();

        fieldAddCarDailyPrice.setTextFormatter(textFormatterDouble);
        fieldAddModelYear.setTextFormatter(textFormatterInt);
    }

    @FXML
    private void btnAddCarOnAction() {
        Brands brand = fieldAddCarBrandId.getSelectionModel().getSelectedItem();
        Colors color = fieldAddCarColorId.getSelectionModel().getSelectedItem();

        int brand_id = 0;
        int color_id = 0;

        if (brand != null) {
            brand_id = brand.getId();
        }
        if (color != null) {
            color_id = color.getId();
        }

        assert brand != null;
        assert color != null;
        if (areCarInputsValid(brand_id, color_id, fieldAddModelYear.getText(), fieldAddModelName.getText(), fieldAddCarDailyPrice.getText(), fieldAddCarDescription.getText())) {

            int carBrandId = brand.getId();
            int carColorId = color.getId();
            int modelYear = Integer.parseInt(fieldAddModelYear.getText());
            String modelName = fieldAddModelName.getText();
            double dailyPrice = Double.parseDouble(fieldAddCarDailyPrice.getText());
            String description = fieldAddCarDescription.getText();

            Task<Boolean> addCarTask = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    return DataSource.getInstance().insertNewCar(carBrandId, carColorId, modelYear, modelName, dailyPrice, description);
                }
            };

            addCarTask.setOnSucceeded(e -> {
                if (addCarTask.valueProperty().get()) {
                    viewCarResponse.setVisible(true);
                    System.out.println("Car added!");
                }
            });

            new Thread(addCarTask).start();
        }

    }

}
