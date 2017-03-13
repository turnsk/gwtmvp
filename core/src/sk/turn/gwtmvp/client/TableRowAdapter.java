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
package sk.turn.gwtmvp.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.dom.client.TableSectionElement;

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
 *      AnchorElement a = (AnchorElement) (elem.getChildCount() > 0 ? elem.getChild(0) :
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
public abstract class TableRowAdapter<T> extends ViewAdapter<T, TableRowView> {

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
    TableRowView row = GWT.create(TableRowView.class);
    TableRowElement rowElement = row.getRootElement();
    for (int i = 0; i < columns; i++) {
      rowElement.appendChild(rowElement.getOwnerDocument().createTDElement());
    }
    return row;
  }

  /**
   * Loops through columns and calls {@code setTableCell} for each.
   */
  @Override
  public void setViewData(TableRowView view, T item) {
    NodeList<TableCellElement> cells = view.getRootElement().getCells();
    for (int i = 0; i < columns; i++) {
      setTableCell(i, cells.getItem(i), item);
    }
  }

  /**
   * Default implementation that populates the table cells with a return value of {@link #getCellText(int, Object)}.
   * Override this method if you need to perform more complex operation on a table cell.
   * @param column The column index of the table cell (0-based)
   * @param elem The DOM element of the table cell
   * @param item The item for this row
   */
  protected void setTableCell(int column, TableCellElement elem, T item) {
    elem.setInnerText(getCellText(column, item));
  }

  /**
   * Method that should return the text content of the table cell.
   * @param column The column index of the table cell (0-based)
   * @param item The item for this row
   * @return Value (inner text) that should be populated into the table cell
   */
  protected abstract String getCellText(int column, T item);

}
