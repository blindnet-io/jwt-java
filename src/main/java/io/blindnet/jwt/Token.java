package io.blindnet.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.blindnet.jwt.json.TokenHeader;
import io.blindnet.jwt.json.TokenPayload;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Base64;

public class Token {
    private static final JsonMapper jsonMapper = new JsonMapper();

    private final Type type;

    private final String appId;
    private final Instant expiration;
    private final String userId;

    private String signature;

    public static Token parse(String raw) {
        String[] split = raw.split("\\.");
        if(split.length != 3)
            throw new IllegalArgumentException("Invalid token");

        TokenHeader header;
        TokenPayload payload;

        try {
            header = jsonMapper.readValue(Base64.getDecoder().decode(split[0]), TokenHeader.class);
            payload = jsonMapper.readValue(Base64.getDecoder().decode(split[1]), TokenPayload.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid token", e);
        }

        if(!"EdDSA".equals(header.alg))
            throw new IllegalArgumentException("Invalid token algorithm");

        Token token = new Token(Type.fromJson(header.typ), payload.app, Instant.ofEpochSecond(payload.exp), payload.uid);
        token.setSignature(split[2]);
        return token;
    }

    Token(Type type, String appId, String userId) {
        this(type, appId, Instant.now().plus(15, ChronoUnit.MINUTES), userId);
    }

    Token(Type type, String appId, Instant expiration, String userId) {
        this.type = type;
        this.appId = appId;
        this.expiration = expiration;
        this.userId = userId;
    }

    public Type getType() {
        return type;
    }

    public String getAppId() {
        return appId;
    }

    public Instant getExpiration() {
        return expiration;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiration);
    }

    public String getUserId() {
        return userId;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String data() {
        TokenHeader header = new TokenHeader("EdDSA", type.getJson());
        TokenPayload payload = new TokenPayload(appId, expiration != null ? expiration.getEpochSecond() : null, userId);

        try {
            return Base64.getEncoder().encodeToString(jsonMapper.writeValueAsBytes(header)) + "." +
                    Base64.getEncoder().encodeToString(jsonMapper.writeValueAsBytes(payload));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Token JSON encoding failed", e);
        }
    }

    @Override
    public String toString() {
        if(signature == null)
            throw new IllegalStateException("Token signature is missing");

        return data() + "." + signature;
    }

    public enum Type {
        APPLICATION("app"),
        USER("user"),
        ANONYMOUS("anon");

        private final String json;

        Type(String json) {
            this.json = json;
        }

        public String getJson() {
            return json;
        }

        public static Type fromJson(String json) {
            return Arrays.stream(values())
                    .filter(t -> t.getJson().equals(json))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Invalid token type"));
        }
    }
}
