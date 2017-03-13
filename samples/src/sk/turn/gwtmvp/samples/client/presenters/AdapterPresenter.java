package sk.turn.gwtmvp.samples.client.presenters;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;

import sk.turn.gwtmvp.client.BasePresenter;
import sk.turn.gwtmvp.client.TableRowAdapter;
import sk.turn.gwtmvp.client.ViewAdapter;
import sk.turn.gwtmvp.samples.client.views.AdapterItemView;
import sk.turn.gwtmvp.samples.client.views.AdapterView;

public class AdapterPresenter extends BasePresenter<AdapterView> {

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

  // There are two possible ways to do this: With a separate view for each item or by filling the table cells in code (no separate View class/html required)
  private static final boolean USE_VIEW_ADAPTER = false;

  private ViewAdapter<Person, AdapterItemView> personViewAdapter;
  private TableRowAdapter<Person> personRowAdapter;
  private List<Person> people = new ArrayList<>();

  public AdapterPresenter(AdapterView view) {
    super("^people|(person/([0-9]+))$", view);
  }

  @Override
  public void onViewLoaded() {
    getView().setHeadingClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        History.newItem("");
        event.stopPropagation();
      }
    });
    if (USE_VIEW_ADAPTER) {
      personViewAdapter = new ViewAdapter<Person, AdapterItemView>(getView().getTable()) {
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
    } else {
      personRowAdapter = new TableRowAdapter<Person>(getView().getTable(), 5) {
        @Override
        protected String getCellText(int column, Person item) {
          return (column == 0 ? item.name : column == 1 ? item.email : column == 2 ? item.phone : item.city);
        }
        @Override
        protected void setTableCell(int column, TableCellElement elem, Person item) {
          if (column != 4) {
            super.setTableCell(column, elem, item);
          } else {
            AnchorElement a = (AnchorElement) (elem.getChildCount() > 0 ? elem.getChild(0) : 
              elem.appendChild(elem.getOwnerDocument().createAnchorElement()));
            a.setInnerText("Edit " + item.name);
            a.setHref("#person/" + item.id);
          }
        }
      };
    }
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
        getView().getHeading().setInnerText(person.name + " (" + person.city + ")");
        getView().getTable().getStyle().setDisplay(Style.Display.NONE);
      }
    } else {
      getView().getHeading().setInnerText("People working their asses off in Turn");
      getView().getTable().getStyle().setDisplay(Style.Display.INITIAL);
      if (people.size() == 0) {
        for (int i = 0; i < 10; i++) {
          people.add(getRandomPerson());
        }
        if (USE_VIEW_ADAPTER) {
          personViewAdapter.setItems(people);
        } else {
          personRowAdapter.setItems(people);
        }
      }
    }
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
