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

public class TaskAdapter_TaskAdapterViewImpl implements sk.turn.gwtmvp.samples.client.tasks.TaskAdapter.TaskAdapterView {
  interface Resources extends ClientBundle {
    Resources INSTANCE = GWT.create(Resources.class);
    @Source("TaskAdapterView.html") TextResource htmlContent();
  }

  private static final Logger LOG = Logger.getLogger("sk.turn.gwtmvp.samples.client.tasks.TaskAdapter_TaskAdapterViewImpl");

  private com.google.gwt.dom.client.Element rootElement = null;
  private final Map<String, Element> elementsMap = new HashMap<>();
  private sk.turn.gwtmvp.samples.client.tasks.TaskAdapter handler;

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
    if (elementsMap.get("root") == null) {
      LOG.severe("Could not find element with data-mvp-id=\"root\" in TaskAdapterView.html.");
    }
    if (elementsMap.get("check") == null) {
      LOG.severe("Could not find element with data-mvp-id=\"check\" in TaskAdapterView.html.");
    }
    if (elementsMap.get("name") == null) {
      LOG.severe("Could not find element with data-mvp-id=\"name\" in TaskAdapterView.html.");
    }
    if (elementsMap.get("nameInput") == null) {
      LOG.severe("Could not find element with data-mvp-id=\"nameInput\" in TaskAdapterView.html.");
    }
    if (elementsMap.get("editButton") == null) {
      LOG.severe("Could not find element with data-mvp-id=\"editButton\" in TaskAdapterView.html.");
    }
    if (elementsMap.get("removeButton") == null) {
      LOG.severe("Could not find element with data-mvp-id=\"removeButton\" in TaskAdapterView.html.");
    }
    sk.turn.gwtmvp.client.EventManager.setEventHandler(getElement("root"), com.google.gwt.event.dom.client.MouseOverEvent.getType(), new com.google.gwt.event.dom.client.MouseOverHandler() {
      @Override
      public void onMouseOver(com.google.gwt.event.dom.client.MouseOverEvent event) {
        if (handler != null) {
          try { handler.onMouseOver(event); }
          catch (Exception e) { LOG.severe("Invoke of TaskAdapter.onMouseOver failed: " + e); }
        } else {
          LOG.severe("Ignoring TaskAdapter.onMouseOver - no HandlerView.handler set");
        }
      }
    });
    sk.turn.gwtmvp.client.EventManager.setEventHandler(getElement("root"), com.google.gwt.event.dom.client.MouseOutEvent.getType(), new com.google.gwt.event.dom.client.MouseOutHandler() {
      @Override
      public void onMouseOut(com.google.gwt.event.dom.client.MouseOutEvent event) {
        if (handler != null) {
          try { handler.onMouseOut(event); }
          catch (Exception e) { LOG.severe("Invoke of TaskAdapter.onMouseOut failed: " + e); }
        } else {
          LOG.severe("Ignoring TaskAdapter.onMouseOut - no HandlerView.handler set");
        }
      }
    });
    sk.turn.gwtmvp.client.EventManager.setEventHandler(getElement("check"), com.google.gwt.event.dom.client.ChangeEvent.getType(), new com.google.gwt.event.dom.client.ChangeHandler() {
      @Override
      public void onChange(com.google.gwt.event.dom.client.ChangeEvent event) {
        if (handler != null) {
          try { handler.onCheckChange(event); }
          catch (Exception e) { LOG.severe("Invoke of TaskAdapter.onCheckChange failed: " + e); }
        } else {
          LOG.severe("Ignoring TaskAdapter.onCheckChange - no HandlerView.handler set");
        }
      }
    });
    sk.turn.gwtmvp.client.EventManager.setEventHandler(getElement("nameInput"), com.google.gwt.event.dom.client.KeyUpEvent.getType(), new com.google.gwt.event.dom.client.KeyUpHandler() {
      @Override
      public void onKeyUp(com.google.gwt.event.dom.client.KeyUpEvent event) {
        if (handler != null) {
          try { handler.onNameKeyUp(event); }
          catch (Exception e) { LOG.severe("Invoke of TaskAdapter.onNameKeyUp failed: " + e); }
        } else {
          LOG.severe("Ignoring TaskAdapter.onNameKeyUp - no HandlerView.handler set");
        }
      }
    });
    sk.turn.gwtmvp.client.EventManager.setEventHandler(getElement("nameInput"), com.google.gwt.event.dom.client.BlurEvent.getType(), new com.google.gwt.event.dom.client.BlurHandler() {
      @Override
      public void onBlur(com.google.gwt.event.dom.client.BlurEvent event) {
        if (handler != null) {
          try { handler.onNameBlur(event); }
          catch (Exception e) { LOG.severe("Invoke of TaskAdapter.onNameBlur failed: " + e); }
        } else {
          LOG.severe("Ignoring TaskAdapter.onNameBlur - no HandlerView.handler set");
        }
      }
    });
    sk.turn.gwtmvp.client.EventManager.setEventHandler(getElement("editButton"), com.google.gwt.event.dom.client.ClickEvent.getType(), new com.google.gwt.event.dom.client.ClickHandler() {
      @Override
      public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
        if (handler != null) {
          try { handler.onEditClick(event); }
          catch (Exception e) { LOG.severe("Invoke of TaskAdapter.onEditClick failed: " + e); }
        } else {
          LOG.severe("Ignoring TaskAdapter.onEditClick - no HandlerView.handler set");
        }
      }
    });
    sk.turn.gwtmvp.client.EventManager.setEventHandler(getElement("removeButton"), com.google.gwt.event.dom.client.ClickEvent.getType(), new com.google.gwt.event.dom.client.ClickHandler() {
      @Override
      public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
        if (handler != null) {
          try { handler.onClick(event); }
          catch (Exception e) { LOG.severe("Invoke of TaskAdapter.onClick failed: " + e); }
        } else {
          LOG.severe("Ignoring TaskAdapter.onClick - no HandlerView.handler set");
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
  public void setHandler(sk.turn.gwtmvp.samples.client.tasks.TaskAdapter handler) {
    this.handler = handler;
  }

  @Override
  public com.google.gwt.dom.client.DivElement getRoot() {
    return elementsMap.get("root").<com.google.gwt.dom.client.DivElement>cast();
  }

  @Override
  public com.google.gwt.dom.client.InputElement getCheck() {
    return elementsMap.get("check").<com.google.gwt.dom.client.InputElement>cast();
  }

  @Override
  public com.google.gwt.dom.client.DivElement getName() {
    return elementsMap.get("name").<com.google.gwt.dom.client.DivElement>cast();
  }

  @Override
  public com.google.gwt.dom.client.InputElement getNameInput() {
    return elementsMap.get("nameInput").<com.google.gwt.dom.client.InputElement>cast();
  }

  @Override
  public com.google.gwt.dom.client.AnchorElement getEditButton() {
    return elementsMap.get("editButton").<com.google.gwt.dom.client.AnchorElement>cast();
  }

  @Override
  public com.google.gwt.dom.client.AnchorElement getRemoveButton() {
    return elementsMap.get("removeButton").<com.google.gwt.dom.client.AnchorElement>cast();
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
