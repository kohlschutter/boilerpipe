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
package com.kohlschutter.boilerpipe.filters.heuristics;

import java.util.List;
import java.util.ListIterator;

import com.kohlschutter.boilerpipe.BoilerpipeFilter;
import com.kohlschutter.boilerpipe.BoilerpipeProcessingException;
import com.kohlschutter.boilerpipe.document.TextBlock;
import com.kohlschutter.boilerpipe.document.TextDocument;
import com.kohlschutter.boilerpipe.labels.DefaultLabels;

/**
 * Merges two blocks using some heuristics.
 */
public final class ContentFusion implements BoilerpipeFilter {

  public static final ContentFusion INSTANCE = new ContentFusion();

  /**
   * Creates a new {@link ContentFusion} instance.
   * 
   */
  public ContentFusion() {
  }

  public boolean process(TextDocument doc) throws BoilerpipeProcessingException {
    List<TextBlock> textBlocks = doc.getTextBlocks();
    if (textBlocks.size() < 2) {
      return false;
    }

    TextBlock prevBlock = textBlocks.get(0);

    boolean changes = false;
    do {
      changes = false;
      for (ListIterator<TextBlock> it = textBlocks.listIterator(1); it.hasNext();) {
        TextBlock block = it.next();

        if (prevBlock.isContent() && block.getLinkDensity() < 0.56
            && !block.hasLabel(DefaultLabels.STRICTLY_NOT_CONTENT)) {

          prevBlock.mergeNext(block);
          it.remove();
          changes = true;
        } else {
          prevBlock = block;
        }
      }
    } while (changes);

    return true;
  }

}
