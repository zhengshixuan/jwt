package com.tianjian.shiro.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tianjian.common.utils.SecurityConsts;

import java.util.Date;

public class JwtUtil {
    /**
     * 30分钟
     * JWT验证过期时间 EXPIRE_TIME 分钟
     */
    private static final long EXPIRE_TIME = 2 * 60 * 1000;

    /**
     * 校验token是否正确
     *
     * @param token  密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static boolean verify(String token, String username, String secret) {
        try {
            String currentTimeMillis = JwtUtil.getClaim(token,"currentTimeMillis");
            //根据密码生成JWT效验器
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", username)
                    .withClaim(SecurityConsts.CURRENT_TIME_MILLIS,currentTimeMillis)
                    .build();
            //效验TOKEN
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @param token 原始token
     * @param param 需获取的参数
     * @return
     */
    public static String getClaim(String token, String param) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(param).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 生成token签名EXPIRE_TIME 分钟后过期
     * @param username 用户名
     * @param secret 密码
     * @param currentTimeMillis 登录的时间
     * @return 加密后的token
     */
    public static String sign(String username, String secret,String currentTimeMillis) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        // 附带username信息
        return JWT.create()
                .withClaim("username", username)
                .withClaim(SecurityConsts.CURRENT_TIME_MILLIS,currentTimeMillis)
                .withExpiresAt(date)
                .sign(algorithm);

    }

}
