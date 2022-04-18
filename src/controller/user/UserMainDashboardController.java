package controller.user;

import controller.UserSessionController;
import controller.user.pages.UserCarsController;
import controller.user.pages.UserHomeController;
import controller.user.pages.UserRentalsController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class handles the simple user dashboard interactions.
 */
public class UserMainDashboardController implements Initializable {
    public Button btnHome;
    public Button btnCars;
    public Button btnRentals;
    public Button lblLogOut;
    public AnchorPane dashHead;
    @FXML
    private StackPane dashContent;
    @FXML
    private Label lblUsrName;


    /**
     * This method handles the Home button click.
     * It loads the home page and it's contents.
     * @param actionEvent       Accepts ActionEvent.
     * @since                   1.0.0
     */
    public void btnHomeOnClick(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = loadFxmlPage("/view/user/pages/home/home.fxml");
        UserHomeController homeController = fxmlLoader.getController();
        homeController.getDashboardCarCount();
        homeController.getDashboardRentalsCount();
    }

    /**
     * This method handles the Orders button click.
     * It loads the Orders page and it's contents.
     * @param actionEvent       Accepts ActionEvent.
     * @since                   1.0.0
     */
    public void btnRentalsOnClick(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = loadFxmlPage("/view/user/pages/rentals/rentals.fxml");
        UserRentalsController rentalsController = fxmlLoader.getController();
        rentalsController.listRentals();
    }

    /**
     * This method handles the Products button click.
     * It loads the Products page and it's contents.
     * @param actionEvent       Accepts ActionEvent.
     * @since                   1.0.0
     */
    public void btnCarsOnClick(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = loadFxmlPage("/view/user/pages/cars/cars.fxml");
        UserCarsController userController = fxmlLoader.getController();
        userController.listCar();
    }

    /**
     * This method handles the LogOut button click.
     * On click and confirmation it opens the login view and clears the user session instance.
     * @param actionEvent       Accepts ActionEvent.
     * @throws IOException      If an input or output exception occurred.
     * @since                   1.0.0
     */
    public void btnLogOutOnClick(ActionEvent actionEvent) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Are you sure that you want to log out?");
        alert.setTitle("Log Out?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            UserSessionController.cleanUserSession();
            Stage dialogStage;
            new Stage();
            Node node = (Node) actionEvent.getSource();
            dialogStage = (Stage) node.getScene().getWindow();
            dialogStage.close();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/view/login.fxml")));
            dialogStage.setScene(scene);
            dialogStage.show();
        }
    }

    /**
     * This private helper method loads the view file.
     * @param view_path         Accepts path of view file.
     * @since                   1.0.0
     */
    private FXMLLoader loadFxmlPage(String view_path) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            fxmlLoader.load(getClass().getResource(view_path).openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        AnchorPane root = fxmlLoader.getRoot();
        dashContent.getChildren().clear();
        dashContent.getChildren().add(root);

        return fxmlLoader;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblUsrName.setText(UserSessionController.getUserFullName());

        FXMLLoader fxmlLoader = loadFxmlPage("/view/user/pages/home/home.fxml");
        UserHomeController homeController = fxmlLoader.getController();
        homeController.getDashboardCarCount();
        homeController.getDashboardRentalsCount();
    }
}
