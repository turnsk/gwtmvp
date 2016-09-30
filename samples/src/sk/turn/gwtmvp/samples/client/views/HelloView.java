package sk.turn.gwtmvp.samples.client.views;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickHandler;

import sk.turn.gwtmvp.client.HtmlElement;
import sk.turn.gwtmvp.client.HtmlHandler;
import sk.turn.gwtmvp.client.View;

public interface HelloView extends View<DivElement> {
  @HtmlElement InputElement getNameInput();
  @HtmlElement SpanElement getCounter();
  @HtmlHandler("greetLink") void setGreetHandler(ClickHandler handler);
}
