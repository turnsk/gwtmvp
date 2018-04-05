package sk.turn.gwtmvp.samples.client.adapter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class PersonPresenter_PersonViewImpl_Resources_sk_InlineClientBundleGenerator implements sk.turn.gwtmvp.samples.client.adapter.PersonPresenter_PersonViewImpl.Resources {
  private static PersonPresenter_PersonViewImpl_Resources_sk_InlineClientBundleGenerator _instance0 = new PersonPresenter_PersonViewImpl_Resources_sk_InlineClientBundleGenerator();
  private void htmlContentInitializer() {
    htmlContent = new com.google.gwt.resources.client.TextResource() {
      // file:/Users/Zajko/Documents/GitHub/gwtmvp/samples/src/main/java/sk/turn/gwtmvp/samples/client/adapter/PersonView.html
      public String getText() {
        return "<div class=\"row\">\n  <div class=\"col-md-12\">\n    <h1>\n      <small><a href=\"javascipr:void(0)\" data-mvp-id=\"back\"><span class=\"glyphicon glyphicon-menu-left\"></span></a></small>\n      <span data-mvp-id=\"name\"></span>\n      <small>ID: <span data-mvp-id=\"id\"></span></small>\n    </h1>\n  </div>\n  <div class=\"col-md-2 text-right\"><b>Email</b></div>\n  <div class=\"col-md-10\" data-mvp-id=\"email\"></div>\n  <div class=\"col-md-2 text-right\"><b>Phone</b></div>\n  <div class=\"col-md-10\" data-mvp-id=\"phone\"></div>\n  <div class=\"col-md-2 text-right\"><b>City</b></div>\n  <div class=\"col-md-10\" data-mvp-id=\"city\"></div>\n  <!-- Source code -->\n  <div class=\"col-md-12\">\n    <h2><small><a href=\"https://github.com/turnsk/gwtmvp/blob/master/samples/src/sk/turn/gwtmvp/samples/client/adapter/PersonPresenter.java\" target=\"_blank\">PersonPresenter.java</a></small></h2>\n    <pre class=\"pre-scrollable\" style=\"max-height: 500px;\">\npublic class PersonPresenter extends BasePresenter&lt;PersonPresenter.PersonView&gt; {\n\n  interface PersonView extends HandlerView&lt;Element, PersonPresenter&gt; {\n    @HtmlElement Element getName();\n    @HtmlElement Element getId();\n    @HtmlElement Element getPhone();\n    @HtmlElement Element getEmail();\n    @HtmlElement Element getCity();\n  }\n\n  public PersonPresenter() {\n    super(\"^person/([0-9]+)$\", (PersonView) GWT.create(PersonView.class));\n    view.setHandler(this);\n  }\n\n  @Override\n  public void onShow(String... groups) {\n    Person person = (groups[1] == null || groups[1].length() == 0 ? null : DataProvider.getPerson(Long.parseLong(groups[1])));\n    if (person == null) {\n      // The person does not exist\n      History.back();\n    }\n    view.getName().setInnerText(person.name);\n    view.getId().setInnerText(String.valueOf(person.id));\n    view.getPhone().setInnerText(person.phone);\n    view.getEmail().setInnerText(person.email);\n    view.getCity().setInnerText(person.city);\n  }\n\n  @HtmlHandler(\"back\")\n  void onBackClicked(ClickEvent event) {\n    History.back();\n  }\n\n}</pre>\n  </div>\n</div>";
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
      case 'htmlContent': return this.@sk.turn.gwtmvp.samples.client.adapter.PersonPresenter_PersonViewImpl.Resources::htmlContent()();
    }
    return null;
  }-*/;
}
