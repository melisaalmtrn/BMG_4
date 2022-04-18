package app.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Bu class kullanılacak yardımcı metotları barındırır
 */
public class HelperMethods {

    /**
     * Bu metot fullname i doğrulamak için kullanılır
     * {@link}              https://stackoverflow.com/a/35458020
     */
    public static boolean validateFullName(String fullName) {
//      ^                       # string başlangıç
//      [a-zA-Z]{4,}            # en az 4 ASCII karekteri kullanılmalı
//      (?: [a-zA-Z]+){0,2}     # bir boşluğun 0 ila 2 kez tekrarlanması
//      $                       # string sonu
        Matcher matcher = Pattern.compile("^[A-Z][a-zA-Z]{3,}(?: [A-Z][a-zA-Z]*){0,2}$", Pattern.CASE_INSENSITIVE).matcher(fullName);
        return matcher.find();
    }

    /**
     * Bu metot kullanıcı adı nın girilip girilmediğini kontrol eder
     * {@link}              https://stackoverflow.com/a/6782475
     */
    public static boolean validateUsername(String username) {
//      ^                       # string başangıç
//      [a-zA-Z]                # küçük büyük karekterler
//      \\w{4, 29}              # kalan öğeler sonuna ulaşıncaya kadar alt çizgiyi içeren ve $ ile temsil edilen kelime öğeleridir.
//      {4, 29}                 # girilen değerler en az 5 en fazla 30 karekter olabilir
        Matcher matcher = Pattern.compile("^[A-Za-z]\\w{4,29}$", Pattern.CASE_INSENSITIVE).matcher(username);
        return matcher.find();
    }

    /**
     * Bu metot girilen email adresini kontol eder
     * {@link}              https://stackoverflow.com/a/8204716
     */
    public static boolean validateEmail(String emailStr) {
        Matcher matcher = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE).matcher(emailStr);
        return matcher.find();
    }

    /**
     * Bu metot şifre doğrulamak için kullanılır
     */
    public static boolean validatePassword(String password) {
//        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
//        ^                 # string başlangıcı
//        (?=.*[0-9])       # bir rakam en az bir kez geçmelidir
//        (?=.*[a-z])       # bir küçük harf en az bir kez geçmelidir
//        (?=.*[A-Z])       # bir büyük harf en az bir kez geçmelidir
//        (?=.*[@#$%^&+=])  # bir özel karekter en az bir kez geçmelidir
//        (?=\S+$)          # boşluk kullanılamamlı
//        .{8,}             # en az 8 karekter
//        $                 # string sonu
        return password.matches("^.{6,16}$");
    }

    /**
     * Bu metot çift giriş için doğrulama yapar
     * {@link}              https://stackoverflow.com/a/23106803
     */
    public static boolean validateCarPrice(String carPrice) {
        return carPrice.matches("^[0-9]+(|\\.)[0-9]+$");
    }

    /**
     * Bu metot uyarı vermek için kullanılır
     */
    public static void alertBox(String infoMessage, String headerText, String title) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(String.valueOf(infoMessage));
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }

}
