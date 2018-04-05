package sk.turn.gwtmvp.samples.client.loader;

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

public class LoadersPresenter_LoadersViewImpl implements sk.turn.gwtmvp.samples.client.loader.LoadersPresenter.LoadersView {
  interface Resources extends ClientBundle {
    Resources INSTANCE = GWT.create(Resources.class);
    @Source("LoadersView.html") TextResource htmlContent();
  }

  private static final Logger LOG = Logger.getLogger("sk.turn.gwtmvp.samples.client.loader.LoadersPresenter_LoadersViewImpl");

  private com.google.gwt.dom.client.DivElement rootElement = null;
  private final Map<String, Element> elementsMap = new HashMap<>();
  private sk.turn.gwtmvp.samples.client.loader.LoadersPresenter handler;

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
    if (elementsMap.get("loader") == null) {
      LOG.severe("Could not find element with data-mvp-id=\"loader\" in LoadersView.html.");
    }
    if (elementsMap.get("countedLoader") == null) {
      LOG.severe("Could not find element with data-mvp-id=\"countedLoader\" in LoadersView.html.");
    }
    if (elementsMap.get("loadCount") == null) {
      LOG.severe("Could not find element with data-mvp-id=\"loadCount\" in LoadersView.html.");
    }
    sk.turn.gwtmvp.client.EventManager.setEventHandler(getElement("showButton"), com.google.gwt.event.dom.client.ClickEvent.getType(), new com.google.gwt.event.dom.client.ClickHandler() {
      @Override
      public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
        if (handler != null) {
          try { handler.onShowClicked(event); }
          catch (Exception e) { LOG.severe("Invoke of LoadersPresenter.onShowClicked failed: " + e); }
        } else {
          LOG.severe("Ignoring LoadersPresenter.onShowClicked - no HandlerView.handler set");
        }
      }
    });
    sk.turn.gwtmvp.client.EventManager.setEventHandler(getElement("hideButton"), com.google.gwt.event.dom.client.ClickEvent.getType(), new com.google.gwt.event.dom.client.ClickHandler() {
      @Override
      public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
        if (handler != null) {
          try { handler.onHideClicked(event); }
          catch (Exception e) { LOG.severe("Invoke of LoadersPresenter.onHideClicked failed: " + e); }
        } else {
          LOG.severe("Ignoring LoadersPresenter.onHideClicked - no HandlerView.handler set");
        }
      }
    });
    sk.turn.gwtmvp.client.EventManager.setEventHandler(getElement("showCountedButton"), com.google.gwt.event.dom.client.ClickEvent.getType(), new com.google.gwt.event.dom.client.ClickHandler() {
      @Override
      public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
        if (handler != null) {
          try { handler.onShowCountedClicked(event); }
          catch (Exception e) { LOG.severe("Invoke of LoadersPresenter.onShowCountedClicked failed: " + e); }
        } else {
          LOG.severe("Ignoring LoadersPresenter.onShowCountedClicked - no HandlerView.handler set");
        }
      }
    });
    sk.turn.gwtmvp.client.EventManager.setEventHandler(getElement("hideCountedButton"), com.google.gwt.event.dom.client.ClickEvent.getType(), new com.google.gwt.event.dom.client.ClickHandler() {
      @Override
      public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
        if (handler != null) {
          try { handler.onHideCountedClicked(event); }
          catch (Exception e) { LOG.severe("Invoke of LoadersPresenter.onHideCountedClicked failed: " + e); }
        } else {
          LOG.severe("Ignoring LoadersPresenter.onHideCountedClicked - no HandlerView.handler set");
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
  public void setHandler(sk.turn.gwtmvp.samples.client.loader.LoadersPresenter handler) {
    this.handler = handler;
  }

  @Override
  public com.google.gwt.dom.client.SpanElement getLoader() {
    return elementsMap.get("loader").<com.google.gwt.dom.client.SpanElement>cast();
  }

  @Override
  public com.google.gwt.dom.client.SpanElement getCountedLoader() {
    return elementsMap.get("countedLoader").<com.google.gwt.dom.client.SpanElement>cast();
  }

  @Override
  public com.google.gwt.dom.client.SpanElement getLoadCount() {
    return elementsMap.get("loadCount").<com.google.gwt.dom.client.SpanElement>cast();
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
