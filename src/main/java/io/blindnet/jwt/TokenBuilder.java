package io.blindnet.jwt;

import java.util.UUID;

/**
 * Used for creating signed {@link Token}s.<br>
 * <br>
 * Tokens created using this builder will expire 15 minutes after their creation time.
 */
public class TokenBuilder {
    private final UUID appId;
    private final TokenPrivateKey key;

    /**
     * Creates a builder
     * @param appId application ID
     * @param key application private key
     * @throws IllegalArgumentException if appId is not a UUID
     */
    public TokenBuilder(String appId, TokenPrivateKey key) throws IllegalArgumentException {
        this(UUID.fromString(appId), key);
    }

    /**
     * Creates a builder.
     * @param appId application ID
     * @param key application private key
     */
    public TokenBuilder(UUID appId, TokenPrivateKey key) {
        this.appId = appId;
        this.key = key;
    }

    /**
     * Creates an {@link Token.Type#APPLICATION} token.
     * @return the created token
     */
    public String app() {
        return key.sign(new Token(Token.Type.APPLICATION, appId, null)).toString();
    }

    /**
     * Creates a {@link Token.Type#USER} token.
     * @param userId the user ID
     * @return the created token
     */
    public String user(String userId) {
        return key.sign(new Token(Token.Type.USER, appId, userId)).toString();
    }

    /**
     * Creates an {@link Token.Type#ANONYMOUS} token.
     * @return the created token
     */
    public String anonymous() {
        return key.sign(new Token(Token.Type.ANONYMOUS, appId, null)).toString();
    }
}
