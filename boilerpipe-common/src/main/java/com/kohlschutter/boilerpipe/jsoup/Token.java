package com.kohlschutter.boilerpipe.jsoup;

/**
 *
 */
public class Token {

    private String value;

    private boolean anchorText;

    public Token(String value, boolean anchorText) {
        this.value = value;
        this.anchorText = anchorText;
    }

    public String getValue() {
        return value;
    }

    public boolean isAnchorText() {
        return anchorText;
    }

    @Override
    public String toString() {
        return "Token{" +
                 "value='" + value + '\'' +
                 ", anchorText=" + anchorText +
                 '}';
    }

}
