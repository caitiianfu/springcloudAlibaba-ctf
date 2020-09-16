package com.unicom.oauth2.controller;

import cn.hutool.json.JSONObject;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 *
 * @Description 获取rsa公钥
 * @param null
 * @return
 * @date 2020/9/13
 * @author ctf
 **/

@RestController
public class KeyPairController {
    @Autowired
    private KeyPair keyPair;
    @GetMapping("/rsa/publicKey")
    public Map<String,Object> getKey(){

      RSAPublicKey rsaPublicKey=(RSAPublicKey) keyPair.getPublic();
      RSAKey rsaKey=new RSAKey.Builder(rsaPublicKey).build();
      return new JWKSet(rsaKey).toJSONObject();
    }

}
