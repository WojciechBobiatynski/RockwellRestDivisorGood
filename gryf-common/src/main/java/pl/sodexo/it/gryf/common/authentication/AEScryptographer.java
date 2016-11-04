package pl.sodexo.it.gryf.common.authentication;

import pl.sodexo.it.gryf.common.exception.authentication.GryfAuthenticationException;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

import static pl.sodexo.it.gryf.common.utils.GryfConstants.CHAR_SET_UTF_8_ENCODING;

/**
 * Klasa zajmująca się enkrypcją i dekrypcją na potrzeby zapisu i odczytu hasła
 *
 * Created by akmiecinski on 04.11.2016.
 */
public final class AEScryptographer {

    private static final String SECRET_KEY = "8l5SA49KtADbCq==";
    private static final String AES_CODE = "AES";

    private AEScryptographer(){}

    public static String encrypt(String text){
        try {
            Key aesKey = new SecretKeySpec(SECRET_KEY.getBytes(), AES_CODE);
            Cipher cipher = Cipher.getInstance(AES_CODE);
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] utf8in = text.getBytes(CHAR_SET_UTF_8_ENCODING);
            byte[] utf8encrypted = cipher.doFinal(utf8in);
            return new BASE64Encoder().encode(utf8encrypted);
        } catch (Exception e) {
            throw new GryfAuthenticationException("Nie udało się zaszyforwać tekstu",e);
        }
    }

    public static String decrypt(String encryptedText){
        try {
            Key aesKey = getKey();
            Cipher cipher = Cipher.getInstance(AES_CODE);
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            byte[] base64decoded = new BASE64Decoder().decodeBuffer(encryptedText);
            byte[] utf8decoded = cipher.doFinal(base64decoded);
            return new String(utf8decoded, CHAR_SET_UTF_8_ENCODING);
        } catch (Exception e) {
            throw new GryfAuthenticationException("Nie udało się zdeszyfrować tekstu",e);
        }
    }

    private static Key getKey() {
        return new SecretKeySpec(SECRET_KEY.getBytes(), AES_CODE);
    }
}
