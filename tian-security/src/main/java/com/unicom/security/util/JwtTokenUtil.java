package com.unicom.security.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * token 生成的工具类
 *JwtToken  格式是 header.payload.signature
 * header的格式是（算法、token的类型）
 * {"alg":"HS512","type":"JWT"}
 * payload的格式是(用户名、创建时间、生成时间)
 * {"sub":"wang","created":1489079981393,"exp":1489684781}
 *signature的生成算法：
 *HMACSHA512(base64UrlEncode(header)+"."+base64UrlEncode(payload),secret)
 * */
public class JwtTokenUtil {
    private  static final Logger log= LoggerFactory.getLogger(JwtTokenUtil.class);
    private  static final String CLAIM_KEY_USERNAME="sub";
    private  static final String CLAIM_KEY_CREATED="created";
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    /**
     * 生成token
     */
    private String generateToken(Map<String,Object> claims){
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generatExpirationDate())
                .signWith(SignatureAlgorithm.HS512,secret)
                .compact();

    }

    private Claims getClaimsFromToken(String token){
        Claims claims=null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            log.info("JWT格式验证失败： {}",token);
        }
        return  claims;
    }

    /**
     * 生成token的过期时间
     * @return
     */
    private Date generatExpirationDate() {
        return new Date(System.currentTimeMillis()+expiration*1000);
    }

    /**
     * 获得登录用户名
     */
    public String getUsernameFromToken(String token){
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        }catch (Exception e){
            username=null;
        }
        return  username;
    }

    /**
     * 验证token是否有效
     */
    public boolean validateToken(String token, UserDetails userDetails){
        String username=getUsernameFromToken(token);
        return username.equals(userDetails.getUsername())&&!isTokenExpire(token);
    }

    /**
     *检验token是否失效
     */
    private boolean isTokenExpire(String token) {
        Date expiredDate=getExpiredDateFromToken(token);
        return expiredDate.before(new Date());
    }

    /**
     *从token获取过期时间
     */
    private Date getExpiredDateFromToken(String token) {
        Claims claims=getClaimsFromToken(token);
        return  claims.getExpiration();
    }
    /**
     *根据用户信息生成token
     */
    public String generateToken(UserDetails userDetails){
        Map<String,Object> claims=new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME,userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED,new Date());
        return  generateToken(claims);
    }

    /**
     * 如果token未过期可刷新
     */
    public String refreshHeadToken(String oldToken){
        if (StrUtil.isEmpty(oldToken)){
            return  null;
        }
        String token=oldToken.substring(tokenHead.length());
        if (StrUtil.isEmpty(token)){
            return  null;
        }
        //token 校验不通过
        Claims claims=getClaimsFromToken(oldToken);
        if (claims==null){
            return  null;
        }
        //如果已经过期，不支持刷新
        if (isTokenExpire(token)){
            return  null;
        }
        //如果token在30分钟前已经刷新则返回原token
        if (tokenRefreshJustBefore(token,30*60)){
            return token;
        }else{
            claims.put(CLAIM_KEY_CREATED,new Date());
            return generateToken(claims);
        }
    }

    /**
     * 判断是否在指定时间内刷新过
     * @param token
     * @param time
     * @return
     */
    private boolean tokenRefreshJustBefore(String token, int time) {
        Claims claims=getClaimsFromToken(token);
        Date created=claims.get(CLAIM_KEY_CREATED,Date.class);
        Date refreshDate=new Date();
        if (refreshDate.after(created)&&refreshDate.before(DateUtil.offsetSecond(created,time))){
            return  true;
        }
        return  false;
    }

}
