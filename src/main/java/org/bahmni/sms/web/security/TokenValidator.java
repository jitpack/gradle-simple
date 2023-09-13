package org.bahmni.sms.web.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
@Service
public class TokenValidator {


    private static final String PUBLIC_KEY_FILE_PATH = "/../tmp/public_key.pem";


    private static Key loadPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String publicKeyString = Files.readString(Paths.get(PUBLIC_KEY_FILE_PATH));

        publicKeyString = publicKeyString
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }



    public static boolean validateToken(String token) {
        try {
            Key publicKey = loadPublicKey();

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return ((String) claims.get("user")).equals("bahmni");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}