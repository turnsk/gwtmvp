package sk.turn.gwtmvp.samples.client.tasks;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ExternalTextResource;
import com.google.gwt.resources.client.ResourceCallback;
import com.google.gwt.resources.client.ResourceException;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class TasksLoginPresenter_TasksLoginViewImpl implements sk.turn.gwtmvp.samples.client.tasks.TasksLoginPresenter.TasksLoginView {
  interface Resources extends ClientBundle {
    Resources INSTANCE = GWT.create(Resources.class);
    @Source("TasksLoginView.html") TextResource htmlContent();
  }

  private static final Logger LOG = Logger.getLogger("sk.turn.gwtmvp.samples.client.tasks.TasksLoginPresenter_TasksLoginViewImpl");

  private com.google.gwt.dom.client.Element rootElement = null;
  private final Map<String, Element> elementsMap = new HashMap<>();
  private sk.turn.gwtmvp.samples.client.tasks.TasksLoginPresenter handler;

  @Override
  public void loadView(final ViewLoadedHandler<com.google.gwt.dom.client.Element> viewLoadedHandler) {
    if (rootElement != null) {
      viewLoadedHandler.onViewLoaded(rootElement);
    }
    loadView(Resources.INSTANCE.htmlContent().getText(), viewLoadedHandler);
  }

  private void loadView(String html, ViewLoadedHandler<com.google.gwt.dom.client.Element> viewLoadedHandler) {
    sk.turn.gwtmvp.samples.client.tasks.Dictionary dict = GWT.create(sk.turn.gwtmvp.samples.client.tasks.Dictionary.class);
    Object dictEntry;
    dictEntry = dict.loginTitle();
    html = html.replace("{mvpDict.loginTitle}", dictEntry instanceof com.google.gwt.safehtml.shared.SafeHtml ? ((com.google.gwt.safehtml.shared.SafeHtml)dictEntry).asString() : dictEntry.toString());
    dictEntry = dict.loginUsername();
    html = html.replace("{mvpDict.loginUsername}", dictEntry instanceof com.google.gwt.safehtml.shared.SafeHtml ? ((com.google.gwt.safehtml.shared.SafeHtml)dictEntry).asString() : dictEntry.toString());
    dictEntry = dict.loginUsernameHint();
    html = html.replace("{mvpDict.loginUsernameHint}", dictEntry instanceof com.google.gwt.safehtml.shared.SafeHtml ? ((com.google.gwt.safehtml.shared.SafeHtml)dictEntry).asString() : dictEntry.toString());
    dictEntry = dict.loginButton();
    html = html.replace("{mvpDict.loginButton}", dictEntry instanceof com.google.gwt.safehtml.shared.SafeHtml ? ((com.google.gwt.safehtml.shared.SafeHtml)dictEntry).asString() : dictEntry.toString());
    Element tempElem = Document.get().createDivElement();
    tempElem.setInnerHTML(html);
    rootElement = (com.google.gwt.dom.client.Element) tempElem.getFirstChild();
    rootElement.removeAttribute("data-mvp-dict");
    addElementToMap(rootElement, elementsMap);
    NodeList<Element> elements = rootElement.getElementsByTagName("*");
    for (int i = 0; i < elements.getLength(); i++) {
      addElementToMap(elements.getItem(i), elementsMap);
    }
    if (elementsMap.get("username") == null) {
      LOG.severe("Could not find element with data-mvp-id=\"username\" in TasksLoginView.html.");
    }
    sk.turn.gwtmvp.client.EventManager.setEventHandler(getElement("login"), com.google.gwt.event.dom.client.ClickEvent.getType(), new com.google.gwt.event.dom.client.ClickHandler() {
      @Override
      public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
        if (handler != null) {
          try { handler.onLogin(event); }
          catch (Exception e) { LOG.severe("Invoke of TasksLoginPresenter.onLogin failed: " + e); }
        } else {
          LOG.severe("Ignoring TasksLoginPresenter.onLogin - no HandlerView.handler set");
        }
      }
    });
    viewLoadedHandler.onViewLoaded(rootElement);
  }

  @Override
  public com.google.gwt.dom.client.Element getRootElement() {
    return rootElement;
  }

  @Override
  public <E2 extends Element> E2 getElement(String mvpId) {
    return (E2) elementsMap.get(mvpId);
  }

  @Override
  public void setHandler(sk.turn.gwtmvp.samples.client.tasks.TasksLoginPresenter handler) {
    this.handler = handler;
  }

  @Override
  public com.google.gwt.dom.client.InputElement getUsername() {
    return elementsMap.get("username").<com.google.gwt.dom.client.InputElement>cast();
  }

  private void addElementToMap(Element element, Map<String, Element> elementsMap) {
    String attrName = (element.hasAttribute("data-mvp-id") ? "data-mvp-id" : element.hasAttribute("data-gwtid") ? "data-gwtid" : null);
    if (attrName == null) {
      return;
    }
    String mvpId = element.getAttribute(attrName);
    if (!mvpId.equals("")) {
      element.removeAttribute(attrName);
      elementsMap.put(mvpId, element);
    }
  }
}
