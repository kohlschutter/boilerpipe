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
package com.kohlschutter.boilerpipe;

import com.kohlschutter.boilerpipe.document.TextDocument;
import com.kohlschutter.boilerpipe.jsoup.JsoupParser;
import com.kohlschutter.boilerpipe.sax.BoilerpipeSAXInput;
import com.kohlschutter.boilerpipe.sax.HTMLDocument;
import com.kohlschutter.boilerpipe.sax.HTMLFetcher;

import java.net.URL;

/**
 *
 */
public class Parsers {

    public static TextDocument parseWithNeko( String path ) throws Exception {

        URL url = Parsers.class.getResource( path );

        final HTMLDocument htmlDoc = HTMLFetcher.fetch( url );

        return new BoilerpipeSAXInput( htmlDoc.toInputSource() ).getTextDocument();

    }

    public static TextDocument parseWithJSoup( String path ) throws Exception {

        JsoupParser jsoupParser = new JsoupParser();

        TextDocument textDocument = jsoupParser.parse( Parsers.class.getResourceAsStream( path ), "UTF-8", "http://example.com" );

        return textDocument;

    }
}
