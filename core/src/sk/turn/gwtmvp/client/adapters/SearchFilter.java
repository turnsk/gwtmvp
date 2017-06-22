package sk.turn.gwtmvp.client.adapters;

import java.util.List;
import java.util.Objects;

/**
 * Simple search filter with user-defined match function
 * @param <T> Type of object that gets paged.
 */
public abstract class SearchFilter<T> extends ViewAdapter.Filter<T> {
  private String query;

  @Override
  protected void applyFilter(List<T> items) {
    if (query == null || query.length() == 0) {
      return;
    }
    for (int i = 0; i < items.size(); i++) {
      if (!matches(items.get(i), query)) {
        items.remove(i--);
      }
    }
  }

  /**
   * Sets the query to search for and refilters the view adapter.
   * @param query Query to search for, null or empty string disables the filter.
   */
  public void setQuery(String query) {
    if (!Objects.equals(query, this.query)) {
      this.query = query;
      notifyFilterChanged();
    }
  }

  /**
   * Implement this method for your own matching logic. The {@code query} parameter is the value set via {@link #setQuery(String)}
   * @param item Object to check whether it matches the query
   * @param query Query string to match the item against
   * @return True if the item matches the query (and should be included in the results), false otherwise
   */
  protected abstract boolean matches(T item, String query);

}
