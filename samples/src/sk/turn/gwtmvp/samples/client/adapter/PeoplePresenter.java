package sk.turn.gwtmvp.samples.client.adapter;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.TableElement;

import sk.turn.gwtmvp.client.BasePresenter;
import sk.turn.gwtmvp.client.HtmlElement;
import sk.turn.gwtmvp.client.TableRowAdapter;
import sk.turn.gwtmvp.client.View;
import sk.turn.gwtmvp.samples.client.adapter.DataProvider.Person;

public class PeoplePresenter extends BasePresenter<PeoplePresenter.PeopleView> {

  interface PeopleView extends View<DivElement> {
    @HtmlElement TableElement getTable();
  }

  private TableRowAdapter<Person> personRowAdapter;

  public PeoplePresenter() {
    super("^people$", (PeopleView) GWT.create(PeopleView.class));
  }

  @Override
  public void onViewLoaded() {
    personRowAdapter = new TableRowAdapter<Person>(view.getTable(), 4) {
      @Override
      protected String getCellText(int column, Person item) {
        switch (column) {
          case 0:
            return item.name;
          case 1:
            return item.email;
          case 2:
            return item.phone;
          case 3:
            return item.city;
        }
        return null;
      }
      @Override
      protected String getCellHistoryToken(int column, Person item) {
        return (column == 0 ? "person/" + item.id : null);
      }
    };
  }

  @Override
  public void onShow(String... groups) {
    personRowAdapter.setItems(DataProvider.getPeople());
  }

}
