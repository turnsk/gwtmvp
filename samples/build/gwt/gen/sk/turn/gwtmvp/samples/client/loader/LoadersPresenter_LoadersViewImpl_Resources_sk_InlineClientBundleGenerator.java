package sk.turn.gwtmvp.samples.client.loader;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class LoadersPresenter_LoadersViewImpl_Resources_sk_InlineClientBundleGenerator implements sk.turn.gwtmvp.samples.client.loader.LoadersPresenter_LoadersViewImpl.Resources {
  private static LoadersPresenter_LoadersViewImpl_Resources_sk_InlineClientBundleGenerator _instance0 = new LoadersPresenter_LoadersViewImpl_Resources_sk_InlineClientBundleGenerator();
  private void htmlContentInitializer() {
    htmlContent = new com.google.gwt.resources.client.TextResource() {
      // file:/Users/Zajko/Documents/GitHub/gwtmvp/samples/src/main/java/sk/turn/gwtmvp/samples/client/loader/LoadersView.html
      public String getText() {
        return "<div class=\"row\">\n  <div class=\"col-md-6\">\n    <h1>Non-counted loader</h1>\n    <p>\n      <span class=\"progress-bar progress-bar-striped active\" data-mvp-id=\"loader\" style=\"float:right; display:none; width: 33%;\">Something is loading...</span>\n      <button class=\"btn btn-primary\" data-mvp-id=\"showButton\">Show loader</button>\n      <button class=\"btn btn-danger\" data-mvp-id=\"hideButton\">Hide loader</button>\n    </p>\n  </div>\n  <div class=\"col-md-6\">\n    <h1>Counted loader</h1>\n    <p>\n      <span class=\"progress-bar progress-bar-striped active\" data-mvp-id=\"countedLoader\" style=\"float:right; display:none; width: 33%;\"><span data-mvp-id=\"loadCount\"></span> item(s) loading...</span>\n      <button class=\"btn btn-primary\" data-mvp-id=\"showCountedButton\">Show counted loader</button>\n      <button class=\"btn btn-danger\" data-mvp-id=\"hideCountedButton\">Hide counted loader</button>\n    </p>\n  </div>\n  <!-- Source code -->\n  <div class=\"col-md-12\">\n    <h2><small><a href=\"https://github.com/turnsk/gwtmvp/blob/master/samples/src/sk/turn/gwtmvp/samples/client/loader/LoadersPresenter.java\" target=\"_blank\">LoadersPresenter.java</a></small></h2>\n    <pre class=\"pre-scrollable\" style=\"max-height: 500px;\">\npublic class LoadersPresenter extends BasePresenter&lt;LoadersPresenter.LoadersView&gt; {\n\n  interface LoadersView extends HandlerView&lt;DivElement, LoadersPresenter&gt; {\n    @HtmlElement SpanElement getLoader();\n    @HtmlElement SpanElement getCountedLoader();\n    @HtmlElement SpanElement getLoadCount();\n  }\n\n  private int loadCount = 0;\n\n  public LoadersPresenter() {\n    super(\"^loaders$\", (LoadersView) GWT.create(LoadersView.class));\n  }\n\n  @Override\n  public void onViewLoaded() {\n    view.setHandler(this);\n    // Register global (non-counted) loader\n    Loader.register(view.getLoader(), false);\n    // Register named (counted) loader\n    Loader.register(\"counted\", view.getCountedLoader(), true);\n  }\n\n  @HtmlHandler(\"showButton\")\n  void onShowClicked(ClickEvent event) {\n    Loader.show();\n  }\n\n  @HtmlHandler(\"hideButton\")\n  void onHideClicked(ClickEvent event) {\n    Loader.hide();\n  }\n\n  @HtmlHandler(\"showCountedButton\")\n  void onShowCountedClicked(ClickEvent event) {\n    Loader.show(\"counted\");\n    view.getLoadCount().setInnerText(String.valueOf(++loadCount));\n  }\n\n  @HtmlHandler(\"hideCountedButton\")\n  void onHideCountedClicked(ClickEvent event) {\n    Loader.hide(\"counted\");\n    if (loadCount &gt; 0) {\n      view.getLoadCount().setInnerText(String.valueOf(--loadCount));\n    }\n  }\n\n}</pre>\n  </div>\n</div>";
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
      case 'htmlContent': return this.@sk.turn.gwtmvp.samples.client.loader.LoadersPresenter_LoadersViewImpl.Resources::htmlContent()();
    }
    return null;
  }-*/;
}
