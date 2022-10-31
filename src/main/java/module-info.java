module io.blindnet.jwt {
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires org.bouncycastle.provider;

    exports io.blindnet.jwt;
    exports io.blindnet.jwt.json to com.fasterxml.jackson.databind;
}
