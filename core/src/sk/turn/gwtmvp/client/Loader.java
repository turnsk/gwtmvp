/*
 * Copyright 2016 Turn s.r.o.
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
import com.google.gwt.dom.client.Style;

/**
 * Helper class for globally managing visibility of loaders (loading indicators). A loader can be
 * either non-counted (a call to {@link #hide()} hides the loader regardless of how many times
 * {@link #show()} has been called. With counted loader you have to call {@code hide()} the same
 * amount of times as you've called {@code show()} before.
 * <p>
 * If you want to use a loader, you have to register it first with a call to
 * {@code register(Element, boolean)} passing an element that represents the loader. To use more
 * than one loader use the tagged versions of the methods.
 */
public class Loader {

  private static class Entry {
    Element element;
    int showedCounter;
  }

  private static Map<String, Entry> entries = new HashMap<>();

  /**
   * Registers a global (general) loader, this method effectively calls
   * {@code register(null, element, isCounted)}.
   * 
   * @param element The element that represents the loader.
   * @param isCounted Whether or not the loader is counted.
   */
  public static void register(Element element, boolean isCounted) {
    register(null, element, isCounted);
  }

  /**
   * Registers a specific loader. Tagging loaders allows you to work with more than one loader on a
   * single page.
   * 
   * @param tag The tag that represents this loader, note that {@code null} is reserved for the
   *          global (general) loader.
   * @param element The element that represents the loader.
   * @param isCounted Whether or not the loader is counted.
   */
  public static void register(String tag, Element element, boolean isCounted) {
    if (entries.containsKey(tag)) {
      return;
    }
    Entry entry = new Entry();
    entry.element = element;
    entry.showedCounter = (isCounted ? 0 : -1);
    entries.put(tag, entry);
  }
  /**
   * Unregisters a specific loader.
   * 
   * @param tag The tag that represents this loader, note that {@code null} is reserved for the
   *          global (general) loader.
   */
  public static void unregister(String tag) {
    if (!entries.containsKey(tag)) {
      return;
    }
    entries.remove(tag);
  }

  /**
   * Shows a loader if it hasn't been already visible before. For counted loaders this also
   * increases the shown counter.
   */
  public static void show() {
    show(null);
  }

  /**
   * Shows a specific loader if it hasn't been already visible before. For counted loaders this also
   * increases the shown counter.
   * 
   * @param tag The tag of the loader to show, note that {@code null} is reserved for the global
   *          (general) loader.
   */
  public static void show(String tag) {
    Entry entry = entries.get(tag);
    if (entry == null) {
      return;
    }
    if (entry.showedCounter == -1) {
      entry.element.getStyle().clearDisplay();
    } else {
      if (entry.showedCounter == 0) {
        entry.element.getStyle().clearDisplay();
      }
      entry.showedCounter++;
    }
  }

  /**
   * Decreases the shown counter for the loader and hides the element if it has reached zero. For
   * non-counted loaders this hides the element immediately.
   */
  public static void hide() {
    hide(null);
  }

  /**
   * Decreases the shown counter for a specific loader and hides the element if it has reached zero.
   * For non-counted loaders this hides the element immediately.
   * 
   * @param tag The tag of the loader to hide, note that {@code null} is reserved for the global
   *          (general) loader.
   */
  public static void hide(String tag) {
    Entry entry = entries.get(tag);
    if (entry == null) {
      return;
    }
    if (entry.showedCounter == -1) {
      entry.element.getStyle().setDisplay(Style.Display.NONE);
    } else {
      if (entry.showedCounter > 0) {
        entry.showedCounter--;
      }
      if (entry.showedCounter == 0) {
        entry.element.getStyle().setDisplay(Style.Display.NONE);
      }
    }
  }

  /**
   * Checks the current state of the loader.
   * 
   * @return True if the loader element is visible, false if not.
   */
  public static boolean isVisible() {
    return isVisible(null);
  }

  /**
   * Checks the current state of a specific loader.
   * 
   * @param tag The tag of the loader to check, note that {@code null} is reserved for the global
   *          (general) loader.
   * @return True if the loader element is visible, false if not or such loader is not registered.
   */
  public static boolean isVisible(String tag) {
    Entry entry = entries.get(tag);
    return (entry != null && !Style.Display.NONE.getCssName().equals(entry.element.getStyle().getDisplay()));
  }

}
