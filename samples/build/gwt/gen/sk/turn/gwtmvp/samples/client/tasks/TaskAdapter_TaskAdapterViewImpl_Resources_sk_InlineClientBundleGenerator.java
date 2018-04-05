package sk.turn.gwtmvp.samples.client.tasks;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class TaskAdapter_TaskAdapterViewImpl_Resources_sk_InlineClientBundleGenerator implements sk.turn.gwtmvp.samples.client.tasks.TaskAdapter_TaskAdapterViewImpl.Resources {
  private static TaskAdapter_TaskAdapterViewImpl_Resources_sk_InlineClientBundleGenerator _instance0 = new TaskAdapter_TaskAdapterViewImpl_Resources_sk_InlineClientBundleGenerator();
  private void htmlContentInitializer() {
    htmlContent = new com.google.gwt.resources.client.TextResource() {
      // file:/Users/Zajko/Documents/GitHub/gwtmvp/samples/src/main/java/sk/turn/gwtmvp/samples/client/tasks/TaskAdapterView.html
      public String getText() {
        return "<div style=\"font-size: 20pt; font-weight: 200;\" data-mvp-id=\"root\">\n  <a href=\"javascript:void(0)\" class=\"btn btn-link\" style=\"float:right; visibility:hidden;\" data-mvp-id=\"removeButton\"><span class=\"glyphicon glyphicon-trash\"></span></a>\n  <a href=\"javascript:void(0)\" class=\"btn btn-link\" style=\"float:right; visibility:hidden;\" data-mvp-id=\"editButton\"><span class=\"glyphicon glyphicon-edit\"></span></a>\n  <input type=\"checkbox\" style=\"float: left; margin-top: 8pt;\" data-mvp-id=\"check\"/>\n  <div style=\"margin-left: 20pt;\" data-mvp-id=\"name\"></div>\n  <input type=\"text\" class=\"form-control hidden\" style=\"margin-left: 20pt; font-size: 20pt; width: 95%;\" data-mvp-id=\"nameInput\"/>\n</div>";
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
      case 'htmlContent': return this.@sk.turn.gwtmvp.samples.client.tasks.TaskAdapter_TaskAdapterViewImpl.Resources::htmlContent()();
    }
    return null;
  }-*/;
}
