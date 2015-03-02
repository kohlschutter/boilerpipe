package com.kohlschutter.boilerpipe.jsoup;

import com.kohlschutter.boilerpipe.document.TextBlock;
import com.kohlschutter.boilerpipe.document.TextDocument;
import com.kohlschutter.boilerpipe.text.Text;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import static com.kohlschutter.boilerpipe.text.Text.isWord;

/**
 *
 */
public class JsoupParser {

    public TextDocument parse( InputStream inputStream, String charsetName , String baseUri ) throws IOException {
        return parse( Jsoup.parse( inputStream, charsetName, baseUri ) );
    }

    public TextDocument parse( URL url, int timeoutMillis ) throws IOException {
        return parse( Jsoup.parse( url, timeoutMillis ) );
    }

    public TextDocument parse( String html ) {
        return parse( Jsoup.parse( html ) );
    }

    /**
     * Parse the given HTML to return a TextDocument.
     *
     * @return
     */
    public TextDocument parse( Document document ) {

        String title = document.head().select( "title" ).text().trim();

        if ( "".equals( title ) ) {
            title = null;
        }

        List<TextBlock> textBlocks = new ArrayList<>();

        Elements textBlockElements = document.body().select( "div,p" );

        for (int offsetBlock = 0; offsetBlock < textBlockElements.size(); offsetBlock++) {
            Element textBlockElement = textBlockElements.get( offsetBlock );
            TextBlock textBlock = createTextBlock( textBlockElement, offsetBlock );
            textBlocks.add( textBlock );
        }

        return new TextDocument( title, textBlocks );

    }

    /**
     * Return a new text block or null if there are no tokens in it.
     */
    private TextBlock createTextBlock( Element textBlockElement, int offsetBlock ) {

        int numWords = 0;
        int numLinkedWords = 0;
        int numWrappedLines = 0;
        int currentLineLength = -1; // don't count the first space
        final int maxLineLength = 80;
        int numTokens = 0;
        int numWordsCurrentLine = 0;

        List<Token> tokens = Tokens.tokenize( textBlockElement );

        // TODO: rewrite this to go through each child element.. then parse it
        // into tokens, but if the element we're on is an anchor, then that
        // should have the specific anchor text.

        for (Token token : tokens) {

            if ( isWord( token.getValue() ) ) {

                numTokens++;
                numWords++;
                numWordsCurrentLine++;

                if ( token.isAnchorText() ) {
                    numLinkedWords++;
                }

                final int tokenLength = token.getValue().length();

                currentLineLength += tokenLength + 1;
                if (currentLineLength > maxLineLength) {
                    numWrappedLines++;
                    currentLineLength = tokenLength;
                    numWordsCurrentLine = 1;
                }

            } else {
                numTokens++;
            }


        }

        if (numTokens == 0) {
            return null;
        }

        int numWordsInWrappedLines;
        if (numWrappedLines == 0) {
            numWordsInWrappedLines = numWords;
            numWrappedLines = 1;
        } else {
            numWordsInWrappedLines = numWords - numWordsCurrentLine;
        }

        BitSet containedTextElements = new BitSet();

        // TODO: now include the Element used for this TextBlock so that I can
        // later (optionally) use it to convert everything to HTML.

        TextBlock tb = new TextBlock( textBlockElement.text(),
                                      containedTextElements,
                                      numWords,
                                      numLinkedWords,
                                      numWordsInWrappedLines,
                                      numWrappedLines,
                                      offsetBlock );

        return tb;

    }

}
