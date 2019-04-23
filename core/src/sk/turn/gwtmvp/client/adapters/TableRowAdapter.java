/*
 * Copyright 2017 Turn s.r.o.
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

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.dom.client.TableSectionElement;
import com.google.gwt.safehtml.shared.SafeHtml;

import sk.turn.gwtmvp.client.View;
import sk.turn.gwtmvp.client.ViewHtml;

/**
 * Subclass of {@link ViewAdapter} to show items in a table row without the need to create {@link View} for it.
 * <p>
 * Example:
 * <pre><code>// Assuming we have a "TableElement table" and "List&lt;Person&gt; people" variables defined
 *TableRowAdapter&lt;Person&gt; personAdapter = new TableRowAdapter&lt;Person&gt;(table, 3) {
 *  {@literal @}Override
 *  protected String getCellText(int column, Person item) {
 *    switch (column) {
 *      case 0:
 *        return item.name;
 *      case 1:
 *        return item.city;
 *      default:
 *        return null;
 *    }
 *    view.getName().setInnerText(item.name);
 *    view.getCity().setInnerText(item.city);
 *    view.getAction().setHref("#person/" + item.id);
 *    view.getAction().setInnerText("Edit " + item.name);
 *  }
 *  {@literal @}Override
 *  protected void setTableCell(int column, TableCellElement elem, Person item) {
 *    if (column != 2) {
 *      super.setTableCell(column, elem, item);
 *    } else {
 *      AnchorElement a = (AnchorElement) (elem.getChildCount() &gt; 0 ? elem.getChild(0) :
 *          elem.appendChild(elem.getOwnerDocument().createAnchorElement()));
 *      a.setInnerText("Edit " + item.name);
 *      a.setHref("#person/" + item.id);
 *    }
 *  }
 *};
 *personAdapter.setItem(people);</code></pre>
 *
 * @param <T> Type of the object being displayed.
 */
public abstract class TableRowAdapter<T> extends ViewAdapter<T, TableRowAdapter.TableRowView> {

  @ViewHtml("<tr></tr>")
  interface TableRowView extends View<TableRowElement> {
  }

  private final int columns;

  /**
   * Constructs a new instance setting the number of columns for each row and attaching it to a table element.
   * @param parentElement The table to attach the item-rows under. This constructor will take the first table 
   * section of the table, to further define the desired table section, use the other version of the constructor.
   * @param columns Number of columns each row will have, this number cannot be changed later.
   */
  public TableRowAdapter(TableElement parentElement, int columns) {
    this(parentElement.getTBodies().getItem(0), columns);
  }

  /**
   * Constructs a new instance setting the number of columns for each row and attaching it to a table section element.
   * @param parentElement The table section to attach the item-rows under. In most cases you'll be fine with the other 
   * version of the constructor.
   * @param columns Number of columns each row will have, this number cannot be changed later.
   */
  public TableRowAdapter(TableSectionElement parentElement, int columns) {
    super(parentElement);
    this.columns = columns;
  }

  /**
   * Creates a table row view and appends all the columns (TDs).
   */
  @Override
  public TableRowView createView() {
    return GWT.create(TableRowView.class);
  }

  /**
   * Loops through columns and calls {@code setTableCell(int, int, TableCellElement, Object)} for each.
   */
  @Override
  public void setViewData(TableRowView view, T item, int position) {
    NodeList<TableCellElement> cells = view.getRootElement().getCells();
    for (int column = 0; column < columns; column++) {
      setTableCell(position, column, cells.getItem(column), item);
    }
	  for (int column = 0; column < columns; column++) {
        setTableCell(position, column, cells.getItem(column), item);
      }
  }

  @Override
  protected void onViewLoaded(TableRowView view) {
    TableRowElement rowElement = view.getRootElement();
    for (int i = 0; i < columns; i++) {
      rowElement.appendChild(rowElement.getOwnerDocument().createTDElement());
    }
  }

  /**
   * Default implementation that populates the table cells with a return value of {@link #getCellContent(int, int, Object)}.
   * Override this method if you need to perform more complex operation on a table cell.
   * @param row The row index of the table cell (0-based)
   * @param column The column index of the table cell (0-based)
   * @param elem The DOM element of the table cell
   * @param item The item for this row
   */
  protected void setTableCell(int row, int column, TableCellElement elem, T item) {
    Object content = getCellContent(row, column, item);
    if (content == null) {
      setTableCell(column, elem, item);
    } else if (content instanceof SafeHtml) {
      elem.setInnerSafeHtml((SafeHtml) content);
    } else {
      elem.setInnerText(content != null ? content.toString() : "");
    }
  }

  /**
   * Default implementation that populates the table cells with a return value of {@link #getCellContent(int, Object)}.
   * Override this method if you need to perform more complex operation on a table cell.
   * @param column The column index of the table cell (0-based)
   * @param elem The DOM element of the table cell
   * @param item The item for this row
   * @deprecated As of release 1.6, replaced by {@code setTableCell(int, int, TableCellElement, Object)}
   */
  @Deprecated
  protected void setTableCell(int column, TableCellElement elem, T item) {
    Object content = getCellContent(column, item);
    if (content instanceof SafeHtml) {
      elem.setInnerSafeHtml((SafeHtml) content);
    } else {
      elem.setInnerText(content != null ? content.toString() : "");
    }
  }

  /**
   * Method that should return the text or {@code SafeHtml} content of the table cell.
   * @param column The column index of the table cell (0-based)
   * @param item The item for this row
   * @return Either {@code String} or {@code SafeHtml} that should be populated into the table cell
   * @deprecated As of release 1.6, replaced by {@link #getCellContent(int, int, Object)}
   */
  @Deprecated
  protected Object getCellContent(int column, T item) {
    return null;
  }

  /**
   * Method that should return the text or {@code SafeHtml} content of the table cell.
   * @param row The row index of the table cell (0-based)
   * @param column The column index of the table cell (0-based)
   * @param item The item for this row
   * @return Either {@code String} or {@code SafeHtml} that should be populated into the table cell
   */
  @Deprecated
  protected Object getCellContent(int column, T item) {
    return null;
  }

  /**
   * Method that should return the text or {@code SafeHtml} content of the table cell.
   * @param row The row index of the table cell (0-based)
   * @param column The column index of the table cell (0-based)
   * @param item The item for this row
   * @return Either {@code String} or {@code SafeHtml} that should be populated into the table cell
   */
  protected Object getCellContent(int row, int column, T item) {
    return getCellContent(column, item);
  }

}