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
package com.kohlschutter.boilerpipe.extractors;

import com.kohlschutter.boilerpipe.BoilerpipeProcessingException;
import com.kohlschutter.boilerpipe.document.TextDocument;
import com.kohlschutter.boilerpipe.filters.simple.MarkEverythingContentFilter;

/**
 * Marks everything as content.
 */
public final class KeepEverythingExtractor extends ExtractorBase {

  public static final KeepEverythingExtractor INSTANCE = new KeepEverythingExtractor();

  private KeepEverythingExtractor() {

  }

  public boolean process(TextDocument doc) throws BoilerpipeProcessingException {
    return MarkEverythingContentFilter.INSTANCE.process(doc);
  }

}
