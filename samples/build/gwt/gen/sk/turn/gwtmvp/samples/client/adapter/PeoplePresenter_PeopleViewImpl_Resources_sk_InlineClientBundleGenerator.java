package sk.turn.gwtmvp.samples.client.adapter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class PeoplePresenter_PeopleViewImpl_Resources_sk_InlineClientBundleGenerator implements sk.turn.gwtmvp.samples.client.adapter.PeoplePresenter_PeopleViewImpl.Resources {
  private static PeoplePresenter_PeopleViewImpl_Resources_sk_InlineClientBundleGenerator _instance0 = new PeoplePresenter_PeopleViewImpl_Resources_sk_InlineClientBundleGenerator();
  private void htmlContentInitializer() {
    htmlContent = new com.google.gwt.resources.client.TextResource() {
      // file:/Users/Zajko/Documents/GitHub/gwtmvp/samples/src/main/java/sk/turn/gwtmvp/samples/client/adapter/PeopleView.html
      public String getText() {
        return "<div class=\"row\">\n  <div class=\"col-md-12\"><h1>Just a random crowd</h1></div>\n  <div class=\"col-md-12\">\n    <table class=\"table table-striped\" data-mvp-id=\"table\">\n      <tr><th>Name</th><th>Email</th><th>Phone</th><th>City</th></tr>\n    </table>\n  </div>\n  <!-- Source code -->\n  <div class=\"col-md-12\">\n    <h2><small><a href=\"https://github.com/turnsk/gwtmvp/blob/master/samples/src/sk/turn/gwtmvp/samples/client/adapter/PeoplePresenter.java\" target=\"_blank\">PeoplePresenter.java</a></small></h2>\n    <pre class=\"pre-scrollable\" style=\"max-height: 500px;\">\npublic class PeoplePresenter extends BasePresenter&lt;PeoplePresenter.PeopleView&gt; {\n\n  interface PeopleView extends View&lt;DivElement&gt; {\n    @HtmlElement TableElement getTable();\n  }\n\n  private TableRowAdapter&lt;Person&gt; personRowAdapter;\n\n  public PeoplePresenter() {\n    super(\"^people$\", (PeopleView) GWT.create(PeopleView.class));\n  }\n\n  @Override\n  public void onViewLoaded() {\n    personRowAdapter = new TableRowAdapter&lt;Person&gt;(view.getTable(), 4) {\n      @Override\n      protected Object getCellContent(int column, Person item) {\n        switch (column) {\n          case 0:\n            return new SafeHtmlBuilder().appendHtmlConstant(\"&lt;a href=\\\"#person/\").append(item.id).appendHtmlConstant(\"\\\"&gt;\")\n                .appendEscaped(item.name).appendHtmlConstant(\"&lt;/a&gt;\").toSafeHtml();\n          case 1:\n            return item.email;\n          case 2:\n            return item.phone;\n          case 3:\n            return item.city;\n        }\n        return null;\n      }\n    };\n  }\n\n  @Override\n  public void onShow(String... groups) {\n    personRowAdapter.setItems(DataProvider.getPeople());\n  }\n\n}</pre>\n  </div>\n  <div class=\"col-md-12\">\n    <h2><small><a href=\"https://github.com/turnsk/gwtmvp/blob/master/samples/src/sk/turn/gwtmvp/samples/client/adapter/DataProvider.java\" target=\"_blank\">DataProvider.java</a></small></h2>\n    <pre class=\"pre-scrollable\" style=\"max-height: 500px;\">\npublic class DataProvider {\n\n  public static class Person {\n    long id;\n    String name;\n    String email;\n    String phone;\n    String city;\n  }\n\n  private static final String[] NAMES = new String[] { \"Reginia Weatherholt\", \"Gwenn Windom\", \"Evelina Mitchener\", \"Sibyl Kohler\", \"Bettyann Evensen\", \n      \"Mervin Leicht\", \"Ambrose Prowse\", \"Lilla Remy\", \"Windy Diener\", \"Donna Maggio\", \"Damien Schimmel\", \"Andria Down\", \"Ettie Reveles\", \"Teodora Copley\", \n      \"Rebekah Moua\", \"Angelique Howser\", \"Lahoma Mcdowell\", \"Torrie Reynoso\", \"Dahlia Elder\", \"Lily Rodney\", \"Fransisca Dufner\", \"Hillary Asbell\", \n      \"Samantha Eddleman\", \"Latasha Bowen\", \"Jeanelle Pullman\", \"Marylou Anselmo\", \"Mireille Miland\", \"Honey Eutsler\", \"Julienne Jamieson\", \"Pearly Geist\", \n      \"Teresita Trawick\", \"Ernesto Krauth\", \"Stacy Deeds\", \"Reda Mcadoo\", \"Trent Rickerson\", \"Shala Grubb\", \"Regena Woodring\", \"Lenny Bowes\", \n      \"Cathie Capobianco\", \"Verlene Wojtczak\", \"Cody Ferguson\", \"Aleisha Humphries\", \"Golda Lolley\", \"Cicely Casady\", \"Jacquiline Michna\", \"Eldora Griggs\", \n      \"Donnie Wills\", \"Deloise Holdsworth\", \"Golden Hites\", \"Suellen Plemmons\" };\n  private static final String[] CITIES = new String[] { \"New York\", \"Los Angeles\", \"Chicago\", \"Houston\", \"Philadelphia\", \"Phoenix\", \"San Antonio\", \"San Diego\", \n      \"Dallas\", \"San Jose\" };\n\n  private static Map&lt;Long, Person&gt; crowd;\n\n  public static List&lt;Person&gt; getPeople() {\n    if (crowd == null) {\n      crowd = new LinkedHashMap&lt;&gt;(10);\n      for (int i = 0; i &lt; 10; i++) {\n        Person p = getRandomPerson();\n        crowd.put(p.id, p);\n      }\n    }\n    return new ArrayList&lt;&gt;(crowd.values());\n  }\n\n  public static Person getPerson(long id) {\n    if (crowd == null) {\n      return null;\n    }\n    return crowd.get(id);\n  }\n\n  private static Person getRandomPerson() {\n    Person person = new Person();\n    person.id = (long) (5000 * Math.random());\n    person.name = NAMES[(int) (NAMES.length * Math.random())];\n    person.email = person.name.replace(' ', '.').toLowerCase() + \"@email.com\";\n    person.phone = \"+\";\n    for (int i = 0; i &lt; 10; i++) {\n      person.phone += String.valueOf(1 + (int) (9 * Math.random()));\n    }\n    person.city = CITIES[(int) (CITIES.length * Math.random())];\n    return person;\n  }\n\n}</pre>\n  </div>\n</div>";
      }
      public String getName() {
        return "htmlContent";
      }
    }
    ;
  }
  private static class htmlContentInitializer {
    static {
      _instance0.htmlContentInitializer();
    }
    static com.google.gwt.resources.client.TextResource get() {
      return htmlContent;
    }
  }
  public com.google.gwt.resources.client.TextResource htmlContent() {
    return htmlContentInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static com.google.gwt.resources.client.TextResource htmlContent;
  
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      htmlContent(), 
    };
  }
  public ResourcePrototype getResource(String name) {
    if (GWT.isScript()) {
      return getResourceNative(name);
    } else {
      if (resourceMap == null) {
        resourceMap = new java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype>();
        resourceMap.put("htmlContent", htmlContent());
      }
      return resourceMap.get(name);
    }
  }
  private native ResourcePrototype getResourceNative(String name) /*-{
    switch (name) {
      case 'htmlContent': return this.@sk.turn.gwtmvp.samples.client.adapter.PeoplePresenter_PeopleViewImpl.Resources::htmlContent()();
    }
    return null;
  }-*/;
}
