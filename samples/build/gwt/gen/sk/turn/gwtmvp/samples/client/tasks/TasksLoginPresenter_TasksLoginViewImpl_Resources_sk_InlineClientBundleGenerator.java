package sk.turn.gwtmvp.samples.client.tasks;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class TasksLoginPresenter_TasksLoginViewImpl_Resources_sk_InlineClientBundleGenerator implements sk.turn.gwtmvp.samples.client.tasks.TasksLoginPresenter_TasksLoginViewImpl.Resources {
  private static TasksLoginPresenter_TasksLoginViewImpl_Resources_sk_InlineClientBundleGenerator _instance0 = new TasksLoginPresenter_TasksLoginViewImpl_Resources_sk_InlineClientBundleGenerator();
  private void htmlContentInitializer() {
    htmlContent = new com.google.gwt.resources.client.TextResource() {
      // file:/Users/Zajko/Documents/GitHub/gwtmvp/samples/src/main/java/sk/turn/gwtmvp/samples/client/tasks/TasksLoginView.html
      public String getText() {
        return "<div class=\"row\" data-mvp-dict=\"sk.turn.gwtmvp.samples.client.tasks.Dictionary\">\n  <div class=\"col-md-offset-4 col-md-4\">\n    <h1>{mvpDict.loginTitle}</h1>\n    <label>{mvpDict.loginUsername}</label>\n    <input type=\"text\" class=\"form-control\" data-mvp-id=\"username\"/>\n    <p class=\"help-block\">{mvpDict.loginUsernameHint}</p>\n    <div class=\"text-right\">\n      <button class=\"btn btn-primary\" data-mvp-id=\"login\">{mvpDict.loginButton}</button>\n    </div>\n    <div class=\"progress hidden\" data-mvp-id=\"progress\"><div class=\"progress-bar progress-bar-striped active\" style=\"width: 100%;\"></div></div>\n  </div>\n  <!-- Source code -->\n  <div class=\"col-md-12\">\n    <h2><small><a href=\"https://github.com/turnsk/gwtmvp/blob/master/samples/src/sk/turn/gwtmvp/samples/client/tasks/TasksLoginPresenter.java\" target=\"_blank\">TasksLoginPresenter.java</a></small></h2>\n    <pre class=\"pre-scrollable\" style=\"max-height: 500px;\">\npublic class TasksLoginPresenter extends BasePresenter&lt;TasksLoginPresenter.TasksLoginView&gt; {\n\n  interface TasksLoginView extends HandlerView&lt;Element, TasksLoginPresenter&gt; {\n    @HtmlElement InputElement getUsername();\n  }\n\n  private static final String REGEXP = \"^tasks/.*$\";\n  private static final Dictionary dict = GWT.create(Dictionary.class);\n\n  public TasksLoginPresenter() {\n    super(Dao.getUsername() != null ? null : REGEXP, GWT.&lt;TasksLoginView&gt;create(TasksLoginView.class));\n    view.setHandler(this);\n    // Add a handler for user login/logout\n    Dao.addOnUserChangedHandler(new Dao.OnUserChangedHandler() {\n      @Override\n      public void onUserChanged(String username) {\n        // Update this Presenter's RegExp to either handle all history tokens or none\n        setTokenRegExp(username != null ? null : REGEXP);\n        // Force MVP to update current presenter\n        History.fireCurrentHistoryState();\n      }\n    });\n  }\n\n  @Override\n  public void onShow(String... groups) {\n    // Give the username input focus\n    view.getUsername().focus();\n  }\n\n  @HtmlHandler(\"login\")\n  void onLogin(ClickEvent event) {\n    final String username = view.getUsername().getValue();\n    if (username.length() == 0) {\n      return;\n    }\n    // You may want to validate the username (and password) here, we'll just grab the list of tasks from server\n    // See if we have the tasks already loaded or stored\n    if (Dao.getTasks() != null) {\n      Dao.setUsername(username);\n      return;\n    }\n    try {\n      showProgess(true);\n      new RequestBuilder(RequestBuilder.GET, GWT.getModuleBaseForStaticFiles() + \"tasks.json\").sendRequest(null, new RequestCallback() {\n        @Override\n        public void onResponseReceived(Request request, Response response) {\n          showProgess(false);\n          if (response.getStatusCode() == 200) {\n            Dao.setTasks(JsonUtils.&lt;JsArray&lt;Task&gt;&gt;safeEval(response.getText()));\n            Dao.setUsername(username);\n          } else {\n            onError(request, new Exception(dict.loginInvalidStatusCode(response.getStatusCode())));\n          }\n        }\n        @Override\n        public void onError(Request request, Throwable exception) {\n          showProgess(false);\n          Window.alert(dict.loginFailed(exception.getMessage()));\n        }\n      });\n    } catch (RequestException e) {\n      showProgess(false);\n      Window.alert(dict.loginFailed(e.getMessage()));\n    }\n  }\n\n  private void showProgess(boolean show) {\n    view.getElement(show ? \"login\" : \"progress\").addClassName(\"hidden\");\n    view.getElement(show ? \"progress\" : \"login\").removeClassName(\"hidden\");\n  }\n\n}</pre>\n  </div>\n</div>";
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
      case 'htmlContent': return this.@sk.turn.gwtmvp.samples.client.tasks.TasksLoginPresenter_TasksLoginViewImpl.Resources::htmlContent()();
    }
    return null;
  }-*/;
}
