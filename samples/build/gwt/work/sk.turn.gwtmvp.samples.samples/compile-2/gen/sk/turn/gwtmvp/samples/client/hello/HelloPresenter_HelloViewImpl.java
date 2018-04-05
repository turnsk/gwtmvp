package sk.turn.gwtmvp.samples.client.hello;

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

public class HelloPresenter_HelloViewImpl implements sk.turn.gwtmvp.samples.client.hello.HelloPresenter.HelloView {
  interface Resources extends ClientBundle {
    Resources INSTANCE = GWT.create(Resources.class);
    @Source("HelloView.html") TextResource htmlContent();
  }

  private static final Logger LOG = Logger.getLogger("sk.turn.gwtmvp.samples.client.hello.HelloPresenter_HelloViewImpl");

  private com.google.gwt.dom.client.DivElement rootElement = null;
  private final Map<String, Element> elementsMap = new HashMap<>();
  private sk.turn.gwtmvp.samples.client.hello.HelloPresenter handler;

  @Override
  public void loadView(final ViewLoadedHandler<com.google.gwt.dom.client.DivElement> viewLoadedHandler) {
    if (rootElement != null) {
      viewLoadedHandler.onViewLoaded(rootElement);
    }
    loadView(Resources.INSTANCE.htmlContent().getText(), viewLoadedHandler);
  }

  private void loadView(String html, ViewLoadedHandler<com.google.gwt.dom.client.DivElement> viewLoadedHandler) {
    Element tempElem = Document.get().createDivElement();
    tempElem.setInnerHTML(html);
    rootElement = (com.google.gwt.dom.client.DivElement) tempElem.getFirstChild();
    addElementToMap(rootElement, elementsMap);
    NodeList<Element> elements = rootElement.getElementsByTagName("*");
    for (int i = 0; i < elements.getLength(); i++) {
      addElementToMap(elements.getItem(i), elementsMap);
    }
    if (elementsMap.get("nameInput") == null) {
      LOG.severe("Could not find element with data-mvp-id=\"nameInput\" in HelloView.html.");
    }
    if (elementsMap.get("counter") == null) {
      LOG.severe("Could not find element with data-mvp-id=\"counter\" in HelloView.html.");
    }
    sk.turn.gwtmvp.client.EventManager.setEventHandler(getElement("nameInput"), com.google.gwt.event.dom.client.KeyPressEvent.getType(), new com.google.gwt.event.dom.client.KeyPressHandler() {
      @Override
      public void onKeyPress(com.google.gwt.event.dom.client.KeyPressEvent event) {
        if (handler != null) {
          try { handler.onNameKeyPress(event); }
          catch (Exception e) { LOG.severe("Invoke of HelloPresenter.onNameKeyPress failed: " + e); }
        } else {
          LOG.severe("Ignoring HelloPresenter.onNameKeyPress - no HandlerView.handler set");
        }
      }
    });
    sk.turn.gwtmvp.client.EventManager.setEventHandler(getElement("greetLink"), com.google.gwt.event.dom.client.ClickEvent.getType(), new com.google.gwt.event.dom.client.ClickHandler() {
      @Override
      public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
        if (handler != null) {
          try { handler.onGreetClick(event); }
          catch (Exception e) { LOG.severe("Invoke of HelloPresenter.onGreetClick failed: " + e); }
        } else {
          LOG.severe("Ignoring HelloPresenter.onGreetClick - no HandlerView.handler set");
        }
      }
    });
    viewLoadedHandler.onViewLoaded(rootElement);
  }

  @Override
  public com.google.gwt.dom.client.DivElement getRootElement() {
    return rootElement;
  }

  @Override
  public <E2 extends Element> E2 getElement(String mvpId) {
    return (E2) elementsMap.get(mvpId);
  }

  @Override
  public void setHandler(sk.turn.gwtmvp.samples.client.hello.HelloPresenter handler) {
    this.handler = handler;
  }

  @Override
  public com.google.gwt.dom.client.InputElement getNameInput() {
    return elementsMap.get("nameInput").<com.google.gwt.dom.client.InputElement>cast();
  }

  @Override
  public com.google.gwt.dom.client.SpanElement getCounter() {
    return elementsMap.get("counter").<com.google.gwt.dom.client.SpanElement>cast();
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
