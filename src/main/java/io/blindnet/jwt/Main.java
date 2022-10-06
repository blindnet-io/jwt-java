package io.blindnet.jwt;

import java.security.NoSuchAlgorithmException;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        System.out.println("Hello world!");

//        TokenPrivateKey key = TokenPrivateKey.generateRandom();
//        System.out.println(key);

        TokenPrivateKey key = TokenPrivateKey.fromString("pX5CBNVs0MRYRm6/eUq0kBCf63Dkv3Dv7+9yhFLO+hk=");

        TokenBuilder builder = new TokenBuilder("a", TokenPrivateKey.fromString(key.toString()));
        System.out.println(builder.app());
        System.out.println(builder.user("123"));
        System.out.println(builder.anonymous());

        Token token = Token.parse(builder.user("testify"));
        System.out.println(key.toPublicKey().verify(token));
    }
}
