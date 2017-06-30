package com.geemeta.core.template.sql;

/**
 * @author itechgee@126.com
 * @date 2017/6/1.
 */
public class JsToken {
    private boolean jsCode;

    private String value;

    private TokenType type;

    public JsToken() {
    }

    public JsToken(boolean jsCode, String value, TokenType type) {
        setJsCode(jsCode);
        setValue(value);
        setType(type);
    }

    public boolean isJsCode() {
        return jsCode;
    }

    public void setJsCode(boolean jsCode) {
        this.jsCode = jsCode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }
}
