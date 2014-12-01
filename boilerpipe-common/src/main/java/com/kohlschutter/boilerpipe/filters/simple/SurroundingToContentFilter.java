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

import java.util.Iterator;
import java.util.List;

import com.kohlschutter.boilerpipe.BoilerpipeFilter;
import com.kohlschutter.boilerpipe.BoilerpipeProcessingException;
import com.kohlschutter.boilerpipe.conditions.TextBlockCondition;
import com.kohlschutter.boilerpipe.document.TextBlock;
import com.kohlschutter.boilerpipe.document.TextDocument;

/**
 * Marks blocks as "content" if their preceding and following blocks are both already marked
 * "content", and the given {@link TextBlockCondition} is met.
 */
public class SurroundingToContentFilter implements BoilerpipeFilter {
  public static final SurroundingToContentFilter INSTANCE_TEXT = new SurroundingToContentFilter(
      new TextBlockCondition() {

        @Override
        public boolean meetsCondition(TextBlock tb) {
          return tb.getLinkDensity() == 0 && tb.getNumWords() > 6;
        }
      });

  private final TextBlockCondition cond;

  public SurroundingToContentFilter(final TextBlockCondition cond) {
    this.cond = cond;
  }

  public boolean process(TextDocument doc) throws BoilerpipeProcessingException {

    List<TextBlock> tbs = doc.getTextBlocks();
    if (tbs.size() < 3) {
      return false;
    }

    TextBlock a = tbs.get(0);
    TextBlock b = tbs.get(1);
    TextBlock c;
    boolean hasChanges = false;
    for (Iterator<TextBlock> it = tbs.listIterator(2); it.hasNext();) {
      c = it.next();
      if (!b.isContent() && a.isContent() && c.isContent() && cond.meetsCondition(b)) {
        b.setIsContent(true);
        hasChanges = true;
      }

      a = c;
      if (!it.hasNext()) {
        break;
      }
      b = it.next();
    }

    return hasChanges;
  }

}
