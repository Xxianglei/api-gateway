package com.xianglei.common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.xianglei.filter.TokenFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class JwtUtils {
    public static final String SECRET_KEY = "123456"; //秘钥
    public static final long TOKEN_EXPIRE_TIME = 30 * 60 * 1000; //token过期时间 30分钟
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 30 * 60 * 1000; //refreshToken过期时间
    private static final String ISSUER = "issuer"; //签发人

    private static Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    /**
     * 生成签名
     */
    public static String generateToken(String flowId) {
        Date now = new Date();
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY); //算法

        String token = JWT.create()
                .withIssuer(ISSUER) //签发人
                .withIssuedAt(now) //签发时间
                .withExpiresAt(new Date(now.getTime() + TOKEN_EXPIRE_TIME)) //过期时间
                .withClaim("user_flowId", flowId) //保存身份标识
                .sign(algorithm);
        return token;
    }

    /**
     * 验证token
     */
    public static boolean verify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY); //算法
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build();
            verifier.verify(token);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 从token获取flowId
     */
    public static String getFlowId(String token) {
        try {
            return JWT.decode(token).getClaim("user_flowId").asString();
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.info("token->flowId error :{}", ex);
        }
        return "";
    }
}