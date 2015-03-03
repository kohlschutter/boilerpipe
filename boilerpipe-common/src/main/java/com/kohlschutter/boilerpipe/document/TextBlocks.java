package com.kohlschutter.boilerpipe.document;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class TextBlocks {

    /**
     *  Get the text from all text blocks.
     */
    public static List<String> text( List<TextBlock> textBlocks ) {

        List<String> result = new ArrayList<>();

        for (TextBlock textBlock : textBlocks) {
            result.add( textBlock.getText() );
        }

        return result;

    }

}
