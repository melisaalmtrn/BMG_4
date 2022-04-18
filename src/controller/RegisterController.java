package controller;

import app.utils.EncryptorFacede;
import app.utils.HelperMethods;
import app.utils.PasswordUtils;
import app.utils.Base64Encryptor;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.DataSource;
import model.User;

import java.io.IOException;
import java.sql.SQLException;


/**
 * Bu class kayıt işlemlerini yapar
 */
public class RegisterController {

    @FXML
    public TextField fullNameField;
    @FXML
    public TextField usernameField;
    @FXML
    public TextField emailField;
    @FXML
    public PasswordField passwordField;

    Stage dialogStage = new Stage();
    Scene scene;

    EncryptorFacede encryptorFacede = new EncryptorFacede();
    Base64Encryptor base64Encryptor = new Base64Encryptor();


    /**
     * Oturum açma işlemini kontrol eder
     * Kullanıcıyı giriş sayfasına yönlendirir
     */
    public void handleLoginButtonAction(ActionEvent actionEvent) throws IOException {
        Stage dialogStage;
        Node node = (Node) actionEvent.getSource();
        dialogStage = (Stage) node.getScene().getWindow();
        dialogStage.close();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/view/login.fxml")));
        dialogStage.setScene(scene);
        dialogStage.show();
    }

    /**
     * Kayıt butonunu kontrol eder
     * Kullanıcının girdiği verileri alır ve gerekli doğrulamaları yapar
     * Girilen bilgiler doğru ise verileri vertabanına kaydeder
     * Yeni bir UserSessionController örneği oluşturur ve kullanıcı ekranına geçiş yapar
     */
    public void handleRegisterButtonAction(ActionEvent actionEvent) throws SQLException {
        String validationErrors = "";
        boolean errors = false;
        String fullName = fullNameField.getText();
        String username = usernameField.getText();
        String email = emailField.getText();
        String providedPassword = passwordField.getText();

        // Full Name doğrulama
        if (fullName == null || fullName.isEmpty()) {
            validationErrors += "Please enter your Name and Surname! \n";
            errors = true;
        } else if (!HelperMethods.validateFullName(fullName)) {
            validationErrors += "Please enter a valid Name and Surname! \n";
            errors = true;
        }

        // Username doğrulama
        if (username == null || username.isEmpty()) {
            validationErrors += "Please enter a username! \n";
            errors = true;
        } else if (!HelperMethods.validateUsername(username)) {
            validationErrors += "Please enter a valid Username! \n";
            errors = true;
        } else {
            User userByUsername = DataSource.getInstance().getUserByUsername(username);
            if (userByUsername.getUsername() != null) {
                validationErrors += "There is already a user registered with this username! \n";
                errors = true;
            }
        }

        // Email doğrulama
        if (email == null || email.isEmpty()) {
            validationErrors += "Please enter an email address! \n";
            errors = true;
        } else if (!HelperMethods.validateEmail(email)) {
            validationErrors += "Please enter a valid Email address! \n";
            errors = true;
        } else {
            User userByEmail = DataSource.getInstance().getUserByEmail(email);
            if (userByEmail.getEmail() != null) {
                validationErrors += "There is already a user registered with this email address! \n";
                errors = true;
            }
        }

        // Password doğrulama
        if (providedPassword == null || providedPassword.isEmpty()) {
            validationErrors += "Please enter the password! \n";
            errors = true;
        } else if (!HelperMethods.validatePassword(providedPassword)){
            validationErrors += "Password must be at least 6 and maximum 16 characters! \n";
            errors = true;
        }

        if (errors) {
            HelperMethods.alertBox(validationErrors, null, "Registration Failed!");
        } else {

            String salt = base64Encryptor.getSalt(30);
            String securePassword = PasswordUtils.generateSecurePassword(providedPassword , salt);

            Task<Boolean> addUserTask = new Task<Boolean>() {
                @Override
                protected Boolean call() {
                    return DataSource.getInstance().insertNewUser(fullName, username, email, securePassword, salt);
                }
            };

            addUserTask.setOnSucceeded(e -> {
                if (addUserTask.valueProperty().get()) {
                    User user = null;
                    try {
                        user = DataSource.getInstance().getUserByEmail(email);
                    } catch (SQLException err) {
                        err.printStackTrace();
                    }

                    // Method invocation 'getId' may produce 'NullPointerException'
                    assert user != null;

                    UserSessionController.setUserId(user.getId());
                    UserSessionController.setUserFullName(user.getFullname());
                    UserSessionController.setUserName(user.getUsername());
                    UserSessionController.setUserEmail(user.getEmail());
                    UserSessionController.setUserAdmin(user.getAdmin());
                    UserSessionController.setUserStatus(user.getStatus());

                    Node node = (Node) actionEvent.getSource();
                    dialogStage = (Stage) node.getScene().getWindow();
                    dialogStage.close();
                    if (user.getAdmin() == 0) {
                        try {
                            scene = new Scene(FXMLLoader.load(getClass().getResource("../view/user/main-dashboard.fxml")));
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    } else if (user.getAdmin() == 1) {
                        try {
                            scene = new Scene(FXMLLoader.load(getClass().getResource("../view/admin/main-dashboard.fxml")));
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                    dialogStage.setScene(scene);
                    dialogStage.show();
                }
            });

            new Thread(addUserTask).start();

        }
    }
}
