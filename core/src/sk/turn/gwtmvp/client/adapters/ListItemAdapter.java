/*
 * Copyright 2018 Turn s.r.o.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
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
