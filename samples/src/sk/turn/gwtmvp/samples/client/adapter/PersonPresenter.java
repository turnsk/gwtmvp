package sk.turn.gwtmvp.samples.client.adapter;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.History;

import sk.turn.gwtmvp.client.BasePresenter;
import sk.turn.gwtmvp.client.HandlerView;
import sk.turn.gwtmvp.client.HtmlHandler;
import sk.turn.gwtmvp.samples.client.adapter.DataProvider.Person;

public class PersonPresenter extends BasePresenter<PersonPresenter.PersonView> {

  interface PersonView extends HandlerView<Element, PersonPresenter> {
  }

  public PersonPresenter() {
    super("^person/([0-9]+)$", (PersonView) GWT.create(PersonView.class));
    getView().setHandler(this);
  }

  @Override
  public void onShow(String... groups) {
    Person person = (groups[1] == null || groups[1].length() == 0 ? null : DataProvider.getPerson(Long.parseLong(groups[1])));
    if (person == null) {
      // The person does not exist
      History.back();
    }
    setElementText("name", person.name);
    setElementText("id", String.valueOf(person.id));
    setElementText("phone", person.phone);
    setElementText("email", person.email);
    setElementText("city", person.city);
  }

  @HtmlHandler("back")
  void onBackClicked(ClickEvent event) {
    History.back();
  }

}
