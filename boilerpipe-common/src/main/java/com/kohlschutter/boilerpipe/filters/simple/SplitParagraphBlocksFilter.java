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
package com.kohlschutter.boilerpipe.filters.simple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.kohlschutter.boilerpipe.BoilerpipeFilter;
import com.kohlschutter.boilerpipe.BoilerpipeProcessingException;
import com.kohlschutter.boilerpipe.document.TextBlock;
import com.kohlschutter.boilerpipe.document.TextDocument;

/**
 * Splits TextBlocks at paragraph boundaries.
 * 
 * NOTE: This is not fully supported (i.e., it will break highlighting support via
 * #getContainedTextElements()), but this one probably is necessary for some other filters.
 * 
 * @see MinClauseWordsFilter
 */
public final class SplitParagraphBlocksFilter implements BoilerpipeFilter {
  public static final SplitParagraphBlocksFilter INSTANCE = new SplitParagraphBlocksFilter();

  /**
   * Returns the singleton instance for TerminatingBlocksFinder.
   */
  public static SplitParagraphBlocksFilter getInstance() {
    return INSTANCE;
  }

  public boolean process(TextDocument doc) throws BoilerpipeProcessingException {
    boolean changes = false;

    final List<TextBlock> blocks = doc.getTextBlocks();
    final List<TextBlock> blocksNew = new ArrayList<TextBlock>();

    for (TextBlock tb : blocks) {
      final String text = tb.getText();
      final String[] paragraphs = text.split("[\n\r]+");
      if (paragraphs.length < 2) {
        blocksNew.add(tb);
        continue;
      }
      final boolean isContent = tb.isContent();
      final Set<String> labels = tb.getLabels();
      for (String p : paragraphs) {
        final TextBlock tbP = new TextBlock(p);
        tbP.setIsContent(isContent);
        tbP.addLabels(labels);
        blocksNew.add(tbP);
        changes = true;
      }
    }

    if (changes) {
      blocks.clear();
      blocks.addAll(blocksNew);
    }

    return changes;
  }

}
