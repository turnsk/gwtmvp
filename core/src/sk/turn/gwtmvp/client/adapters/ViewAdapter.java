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
package sk.turn.gwtmvp.client.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.DomEvent;

import sk.turn.gwtmvp.client.View;

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
 *  &lt;td data-mvp-id="name"&gt;&lt;/td&gt;
 *  &lt;td data-mvp-id="city"&gt;&lt;/td&gt;
 *  &lt;td&gt;&lt;a data-mvp-id="action"&gt;&lt;/a&gt;&lt;/td&gt;
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

  /**
   * Interface for filtering items in a view adapter. A filter may be used e.g. for paging, sorting, searching, etc.
   *
   * @param <T> Type of the object to filter.
   */
  public abstract static class Filter<T> {
    private ViewAdapter<T, ? extends View<? extends Element>> adapter;
    /**
     * Called automatically by the {@code ViewAdapter} to set reference to itself for later filter-changed notifications.
     * @param adapter {@code ViewAdapter} associated with this filter.
     */
    protected <V extends View<? extends Element>> void setAdapter(ViewAdapter<T, V> adapter) {
      this.adapter = adapter;
    }
    /**
     * Call this method when this filter parameters have changed in order to re-filter the associated {@code ViewAdapter}.
     */
    protected void notifyFilterChanged() {
      if (adapter != null) {
        adapter.notifyDataSetChanged();
      }
    }
    /**
     * Method that should do the actual filtering, e.g. changing the order, removing some items, etc.
     * 
     * @param items The list to apply the filter on, the filter must change items within this list.
     */
    protected abstract void applyFilter(List<T> items);
  }

  private static class Entry<T, V extends View<? extends Element>> implements ItemView<T, V> {
    private T item;
    private V view;
    public Entry(T item, V view) {
      this.item = item;
      this.view = view;
    }
    public T getItem() { return item; }
    public V getView() { return view; }
  }

  private class EntryIterator implements Iterator<ItemView<T, V>> {
    private int index;
    @Override
    public boolean hasNext() {
      return (index < filteredList.size());
    }
    @Override
    public ItemView<T, V> next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      int i = index++;
      return new Entry<T, V>(filteredList.get(i), usedViews.get(i));
    }
    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  private static final Logger LOG = Logger.getLogger(ViewAdapter.class.getName());

  private final Element parentElement;
  private final List<T> fullList = new ArrayList<>();
  private final List<Filter<T>> filters = new ArrayList<>();
  private List<T> filteredList = new ArrayList<>();
  private final List<V> usedViews = new ArrayList<>();
  private final List<V> availableViews = new ArrayList<>();
  private final Map<Element, Integer> rootElementsToIndexMap = new HashMap<>();
  private boolean notifyOnChange = true;

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
    return new EntryIterator();
  }

  /**
   * Adds a single item to the adapter, either creating a new view or reusing an old one and
   * immediately attaching the view to the parent.
   * @param item Item to add.
   * @return This instance for easy method call chaining
   */
  public ViewAdapter<T, V> addItem(final T item) {
    if (item != null) {
      fullList.add(item);
      if (notifyOnChange) {
        notifyDataSetChanged();
      }
    }
    return this;
  }

  /**
   * Calls {@code removeAt(index, true)}
   */
  public T removeAt(int index) {
    return removeAt(index, true);
  }

  /**
   * Removes an item at the specified index and return the instance. The corresponding view is also
   * removed from the DOM tree.
   * 
   * @param index Zero-based index of the item to remove.
   * @param inFiltered Whether the index points to the filtered or full list of items, 
   *        if you're not using filters this parameter doesn't make a difference.
   * @return The object removed or {@code null} if the index is out of bounds.
   */
  public T removeAt(int index, boolean inFiltered) {
    if (index < 0 || (inFiltered && index >= fullList.size()) || (!inFiltered && index >= filteredList.size())) {
      return null;
    }
    T item;
    if (inFiltered) {
      item = filteredList.get(index);
      removeItem(item);
    } else {
      item = fullList.remove(index);
      if (notifyOnChange) {
        notifyDataSetChanged();
      }
    }
    return item;
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
    if (fullList.remove(item)) {
      if (notifyOnChange) {
        notifyDataSetChanged();
      }
      return true;
    }
    return false;
  }

  /**
   * Replaces all the items in the adapter and updates the views as necessary.
   * @param items List of items to set to the adapter.
   * @return This instance for easy method call chaining
   */
  public ViewAdapter<T, V> setItems(Iterable<T> items) {
    fullList.clear();
    for (T item : items) {
      fullList.add(item);
    }
    if (notifyOnChange) {
      notifyDataSetChanged();
    }
    return this;
  }

  /**
   * Returns the count of the (filtered) items in the adapter.
   * 
   * @return Count of the filtered items.
   */
  public int getCount() {
    return getFilteredCount();
  }

  /**
   * Returns the count of all the items in the adapter (including the ones filtered out).
   * 
   * @return Count of all the items.
   */
  public int getTotalCount() {
    return fullList.size();
  }

  /**
   * Returns the count of the filtered items in the adapter.
   * 
   * @return Count of the filtered items.
   */
  public int getFilteredCount() {
    return filteredList.size();
  }

  /**
   * Calls {@code getItem(index, true)}
   */
  public T getItem(int index) {
    return getItem(index, true);
  }

  /**
   * Returns the item at the specified index.
   * 
   * @param index The index of the item to retrieve.
   * @param inFiltered Whether the index points to the filtered or full list of items, 
   *        if you're not using filters this parameter doesn't make a difference.
   * @return The item at the specified index or null if out of bounds.
   */
  public T getItem(int index, boolean inFiltered) {
    return (index < 0 ? null : 
      inFiltered ? (index < filteredList.size() ? filteredList.get(index) : null) : 
        (index < fullList.size() ? fullList.get(index) : null));
  }

  /**
   * Returns the view at the specified index.
   * 
   * @param index The index of the view to retrieve.
   * @return The view at the specified index or null if out of bounds.
   */
  public V getItemView(int index) {
    return (index >= 0 && index < usedViews.size() ? usedViews.get(index) : null);
  }

  /**
   * Removes all the items/views from this adapter.
   * @return This instance for easy method call chaining
   */
  public ViewAdapter<T, V> clear() {
    fullList.clear();
    if (notifyOnChange) {
      notifyDataSetChanged();
    }
    return this;
  }

  /**
   * Reloads the views in the adapter, optionally selecting specifying indices to reload.
   * 
   * @param indices A list of indices to reload in the list, leave empty to reload all items.
   */
  @Deprecated
  public void reload(int... indices) {
    if (indices.length == 0) {
      notifyDataSetChanged();
    } else {
      for (int i : indices) {
        if (i < usedViews.size()) {
          safeSetViewData(usedViews.get(i), filteredList.get(i), i);
        }
      }
    }
  }

  /**
   * Reloads the views in the adapter.
   * @return This instance for easy method call chaining
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public ViewAdapter<T, V> notifyDataSetChanged() {
    if (!notifyOnChange) {
      notifyOnChange = true;
    }
    filteredList.clear();
    filteredList.addAll(fullList);
    for (Filter<T> filter : filters) {
      filter.applyFilter(filteredList);
    }
    for (int i = 0; i < filteredList.size(); i++) {
      if (i < usedViews.size()) {
        safeSetViewData(usedViews.get(i), filteredList.get(i), i);
      } else if (availableViews.size() > 0) {
        V view = availableViews.remove(0);
        usedViews.add(view);
        parentElement.appendChild(view.getRootElement());
        rootElementsToIndexMap.put(view.getRootElement(), i);
        safeSetViewData(view, filteredList.get(i), i);
      } else {
        final V view = createView();
        final int index = i;
        final T item = filteredList.get(i);
        view.loadView(new View.ViewLoadedHandler() {
          @Override
          public void onViewLoaded(Element rootElement) {
            // Allow the implementor to do any one-time initialization
            ViewAdapter.this.onViewLoaded(view);
            // Make sure the list hasn't changed in the meantime
            if (index < filteredList.size() && filteredList.get(index) == item) {
              usedViews.add(view);
              parentElement.appendChild(view.getRootElement());
              rootElementsToIndexMap.put(view.getRootElement(), index);
              safeSetViewData(view, item, index);
            } else {
              availableViews.add(view); // Let's not put the view to waste
            }
          }
        });
      }
    }
    // Detach and mark for reuse all excessive views
    while (usedViews.size() > filteredList.size()) {
      V view = usedViews.remove(filteredList.size());
      view.getRootElement().removeFromParent();
      availableViews.add(view);
    }
    return this;
  }

  /**
   * Control whether methods that change the list (add, remove, removeAt, clear) automatically call {@link #notifyDataSetChanged()}. 
   * If set to false, caller must manually call notifyDataSetChanged() to have the changes reflected in the attached view. 
   * The default is true, and calling notifyDataSetChanged() resets the flag to true.
   * 
   * @param notifyOnChange If true, modifications to the list will automatically call {@link #notifyDataSetChanged()}
   * @return This instance for easy method call chaining
   */
  public ViewAdapter<T, V> setNotifyOnChange(boolean notifyOnChange) {
    if (this.notifyOnChange != notifyOnChange) {
      this.notifyOnChange = notifyOnChange;
      if (notifyOnChange) {
        notifyDataSetChanged();
      }
    }
    return this;
  }

  /**
   * Adds a new filter to the local filter chain. Filters are evaluated linearly in the order they were added, 
   * where the next filter gets only the filtered items from the previous one.
   * @param filter Filter to add to the chain
   * @return This instance for easy method call chaining
   */
  public ViewAdapter<T, V> addFilter(Filter<T> filter) {
    if (!filters.contains(filter)) {
      filter.setAdapter(this);
      filters.add(filter);
    }
    return this;
  }

  /**
   * Returns the list of current filters, may be empty. Note that you are responsible for refreshing the view adapter 
   * when you change the filters or update an individual filter.
   * <p>
   * You can chain more filters in a linear manner, the filters will be applied in the order as they are in the list.
   * @return List of filters for this view adapter
   */
  public List<Filter<T>> getFilters() {
    return filters;
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
   * If you need position of view use {@link #setViewData(View, Object, int)}.
   *
   * @param view Instance of the view.
   * @param item Object to populate into the view.
   * @deprecated As of release 1.6, replaced by {@link #setViewData(V, T, int)}
   */
  @Deprecated
  protected void setViewData(V view, T item) {

  }

  /**
   * Override this method to update the view on the position with the data from the object.
   *
   * @param view Instance of the view.
   * @param item Object to populate into the view.
   * @param position Position of the view in adapter.
   */
  protected void setViewData(V view, T item, int position) {
    setViewData(view, item);
  }

  private void safeSetViewData(V view, T item, int position) {
    try {
      setViewData(view, item, position);
    } catch (Exception e) {
      LOG.log(Level.SEVERE, "Call to ViewAdapter.setViewData(V, T) failed.", e);
    }
  }

}
