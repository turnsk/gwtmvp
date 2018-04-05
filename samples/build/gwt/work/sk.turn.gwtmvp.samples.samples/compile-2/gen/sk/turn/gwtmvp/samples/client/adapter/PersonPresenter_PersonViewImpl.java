package sk.turn.gwtmvp.samples.client.adapter;

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

public class PersonPresenter_PersonViewImpl implements sk.turn.gwtmvp.samples.client.adapter.PersonPresenter.PersonView {
  interface Resources extends ClientBundle {
    Resources INSTANCE = GWT.create(Resources.class);
    @Source("PersonView.html") TextResource htmlContent();
  }

  private static final Logger LOG = Logger.getLogger("sk.turn.gwtmvp.samples.client.adapter.PersonPresenter_PersonViewImpl");

  private com.google.gwt.dom.client.Element rootElement = null;
  private final Map<String, Element> elementsMap = new HashMap<>();
  private sk.turn.gwtmvp.samples.client.adapter.PersonPresenter handler;

  @Override
  public void loadView(final ViewLoadedHandler<com.google.gwt.dom.client.Element> viewLoadedHandler) {
    if (rootElement != null) {
      viewLoadedHandler.onViewLoaded(rootElement);
    }
    loadView(Resources.INSTANCE.htmlContent().getText(), viewLoadedHandler);
  }

  private void loadView(String html, ViewLoadedHandler<com.google.gwt.dom.client.Element> viewLoadedHandler) {
    Element tempElem = Document.get().createDivElement();
    tempElem.setInnerHTML(html);
    rootElement = (com.google.gwt.dom.client.Element) tempElem.getFirstChild();
    addElementToMap(rootElement, elementsMap);
    NodeList<Element> elements = rootElement.getElementsByTagName("*");
    for (int i = 0; i < elements.getLength(); i++) {
      addElementToMap(elements.getItem(i), elementsMap);
    }
    if (elementsMap.get("name") == null) {
      LOG.severe("Could not find element with data-mvp-id=\"name\" in PersonView.html.");
    }
    if (elementsMap.get("id") == null) {
      LOG.severe("Could not find element with data-mvp-id=\"id\" in PersonView.html.");
    }
    if (elementsMap.get("phone") == null) {
      LOG.severe("Could not find element with data-mvp-id=\"phone\" in PersonView.html.");
    }
    if (elementsMap.get("email") == null) {
      LOG.severe("Could not find element with data-mvp-id=\"email\" in PersonView.html.");
    }
    if (elementsMap.get("city") == null) {
      LOG.severe("Could not find element with data-mvp-id=\"city\" in PersonView.html.");
    }
    sk.turn.gwtmvp.client.EventManager.setEventHandler(getElement("back"), com.google.gwt.event.dom.client.ClickEvent.getType(), new com.google.gwt.event.dom.client.ClickHandler() {
      @Override
      public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
        if (handler != null) {
          try { handler.onBackClicked(event); }
          catch (Exception e) { LOG.severe("Invoke of PersonPresenter.onBackClicked failed: " + e); }
        } else {
          LOG.severe("Ignoring PersonPresenter.onBackClicked - no HandlerView.handler set");
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
  public void setHandler(sk.turn.gwtmvp.samples.client.adapter.PersonPresenter handler) {
    this.handler = handler;
  }

  @Override
  public com.google.gwt.dom.client.Element getName() {
    return elementsMap.get("name").<com.google.gwt.dom.client.Element>cast();
  }

  @Override
  public com.google.gwt.dom.client.Element getId() {
    return elementsMap.get("id").<com.google.gwt.dom.client.Element>cast();
  }

  @Override
  public com.google.gwt.dom.client.Element getPhone() {
    return elementsMap.get("phone").<com.google.gwt.dom.client.Element>cast();
  }

  @Override
  public com.google.gwt.dom.client.Element getEmail() {
    return elementsMap.get("email").<com.google.gwt.dom.client.Element>cast();
  }

  @Override
  public com.google.gwt.dom.client.Element getCity() {
    return elementsMap.get("city").<com.google.gwt.dom.client.Element>cast();
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
