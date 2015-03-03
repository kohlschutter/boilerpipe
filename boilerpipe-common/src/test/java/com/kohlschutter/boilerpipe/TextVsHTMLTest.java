package com.kohlschutter.boilerpipe;

import com.kohlschutter.boilerpipe.document.TextDocument;
import com.kohlschutter.boilerpipe.extractors.ArticleExtractor;
import org.junit.Test;

/**
 *
 */
public class TextVsHTMLTest {

    @Test
    public void test1() throws Exception {

        ArticleExtractor articleExtractor = ArticleExtractor.getInstance();


        TextDocument td;
        td = Parsers.parseWithJSoup( "/test3.html" );
        String text = articleExtractor.getText( td );

        td = Parsers.parseWithJSoup( "/test3.html" );
        String html = articleExtractor.getHTML( td );

        System.out.printf( "text: \n%s\n", text );
        System.out.printf( "html: \n%s\n", html );

    }

}
