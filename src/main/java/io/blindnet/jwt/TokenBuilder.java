package io.blindnet.jwt;

import java.util.UUID;

public class TokenBuilder {
    private final UUID appId;
    private final TokenPrivateKey key;

    public TokenBuilder(String appId, TokenPrivateKey key) {
        this(UUID.fromString(appId), key);
    }

    public TokenBuilder(UUID appId, TokenPrivateKey key) {
        this.appId = appId;
        this.key = key;
    }

    public String app() {
        return key.sign(new Token(Token.Type.APPLICATION, appId, null)).toString();
    }

    public String user(String userId) {
        return key.sign(new Token(Token.Type.USER, appId, userId)).toString();
    }

    public String anonymous() {
        return key.sign(new Token(Token.Type.ANONYMOUS, appId, null)).toString();
    }
}
