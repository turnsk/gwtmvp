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

public class PhonebookPresenter_PhonebookViewImpl implements sk.turn.gwtmvp.samples.client.adapter.PhonebookPresenter.PhonebookView {
  interface Resources extends ClientBundle {
    Resources INSTANCE = GWT.create(Resources.class);
    @Source("PhonebookView.html") TextResource htmlContent();
  }

  private static final Logger LOG = Logger.getLogger("sk.turn.gwtmvp.samples.client.adapter.PhonebookPresenter_PhonebookViewImpl");

  private com.google.gwt.dom.client.Element rootElement = null;
  private final Map<String, Element> elementsMap = new HashMap<>();
  private sk.turn.gwtmvp.samples.client.adapter.PhonebookPresenter handler;

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
    if (elementsMap.get("searchInput") == null) {
      LOG.severe("Could not find element with data-mvp-id=\"searchInput\" in PhonebookView.html.");
    }
    if (elementsMap.get("table") == null) {
      LOG.severe("Could not find element with data-mvp-id=\"table\" in PhonebookView.html.");
    }
    if (elementsMap.get("nameSortIndicator") == null) {
      LOG.severe("Could not find element with data-mvp-id=\"nameSortIndicator\" in PhonebookView.html.");
    }
    if (elementsMap.get("citySortIndicator") == null) {
      LOG.severe("Could not find element with data-mvp-id=\"citySortIndicator\" in PhonebookView.html.");
    }
    if (elementsMap.get("prevPage") == null) {
      LOG.severe("Could not find element with data-mvp-id=\"prevPage\" in PhonebookView.html.");
    }
    if (elementsMap.get("currentPage") == null) {
      LOG.severe("Could not find element with data-mvp-id=\"currentPage\" in PhonebookView.html.");
    }
    if (elementsMap.get("nextPage") == null) {
      LOG.severe("Could not find element with data-mvp-id=\"nextPage\" in PhonebookView.html.");
    }
    sk.turn.gwtmvp.client.EventManager.setEventHandler(getElement("searchInput"), com.google.gwt.event.dom.client.KeyUpEvent.getType(), new com.google.gwt.event.dom.client.KeyUpHandler() {
      @Override
      public void onKeyUp(com.google.gwt.event.dom.client.KeyUpEvent event) {
        if (handler != null) {
          try { handler.onSearchKeyUp(event); }
          catch (Exception e) { LOG.severe("Invoke of PhonebookPresenter.onSearchKeyUp failed: " + e); }
        } else {
          LOG.severe("Ignoring PhonebookPresenter.onSearchKeyUp - no HandlerView.handler set");
        }
      }
    });
    sk.turn.gwtmvp.client.EventManager.setEventHandler(getElement("nameHeader"), com.google.gwt.event.dom.client.ClickEvent.getType(), new com.google.gwt.event.dom.client.ClickHandler() {
      @Override
      public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
        if (handler != null) {
          try { handler.onTableHeaderClick(event); }
          catch (Exception e) { LOG.severe("Invoke of PhonebookPresenter.onTableHeaderClick failed: " + e); }
        } else {
          LOG.severe("Ignoring PhonebookPresenter.onTableHeaderClick - no HandlerView.handler set");
        }
      }
    });
    sk.turn.gwtmvp.client.EventManager.setEventHandler(getElement("cityHeader"), com.google.gwt.event.dom.client.ClickEvent.getType(), new com.google.gwt.event.dom.client.ClickHandler() {
      @Override
      public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
        if (handler != null) {
          try { handler.onTableHeaderClick(event); }
          catch (Exception e) { LOG.severe("Invoke of PhonebookPresenter.onTableHeaderClick failed: " + e); }
        } else {
          LOG.severe("Ignoring PhonebookPresenter.onTableHeaderClick - no HandlerView.handler set");
        }
      }
    });
    sk.turn.gwtmvp.client.EventManager.setEventHandler(getElement("prevPage"), com.google.gwt.event.dom.client.ClickEvent.getType(), new com.google.gwt.event.dom.client.ClickHandler() {
      @Override
      public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
        if (handler != null) {
          try { handler.onPrevPageClick(event); }
          catch (Exception e) { LOG.severe("Invoke of PhonebookPresenter.onPrevPageClick failed: " + e); }
        } else {
          LOG.severe("Ignoring PhonebookPresenter.onPrevPageClick - no HandlerView.handler set");
        }
      }
    });
    sk.turn.gwtmvp.client.EventManager.setEventHandler(getElement("nextPage"), com.google.gwt.event.dom.client.ClickEvent.getType(), new com.google.gwt.event.dom.client.ClickHandler() {
      @Override
      public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
        if (handler != null) {
          try { handler.onPageNumberClick(event); }
          catch (Exception e) { LOG.severe("Invoke of PhonebookPresenter.onPageNumberClick failed: " + e); }
        } else {
          LOG.severe("Ignoring PhonebookPresenter.onPageNumberClick - no HandlerView.handler set");
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
  public void setHandler(sk.turn.gwtmvp.samples.client.adapter.PhonebookPresenter handler) {
    this.handler = handler;
  }

  @Override
  public com.google.gwt.dom.client.InputElement getSearchInput() {
    return elementsMap.get("searchInput").<com.google.gwt.dom.client.InputElement>cast();
  }

  @Override
  public com.google.gwt.dom.client.TableElement getTable() {
    return elementsMap.get("table").<com.google.gwt.dom.client.TableElement>cast();
  }

  @Override
  public com.google.gwt.dom.client.Element getNameSortIndicator() {
    return elementsMap.get("nameSortIndicator").<com.google.gwt.dom.client.Element>cast();
  }

  @Override
  public com.google.gwt.dom.client.Element getCitySortIndicator() {
    return elementsMap.get("citySortIndicator").<com.google.gwt.dom.client.Element>cast();
  }

  @Override
  public com.google.gwt.dom.client.Element getPrevPage() {
    return elementsMap.get("prevPage").<com.google.gwt.dom.client.Element>cast();
  }

  @Override
  public com.google.gwt.dom.client.Element getCurrentPage() {
    return elementsMap.get("currentPage").<com.google.gwt.dom.client.Element>cast();
  }

  @Override
  public com.google.gwt.dom.client.Element getNextPage() {
    return elementsMap.get("nextPage").<com.google.gwt.dom.client.Element>cast();
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
