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

public class TokenPublicKey {
    private final Ed25519PublicKeyParameters key;

    public static TokenPublicKey fromString(String key) throws IllegalArgumentException {
        return new TokenPublicKey(new Ed25519PublicKeyParameters(Base64.getDecoder().decode(key)));
    }

    public TokenPublicKey(Ed25519PublicKeyParameters key) {
        this.key = key;
    }

    public boolean verify(byte[] data, byte[] signature) {
        Ed25519Signer signer = new Ed25519Signer();
        signer.init(false, key);
        signer.update(data, 0, data.length);
        return signer.verifySignature(signature);
    }

    public boolean verify(Token token) {
        return verify(token.data().getBytes(StandardCharsets.UTF_8), Base64.getDecoder().decode(token.getSignature()));
    }

    @Override
    public String toString() {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}
