package io.blindnet.jwt;

import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator;
import org.bouncycastle.crypto.params.Ed25519KeyGenerationParameters;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.signers.Ed25519Signer;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * An Ed25519 private key that can be used to sign {@link Token}s.<br>
 * <br>
 * Use {@link TokenPrivateKey#fromString(String)} to get an instance from an
 * existing key in its string form.
 */
public class TokenPrivateKey {
    private final Ed25519PrivateKeyParameters key;

    /**
     * Generates a new random private key.<br>
     * This is usually not useful in a production application, but can be used
     * for creating an application key pair during initial setup.<br>
     * The public key can be retrieved using {@link TokenPrivateKey#toPublicKey()}.
     * @return a random private key
     */
    public static TokenPrivateKey generateRandom() {
        Ed25519KeyPairGenerator generator = new Ed25519KeyPairGenerator();
        generator.init(new Ed25519KeyGenerationParameters(new SecureRandom()));
        return new TokenPrivateKey((Ed25519PrivateKeyParameters) generator.generateKeyPair().getPrivate());
    }

    /**
     * Parses an Ed25519 private key from its string form (Base64 encoded).
     * @param key the key to parse, in its string form
     * @return the parsed key
     * @throws IllegalArgumentException if the key is invalid
     */
    public static TokenPrivateKey fromString(String key) throws IllegalArgumentException {
        return new TokenPrivateKey(new Ed25519PrivateKeyParameters(Base64.getDecoder().decode(key)));
    }

    /**
     * Creates a private key instance from an existing Bouncy Castle Ed25519 private key.<br>
     * <b>This is usually not what you're looking for. Use {@link TokenPrivateKey#fromString(String)} instead.</b>
     * @param key the Bouncy Castle Ed25519 private key
     */
    public TokenPrivateKey(Ed25519PrivateKeyParameters key) {
        this.key = key;
    }

    /**
     * Signs data contained in a byte array and returns the signature in a 64-byte array.
     * @param data data to be signed
     * @return the signature in a 64-byte array
     */
    public byte[] sign(byte[] data) {
        Ed25519Signer signer = new Ed25519Signer();
        signer.init(true, key);
        signer.update(data, 0, data.length);
        return signer.generateSignature();
    }

    /**
     * Signs a token.
     * @param token the token to be signed
     * @return the signed token, for chaining
     */
    public Token sign(Token token) {
        token.setSignature(Base64.getUrlEncoder().withoutPadding().encodeToString(sign(token.data().getBytes(StandardCharsets.UTF_8))));
        return token;
    }

    public TokenPublicKey toPublicKey() {
        return new TokenPublicKey(key.generatePublicKey());
    }

    /**
     * @return the base64 string representation of this key.
     */
    @Override
    public String toString() {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}
