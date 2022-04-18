package controller.admin.pages.cars;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import model.Brands;
import model.Car;
import model.Colors;
import model.DataSource;

import java.awt.*;

public class ViewCarController extends CarsController {


    @FXML
    public ComboBox<Brands> fieldViewCarBrandId;
    public TextField fieldViewModelName;
    public ComboBox<Colors> fieldViewCarColorId;
    public TextField fieldViewModelYear;
    public TextField fieldViewCarDailyPrice;
    public TextArea fieldViewCarDescription;
    public Text viewModelName;


    @FXML
    private void initialize(){
        fieldViewCarBrandId.setItems(FXCollections.observableArrayList(DataSource.getInstance().getCarBrands(DataSource.ORDER_BY_ASC)));
        fieldViewCarColorId.setItems(FXCollections.observableArrayList(DataSource.getInstance().getCarColors(DataSource.ORDER_BY_ASC)));
    }

    public void fillViewingCarFields(int car_id){
        Task<ObservableList<Car>> fillCarTask = new Task<ObservableList<Car>>() {
            @Override
            protected ObservableList<Car> call() {
                return FXCollections.observableArrayList(DataSource.getInstance().getOneCar(car_id));
            }
        };

        fillCarTask.setOnSucceeded(e ->{
            viewModelName.setText("Viewing: " + fillCarTask.valueProperty().getValue().get(0).getModel_name());
            fieldViewModelName.setText(fillCarTask.valueProperty().getValue().get(0).getModel_name());
            fieldViewModelYear.setText(String.valueOf(fillCarTask.valueProperty().getValue().get(0).getModel_year()));
            fieldViewCarDailyPrice.setText(String.valueOf(fillCarTask.valueProperty().getValue().get(0).getDaily_price()));
            fieldViewCarDescription.setText(fillCarTask.valueProperty().getValue().get(0).getDescription());

            Brands brand = new Brands();
            brand.setId(fillCarTask.valueProperty().getValue().get(0).getBrand_id());
            brand.setBrand_name(fillCarTask.valueProperty().getValue().get(0).getBrand_name());
            fieldViewCarBrandId.getSelectionModel().select(brand);

            Colors color = new Colors();
            color.setId(fillCarTask.valueProperty().getValue().get(0).getColor_id());
            color.setColor_name(fillCarTask.valueProperty().getValue().get(0).getColor_name());
            fieldViewCarColorId.getSelectionModel().select(color);

        });

        new Thread(fillCarTask).start();
    }










}
