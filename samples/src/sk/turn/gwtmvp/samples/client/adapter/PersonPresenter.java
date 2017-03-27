package sk.turn.gwtmvp.samples.client.adapter;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.user.client.History;

import sk.turn.gwtmvp.client.BasePresenter;
import sk.turn.gwtmvp.client.HtmlHandler;
import sk.turn.gwtmvp.client.View;
import sk.turn.gwtmvp.samples.client.adapter.DataProvider.Person;

public class PersonPresenter extends BasePresenter<PersonPresenter.PersonView> {

  interface PersonView extends View<Element> {
    @HtmlHandler("back") void setBackHandler(ClickHandler handler);
  }

  public PersonPresenter() {
    super("^person/([0-9]+)$", (PersonView) GWT.create(PersonView.class));
  }

  @Override
  public void onViewLoaded() {
    getView().setBackHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        History.back();
      }
    });
  }

  @Override
  public void onShow(MatchResult matchResult) {
    Person person = DataProvider.getPerson(Long.parseLong(matchResult.getGroup(1)));
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

}
