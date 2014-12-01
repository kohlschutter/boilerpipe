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
import com.kohlschutter.boilerpipe.filters.english.DensityRulesClassifier;
import com.kohlschutter.boilerpipe.filters.heuristics.BlockProximityFusion;
import com.kohlschutter.boilerpipe.filters.heuristics.SimpleBlockFusionProcessor;

/**
 * A quite generic full-text extractor.
 */
public class DefaultExtractor extends ExtractorBase {
  public static final DefaultExtractor INSTANCE = new DefaultExtractor();

  /**
   * Returns the singleton instance for {@link DefaultExtractor}.
   */
  public static DefaultExtractor getInstance() {
    return INSTANCE;
  }

  public boolean process(TextDocument doc) throws BoilerpipeProcessingException {

    return

    SimpleBlockFusionProcessor.INSTANCE.process(doc)
        | BlockProximityFusion.MAX_DISTANCE_1.process(doc)
        | DensityRulesClassifier.INSTANCE.process(doc);
  }
}
