package sk.turn.gwtmvp.client.adapters;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Simple sorting filter for view adapters.
 * @param <T> Type of object that gets paged.
 */
public class SortingFilter<T> extends ViewAdapter.Filter<T> {

  private final Comparator<T> comparator;

  /**
   * Creates a new instance of the filter with a specified comparator.
   * @param comparator Comparator to sort the items with
   */
  public SortingFilter(Comparator<T> comparator) {
    this.comparator = comparator;
  }

  @Override
  protected void applyFilter(List<T> items) {
    Collections.sort(items, comparator);
  }

}
