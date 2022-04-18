package app.utils;

import model.Cars;
import model.Company;
import model.DataSource;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static app.utils.EncryptorFacede.EncType.BASE64;
import static app.utils.EncryptorFacede.EncType.SHA;
import static org.junit.jupiter.api.Assertions.*;


class EncryptorFacedeTest {


    EncryptorFacede testInstance = new EncryptorFacede();
    Base64Encryptor base64Encryptor = new Base64Encryptor();

    DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource (DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Test
    public void base64(){

        String s = "w";
        String password = base64Encryptor.getSalt(20);
        byte[] hashing = Base64Encryptor.encrypt( s.toCharArray() ,password.getBytes());
        String expect = "w";
        boolean actual =  PasswordUtils.verifyUserPassword(s,password, expect);
    }


    @org.junit.jupiter.api.Test
    void encrypt() {
//            testInstance.encrypt(0, 1 ,2 ,BASE64);
//        SHAEncryptor shaEncryptor = new SHAEncryptor();
//        String s = "w";
//        String expect = "w";
//        byte[] actual = base64Encryptor.encrypt(s)
//        assertEquals(expect,actual);
    }

    @Test
    public void userTest() {
        User user = new User();

        user.setName("Deneme");
        String name = user.getName();
        Assertions.assertEquals("Deneme",name);

        user.setUsername("deneme");
        String username = user.getUsername();
        Assertions.assertEquals("deneme",username);

        user.setEmail("deneme@deneme.com");
        String email = user.getEmail();
        Assertions.assertEquals("deneme@deneme.com",email);

        user.setPassword("123");
        String password = "123";
        Assertions.assertEquals("123",password);

        System.out.println("User test çalıştı");

    }


    public void loginTest() {

        String username = "hasangozen";
        String password = "hasan1";
        int sayac = 0;
        try {
            Statement statement = this.getDataSource().conn.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from users");

            while (resultSet.next()) {
                User user = new User();
                resultSet.getString(username);
                resultSet.getString(password);
                if (resultSet.getString(username).equals(username) && resultSet.getString(password).equals(password)) {
                    sayac++;
                }
            }

            if (sayac == 0) {
                System.out.println("Giriş başarısız");
            } else {
                System.out.println("Giriş başarılı");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void observerTest() {
        BigDecimal maxKilometre = BigDecimal.valueOf(100);

        Cars cars = new Cars(maxKilometre);

        Company rentACar = new Company("Rent a car");


        cars.add(rentACar);


        for (int i = cars.getKilometre().intValue(); i <= 110; i++ ){
            cars.setKilometre(BigDecimal.valueOf(i));
        }
    }

}