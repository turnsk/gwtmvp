package sk.turn.gwtmvp.samples.client.views;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickHandler;

import sk.turn.gwtmvp.client.HtmlElement;
import sk.turn.gwtmvp.client.HtmlHandler;
import sk.turn.gwtmvp.client.View;

public interface LoadersView extends View<DivElement> {
  @HtmlElement SpanElement getLoader();
  @HtmlHandler("showButton") void setShowHandler(ClickHandler handler);
  @HtmlHandler("hideButton") void setHideHandler(ClickHandler handler);
  @HtmlElement SpanElement getCountedLoader();
  @HtmlElement SpanElement getLoadCount();
  @HtmlHandler("showCountedButton") void setShowCountedHandler(ClickHandler handler);
  @HtmlHandler("hideCountedButton") void setHideCountedHandler(ClickHandler handler);
}
