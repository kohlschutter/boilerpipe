/**
 * boilerpipe
 *
 * Copyright (c) 2009 Christian Kohlschütter
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
package de.l3s.boilerpipe.document;

import java.util.List;

/**
 * A text document, consisting of one or more {@link TextBlock}s.
 * 
 * @author Christian Kohlschütter
 */
public class TextDocument {
    final List<TextBlock> textBlocks;
    final String title;

    /**
     * Creates a new {@link TextDocument} with given {@link TextBlock}s, and no
     * title.
     * 
     * @param textBlocks
     *            The text blocks of this document.
     */
    public TextDocument(final List<TextBlock> textBlocks) {
        this(null, textBlocks);
    }

    /**
     * Creates a new {@link TextDocument} with given {@link TextBlock}s and
     * given title.
     * 
     * @param title
     *            The "main" title for this text document.
     * @param textBlocks
     *            The text blocks of this document.
     */
    public TextDocument(final String title, final List<TextBlock> textBlocks) {
        this.title = title;
        this.textBlocks = textBlocks;
    }

    /**
     * Returns the {@link TextBlock}s of this document.
     * 
     * @return A list of {@link TextBlock}s, in sequential order of appearance.
     */
    public List<TextBlock> getTextBlocks() {
        return textBlocks;
    }

    /**
     * Returns the "main" title for this document, or <code>null</code> if no
     * such title has ben set.
     * 
     * @return The "main" title.
     */
    public String getTitle() {
        return title;
    }
}
