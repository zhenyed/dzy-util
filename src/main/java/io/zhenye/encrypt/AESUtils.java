package io.zhenye.encrypt;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AESUtils {

    private static final String AES_CIPHER = "AES/GCM/NoPadding";

    private static final String AES_ALGORITHM = "AES";

    private static final int GCM_TAG_LENGTH = 128;

    private static byte[] encrypt(byte[] plainTextBytes, String key, String iv) throws
            NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        // Get Cipher Instance
        Cipher cipher = Cipher.getInstance(AES_CIPHER);

        // Create SecretKeySpec
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), AES_ALGORITHM);

        // Create GCMParameterSpec
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv.getBytes());

        // Initialize Cipher for ENCRYPT_MODE
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmParameterSpec);

        // Perform Encryption
        return cipher.doFinal(plainTextBytes);
    }

    public static String encrypt(String plainText, String key, String iv)
            throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException,
            IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException {
        return Base64.getEncoder().encodeToString(encrypt(plainText.getBytes("utf-8"), key, iv));
    }

    private static byte[] decrypt(byte[] cipherTextBytes, String key, String iv) throws
            NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        // Get Cipher Instance
        Cipher cipher = Cipher.getInstance(AES_CIPHER);

        // Create SecretKeySpec
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), AES_ALGORITHM);

        // Create GCMParameterSpec
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv.getBytes());

        // Initialize Cipher for DECRYPT_MODE
        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec);

        // Perform Decryption
        return cipher.doFinal(cipherTextBytes);
    }

    public static String decrypt(String cipherText, String key, String iv) throws
            NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException,
            IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException {
        return new String(decrypt(Base64.getDecoder().decode(cipherText), key, iv), "utf-8");
    }

}
