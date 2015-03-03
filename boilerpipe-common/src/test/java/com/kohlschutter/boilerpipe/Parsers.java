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

        return new BoilerpipeSAXInput(htmlDoc.toInputSource())
                 .getTextDocument();

    }

    public static TextDocument parseWithJSoup( String path ) throws Exception {

        //JsoupParser jsoupParser = new JsoupParser();
        JsoupParser jsoupParser = new JsoupParser();

        TextDocument textDocument = jsoupParser.parse( Parsers.class.getResourceAsStream( path ), "UTF-8", "http://example.com" );

        return textDocument;

    }
}
