/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2009 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * Sonar is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.colorizer;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.sonar.channel.Channel;
import org.sonar.channel.CodeReader;

public class HtmlRenderer extends Renderer {

  private HtmlOptions options = null;

  public HtmlRenderer(HtmlOptions options) {
    this.options = options;
  }

  public HtmlRenderer() {
    this(HtmlOptions.DEFAULT);
  }

  public String render(Reader code, List<? extends Channel<HtmlCodeBuilder>> tokenizers) {
    try {
      List<Channel<HtmlCodeBuilder>> allTokenizers = new ArrayList<Channel<HtmlCodeBuilder>>();
      HtmlCodeBuilder codeBuilder = new HtmlCodeBuilder();
      HtmlDecorator htmlDecorator = new HtmlDecorator(options);

      // optimization
      if (options != null && options.isGenerateTable()) {
        codeBuilder.appendWithoutTransforming(htmlDecorator.getTagBeginOfFile());
        allTokenizers.add(htmlDecorator);
      }
      allTokenizers.addAll(tokenizers);

      new TokenizerDispatcher(allTokenizers).colorize(new CodeReader(code), codeBuilder);
      // optimization
      if (options != null && options.isGenerateTable()) {
        codeBuilder.appendWithoutTransforming(htmlDecorator.getTagEndOfFile());
      }
      return codeBuilder.toString();
    } catch (Exception e) {
      throw new SynhtaxHighlightingException("Can not render code", e);
    }
  }
}