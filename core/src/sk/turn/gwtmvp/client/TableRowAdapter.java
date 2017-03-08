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
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableRowElement;

public abstract class TableRowAdapter<T> extends ViewAdapter<T, TableRowView> {

  private final int columns;

  public TableRowAdapter(Element parentElement, int columns) {
    super(parentElement);
    this.columns = columns;
  }

  @Override
  public TableRowView createView() {
    return GWT.create(TableRowView.class);
  }

  @Override
  public void setViewData(TableRowView view, T item) {
    TableRowElement row = view.getRootElement();
    NodeList<TableCellElement> cells = row.getCells();
    for (int i = 0; i < columns; i++) {
      TableCellElement cell;
      if (i < cells.getLength()) {
        cell = cells.getItem(i);
      } else {
        view.getRootElement().appendChild(cell = row.getOwnerDocument().createTDElement());
      }
      setTableCell(i, cell, item);
    }
  }

  protected abstract void setTableCell(int column, TableCellElement elem, T item);

}
