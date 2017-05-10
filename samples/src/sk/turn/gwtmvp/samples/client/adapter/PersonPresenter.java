package sk.turn.gwtmvp.samples.client.adapter;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.History;

import sk.turn.gwtmvp.client.BasePresenter;
import sk.turn.gwtmvp.client.HandlerView;
import sk.turn.gwtmvp.client.HtmlElement;
import sk.turn.gwtmvp.client.HtmlHandler;
import sk.turn.gwtmvp.samples.client.adapter.DataProvider.Person;

public class PersonPresenter extends BasePresenter<PersonPresenter.PersonView> {

  interface PersonView extends HandlerView<Element, PersonPresenter> {
    @HtmlElement Element getName();
    @HtmlElement Element getId();
    @HtmlElement Element getPhone();
    @HtmlElement Element getEmail();
    @HtmlElement Element getCity();
  }

  public PersonPresenter() {
    super("^person/([0-9]+)$", (PersonView) GWT.create(PersonView.class));
    view.setHandler(this);
  }

  @Override
  public void onShow(String... groups) {
    Person person = (groups[1] == null || groups[1].length() == 0 ? null : DataProvider.getPerson(Long.parseLong(groups[1])));
    if (person == null) {
      // The person does not exist
      History.back();
    }
    view.getName().setInnerText(person.name);
    view.getId().setInnerText(String.valueOf(person.id));
    view.getPhone().setInnerText(person.phone);
    view.getEmail().setInnerText(person.email);
    view.getCity().setInnerText(person.city);
  }

  @HtmlHandler("back")
  void onBackClicked(ClickEvent event) {
    History.back();
  }

}
