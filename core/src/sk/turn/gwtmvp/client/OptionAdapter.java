package sk.turn.gwtmvp.client;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;

/**
 * Implementation of {@link ViewAdapter} to populate options in a select element.
 */
public class OptionAdapter extends ViewAdapter<Map.Entry<String, String>, OptionAdapter.OptionView> {

  static class OptionView implements View<OptionElement> {
    private OptionElement rootElement;
    @Override
    public OptionElement getRootElement() {
      if (rootElement == null) {
        rootElement = Document.get().createOptionElement();
      }
      return rootElement;
    }
    @Override
    public <E2 extends Element> E2 getElement(String gwtId) {
      return null;
    }
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
    return new OptionView();
  }

  @Override
  protected void setViewData(OptionView view, Map.Entry<String, String> item) {
    view.rootElement.setValue(item.getKey());
    view.rootElement.setInnerText(item.getValue());
  }

}
