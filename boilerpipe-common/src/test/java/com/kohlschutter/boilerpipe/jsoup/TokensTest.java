package com.kohlschutter.boilerpipe.jsoup;

import com.kohlschutter.boilerpipe.corpora.Formatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import static com.kohlschutter.boilerpipe.corpora.Formatter.table;
import static org.junit.Assert.*;

public class TokensTest {

    @Test
    public void testBasicFunctionality() throws Exception {

        Document doc = Jsoup.parse( "<body> The <a>world</a> is <b>super</b> cool.</body>" );

        assertEquals( "Token{value='The', anchorText=false}\n" +
                        "Token{value='world', anchorText=true}\n" +
                        "Token{value='is', anchorText=false}\n" +
                        "Token{value='super', anchorText=false}\n" +
                        "Token{value='cool.', anchorText=false}",
                      table( Tokens.tokenize( doc.body() ) ) );

    }

    @Test
    public void testWithEmptyElement() throws Exception {

        Document doc = Jsoup.parse( "<body> The </img></body>" );

        assertEquals( "Token{value='The', anchorText=false}",
                      table( Tokens.tokenize( doc.body() ) ) );

    }

}