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
 * Implementation of {@link ViewAdapter} to populate options in a select element.
 */
public class OptionAdapter extends ViewAdapter<Map.Entry<String, String>, OptionAdapter.OptionView> {

  @ViewHtml("<option></option>")
  interface OptionView extends View<OptionElement> {
  }

  public OptionAdapter(SelectElement parentElement) {
    super(parentElement);
  }

  public void setStringItems(Iterable<String> values) {
    List<Map.Entry<String, String>> items = new ArrayList<>();
    for (String value : values) {
      items.add(new AbstractMap.SimpleEntry<>(value, value));
    }
    setItems(items);
  }

  @Override
  protected OptionView createView() {
    return GWT.create(OptionView.class);
  }

  @Override
  protected void setViewData(OptionView view, Map.Entry<String, String> item) {
    view.getRootElement().setValue(item.getKey());
    view.getRootElement().setInnerText(item.getValue());
  }

}
