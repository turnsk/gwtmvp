package sk.turn.gwtmvp.client.adapters;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Filter for sorting a list by configurable number of different comparators. The class uses {@code column} for identifying 
 * the individual comparators, although it can be also viewed as a (column or anything else for that matter) identifier.
 * @param <T> Type of object that gets sorted.
 */
public class ColumnSortingFilter<T> extends ViewAdapter.Filter<T> {

  private final Map<Integer, Comparator<T>> comparators = new HashMap<>();
  private final Comparator<T> comparator = new Comparator<T>() {
    @Override
    public int compare(T o1, T o2) {
      if (column == -1 || !comparators.containsKey(column)) {
        return 0;
      }
      return (ascending ? 1 : -1) * comparators.get(column).compare(o1, o2);
    }
  };
  private int column = -1;
  private boolean ascending = true;

  /**
   * Adds a new comparator identified by a column index/ID.
   * @param column Column index/ID to assign the comparator to, does not need to me sequential (you can start with 3 then add 6, after that 1, etc.)
   * @param comparator Comparator to use when sorting by this column
   * @return This instance for easy method call chaining
   */
  public ColumnSortingFilter<T> setColumnComparator(int column, Comparator<T> comparator) {
    comparators.put(column, comparator);
    if (this.column == -1) {
      this.column = column;
    }
    return this;
  }

  @Override
  protected void applyFilter(List<T> items) {
    Collections.sort(items, comparator);
  }

  /**
   * Returns the current column index/ID that is used for sorting.
   * @return Current column index/ID
   */
  public int getColumn() {
    return column;
  }

  /**
   * Whether or not sorting using ascending order
   * @return Whether or not sorting using ascending order
   */
  public boolean isAscending() {
    return ascending;
  }

  /**
   * Sets the current sorting column index/ID and sorting order and refilters the adapter data.
   * @param column Current column index/ID to sort by
   * @param ascending Ascending/descending flag
   */
  public void setSortColumn(int column, boolean ascending) {
    if (!comparators.containsKey(column)) {
      throw new IllegalArgumentException("Column " + column + " is not configured for sorting");
    }
    if (column != this.column || ascending != this.ascending) {
      this.column = column;
      this.ascending = ascending;
      notifyFilterChanged();
    }
  }

}
