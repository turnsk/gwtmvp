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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.DomEvent;

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
 * <pre><code>public interface PersonView extends View&lt;TableRowElement&gt; {
 *  {@literal @}HtmlElement TableCellElement getName();
 *  {@literal @}HtmlElement TableCellElement getCity();
 *  {@literal @}HtmlElement AnchorElement getAction();
 *}</code></pre>
 * <p>
 * PersonView.html
 * <pre><code>&lt;tr&gt;
 *  &lt;td data-mvpId="name"&gt;&lt;/td&gt;
 *  &lt;td data-mvpId="city"&gt;&lt;/td&gt;
 *  &lt;td&gt;&lt;a data-mvpId="action"&gt;&lt;/a&gt;&lt;/td&gt;
 *&lt;/tr&gt;</code></pre>
 * 
 * @param <T> Type of the object being displayed.
 * @param <V> Type of the view able to show one instance of class {@code T}
 */
public abstract class ViewAdapter<T, V extends View<? extends Element>> implements Iterable<ViewAdapter.ItemView<T, V>> {

  public interface ItemView<T, V extends View<? extends Element>> {
    T getItem();
    V getView();
  }

  private static class Entry<T, V extends View<? extends Element>> implements ItemView<T, V> {
    private T item;
    private V view;
    public Entry(T item) {
      this.item = item;
    }
    public T getItem() { return item; }
    public V getView() { return view; }
  }

  private static final Logger LOG = Logger.getLogger(ViewAdapter.class.getName());

  private final Element parentElement;
  private List<ItemView<T, V>> entries = new ArrayList<>();
  private final List<V> availableViews = new ArrayList<>();
  private final Map<Element, Integer> rootElementsToIndexMap = new HashMap<>();

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
   * Returns an iterator over item-view pairs of this adapter.
   * 
   * @return An iterator.
   */
  @Override
  public Iterator<ItemView<T, V>> iterator() {
    return entries.iterator();
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
    Entry<T, V> entry = new Entry<>(item);
    if (availableViews.size() > 0) {
      entry.view = availableViews.remove(0);
    } else {
      entry.view = createView();
      // Allow the implementor to do any one-time initialization
      onViewLoaded(entry.view);
    }
    parentElement.appendChild(entry.view.getRootElement());
    rootElementsToIndexMap.put(entry.view.getRootElement(), entries.size());
    entries.add(entry);
    safeSetViewData(entry);
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
    ItemView<T, V> entry = entries.remove(index);
    entry.getView().getRootElement().removeFromParent();
    rootElementsToIndexMap.remove(entry.getView().getRootElement());
    availableViews.add(entry.getView());
    return entry.getItem();
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
      if (item.equals(entries.get(i).getItem())) {
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
    int index = 0;
    for (T item : items) {
      if (index < entries.size()) {
        // Reuse existing entry
        Entry<T, V> entry = (Entry<T, V>) entries.get(index);
        entry.item = item;
        safeSetViewData(entry);
      } else {
        // Add new entry
        addItem(item);
      }
      index++;
    }
    // Remove all excessive entries
    while (index < entries.size()) {
      removeAt(index);
    }
  }

  /**
   * Returns the count of the items in the adapter.
   * 
   * @return Count of the items.
   */
  public int getCount() {
    return entries.size();
  }

  /**
   * Returns the item at the specified index.
   * 
   * @param index The index of the item to retrieve.
   * @return The item at the specified index or null if out of bounds.
   */
  public T getItem(int index) {
    return (index >= 0 && index < entries.size() ? entries.get(index).getItem() : null);
  }

  /**
   * Returns the view at the specified index.
   * 
   * @param index The index of the view to retrieve.
   * @return The view at the specified index or null if out of bounds.
   */
  public V getItemView(int index) {
    return (index >= 0 && index < entries.size() ? entries.get(index).getView() : null);
  }

  /**
   * Removes all the items/views from this adapter.
   */
  public void clear() {
    for (ItemView<T, V> entry : entries) {
      entry.getView().getRootElement().removeFromParent();
      availableViews.add(entry.getView());
    }
    entries.clear();
    rootElementsToIndexMap.clear();
  }

  /**
   * Reloads the views in the adapter, optionally selecting specifying indices to reload.
   * 
   * @param indices A list of indices to reload in the list, leave empty to reload all items.
   */
  public void reload(int... indices) {
    if (indices.length > 0) {
      for (int index : indices) {
        if (index >= 0 && index < entries.size()) {
          ItemView<T, V> entry = entries.get(index);
          setViewData(entry.getView(), entry.getItem());
        }
      }
    } else {
      for (ItemView<T, V> entry : entries) {
        setViewData(entry.getView(), entry.getItem());
      }
    }
  }

  /**
   * Returns the item index that is related to an event fired from within an item view. If the event is not 
   * fired from a sub-view of the item or the view is no longer attached in the hierarchy, it returns -1.
   * @param event The event from the event handler.
   * @return The item index associated with the event or -1 if the event was not fired from this {@code ViewAdapter}.
   */
  public int getItemIndexFromEvent(DomEvent<?> event) {
    Element e = event.getNativeEvent().getCurrentEventTarget().cast();
    while (e != null && e.getParentElement() != parentElement) {
      e = e.getParentElement();
    }
    if (e == null) {
      return -1;
    }
    Integer index = rootElementsToIndexMap.get(e);
    return (index == null ? -1 : index);
  }

  /**
   * Returns the item that is related to an event fired from within an item view. If the event is not 
   * fired from a sub-view of the item or the view is no longer attached in the hierarchy, it returns null.
   * @param event The event from the event handler.
   * @return The item associated with the event or null if the event was not fired from this {@code ViewAdapter}.
   */
  public T getItemFromEvent(DomEvent<?> event) {
    int index = getItemIndexFromEvent(event);
    return (index == -1 ? null : getItem(index));
  }

  /**
   * Override this method to return a {@link View} instance representing a single adapter item. This
   * method will not be called if there is a currently unused view that will be reused. For any one-time 
   * initialization of the view override the {@link #onViewLoaded(View)} method.
   * 
   * @return Instance of a {@link View} sub-interface.
   */
  protected abstract V createView();

  /**
   * Override this method to do one-time initialization of the created view. This method is called 
   * after the view has been loaded so it's safe to set your handlers here.
   * 
   * @param view Instance of the view.
   */
  protected void onViewLoaded(V view) {
    // Empty implementation
  }

  /**
   * Override this method to update the view with the data from the object.
   * 
   * @param view Instance of the view.
   * @param item Object to populate into the view.
   */
  protected abstract void setViewData(V view, T item);

  private void safeSetViewData(Entry<T, V> entry) {
    try {
      setViewData(entry.view, entry.item);
    } catch (Exception e) {
      LOG.log(Level.SEVERE, "Call to ViewAdapter.setViewData(V, T) failed.", e);
    }
  }

}
