/**
 * boilerpipe
 *
 * Copyright (c) 2009, 2014 Christian Kohlschütter
 *
 * The author licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kohlschutter.boilerpipe.corpora;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 */
public class CorporaCache {

    private static String ROOT = "src/test/resources/";

    private final Class<?> parent;

    public CorporaCache(Class<?> parent) {
        this.parent = parent;
    }

    public boolean contains( String key ) {
        String path = computePath( key );
        File file = new File( ROOT, path );
        return file.exists();
    }

    public void write( String key, String data ) throws IOException {

        String path = computePath( key );

        File file = new File( ROOT, path );

        System.out.printf( "Writing cache data to: %s\n", file.getAbsolutePath() );

        Files.createDirectories( Paths.get( file.getParent() ) );

        try( OutputStream out = new FileOutputStream( file ) ) {

            out.write( data.getBytes( Charsets.UTF_8 ) );

        }

    }

    public String read( String key ) throws IOException {

        String path = computePath( key );

        try (InputStream is = parent.getResourceAsStream( path )) {

            if ( is == null )
                return null;

            byte[] data = ByteStreams.toByteArray( is );

            return new String( data, Charsets.UTF_8 );

        }

    }

    private String computePath( String key ) {
        return String.format( "/corpora/%s.%s.dat", parent.getName(), key );
    }

}


