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
import java.util.UUID;

/**
 * A Blindnet JWT.<br>
 * <br>
 * Use {@link TokenBuilder} for creating tokens, and {@link Token#parse(String)} for parsing existing ones.
 */
public class Token {
    private static final JsonMapper jsonMapper = new JsonMapper();

    private final Type type;

    private final UUID appId;
    private final Instant expiration;
    private final String userId;

    private String data;
    private String signature;

    /**
     * Parses a Token from its string representation.
     * @param raw the string representation
     * @return the parsed Token
     * @throws IllegalArgumentException if the token could not be parsed
     */
    public static Token parse(String raw) throws IllegalArgumentException {
        String[] split = raw.split("\\.");
        if(split.length != 3)
            throw new IllegalArgumentException("Invalid token");

        TokenHeader header;
        TokenPayload payload;

        try {
            header = jsonMapper.readValue(Base64.getUrlDecoder().decode(split[0]), TokenHeader.class);
            payload = jsonMapper.readValue(Base64.getUrlDecoder().decode(split[1]), TokenPayload.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid token", e);
        }

        if(!"EdDSA".equals(header.alg))
            throw new IllegalArgumentException("Invalid token algorithm");

        Token token = new Token(Type.fromJson(header.typ), payload.app, Instant.ofEpochSecond(payload.exp), payload.uid);
        token.setData(split[0] + "." + split[1]);
        token.setSignature(split[2]);
        return token;
    }

    Token(Type type, UUID appId, String userId) {
        this(type, appId, Instant.now().plus(15, ChronoUnit.MINUTES), userId);
    }

    Token(Type type, UUID appId, Instant expiration, String userId) {
        this.type = type;
        this.appId = appId;
        this.expiration = expiration;
        this.userId = userId;
    }

    public Type getType() {
        return type;
    }

    public UUID getAppId() {
        return appId;
    }

    public Instant getExpiration() {
        return expiration;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiration);
    }

    /**
     * Returns the user ID contained in this Token, if any.<br>
     * <b>Only {@link Type#USER} tokens contain a user ID.</b> Use {@link Token#getType()} to check the token type
     * before calling this method.
     * @return the user ID in this Token, or null if this is not a user token
     */
    public String getUserId() {
        return userId;
    }

    private void setData(String data) {
        this.data = data;
    }

    /**
     * Returns the signature of this token, if any.<br>
     * Parsed tokens (using {@link Token#parse(String)}) and tokens created with a {@link TokenBuilder}
     * will always contain a signature.
     * @return the signature of this token, or null if it is not signed
     */
    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    /**
     * Returns the string representation of the data part of this token.<br>
     * This is the part of the token which must be signed. The signature is then
     * appended to the output of this method to make the output of {@link Token#toString()},
     * which is the actual string representation to be used in requests.
     * @return the string representation of the data part of this token
     */
    public String data() {
        if(data == null) {
            TokenHeader header = new TokenHeader("EdDSA", type.getJson());
            TokenPayload payload = new TokenPayload(appId, expiration != null ? expiration.getEpochSecond() : null, userId);

            try {
                data = Base64.getUrlEncoder().withoutPadding().encodeToString(jsonMapper.writeValueAsBytes(header)) + "." +
                        Base64.getUrlEncoder().withoutPadding().encodeToString(jsonMapper.writeValueAsBytes(payload));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Token JSON encoding failed", e);
            }
        }

        return data;
    }

    /**
     * Returns the string representation of this token, to be used in HTTP requests against devkit APIs,
     * by adding an Authorization header of the following format: Bearer [token].<br>
     * <br>
     * <b>A signature must be set before calling this method</b>, either by directly calling
     * {@link Token#setSignature(String)} with the string signature, or, more commonly, by calling
     * {@link TokenPrivateKey#sign(Token)}.
     * @return the string representation of this token
     * @throws IllegalStateException if this token does not have a signature
     */
    @Override
    public String toString() throws IllegalStateException {
        if(signature == null)
            throw new IllegalStateException("Token signature is missing");

        return data() + "." + signature;
    }

    /**
     * JWT types.
     */
    public enum Type {
        /**
         * Application-scoped JWTs authenticate either the application itself or users
         * with admin rights. Application JWTs do not carry any user information.
         */
        APPLICATION("app"),

        /**
         * User-scoped JWTs authenticate users of the application, by carrying a user ID.
         */
        USER("user"),

        /**
         * Anonymous JWTs authenticate users, but do not carry a user ID.
         */
        ANONYMOUS("anon");

        private final String json;

        Type(String json) {
            this.json = json;
        }

        private String getJson() {
            return json;
        }

        private static Type fromJson(String json) throws IllegalArgumentException {
            return Arrays.stream(values())
                    .filter(t -> t.getJson().equals(json))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Invalid token type"));
        }
    }
}
