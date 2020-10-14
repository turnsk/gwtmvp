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

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.google.gwt.dom.client.Element;

import sk.turn.gwtmvp.client.View;

/**
 * Helper class to provide support for lists of reusable {@link View}s. Similar to Android's
 * AdapterView concept, this class is reusing detached views and only updating the content per
 * implementor's use-case.
 * <p>
 * Anytime you need to show repetitive views (e.g. table rows, (un)ordered lists, etc.) ViewAdapter
 * allows you to remove the complexity and make the task effectively.
 * <p>
 * If you need to hold some data against individual views (adapter itself does not hold any kind of view-related
 * data) using {@link ViewHolderAdapter} is recommended.
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
public abstract class ViewAdapter<T, V extends View<? extends Element>> extends ViewHolderAdapter<T, ViewAdapter.ViewHolder<T, V>> implements Iterable<ViewAdapter.ItemView<T, V>> {

  public interface ItemView<T, V extends View<? extends Element>> {
    T getItem();
    V getView();
  }

  public static final class ViewHolder<T, V extends View<? extends Element>> extends ViewHolderAdapter.ViewHolder<T, V> {
    private final ViewAdapter<T, V> adapter;
    public ViewHolder(V view, ViewAdapter<T, V> adapter) {
      super(view);
      this.adapter = adapter;
    }
    @Override
    protected void onViewLoaded() {
      super.onViewLoaded();
      adapter.onViewLoaded(view);
    }
    @Override
    protected void bind(T item, int position) {
      adapter.setViewData(view, item, position);
    }
  }

  /**
   * Interface for filtering items in a view adapter. A filter may be used e.g. for paging, sorting, searching, etc.
   *
   * @param <T> Type of the object to filter.
   */
  public abstract static class Filter<T> extends ViewHolderAdapter.Filter<T> { }

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
      return (index < getCount());
    }
    @Override
    public ItemView<T, V> next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      int i = index++;
      return new Entry<T, V>(getItem(i), getItemViewHolder(i).view);
    }
    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  /**
   * Creates a new instance of ViewAdapter with a specific parent element for all the sub-views.
   * This will in most cases be a {@code TableElement}, {@code UListElement}, {@code OListElement},
   * {@code DivElement}, but may be anything that extends an {@code Element} class.
   *
   * @param parentElement Element that all the sub-views will be attached under.
   */
  public ViewAdapter(Element parentElement) {
    super(parentElement);
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
   * Returns the count of the filtered items in the adapter.
   *
   * @return Count of the filtered items.
   */
  public int getFilteredCount() {
    return getCount();
  }

  /**
   * Returns the item at the specified index.
   *
   * @param index The index of the item to retrieve.
   * @param inFiltered Whether the index points to the filtered or full list of items,
   *        if you're not using filters this parameter doesn't make a difference.
   * @return The item at the specified index or null if out of bounds.
   */
  @SuppressWarnings("deprecation")
  public T getItem(int index, boolean inFiltered) {
    return (index < 0 ? null : inFiltered ? getItem(index) : (index < getFullList().size() ? getFullList().get(index) : null));
  }

  /**
   * Returns the view at the specified index.
   *
   * @param index The index of the view to retrieve.
   * @return The view at the specified index or null if out of bounds.
   */
  public V getItemView(int index) {
    ViewHolder<T, V> viewHolder = getItemViewHolder(index);
    return (viewHolder != null ? viewHolder.view : null);
  }

  @Override
  protected final ViewAdapter.ViewHolder<T, V> createViewHolder() {
    return new ViewHolder<T, V>(createView(), this);
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
   * @deprecated As of release 1.6, replaced by {@link #setViewData(View, Object, int)}
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

}