/*
 * Copyright 2016 Turn s.r.o.
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
package sk.turn.gwtmvp.client;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Element;

/**
 * Helper class to provide support for lists of reusable {@link View}s. Similar to Android's
 * AdapterView concept, this class is reusing detached views and only updating the content per
 * implementor's use-case.
 * <p>
 * Anytime you need to show repetitive views (e.g. table rows, (un)ordered lists, etc.) ViewAdapter
 * allows you to remove the complexity and make the task effectively.
 * <p>
 * Example:
 * <pre><code>// Assuming we have a "TableElement tableElement" and "List&lt;Person&gt; people" variables defined
 *ViewAdapter&lt;Person, PersonView&gt; personAdapter = new ViewAdapter&lt;Person, PersonView&gt;(tableElement) {
 *  {@literal @}Override
 *  protected PersonView createView() {
 *    return GWT.create(PersonView.class);
 *  }
 *  {@literal @}Override
 *  protected void setViewData(PersonView view, Person item) {
 *    view.getName().setInnerText(item.name);
 *    view.getCity().setInnerText(item.city);
 *    view.getAction().setHref("#person/" + item.id);
 *    view.getAction().setInnerText("Edit " + item.name);
 *  }
 *};
 *personAdapter.setItem(people);</code></pre>
 * <p>
 * PersonView.java
 * <pre><code>public interface ListItemView extends View&lt;TableRowElement&gt; {
 *  {@literal @}HtmlElement TableCellElement getName();
 *  {@literal @}HtmlElement TableCellElement getCity();
 *  {@literal @}HtmlElement AnchorElement getAction();
 *}</code></pre>
 * <p>
 * PersonView.html
 * <pre><code>&lt;tr&gt;
 *  &lt;td data-gwtid="name"&gt;&lt;/td&gt;
 *  &lt;td data-gwtid="city"&gt;&lt;/td&gt;
 *  &lt;td&gt;&lt;a data-gwtid="action"&gt;&lt;/a&gt;&lt;/td&gt;
 *&lt;/tr&gt;</code></pre>
 * 
 * @param <T> Type of the object being displayed.
 * @param <V> Type of the view able to show one instance of class {@code T}
 */
public abstract class ViewAdapter<T, V extends View<? extends Element>> {

  private static class Entry<T, V extends View<? extends Element>> {
    T item;
    V view;
  }

  private static final Logger LOG = Logger.getLogger(ViewAdapter.class.getName());

  private final Element parentElement;
  private List<Entry<T, V>> entries = new ArrayList<>();
  private final List<V> availableViews = new ArrayList<>();

  /**
   * Creates a new instance of ViewAdapter with a specific parent element for all the sub-views.
   * This will in most cases be a {@code TableElement}, {@code UListElement}, {@code OListElement},
   * {@code DivElement}, but may be anything that extends an {@code Element} class.
   * 
   * @param parentElement Element that all the sub-views will be attached under.
   */
  public ViewAdapter(Element parentElement) {
    this.parentElement = parentElement;
  }

  /**
   * Adds a single item to the adapter, either creating a new view or reusing an old one and
   * immediately attaching the view to the parent.
   * 
   * @param item Item to add.
   */
  public void addItem(T item) {
    if (item == null) {
      return;
    }
    Entry<T, V> entry = new Entry<>();
    entry.item = item;
    if (availableViews.size() > 0) {
      entry.view = availableViews.remove(0);
    } else {
      entry.view = createView();
    }
    parentElement.appendChild(entry.view.getRootElement());
    entries.add(entry);
    try {
      setViewData(entry.view, entry.item);
    } catch (Exception e) {
      LOG.log(Level.SEVERE, "Call to ViewAdapter.setViewData(V, T) failed.", e);
    }
  }

  /**
   * Removes an item at the specified index and return the instance. The corresponding view is also
   * removed from the DOM tree.
   * 
   * @param index Zero-based index of the item to remove.
   * @return The object removed or {@code null} if the index is out of bounds.
   */
  public T removeAt(int index) {
    if (index < 0 || index >= entries.size()) {
      return null;
    }
    Entry<T, V> entry = entries.remove(index);
    entry.view.getRootElement().removeFromParent();
    availableViews.add(entry.view);
    return entry.item;
  }

  /**
   * Removes a specific item from the adapter and also the corresponding view from the DOM tree.
   * 
   * @param item Item to remove.
   * @return True if the item/view has been removed, false if the item was not found in the current
   *         list.
   */
  public boolean removeItem(T item) {
    if (item == null) {
      return false;
    }
    for (int i = 0; i < entries.size(); i++) {
      if (item.equals(entries.get(i).item)) {
        removeAt(i);
        return true;
      }
    }
    return false;
  }

  /**
   * Replaces all the items in the adapter and updates the views as necessary.
   * 
   * @param items List of items to set to the adapter.
   */
  public void setItems(Iterable<T> items) {
    // TODO Make the update more intelligent
    clear();
    for (T item : items) {
      addItem(item);
    }
  }

  /**
   * Removes all the items/views from this adapter.
   */
  public void clear() {
    for (Entry<T, V> entry : entries) {
      entry.view.getRootElement().removeFromParent();
      availableViews.add(entry.view);
    }
    entries.clear();
  }

  /**
   * Override this method to return a {@link View} instance representing a single adapter item. This
   * method will not be called if there is a currently unused view that will be reused.
   * 
   * @return Instance of a {@link View} sub-interface.
   */
  protected abstract V createView();

  /**
   * Override this method to update the view with the data from the object.
   * 
   * @param view Instance of the view.
   * @param item Object to populate into the view.
   */
  protected abstract void setViewData(V view, T item);

}
