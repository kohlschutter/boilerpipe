package com.kohlschutter.boilerpipe.text;

import java.util.regex.Pattern;

/**
 *
 */
public class Text {

    // TODO: probably migrate to ICU4J for this.
    private static final Pattern PAT_VALID_WORD_CHARACTER = Pattern
                                                              .compile("[\\p{L}\\p{Nd}\\p{Nl}\\p{No}]");

    public static boolean isWord(final String token) {
        return PAT_VALID_WORD_CHARACTER.matcher(token).find();
    }

}
