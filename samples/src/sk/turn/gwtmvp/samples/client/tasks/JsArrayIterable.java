package sk.turn.gwtmvp.samples.client.tasks;

import java.util.NoSuchElementException;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class JsArrayIterable<T extends JavaScriptObject> extends JsArray<T> implements Iterable<T> {

  private class Iterator implements java.util.Iterator<T> {
    private int index = -1;
    @Override
    public boolean hasNext() {
      return (index + 1 < length());
    }
    @Override
    public T next() {
      index++;
      if (index >= length()) {
        throw new NoSuchElementException();
      }
      return get(index);
    }
    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  protected JsArrayIterable() {
  }

  @Override
  public final Iterator iterator() {
    return new Iterator();
  }

}
