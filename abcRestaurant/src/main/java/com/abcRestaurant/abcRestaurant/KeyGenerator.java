package com.abcRestaurant.abcRestaurant;

import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;

public class KeyGenerator {

    public static void main(String[] args) {
        // Generate a new secret key
        Key key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);

        // Print the key in Base64 format
        System.out.println("Secret Key: " + Base64.getEncoder().encodeToString(key.getEncoded()));
    }
}
