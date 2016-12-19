package sk.turn.gwtmvp.samples.client.presenters;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;

import sk.turn.gwtmvp.client.Presenter;
import sk.turn.gwtmvp.client.ViewAdapter;
import sk.turn.gwtmvp.samples.client.views.AdapterItemView;
import sk.turn.gwtmvp.samples.client.views.AdapterView;

public class AdapterPresenter implements Presenter<AdapterView> {

  private static class Person {
    long id;
    String name;
    String email;
    String phone;
    String city;
  }

  private static final String[] NAMES = new String[] { "Reginia Weatherholt", "Gwenn Windom", "Evelina Mitchener", "Sibyl Kohler", "Bettyann Evensen", 
      "Mervin Leicht", "Ambrose Prowse", "Lilla Remy", "Windy Diener", "Donna Maggio", "Damien Schimmel", "Andria Down", "Ettie Reveles", "Teodora Copley", 
      "Rebekah Moua", "Angelique Howser", "Lahoma Mcdowell", "Torrie Reynoso", "Dahlia Elder", "Lily Rodney", "Fransisca Dufner", "Hillary Asbell", 
      "Samantha Eddleman", "Latasha Bowen", "Jeanelle Pullman", "Marylou Anselmo", "Mireille Miland", "Honey Eutsler", "Julienne Jamieson", "Pearly Geist", 
      "Teresita Trawick", "Ernesto Krauth", "Stacy Deeds", "Reda Mcadoo", "Trent Rickerson", "Shala Grubb", "Regena Woodring", "Lenny Bowes", 
      "Cathie Capobianco", "Verlene Wojtczak", "Cody Ferguson", "Aleisha Humphries", "Golda Lolley", "Cicely Casady", "Jacquiline Michna", "Eldora Griggs", 
      "Donnie Wills", "Deloise Holdsworth", "Golden Hites", "Suellen Plemmons" };
  private static final String[] CITIES = new String[] { "New York", "Los Angeles", "Chicago", "Houston", "Philadelphia", "Phoenix", "San Antonio", "San Diego", 
      "Dallas", "San Jose" };

  private final RegExp tokenRegExp = RegExp.compile("^people|(person/([0-9]+))$");
  private AdapterView view;
  private ViewAdapter<Person, AdapterItemView> personAdapter;
  private List<Person> people = new ArrayList<>();

  public AdapterPresenter(AdapterView view) {
    this.view = view;
  }

  @Override
  public RegExp getTokenRegExp() {
    return tokenRegExp;
  }

  @Override
  public AdapterView getView() {
    return view;
  }

  @Override
  public void onViewLoaded() {
    view.setHeadingClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        History.newItem("");
        event.stopPropagation();
      }
    });
    personAdapter = new ViewAdapter<Person, AdapterItemView>(view.getTable()) {
      @Override
      protected AdapterItemView createView() {
        return GWT.create(AdapterItemView.class);
      }
      @Override
      protected void setViewData(AdapterItemView view, final Person item) {
        view.getName().setInnerText(item.name);
        view.getEmail().setInnerText(item.email);
        view.getPhone().setInnerText(item.phone);
        view.getCity().setInnerText(item.city);
        view.getAction().setHref("#person/" + item.id);
        view.getAction().setInnerText("Edit " + item.name);
      }
    };
  }

  @Override
  public void onShow(MatchResult matchResult) {
    if (matchResult.getGroupCount() >= 3 && matchResult.getGroup(2) != null) {
      long personId = Long.parseLong(matchResult.getGroup(2));
      Person person = null;
      for (Person p : people) {
        if (p.id == personId) {
          person = p;
          break;
        }
      }
      if (person == null) {
        Window.alert("Couldn't find person with id " + personId);
        History.newItem("");
      } else {
        view.getHeading().setInnerText(person.name + " (" + person.city + ")");
        view.getTable().getStyle().setDisplay(Style.Display.NONE);
      }
    } else {
      view.getHeading().setInnerText("People working their asses off in Turn");
      view.getTable().getStyle().setDisplay(Style.Display.INITIAL);
      people.clear();
      for (int i = 0; i < 10; i++) {
        people.add(getRandomPerson());
      }
      personAdapter.setItems(people);
    }
  }

  @Override
  public void onHide() {
  }

  private Person getRandomPerson() {
    Person person = new Person();
    person.id = (long) (5000 * Math.random());
    person.name = NAMES[(int) (NAMES.length * Math.random())];
    person.email = person.name.replace(' ', '.').toLowerCase() + "@email.com";
    person.phone = "+";
    for (int i = 0; i < 10; i++) {
      person.phone += String.valueOf(1 + (int) (9 * Math.random()));
    }
    person.city = CITIES[(int) (CITIES.length * Math.random())];
    return person;
  }

}
