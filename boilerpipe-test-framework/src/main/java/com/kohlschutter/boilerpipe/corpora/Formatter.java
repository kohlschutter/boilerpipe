package com.kohlschutter.boilerpipe.corpora;

import com.google.common.base.Joiner;

import java.util.Collection;

/**
 *
 */
public class Formatter {

    public static String table( Collection<?> values ) {
        return Joiner.on("\n" ).join( values );
    }

}
