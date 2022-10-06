package io.blindnet.jwt.json;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenPayload {
    public UUID app;
    public Long exp;
    public String uid;

    public TokenPayload() {}

    public TokenPayload(UUID app, Long exp, String uid) {
        this.app = app;
        this.exp = exp;
        this.uid = uid;
    }
}
