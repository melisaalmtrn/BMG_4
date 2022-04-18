package app.utils;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;


/**
 * Bu class , aşağıdakiler için yardımcı yöntemler sağlar:
 * Güvenli kullanıcı şifresini Base64'e kodlar
 * Salt değeri oluşturur
 * Kullanıcı parolasını şifrelemek için parola tabanlı şifreleme kullanır
 * Girilen şifreyi doğrular
 * {@link}      https://www.appsdeveloperblog.com/encrypt-user-password-example-java/
 */
public class PasswordUtils {

    private static final Random RANDOM = new SecureRandom();
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";



    /**
     * Bu metot parolayı oluşturur
     * {@link}      https://www.appsdeveloperblog.com/encrypt-user-password-example-java/
     *
     * @param password
     * @param
     * @return
     */
    public static String generateSecurePassword(String password, String salt) {
        Base64Encryptor base64Encryptor = new Base64Encryptor();
        String returnValue = null;
//
//        byte[] securePassword = hash(password.toCharArray(), salt.getBytes());
//
//        returnValue = Base64.getEncoder().encodeToString(securePassword);
//
//        return returnValue;

        byte[] securePassword = base64Encryptor.encrypt(password.toCharArray(), salt.getBytes());

        returnValue = Base64.getEncoder().encodeToString(securePassword);

        return returnValue;
    }

    /**
     * Bu metot kullanıcının girdiği şifrenin veritabanındaki şifre ile aynı olup olmadığını kontrol eder
     * {@link}          https://www.appsdeveloperblog.com/encrypt-user-password-example-java/
     */
    public static boolean verifyUserPassword(String providedPassword, String securedPassword, String salt) {
        boolean returnValue = false;

        // Yeni şifre oluşturur
        String newSecurePassword = generateSecurePassword(providedPassword , salt);

        // Eski şifre ile yeni şifrenin aynı olup olmadığını kontrol eder
        returnValue = newSecurePassword.equalsIgnoreCase(securedPassword);

        return returnValue;
    }



}
