package cn.com.qucl.common.utils;


import cn.com.qucl.common.exceptions.DataConvertException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Slf4j
public class AESUtils {

    /**
     * 密钥
     */
    private final String password;

    /**
     * 加密类型
     */
    private static final String KEY_ALGORITHM = "AES";

    /**
     * 默认加密算法
     */
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * 签名算法
     */
    private static final String SIGN_ALGORITHMS = "SHA1PRNG";

    public AESUtils(String password) {
        this.password = password;
    }

    /**
     * AES 加密操作
     */
    public String encrypt(String content) {
        try {
            // 创建密码器
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);
            SecureRandom sr = new SecureRandom();
            // 初始化为加密模式的密码器
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(), sr);
            // 加密
            byte[] result = cipher.doFinal(byteContent);
            return Base64.encodeBase64String(result);
        } catch (Exception ex) {
            log.error("content:" + content);
            log.error(ex.getMessage(), ex);
            throw new DataConvertException("加密失败:" + ex.getMessage());
        }
    }

    /**
     * 解密
     */
    public String decrypt(String content) {
        try {
            //url编码时 "+"会被转成%2B 但解码时却会变成" "
            content = content.replace(" ", "+");

            SecureRandom sr = new SecureRandom();
            //实例化
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            //使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), sr);
            //执行操作
            byte[] bytes = Base64.decodeBase64(content);
            byte[] result = cipher.doFinal(bytes);
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            log.error("content:" + content);
            log.error(ex.getMessage(), ex);
            throw new DataConvertException("解密失败:" + ex.getMessage());
        }
    }

    /**
     * 生成加密秘钥
     */
    private SecretKeySpec getSecretKey() throws NoSuchAlgorithmException {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        //AES 要求密钥长度为 128
        //不这么写会出现Given final block not properly padded的异常
        //SecureRandom 实现完全隨操作系统本身的內部狀態，除非調用方在調用 getInstance 方法之後又調用了 setSeed 方法；
        //该实现在 windows 上每次生成的 key 都相同，但是在 solaris 或部分 linux 系统上则不同。
        SecureRandom secureRandom = SecureRandom.getInstance(SIGN_ALGORITHMS);
        secureRandom.setSeed(password.getBytes(StandardCharsets.UTF_8));
        kg.init(128, secureRandom);
        //生成一个密钥
        SecretKey secretKey = kg.generateKey();
        // 转换为AES专用密钥
        return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
    }
}
