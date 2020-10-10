package cn.com.qucl.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author qucl
 * @date 2020/9/16 15:50
 * rsa加密工具
 * 非对称加密
 */
@Slf4j
public class RSAUtils {
    private static final String ALGORITHM = "RSA";

    private static final String PROVIDER = "BC";

    private static final String TRANSFORMATION = "RSA/None/PKCS1Padding";

    private static final int KEY_SIZE = 1024;

    private static KeyPair keyPair = null;

    static {
        try {
            //初始化密钥对
            Security.addProvider(new BouncyCastleProvider());
            SecureRandom secureRandom = new SecureRandom();
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM, PROVIDER);
            keyPairGenerator.initialize(KEY_SIZE, secureRandom);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }


    /**
     * 获取公钥
     */
    private static RSAPublicKey getPublicKey() {
        return (RSAPublicKey) keyPair.getPublic();
    }

    /**
     * 获取Base64编码的公钥
     */
    public static String getBase64PublicKey() {
        RSAPublicKey publicKey = getPublicKey();
        return Base64.encodeBase64String(publicKey.getEncoded());
    }

    /**
     * 使用公钥加密
     *
     * @param data 内容
     * @return 加密后的字符串
     */
    public static String encrypt(byte[] data) {
        String cipherText = "";
        try {
            Cipher cipher = Cipher.getInstance(keyPair.getPublic().getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
            cipherText = Base64.encodeBase64String(cipher.doFinal(data));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return cipherText;
    }


    /**
     * 使用私钥解密
     *
     * @param cipherText 加密后的字符串
     * @return 解密后的数据
     */
    public static String decrypt(String cipherText) {
        String plaintext = "";
        try {
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance(TRANSFORMATION, PROVIDER);
            RSAPrivateKey pbk = (RSAPrivateKey) keyPair.getPrivate();
            cipher.init(Cipher.DECRYPT_MODE, pbk);
            byte[] data = cipher.doFinal(Base64.decodeBase64(cipherText));
            plaintext = new String(data);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return plaintext;
    }
}
