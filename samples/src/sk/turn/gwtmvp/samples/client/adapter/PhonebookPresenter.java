package sk.turn.gwtmvp.samples.client.adapter;

import java.util.Comparator;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;

import sk.turn.gwtmvp.client.BasePresenter;
import sk.turn.gwtmvp.client.HandlerView;
import sk.turn.gwtmvp.client.HtmlElement;
import sk.turn.gwtmvp.client.HtmlHandler;
import sk.turn.gwtmvp.client.adapters.ColumnSortingFilter;
import sk.turn.gwtmvp.client.adapters.PagingFilter;
import sk.turn.gwtmvp.client.adapters.PagingFilter.PagingChangeEvent;
import sk.turn.gwtmvp.client.adapters.SearchFilter;
import sk.turn.gwtmvp.client.adapters.TableRowAdapter;
import sk.turn.gwtmvp.samples.client.adapter.DataProvider.Person;

public class PhonebookPresenter extends BasePresenter<PhonebookPresenter.PhonebookView> {

  interface PhonebookView extends HandlerView<Element, PhonebookPresenter> {
    @HtmlElement InputElement getSearchInput();
    @HtmlElement TableElement getTable();
    @HtmlElement Element getNameSortIndicator();
    @HtmlElement Element getCitySortIndicator();
    @HtmlElement Element getPrevPage();
    @HtmlElement Element getCurrentPage();
    @HtmlElement Element getNextPage();
  }

  private TableRowAdapter<DataProvider.Person> adapter;
  private SearchFilter<DataProvider.Person> searchFilter;
  private ColumnSortingFilter<DataProvider.Person> sortFilter;
  private PagingFilter<DataProvider.Person> pagingFilter;

  public PhonebookPresenter() {
    super("^phonebook$", GWT.<PhonebookView>create(PhonebookView.class));
    view.setHandler(this);
  }

  @Override
  public void onViewLoaded() {
    // Initialize the table row adapter
    adapter = new TableRowAdapter<DataProvider.Person>(view.getTable(), 3) {
      @Override
      protected Object getCellContent(int row, int column, Person item) {
        switch (column) {
          case 0:
            return item.name;
          case 1:
            return item.city;
          case 2:
            return item.phone;
        }
        return null;
      }
    };
    // Initialize search filter
    adapter.addFilter(searchFilter = new SearchFilter<DataProvider.Person>() {
      @Override
      protected boolean matches(Person item, String query) {
        return (item.name.toLowerCase().contains(query.toLowerCase()) || 
            item.city.toLowerCase().contains(query.toLowerCase()) || 
            item.phone.contains(query));
      }
    });
    // Initialize the column sorting filter
    adapter.addFilter(sortFilter = new ColumnSortingFilter<Person>()
        .setColumnComparator(0, new Comparator<Person>() {
          @Override
          public int compare(Person o1, Person o2) {
            return o1.name.compareToIgnoreCase(o2.name);
          }
        })
        .setColumnComparator(1, new Comparator<Person>() {
          @Override
          public int compare(Person o1, Person o2) {
            return o1.city.compareToIgnoreCase(o2.city);
          }
        }));
    // Initialize the paging filter
    adapter.addFilter(pagingFilter = new PagingFilter<>(10));
    pagingFilter.addPagingChangeHandler(new PagingFilter.PagingChangeHandler() {
      @Override
      public void onPagingChange(PagingChangeEvent event) {
        view.getPrevPage().getParentElement().setClassName(event.getPageCount() > 0 && event.getPage() > 0 ? "" : "disabled");
        view.getCurrentPage().setInnerText(String.valueOf(event.getPage() + 1) + " / " + String.valueOf(event.getPageCount()));
        view.getNextPage().getParentElement().setClassName(event.getPageCount() > 0 && event.getPage() < event.getPageCount() - 1 ? "" : "disabled");
      }
    });
    // Load some random crowd
    adapter.setNotifyOnChange(false);
    for (int i = 0; i < 100; i++) {
      adapter.addItem(DataProvider.getRandomPerson());
    }
    adapter.notifyDataSetChanged();
  }

  @HtmlHandler("searchInput")
  void onSearchKeyUp(KeyUpEvent event) {
    searchFilter.setQuery(view.getSearchInput().getValue());
  }

  @HtmlHandler({"nameHeader", "cityHeader"})
  void onTableHeaderClick(ClickEvent event) {
    // Find the column we're to sort the table by
    int column = getElementIndexInParent(event.getNativeEvent().getCurrentEventTarget().<Element>cast().getParentElement());
    // Set the sort filter
    sortFilter.setSortColumn(column, sortFilter.getColumn() != column || !sortFilter.isAscending());
    // Update sorting indicators in the table header
    view.getNameSortIndicator().setInnerHTML(column != 0 ? "" : sortFilter.isAscending() ? "&#9650;" : "&#9660;");
    view.getCitySortIndicator().setInnerHTML(column != 1 ? "" : sortFilter.isAscending() ? "&#9650;" : "&#9660;");
  }

  @HtmlHandler("prevPage")
  void onPrevPageClick(ClickEvent event) {
    pagingFilter.setPage(pagingFilter.getPage() - 1);
  }

  @HtmlHandler("nextPage")
  void onPageNumberClick(ClickEvent event) {
    pagingFilter.setPage(pagingFilter.getPage() + 1);
  }

  private int getElementIndexInParent(Element elem) {
    int index = 0;
    while ((elem = elem.getPreviousSiblingElement()) != null) {
      index++;
    }
    return index;
  }

}
