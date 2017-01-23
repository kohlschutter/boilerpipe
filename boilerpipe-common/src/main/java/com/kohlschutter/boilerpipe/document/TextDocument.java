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
package com.kohlschutter.boilerpipe.document;

import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * A text document, consisting of one or more {@link TextBlock}s.
 */
public class TextDocument implements Cloneable {
  final List<TextBlock> textBlocks;
  String title;

  /**
   * Creates a new {@link TextDocument} with given {@link TextBlock}s, and no title.
   * 
   * @param textBlocks The text blocks of this document.
   */
  public TextDocument(final List<TextBlock> textBlocks) {
    this(null, textBlocks);
  }

  /**
   * Creates a new {@link TextDocument} with given {@link TextBlock}s and given title.
   * 
   * @param title The "main" title for this text document.
   * @param textBlocks The text blocks of this document.
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
   * Returns the "main" title for this document, or <code>null</code> if no such title has ben set.
   * 
   * @return The "main" title.
   */
  public String getTitle() {
    return title;
  }

  /**
   * Updates the "main" title for this document.
   * 
   * @param title
   */
  public void setTitle(final String title) {
    this.title = title;
  }

  /**
   * Returns the {@link TextDocument}'s content.
   * 
   * @return The content text.
   */
  public String getContent() {
    return getText( true, false );
  }

  public String getContentAsHTML() {
    return getHTML( true, false );
  }

  public Elements getContentAsHTMLElements() {
    return getHTMLElements( true, false );
  }

  /**
   * Returns the {@link TextDocument}'s content, non-content or both
   * 
   * @param includeContent Whether to include TextBlocks marked as "content".
   * @param includeNonContent Whether to include TextBlocks marked as "non-content".
   * @return The text.
   */
  public String getText(boolean includeContent, boolean includeNonContent) {

    StringBuilder sb = new StringBuilder();

    List<TextBlock> textBlocks = filterTextBlocks( includeContent, includeNonContent );

    for (TextBlock textBlock : textBlocks) {
      String text = textBlock.getText();
      sb.append(text);
      sb.append('\n');

    }

    return sb.toString();

  }

  public String getHTML(boolean includeContent, boolean includeNonContent) {

    StringBuilder sb = new StringBuilder();

    List<TextBlock> textBlocks = filterTextBlocks( includeContent, includeNonContent );

    for (TextBlock textBlock : textBlocks) {
      String html = textBlock.getHTML();
      sb.append(html);
      sb.append('\n');

    }

    return sb.toString();

  }

  public Elements getHTMLElements(boolean includeContent, boolean includeNonContent) {

    Elements result = new Elements();

    List<TextBlock> textBlocks = filterTextBlocks( includeContent, includeNonContent );

    for (TextBlock textBlock : textBlocks) {
      result.addAll( textBlock.getElements() );
    }

    return result;

  }



  public List<TextBlock> filterTextBlocks(boolean includeContent, boolean includeNonContent) {

    List<TextBlock> result = new ArrayList<>();

    LOOP : for (TextBlock block : getTextBlocks()) {
      if (block.isContent()) {
        if (!includeContent) {
          continue LOOP;
        }
      } else {
        if (!includeNonContent) {
          continue LOOP;
        }
      }
      result.add( block );
    }

    return result;
  }


  /**
   * Returns detailed debugging information about the contained {@link TextBlock}s.
   * 
   * @return Debug information.
   */
  public String debugString() {
    StringBuilder sb = new StringBuilder();
    for (TextBlock tb : getTextBlocks()) {
      sb.append(tb.toString());
      sb.append('\n');
    }
    return sb.toString();
  }

  public TextDocument clone() {
    final List<TextBlock> list = new ArrayList<>(textBlocks.size());
    for (TextBlock tb : textBlocks) {
      list.add(tb.clone());
    }
    return new TextDocument(title, list);
  }

  @Override
  public String toString() {
    return "TextDocument{" +
             "textBlocks=" + textBlocks +
             ", title='" + title + '\'' +
             '}';
  }

}
