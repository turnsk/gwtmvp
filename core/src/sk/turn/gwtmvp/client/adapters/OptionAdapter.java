package sk.turn.gwtmvp.client.adapters;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;

import sk.turn.gwtmvp.client.View;
import sk.turn.gwtmvp.client.ViewHtml;

/**
 * Implementation of {@link ViewAdapter} to populate options in a &lt;select&gt; element.
 */
public class OptionAdapter<T> extends ViewAdapter<Map.Entry<String, T>, OptionAdapter.OptionView> {

  @ViewHtml("<option></option>")
  interface OptionView extends View<OptionElement> {
  }

  /**
   * Constructs a new adapter that will add the options under the specified &lt;select&gt; element.
   * @param parentElement The parent &lt;select&gt; element.
   */
  public OptionAdapter(SelectElement parentElement) {
    super(parentElement);
  }

  /**
   * Short-hand method to set the items in a list.
   * @param values The list of objects to populate the list from.
   */
  public void setStringItems(Iterable<T> values) {
    List<Map.Entry<String, T>> items = new ArrayList<>();
    for (T value : values) {
      items.add(new AbstractMap.SimpleEntry<>(toString(value), value));
    }
    setItems(items);
  }

  @Override
  protected OptionView createView() {
    return GWT.create(OptionView.class);
  }

  @Override
  protected void setViewData(OptionView view, Map.Entry<String, T> item, int position) {
    view.getRootElement().setValue(item.getKey());
    view.getRootElement().setInnerText(toString(item.getValue()));
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
