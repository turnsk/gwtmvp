package sk.turn.gwtmvp.client;

import com.google.gwt.dom.client.Element;

public interface HandlerView<E extends Element, H> extends View<E> {
  void setHandler(H handler);
}
