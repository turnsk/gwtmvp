package sk.turn.gwtmvp.samples.client.views;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.event.dom.client.ClickHandler;

import sk.turn.gwtmvp.client.HtmlElement;
import sk.turn.gwtmvp.client.HtmlHandler;
import sk.turn.gwtmvp.client.View;

public interface SimpleView extends View<DivElement> {
  @HtmlElement HeadingElement getHeading();
  @HtmlElement TableElement getTable();
  @HtmlHandler({ "heading" }) void setHeadingClickHandler(ClickHandler handler);
}
