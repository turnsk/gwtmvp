package sk.turn.gwtmvp.samples.client;

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

public class SamplesEntryPoint_SamplesViewImpl implements sk.turn.gwtmvp.samples.client.SamplesEntryPoint.SamplesView {
  interface Resources extends ClientBundle {
    Resources INSTANCE = GWT.create(Resources.class);
    @Source("SamplesView.html") TextResource htmlContent();
  }

  private static final Logger LOG = Logger.getLogger("sk.turn.gwtmvp.samples.client.SamplesEntryPoint_SamplesViewImpl");

  private com.google.gwt.dom.client.Element rootElement = null;
  private final Map<String, Element> elementsMap = new HashMap<>();

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
