package io.blindnet.jwt;

import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator;
import org.bouncycastle.crypto.params.Ed25519KeyGenerationParameters;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.crypto.signers.Ed25519Signer;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * An Ed25519 public key that can be used to verify {@link Token} signatures.<br>
 * <br>
 * Use {@link TokenPublicKey#fromString(String)} to get an instance from an
 * existing key in its string form.<br>
 * Use {@link TokenPrivateKey#toPublicKey()} to get an instance from a private key.
 */
public class TokenPublicKey {
    private final Ed25519PublicKeyParameters key;

    /**
     * Parses an Ed25519 public key from its string form (Base64 encoded).
     * @param key the key to parse, in its string form
     * @return the parsed key
     * @throws IllegalArgumentException if the key is invalid
     */
    public static TokenPublicKey fromString(String key) throws IllegalArgumentException {
        return new TokenPublicKey(new Ed25519PublicKeyParameters(Base64.getDecoder().decode(key)));
    }
    /**
     * Creates a public key instance from an existing Bouncy Castle Ed25519 public key.<br>
     * <b>This is usually not what you're looking for. Use {@link TokenPublicKey#fromString(String)} instead.</b>
     * @param key the Bouncy Castle Ed25519 public key
     */
    public TokenPublicKey(Ed25519PublicKeyParameters key) {
        this.key = key;
    }

    /**
     * Verifies the signature of some data.
     * @param data data whose signature will be verified
     * @param signature signature to verify
     * @return whether the signature is valid
     */
    public boolean verify(byte[] data, byte[] signature) {
        Ed25519Signer signer = new Ed25519Signer();
        signer.init(false, key);
        signer.update(data, 0, data.length);
        return signer.verifySignature(signature);
    }

    /**
     * Verifies the signature of a token.
     * @param token the token to verify
     * @return whether the token signature is valid
     */
    public boolean verify(Token token) {
        return verify(token.data().getBytes(StandardCharsets.UTF_8), Base64.getDecoder().decode(token.getSignature()));
    }

    /**
     * @return the base64 string representation of this key.
     */
    @Override
    public String toString() {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}
