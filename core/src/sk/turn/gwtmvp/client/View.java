package sk.turn.gwtmvp.client;

import com.google.gwt.dom.client.Element;

public interface View<E extends Element> {
  E getRootElement();
}
