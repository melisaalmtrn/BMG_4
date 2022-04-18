package app.utils;

public class EncryptorFacede {

    MD5Encryptor md5Encryptor = new MD5Encryptor();
    SHAEncryptor shaEncryptor = new SHAEncryptor();
    Base64Encryptor base64Encryptor = new Base64Encryptor();


    public byte[] encrypt( char[] sourcepassword , String password, byte[] salt , EncType encType){

        switch (encType){
            case MD5: md5Encryptor.encrypt(password); break;
            case SHA: shaEncryptor.encrypt(password); break;
            case BASE64: base64Encryptor.encrypt(sourcepassword ,salt);
            default: throw new IllegalArgumentException(encType.toString());
        }







        return new byte[0];

    }

    public enum EncType {
        SHA,
        MD5,
        BASE64

    }

}

