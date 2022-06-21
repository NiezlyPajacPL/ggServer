package helpers;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public class HashPassword {

    public HashPassword() {
    }

    public String generateSecuredPassword(String password) {
        return BCrypt.hashpw(password,BCrypt.gensalt(12));
    }


    public boolean checkIfPasswordMatches(String nickname,String password){
        try {
            FileHandler fileHandler = new FileHandler();
            if(BCrypt.checkpw(password,fileHandler.getHashedPassword(nickname))){
                return  true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  false;
    }

   /* public String generateSecuredPassword() {
        char[] passwordInCharArray = password.toCharArray();
        byte[] salt = getSalt();


        PBEKeySpec pbeKeySpec = new PBEKeySpec(passwordInCharArray, salt, 1000, 64 * 8);
        SecretKeyFactory secretKeyFactory = null;
        try {
            secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = secretKeyFactory.generateSecret(pbeKeySpec).getEncoded();
            return 1000 + ":" + toHex(salt) + ":" + toHex(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

    }

    private byte[] getSalt() {
        SecureRandom sr = null;
        try {
            sr = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    private static String toHex(byte[] array) {
        BigInteger bigInteger = new BigInteger(1, array);
        String hex = bigInteger.toString(16);

        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    */
}