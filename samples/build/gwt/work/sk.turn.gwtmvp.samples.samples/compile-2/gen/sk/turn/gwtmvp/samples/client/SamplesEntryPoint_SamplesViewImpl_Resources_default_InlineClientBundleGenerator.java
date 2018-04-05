package sk.turn.gwtmvp.samples.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class SamplesEntryPoint_SamplesViewImpl_Resources_default_InlineClientBundleGenerator implements sk.turn.gwtmvp.samples.client.SamplesEntryPoint_SamplesViewImpl.Resources {
  private static SamplesEntryPoint_SamplesViewImpl_Resources_default_InlineClientBundleGenerator _instance0 = new SamplesEntryPoint_SamplesViewImpl_Resources_default_InlineClientBundleGenerator();
  private void htmlContentInitializer() {
    htmlContent = new com.google.gwt.resources.client.TextResource() {
      // file:/Users/Zajko/Documents/GitHub/gwtmvp/samples/src/main/java/sk/turn/gwtmvp/samples/client/SamplesView.html
      public String getText() {
        return "<div class=\"row\">\n  <div class=\"col-md-6\">\n    <div class=\"jumbotron\">\n      <h1>Hello world!</h1>\n      <p>Sample showing the basic GWT MVP usage with a simple View and Presenter.</p>\n      <p><a class=\"btn btn-primary btn-lg\" href=\"#hello\">See sample</a></p>\n    </div>\n  </div>\n  <div class=\"col-md-6\">\n    <div class=\"jumbotron\">\n      <h1>Loaders</h1>\n      <p>Demonstrating the use of loaders, both counted and non-counted.</p>\n      <p><a class=\"btn btn-primary btn-lg\" href=\"#loaders\">See sample</a></p>\n    </div>\n  </div>\n  <div class=\"col-md-6\">\n    <div class=\"jumbotron\">\n      <h1>Table adapter</h1>\n      <p>Demonstrating the use of <code>TableRowAdapter</code> for effectively displaying repetitive variable data in a table.</p>\n      <p><a class=\"btn btn-primary btn-lg\" href=\"#people\">See sample</a></p>\n    </div>\n  </div>\n  <div class=\"col-md-6\">\n    <div class=\"jumbotron\">\n      <h1>Phonebook</h1>\n      <p><code>TableRowAdapter</code> with a demonstration of various <code>Filter</code>s (paging, column sorting and search)</p>\n      <p><a class=\"btn btn-primary btn-lg\" href=\"#phonebook\">See sample</a></p>\n    </div>\n  </div>\n  <div class=\"col-md-6\">\n    <div class=\"jumbotron\">\n      <h1>Tasks app</h1>\n      <p>A more complex sample showing various usage-scenarios of GWT MVP on a task manager.</p>\n      <p><a class=\"btn btn-primary btn-lg\" href=\"#tasks/list\">See sample</a></p>\n    </div>\n  </div>\n  <!-- Source code -->\n  <div class=\"col-md-12\">\n    <h2><small><a href=\"https://github.com/turnsk/gwtmvp/blob/master/samples/src/sk/turn/gwtmvp/samples/client/SamplesEntryPoint.java\" target=\"_blank\">SamplesEntryPoint.java</a></small></h2>\n    <pre class=\"pre-scrollable\" style=\"max-height: 500px;\">\npublic class SamplesEntryPoint implements EntryPoint {\n\n  interface SamplesView extends View&lt;Element&gt; {\n  }\n\n  @Override\n  public void onModuleLoad() {\n    Mvp mvp = new Mvp(Document.get().getElementById(\"container\"));\n    mvp.addPresenter(new HelloPresenter());\n    mvp.addPresenter(new LoadersPresenter());\n    mvp.addPresenter(new PeoplePresenter());\n    mvp.addPresenter(new PersonPresenter());\n    mvp.addPresenter(new PhonebookPresenter());\n    // This last presenter catches any history token that hasn't been caught by other presenters\n    mvp.addPresenter(new BasePresenter&lt;SamplesView&gt;(\".*\", (SamplesView) GWT.create(SamplesView.class)));\n    mvp.start();\n  }\n\n}</pre>\n  </div>\n</div>";
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
      case 'htmlContent': return this.@sk.turn.gwtmvp.samples.client.SamplesEntryPoint_SamplesViewImpl.Resources::htmlContent()();
    }
    return null;
  }-*/;
}
