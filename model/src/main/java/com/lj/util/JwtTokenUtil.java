package com.lj.util;

import io.jsonwebtoken.*;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 *      JWT > Json web token
 *          是为了在网络应用环境间传递声明而执行的一种基于JSON的开放标准（(RFC 7519).
 *          该token被设计为紧凑且安全的，特别适用于分布式站点的单点登录（SSO）场景。
 *          JWT的声明一般被用来在身份提供者和服务提供者间传递被认证的用户身份信息，
 *          以便于从资源服务器获取资源，也可以增加一些额外的其它业务逻辑所必须的声明信息，
 *          该token也可直接被用于认证，也可被加密。
 *
 *          基于token的鉴权机制类似于http协议也是无状态的，它不需要在服务端去保留用户的认
 *          证信息或者会话信息。这就意味着基于token认证机制的应用不需要去考虑用户在哪一台服
 *          务器登录了，这就为应用的扩展提供了便利。
 *
 *      流程上是这样的：
 *
 *          用户使用用户名密码来请求服务器
 *          服务器进行验证用户的信息
 *          服务器通过验证发送给用户一个token
 *          客户端存储token，并在每次请求时附送上这个token值
 *          服务端验证token值，并返回数据
 */
public class JwtTokenUtil {
    //token字符串有效时间
    private static long tokenExpiration = 24 * 60 * 60 * 1000; //1天
    //token字符串刷新续约时间
    private static long tokenRefreshTime = 7 * 24 * 60 * 60 * 1000; //7天
    //加密编码秘钥
    private static String tokenSignKey = "123456";

    //根据userid  和  username 生成token字符串
    public static String createToken(Long userId, String userName,long tokenExpiration) {
        String token = Jwts.builder()
                //设置token分类
                .setSubject("XPZ-USER")
                //token字符串有效时长
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                //私有部分（用户信息）
                .claim("userId", userId)
                .claim("role", userName)
                //根据秘钥使用加密编码方式进行加密，对字符串压缩
                .signWith(SignatureAlgorithm.HS256, tokenSignKey)
                .compressWith(CompressionCodecs.GZIP)
                .compact();
        return token;
    }

    //返回token的过期时间
    private static Date getTokenExpiration(String token){
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        return claims.getExpiration();
    }

    //判断token是否快要过期
    public static boolean isExpiringSoon(String token){
        Date expirationTime = getTokenExpiration(token);
        //过期前1小时
        Date refreshTime = new Date(expirationTime.getTime() - 60 * 60 * 1000);
        return new Date(System.currentTimeMillis()).after(refreshTime);
    }

    public static String createToken(Long userId, String role) {
        return createToken(userId,role,tokenExpiration);
    }

    //刷新token有效期
    public static String refreshToken(String token){
        Long userId = getUserId(token);
        String role = getUserRole(token);
        return createToken(userId,role,tokenRefreshTime);
    }

    //从token字符串获取userid
    public static Long getUserId(String token) {
        if(StringUtils.isEmpty(token)) return null;
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        Integer userId = (Integer)claims.get("userId");
        return userId.longValue();
    }

    //从token字符串获取role
    public static String getUserRole(String token) {
        if(StringUtils.isEmpty(token)) return "";
        Jws<Claims> claimsJws
                = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        return (String)claims.get("role");
    }

    public static void main(String[] args) {
        String token = createToken(12L, "user_34408");
        System.out.println("token刷新前:" + getTokenExpiration(token));
        token = refreshToken(token);
        System.out.println("token刷新后:" + getTokenExpiration(token));
    }
}
