/*
 * Copyright 2017 Turn s.r.o.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package sk.turn.gwtmvp.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;

/**
 * Helper class to bind and unbind native browser events.
 */
public class EventManager {

  private static final Map<Element, HandlerManager> handlerManagers = new HashMap<>();

  /**
   * Sets an event handler for an {@code Element}. All previous event handlers of this type of event are removed.
   * @param <H> Class of {@linkplain EventHandler} that will handle the event
   * @param element The element to bind the event handler for
   * @param eventType The event type (e.g. {@code ClickEvent.getType()})
   * @param handler The handler that handler the event
   */
  public static <H extends EventHandler> void setEventHandler(final Element element, DomEvent.Type<H> eventType, H handler) {
    if (element == null) {
      return;
    }
    HandlerManager hm = handlerManagers.get(element);
    if (hm == null) {
      handlerManagers.put(element, hm = new HandlerManager(element));
      DOM.setEventListener(element, new EventListener() {
        @Override
        public void onBrowserEvent(Event event) {
          DomEvent.fireNativeEvent(event, handlerManagers.get(element), element);
        }
      });
    }
    DOM.sinkBitlessEvent(element, eventType.getName());
    while (hm.getHandlerCount(eventType) > 0) {
      hm.removeHandler(eventType, hm.getHandler(eventType, 0));
    }
    hm.addHandler(eventType, handler);
  }

  /**
   * Clears all event handlers of a specific event type from an element.
   * @param <H> Class of {@linkplain EventHandler} that will be removed
   * @param element The element to clear the event handlers from
   * @param eventType The event type
   */
  public static <H extends EventHandler> void clearEventHandlers(Element element, GwtEvent.Type<H> eventType) {
    HandlerManager hm = handlerManagers.get(element);
    while (hm != null && hm.getHandlerCount(eventType) > 0) {
      hm.removeHandler(eventType, hm.getHandler(eventType, 0));
    }
  }

}
