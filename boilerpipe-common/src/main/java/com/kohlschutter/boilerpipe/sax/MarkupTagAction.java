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
package com.kohlschutter.boilerpipe.sax;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.kohlschutter.boilerpipe.document.TextBlock;
import com.kohlschutter.boilerpipe.labels.DefaultLabels;
import com.kohlschutter.boilerpipe.labels.LabelAction;

/**
 * Assigns labels for element CSS classes and ids to the corresponding {@link TextBlock}. CSS
 * classes are prefixed by <code>{@link DefaultLabels#MARKUP_PREFIX}.</code>, and IDs are prefixed
 * by <code>{@link DefaultLabels#MARKUP_PREFIX}#</code>
 */
public final class MarkupTagAction implements TagAction {

  private final boolean isBlockLevel;
  private LinkedList<List<String>> labelStack = new LinkedList<List<String>>();

  public MarkupTagAction(final boolean isBlockLevel) {
    this.isBlockLevel = isBlockLevel;
  }

  private static final Pattern PAT_NUM = Pattern.compile("[0-9]+");

  @Override
  public boolean start(BoilerpipeHTMLContentHandler instance, String localName, String qName,
      Attributes atts) throws SAXException {
    List<String> labels = new ArrayList<String>(5);
    labels.add(DefaultLabels.MARKUP_PREFIX + localName);

    String classVal = atts.getValue("class");

    if (classVal != null && classVal.length() > 0) {
      classVal = PAT_NUM.matcher(classVal).replaceAll("#");
      classVal = classVal.trim();
      String[] vals = classVal.split("[ ]+");
      labels.add(DefaultLabels.MARKUP_PREFIX + "." + classVal.replace(' ', '.'));
      if (vals.length > 1) {
        for (String s : vals) {
          labels.add(DefaultLabels.MARKUP_PREFIX + "." + s);
        }
      }
    }

    String id = atts.getValue("id");
    if (id != null && id.length() > 0) {
      id = PAT_NUM.matcher(id).replaceAll("#");
      labels.add(DefaultLabels.MARKUP_PREFIX + "#" + id);
    }

    Set<String> ancestors = getAncestorLabels();
    List<String> labelsWithAncestors =
        new ArrayList<String>((ancestors.size() + 1) * labels.size());

    for (String l : labels) {
      for (String an : ancestors) {
        labelsWithAncestors.add(an);
        labelsWithAncestors.add(an + " " + l);
      }
      labelsWithAncestors.add(l);
    }

    instance.addLabelAction(new LabelAction(labelsWithAncestors
        .toArray(new String[labelsWithAncestors.size()])));

    labelStack.add(labels);

    return isBlockLevel;
  }

  @Override
  public boolean end(BoilerpipeHTMLContentHandler instance, String localName, String qName)
      throws SAXException {

    labelStack.removeLast();
    return isBlockLevel;
  }

  public boolean changesTagLevel() {
    return isBlockLevel;
  }

  private Set<String> getAncestorLabels() {
    Set<String> set = new HashSet<String>();
    for (List<String> labels : labelStack) {
      if (labels == null) {
        continue;
      }
      set.addAll(labels);
    }
    return set;
  }
}
