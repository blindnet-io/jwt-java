package io.blindnet.jwt.json;

public class TokenHeader {
    public String alg;
    public String typ;

    public TokenHeader() {}

    public TokenHeader(String alg, String typ) {
        this.alg = alg;
        this.typ = typ;
    }
}
