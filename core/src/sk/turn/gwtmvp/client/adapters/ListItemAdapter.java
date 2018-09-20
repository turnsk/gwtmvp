package sk.turn.gwtmvp.client.adapters;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LIElement;
import sk.turn.gwtmvp.client.View;
import sk.turn.gwtmvp.client.ViewHtml;

/**
 * Implementation of {@link ViewAdapter} to populate li elements in an ul/ol element.
 */
public class ListItemAdapter<T> extends ViewAdapter<T, ListItemAdapter.ListItemView> {

  @ViewHtml("<li></li>")
  interface ListItemView extends View<LIElement> {
  }

  /**
   * Constructs a new adapter that will add the list items under the specified &lt;ul&gt; or &lt;ol&gt; element.
   * @param parentElement The parent element.
   */
  public ListItemAdapter(Element parentElement) {
    super(parentElement);
  }

  @Override
  protected ListItemView createView() {
    return GWT.create(ListItemView.class);
  }

  @Override
  protected void setViewData(ListItemView view, T item, int position) {
    view.getRootElement().setInnerText(toString(item));
  }

  /**
   * Override this method if you need to handle custom object-to-string conversion. The default implementation returns
   * {@code String.valueOf(value)} for non-null values and empty string for null values.
   * @param value The object that needs to be represented as a string
   * @return The string representation of the value
   */
  protected String toString(T value) {
    return (value != null ? String.valueOf(value) : "");
  }

}
