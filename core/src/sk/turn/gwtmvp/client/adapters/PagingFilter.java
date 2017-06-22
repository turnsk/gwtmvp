package sk.turn.gwtmvp.client.adapters;

import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.SimpleEventBus;

import sk.turn.gwtmvp.client.View;

/**
 * Simple paging filter for {@code ViewAdapter}
 * @param <T> Type of object that gets paged.
 */
public class PagingFilter<T> extends ViewAdapter.Filter<T> {

  /**
   * Event handler when the paging changes (either the current page or total pages count).
   */
  public interface PagingChangeHandler {
    void onPagingChange(PagingChangeEvent event);
  }

  /**
   * Event object for {@link #PagingChangeHandler}
   */
  public static class PagingChangeEvent extends Event<PagingChangeHandler> {
    private static Event.Type<PagingChangeHandler> TYPE;
    public static Event.Type<PagingChangeHandler> getType() {
      if (TYPE == null) {
        TYPE = new Event.Type<PagingChangeHandler>();
      }
      return TYPE;
    }
    private final int page;
    private final int pageCount;
    private PagingChangeEvent(int page, int pageCount) {
      this.page = page;
      this.pageCount = pageCount;
    }
    @Override
    public Event.Type<PagingChangeHandler> getAssociatedType() {
      return TYPE;
    }
    @Override
    protected void dispatch(PagingChangeHandler handler) {
      handler.onPagingChange(this);
    }
    /**
     * Returns the current page index
     * @return Current page index
     */
    public int getPage() {
      return page;
    }
    /**
     * Returns the total page count
     * @return Total page count
     */
    public int getPageCount() {
      return pageCount;
    }
  }

  private final int pageSize;
  private int page;
  private int totalCount;
  private int pageCount;
  private EventBus eventBus;

  /**
   * Initializes the filter with a fixed number of items per page. The page size cannot be changed later.
   * @param pageSize The maximum number of items to show per page.
   */
  public <V extends View<? extends Element>> PagingFilter(int pageSize) {
    this.pageSize = pageSize;
  }

  @Override
  protected void applyFilter(List<T> items) {
    totalCount = items.size();
    int pageCount = (int) Math.ceil((double) totalCount / pageSize);
    if (pageCount != this.pageCount) {
      this.pageCount = pageCount;
      if (pageCount > 0 && page >= pageCount) {
        page = pageCount - 1;
      }
      if (eventBus != null) {
        eventBus.fireEvent(new PagingChangeEvent(page, pageCount));
      }
    }
    int firstIndex = page * pageSize;
    int lastIndex = Math.min(totalCount, firstIndex + pageSize);
    for (int i = items.size() - 1; i >= lastIndex ; i--) {
      items.remove(i);
    }
    for (int i = 0; i < firstIndex; i++) {
      items.remove(0);
    }
  }

  /**
   * Retrieves the page size of this filter.
   * @return The maximum number of items per page
   */
  public int getPageSize() {
    return pageSize;
  }

  /**
   * Returns the number of pages in the current state.
   * @return The number of pages in the current state
   */
  public int getPageCount() {
    return pageCount;
  }

  /**
   * Returns the current page index.
   * @return The current page index
   */
  public int getPage() {
    return page;
  }

  /**
   * Sets the current page index and refilters the view adapter to update the UI.
   * @param page
   */
  public void setPage(int page) {
    if (page != this.page && page >= 0 && page < getPageCount()) {
      this.page = page;
      if (eventBus != null) {
        eventBus.fireEvent(new PagingChangeEvent(page, pageCount));
      }
      notifyFilterChanged();
    }
  }

  /**
   * Adds handler for paging changes, i.e. total page count change and current page change.
   * @param handler The paging change handler
   * @return Handler registration that can be used to remove the handler later
   */
  public HandlerRegistration addPagingChangeHandler(PagingChangeHandler handler) {
    if (eventBus == null) {
      eventBus = new SimpleEventBus();
    }
    return eventBus.addHandler(PagingChangeEvent.getType(), handler);
  }

}
