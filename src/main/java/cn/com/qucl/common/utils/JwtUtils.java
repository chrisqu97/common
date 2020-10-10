package cn.com.qucl.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

/**
 * @author qucl
 * @date 2019/9/25 20:07
 */
@Slf4j
public class JwtUtils {
    private final String issuer;
    /**
     * 密钥
     */
    private final String key;
    /**
     * 算法
     */
    private static final String ALGORITHM = "AES";

    public JwtUtils(String issuer, String key) {
        this.key = key;
        this.issuer = issuer;
    }

    /**
     * 签发JWT
     *
     * @param userId    id
     * @param content   json数据
     * @param ttlMillis 失效时间
     * @return token
     */
    public String createAuthToken(Integer userId, String content, long ttlMillis) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        SecretKey secretKey = generalKey();
        JwtBuilder builder = Jwts.builder()
                .setId(userId.toString())
                //主要内容
                .setSubject(content)
                //签发人
                .setIssuer(issuer)
                //时间戳
                .setIssuedAt(now)
                //签名算法以及密匙
                .signWith(signatureAlgorithm, secretKey);
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            // 设置过期时间
            builder.setExpiration(new Date(expMillis));
        }
        return builder.compact();
    }


    private SecretKey generalKey() {
        byte[] encodedKey = Base64.decode(key);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, ALGORITHM);
    }

    /**
     * 解析JWT字符串
     *
     * @param jwtStr jwt字符串
     */
    public Claims parseToken(String jwtStr) {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwtStr)
                .getBody();
    }
}