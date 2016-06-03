package org.inferred.freebuilder.processor.util;

import org.inferred.freebuilder.processor.util.feature.Feature;
import org.inferred.freebuilder.processor.util.feature.FeatureType;

/**
 * A Block contains a variables section followed by a body. Variables will only be added on first
 * use.
 */
public class Block implements SourceBuilder, Excerpt {

  private final SourceStringBuilder variables;
  private final SourceStringBuilder body;

  public Block(SourceBuilder parent) {
    variables = parent.subBuilder();
    body = parent.subBuilder();
  }

  /**
   * Declare a variable {@code name}, returning an Excerpt to use it. On first use, the declaration
   * will be added to the variables section of this body.
   */
  public Excerpt variable(final String name, final String declfmt, final Object... declArgs) {
    return new Excerpt() {
      private boolean declared = false;

      @Override
      public void addTo(SourceBuilder source) {
        if (!declared) {
          variables.addLine(declfmt, declArgs);
          declared = true;
        }
        source.add(name);
      }
    };
  }

  @Override
  public Block add(String fmt, Object... args) {
    body.add(fmt, args);
    return this;
  }

  @Override
  public Block addLine(String fmt, Object... args) {
    body.addLine(fmt, args);
    return this;
  }

  @Override
  public Block add(Excerpt excerpt) {
    body.add(excerpt);
    return this;
  }

  @Override
  public SourceStringBuilder subBuilder() {
    return body.subBuilder();
  }

  @Override
  public <T extends Feature<T>> T feature(FeatureType<T> featureType) {
    return body.feature(featureType);
  }

  @Override
  public void addTo(SourceBuilder source) {
    source.add("%s", variables).add("%s", body);
  }
}