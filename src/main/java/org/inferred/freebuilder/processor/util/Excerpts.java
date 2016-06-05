package org.inferred.freebuilder.processor.util;

public class Excerpts {

  private static final class AddingExcerpt implements Excerpt {
    private final Object[] args;
    private final String fmt;

    private AddingExcerpt(Object[] args, String fmt) {
      this.args = args;
      this.fmt = fmt;
    }

    @Override
    public void addTo(SourceBuilder source) {
      source.add(fmt, args);
    }
  }

  public static Excerpt add(final String fmt, final Object... args) {
    return new AddingExcerpt(args, fmt);
  }

  private static final class EmptyExcerpt implements Excerpt {
    @Override
    public void addTo(SourceBuilder source) {}
  }

  private static final Excerpt EMPTY = new EmptyExcerpt();

  public static Excerpt empty() {
    return EMPTY;
  }

  private static final class JoiningExcerpt implements Excerpt {
    private final String separator;
    private final Iterable<?> excerpts;

    private JoiningExcerpt(String separator, Iterable<?> excerpts) {
      this.separator = separator;
      this.excerpts = excerpts;
    }

    @Override
    public void addTo(SourceBuilder source) {
      String itemPrefix = "";
      for (Object object : excerpts) {
        source.add("%s%s", itemPrefix, object);
        itemPrefix = separator;
      }
    }
  }

  public static Object join(final String separator, final Iterable<?> excerpts) {
    return new JoiningExcerpt(separator, excerpts);
  }

  private Excerpts () {}
}
