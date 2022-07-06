package helpers;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Objects;

public class PasswordHasher {

    public SecuredPassword generateSecuredPassword(String passwordToHash) {
        String salt = getSalt();
        String securePassword = getSecurePassword(passwordToHash, salt);
        return new SecuredPassword(securePassword, salt);
    }

    public boolean checkIfPasswordMatches(String nickname,String password){
        try {
            DataBaseManager dataBaseManager = new DataBaseManager();
            SecuredPassword passwordFromDB = dataBaseManager.getHashedPassword(nickname);

            if(Objects.equals(getSecurePassword(password, passwordFromDB.salt), passwordFromDB.password)){
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String getSecurePassword(String passwordToHash, String salt) {
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");

            // Add password bytes to digest
            messageDigest.update(salt.getBytes());

            // Get the hash's bytes
            byte[] bytes = messageDigest.digest(passwordToHash.getBytes());

            // This bytes[] has bytes in decimal format;
            // Convert it to hexadecimal format
            StringBuilder stringBuilder = new StringBuilder();

            for (int i = 0; i < bytes.length; i++) {
                stringBuilder.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            // Get complete hashed password in hex format
            generatedPassword = stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    private String getSalt() {
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");
            // Create array for salt
            byte[] salt = new byte[16];
            // Get a random salt
            secureRandom.nextBytes(salt);
            // return salt
            return new String(salt);
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return "UNKNOWN";
    }

    private static int randomPosition() {
        return (int) ((Math.random() * 2) + 1);
    }
}