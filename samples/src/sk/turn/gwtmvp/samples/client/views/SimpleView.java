package sk.turn.gwtmvp.samples.client.views;

import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ClickHandler;

import sk.turn.gwtmvp.client.HtmlElement;
import sk.turn.gwtmvp.client.HtmlHandler;
import sk.turn.gwtmvp.client.View;

public interface SimpleView extends View<DivElement> {
  @HtmlElement AnchorElement getLink();
  @HtmlElement DivElement getDiv();
  @HtmlHandler({ "link", "div" }) void setClickHandler(ClickHandler handler);
}
