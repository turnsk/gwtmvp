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
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;

public class EventManager {

  private static final Map<Element, HandlerManager> handlerManagers = new HashMap<>();

  public static <H extends EventHandler> void setEventHandler(final Element element, int event, GwtEvent.Type<H> eventType, H handler) {
    HandlerManager hm = handlerManagers.get(element);
    if (hm == null) {
      handlerManagers.put(element, hm = new HandlerManager(element));
      Event.setEventListener(element, new EventListener() {
        @Override
        public void onBrowserEvent(Event event) {
          DomEvent.fireNativeEvent(event, handlerManagers.get(element), element);
        }
      });
    }
    Event.sinkEvents(element, event);
    while (hm.getHandlerCount(eventType) > 0) {
      hm.removeHandler(eventType, hm.getHandler(eventType, 0));
    }
    hm.addHandler(eventType, handler);
  }

  public static <H extends EventHandler> void clearEventHandlers(Element element, GwtEvent.Type<H> eventType) {
    HandlerManager hm = handlerManagers.get(element);
    while (hm != null && hm.getHandlerCount(eventType) > 0) {
      hm.removeHandler(eventType, hm.getHandler(eventType, 0));
    }
  }

}
