package app.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHAEncryptor implements Facede {

//    public static byte[] encrypt (String password)  {

//        try {
//            MessageDigest md = MessageDigest.getInstance("SHA-256");
//            byte[] result = md.digest(password.getBytes());
//            StringBuffer sb = new StringBuffer();
//            for (int i = 0; i < result.length; i++ ){
//                sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
//            }
//            return  sb.toString();
//
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
//    }


    public static String encrypt(String password)
    {
        try {
            // getInstance() method is called with algorithm SHA-1
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(password.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            // return the HashText
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }





    @Override
    public void encrypt() {

    }
}
