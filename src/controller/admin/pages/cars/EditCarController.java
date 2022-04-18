package controller.admin.pages.cars;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.text.Text;
import model.Brands;
import model.Car;
import model.Colors;
import model.DataSource;

public class EditCarController extends CarsController{

    @FXML
    public Text viewCarResponse;
    public ComboBox<Brands> fieldEditCarBrandId;
    public TextField fieldEditModelName;
    public ComboBox<Colors> fieldEditCarColorId;
    public TextField fieldEditModelYear;
    public TextField fieldEditCarDailyPrice;
    public TextArea fieldEditCarDescription;
    public TextField fieldEditCarId;
    public Text viewModelName;


    @FXML
    private void initialize(){
        fieldEditCarBrandId.setItems(FXCollections.observableArrayList(DataSource.getInstance().getCarBrands(DataSource.ORDER_BY_ASC)));
        fieldEditCarColorId.setItems(FXCollections.observableArrayList(DataSource.getInstance().getCarColors(DataSource.ORDER_BY_ASC)));

        TextFormatter<Double> textFormatterDouble = formatDoubleField();
        TextFormatter<Integer> textFormatterInt = formatIntField();

        fieldEditCarDailyPrice.setTextFormatter(textFormatterDouble);
        fieldEditModelYear.setTextFormatter(textFormatterInt);

    }

    @FXML
    private void btnEditCarOnAction(){
        Brands brand = fieldEditCarBrandId.getSelectionModel().getSelectedItem();
        Colors color = fieldEditCarColorId.getSelectionModel().getSelectedItem();

        int brand_id = 0;
        int color_id = 0;

        if(brand != null){
            brand_id = brand.getId();
        }
        if(color != null){
            color_id = color.getId();
        }

        assert brand != null;
        assert  color != null;
        if (areCarInputsValid(brand_id, color_id, fieldEditModelYear.getText(), fieldEditModelName.getText(), fieldEditCarDailyPrice.getText(), fieldEditCarDescription.getText()));

        int car_id =Integer.parseInt(fieldEditCarId.getText());
        int carBrandId = brand.getId();
        int carColorId = color.getId();
        int modelYear = Integer.parseInt(fieldEditModelYear.getText());
        String modelName = fieldEditModelName.getText();
        double dailyPrice = Double.parseDouble(fieldEditCarDailyPrice.getText());
        String description = fieldEditCarDescription.getText();

        Task<Boolean> addCarTask = new Task<Boolean>() {
            @Override
            protected Boolean call() {
                return DataSource.getInstance().updateOneCar(car_id, carBrandId, carColorId, modelYear , modelName , dailyPrice, description );
            }
        };

        addCarTask.setOnSucceeded(e -> {
            if (addCarTask.valueProperty().get()) {
                viewCarResponse.setVisible(true);
                System.out.println("Car edited!");
            }
        });

        new Thread(addCarTask).start();
    }


    public void fillEditingCarFields(int car_id){
        Task<ObservableList<Car>> fillCarTask = new Task<ObservableList<Car>>() {
            @Override
            protected ObservableList<Car> call()  {
                return FXCollections.observableArrayList(DataSource.getInstance().getOneCar(car_id));
            }
        };

        fillCarTask.setOnSucceeded(e -> {
            Brands brand = new Brands();
            brand.setId(fillCarTask.valueProperty().getValue().get(0).getBrand_id());
            brand.setBrand_name(fillCarTask.valueProperty().getValue().get(0).getBrand_name());
            fieldEditCarBrandId.getSelectionModel().select(brand);

            Colors color = new Colors();
            color.setId(fillCarTask.valueProperty().getValue().get(0).getColor_id());
            color.setColor_name(fillCarTask.valueProperty().getValue().get(0).getColor_name());
            fieldEditCarColorId.getSelectionModel().select(color);

            viewModelName.setText("Editing: " + fillCarTask.valueProperty().getValue().get(0).getModel_name());
            fieldEditCarId.setText(String.valueOf(fillCarTask.valueProperty().getValue().get(0).getId()));
            fieldEditModelName.setText(fillCarTask.valueProperty().getValue().get(0).getModel_name());
            fieldEditModelYear.setText(String.valueOf(fillCarTask.valueProperty().getValue().get(0).getModel_year()));
            fieldEditCarDailyPrice.setText(String.valueOf(fillCarTask.valueProperty().getValue().get(0).getDaily_price()));
            fieldEditCarDescription.setText(fillCarTask.valueProperty().getValue().get(0).getDescription());


        });

        new Thread(fillCarTask).start();
    }

}
