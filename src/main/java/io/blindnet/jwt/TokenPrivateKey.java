package io.blindnet.jwt;

import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator;
import org.bouncycastle.crypto.params.Ed25519KeyGenerationParameters;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.signers.Ed25519Signer;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class TokenPrivateKey {
    private final Ed25519PrivateKeyParameters key;

    public static TokenPrivateKey generateRandom() {
        Ed25519KeyPairGenerator generator = new Ed25519KeyPairGenerator();
        generator.init(new Ed25519KeyGenerationParameters(new SecureRandom()));
        return new TokenPrivateKey((Ed25519PrivateKeyParameters) generator.generateKeyPair().getPrivate());
    }

    public static TokenPrivateKey fromString(String key) throws IllegalArgumentException {
        return new TokenPrivateKey(new Ed25519PrivateKeyParameters(Base64.getDecoder().decode(key)));
    }

    public TokenPrivateKey(Ed25519PrivateKeyParameters key) {
        this.key = key;
    }

    public byte[] sign(byte[] data) {
        Ed25519Signer signer = new Ed25519Signer();
        signer.init(true, key);
        signer.update(data, 0, data.length);
        return signer.generateSignature();
    }

    public Token sign(Token token) {
        token.setSignature(Base64.getEncoder().encodeToString(sign(token.data().getBytes(StandardCharsets.UTF_8))));
        return token;
    }

    public TokenPublicKey toPublicKey() {
        return new TokenPublicKey(key.generatePublicKey());
    }

    @Override
    public String toString() {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}
