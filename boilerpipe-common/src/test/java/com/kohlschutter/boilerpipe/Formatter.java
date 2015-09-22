package com.kohlschutter.boilerpipe;

import java.util.Collection;

/**
 *
 */
public class Formatter {

    public static String table( Collection<? extends Object> collection ) {

        StringBuilder buff = new StringBuilder();

        for (Object value : collection) {
            buff.append( String.format( "%s\n", value.toString() ) );
        }

        return buff.toString();

    }

}
