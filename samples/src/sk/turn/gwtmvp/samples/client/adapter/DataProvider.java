package sk.turn.gwtmvp.samples.client.adapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DataProvider {

  public static class Person {
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

  private static Map<Long, Person> crowd;

  public static List<Person> getPeople() {
    if (crowd == null) {
      crowd = new LinkedHashMap<>(10);
      for (int i = 0; i < 10; i++) {
        Person p = getRandomPerson();
        crowd.put(p.id, p);
      }
    }
    return new ArrayList<>(crowd.values());
  }

  public static Person getPerson(long id) {
    if (crowd == null) {
      return null;
    }
    return crowd.get(id);
  }

  public static Person getRandomPerson() {
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
