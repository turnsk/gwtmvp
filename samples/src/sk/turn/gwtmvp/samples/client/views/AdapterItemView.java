package sk.turn.gwtmvp.samples.client.views;

import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableRowElement;

import sk.turn.gwtmvp.client.HtmlElement;
import sk.turn.gwtmvp.client.View;

public interface AdapterItemView extends View<TableRowElement> {
  @HtmlElement TableCellElement getName();
  @HtmlElement TableCellElement getEmail();
  @HtmlElement TableCellElement getPhone();
  @HtmlElement TableCellElement getCity();
  @HtmlElement AnchorElement getAction();
}
