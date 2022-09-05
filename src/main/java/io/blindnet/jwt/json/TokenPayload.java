package io.blindnet.jwt.json;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenPayload {
    public String app;
    public Long exp;
    public String uid;

    public TokenPayload() {}

    public TokenPayload(String app, Long exp, String uid) {
        this.app = app;
        this.exp = exp;
        this.uid = uid;
    }
}
