package sk.turn.gwtmvp.samples.client.hello;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class HelloPresenter_HelloViewImpl_Resources_default_InlineClientBundleGenerator implements sk.turn.gwtmvp.samples.client.hello.HelloPresenter_HelloViewImpl.Resources {
  private static HelloPresenter_HelloViewImpl_Resources_default_InlineClientBundleGenerator _instance0 = new HelloPresenter_HelloViewImpl_Resources_default_InlineClientBundleGenerator();
  private void htmlContentInitializer() {
    htmlContent = new com.google.gwt.resources.client.TextResource() {
      // file:/Users/Zajko/Documents/GitHub/gwtmvp/samples/src/main/java/sk/turn/gwtmvp/samples/client/hello/HelloView.html
      public String getText() {
        return "<div class=\"row\">\n  <div class=\"col-md-12 form-inline\">\n    <div class=\"form-group well\">\n      <label>Name</label>\n      <input type=\"text\" value=\"John Doe\" class=\"form-control\" data-mvp-id=\"nameInput\"/>\n      <button class=\"btn btn-default\" data-mvp-id=\"greetLink\">Greet</button>\n      for the <span data-mvp-id=\"counter\"></span> time!\n    </div>\n  </div>\n  <!-- Source code -->\n  <div class=\"col-md-12\">\n    <h2><small><a href=\"https://github.com/turnsk/gwtmvp/blob/master/samples/src/sk/turn/gwtmvp/samples/client/hello/HelloPresenter.java\" target=\"_blank\">HelloPresenter.java</a></small></h2>\n    <pre class=\"pre-scrollable\" style=\"max-height: 500px;\">\npublic class HelloPresenter extends BasePresenter&lt;HelloPresenter.HelloView&gt; {\n\n  interface HelloView extends HandlerView&lt;DivElement, HelloPresenter&gt; {\n    @HtmlElement InputElement getNameInput();\n    @HtmlElement SpanElement getCounter();\n  }\n\n  private int counter = 1;\n\n  public HelloPresenter() {\n    super(\"^hello$\", GWT.&lt;HelloView&gt;create(HelloView.class));\n    view.setHandler(this);\n  }\n\n  @Override\n  public void onShow(String... groups) {\n    view.getCounter().setInnerText(formatCounter());\n    view.getNameInput().focus();\n  }\n\n  @HtmlHandler(\"nameInput\")\n  void onNameKeyPress(KeyPressEvent event) {\n    if (event.getCharCode() == KeyCodes.KEY_ENTER) {\n      onGreetClick(null);\n    }\n  }\n\n  @HtmlHandler(\"greetLink\")\n  void onGreetClick(ClickEvent event) {\n    String name = view.getNameInput().getValue();\n    Window.alert(\"Hello \" + (name.length() == 0 ? \"Mr. Nobody\" : name) + \" for the \" + formatCounter() + \" time!\");\n    counter++;\n    view.getCounter().setInnerText(formatCounter());\n  }\n\n  private String formatCounter() {\n    return counter + (counter % 10 == 1 &amp;&amp; (counter / 10) % 10 != 1 ? \"st\" : \n      counter % 10 == 2 &amp;&amp; (counter / 10) % 10 != 1 ? \"nd\" : \n        counter % 10 == 3 &amp;&amp; (counter / 10) % 10 != 1 ? \"rd\" : \"th\");\n  }\n\n}</pre>\n  </div>\n</div>";
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
      case 'htmlContent': return this.@sk.turn.gwtmvp.samples.client.hello.HelloPresenter_HelloViewImpl.Resources::htmlContent()();
    }
    return null;
  }-*/;
}
