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
package com.kohlschutter.boilerpipe;

import java.io.Reader;

import org.xml.sax.InputSource;

import com.kohlschutter.boilerpipe.document.TextDocument;

/**
 * Describes a complete filter pipeline.
 */
public interface BoilerpipeExtractor extends BoilerpipeFilter {
  /**
   * Extracts text from the HTML code given as a String.
   * 
   * @param html The HTML code as a String.
   * @return The extracted text.
   * @throws BoilerpipeProcessingException
   */
  public String getText(final String html) throws BoilerpipeProcessingException;

  /**
   * Extracts text from the HTML code available from the given {@link InputSource}.
   * 
   * @param is The InputSource containing the HTML
   * @return The extracted text.
   * @throws BoilerpipeProcessingException
   */
  public String getText(final InputSource is) throws BoilerpipeProcessingException;

  /**
   * Extracts text from the HTML code available from the given {@link Reader}.
   * 
   * @param r The Reader containing the HTML
   * @return The extracted text.
   * @throws BoilerpipeProcessingException
   */
  public String getText(final Reader r) throws BoilerpipeProcessingException;

  /**
   * Extracts text from the given {@link TextDocument} object.
   * 
   * @param doc The {@link TextDocument}.
   * @return The extracted text.
   * @throws BoilerpipeProcessingException
   */
  public String getText(TextDocument doc) throws BoilerpipeProcessingException;
}
