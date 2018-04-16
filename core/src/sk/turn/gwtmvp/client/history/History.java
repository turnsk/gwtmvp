/*
 * Copyright 2018 Turn s.r.o.
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
package sk.turn.gwtmvp.client.history;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

public class History {

  private static class HistoryEventSource implements HasValueChangeHandlers<String> {
    private HandlerManager handlers = new HandlerManager(null);

    @Override
    public void fireEvent(GwtEvent<?> event) {
      handlers.fireEvent(event);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
      return handlers.addHandler(ValueChangeEvent.getType(), handler);
    }

    private void fireValueChangedEvent(String newToken) {
      ValueChangeEvent.fire(this, newToken);
    }
  }

  static {
    setHistoryAgent(new GwtHistoryAgent());
  }

  private static String token;
  private static HistoryEventSource historyEventSource = new HistoryEventSource();
  private static HistoryAgent historyAgent;

  public static HistoryAgent getHistoryAgent() {
    return historyAgent;
  }

  public static void setHistoryAgent(HistoryAgent historyAgent) {
    if (History.historyAgent != null) {
      History.historyAgent.stop();
    }
    if (historyAgent == null) {
      throw new NullPointerException("historyAgent cannot be null");
    }
    History.historyAgent = historyAgent;
    token = historyAgent.getToken();
    historyAgent.start();
  }

  /**
   * Adds a {@code com.google.gwt.event.logical.shared.ValueChangeEvent} handler
   * to be informed of changes to the browser's history stack.
   *
   * @param handler the handler
   * @return the registration used to remove this value change handler
   */
  public static HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
    return historyEventSource.addValueChangeHandler(handler);
  }

  /**
   * Programmatic equivalent to the user pressing the browser's 'back' button.
   */
  public static native void back() /*-{
    $wnd.history.back();
  }-*/;

  /**
   * Encode a history token for use as part of a URI.
   *
   * @param historyToken the token to encode
   * @return the encoded token, suitable for use as part of a URI
   */
  public static String encodeHistoryToken(String historyToken) {
    return historyAgent.encodeHistoryToken(historyToken);
  }

  /**
   * Fire
   * {@code ValueChangeHandler.onValueChange(com.google.gwt.event.logical.shared.ValueChangeEvent)}
   * events with the current history state. This is most often called at the end
   * of an application's {@code com.google.gwt.core.client.EntryPoint.onModuleLoad()} to inform
   * history handlers of the initial application state.
   */
  public static void fireCurrentHistoryState() {
    historyEventSource.fireValueChangedEvent(token);
  }

  /**
   * Programmatic equivalent to the user pressing the browser's 'forward'
   * button.
   */
  public static native void forward() /*-{
    $wnd.history.forward();
  }-*/;

  /**
   * Gets the current history token. The handler will not receive a
   * {@code ValueChangeHandler.onValueChange(com.google.gwt.event.logical.shared.ValueChangeEvent)}
   * event for the initial token; requiring that an application request the
   * token explicitly on startup gives it an opportunity to run different
   * initialization code in the presence or absence of an initial token.
   *
   * @return the actual token
   */
  public static String getToken() {
    return token;
  }

  /**
   * Adds a new browser history entry. Calling this method will cause
   * {@code ValueChangeHandler.onValueChange(com.google.gwt.event.logical.shared.ValueChangeEvent)}
   * to be called as well.
   *
   * @param historyToken the token to associate with the new history item
   */
  public static void newItem(String historyToken) {
    newItem(historyToken, true);
  }

  /**
   * Adds a new browser history entry. Calling this method will cause
   * {@code ValueChangeHandler.onValueChange(com.google.gwt.event.logical.shared.ValueChangeEvent)}
   * to be called as well if and only if issueEvent is true.
   *
   * @param historyToken the token to associate with the new history item
   * @param issueEvent true if a
   *      {@code ValueChangeHandler.onValueChange(com.google.gwt.event.logical.shared.ValueChangeEvent)}
   *      event should be issued
   */
  public static void newItem(String historyToken, boolean issueEvent) {
    if (historyToken == null) {
      historyToken = "";
    }
    if (!historyToken.equals(token)) {
      token = historyToken;
      historyAgent.setToken(historyToken);
      if (issueEvent) {
        fireCurrentHistoryState();
      }
    }
  }

  /**
   * Replace the current history token on top of the browsers history stack.
   *
   * <p>Note: This method has problems. The URL is updated with window.location.replace,
   * this unfortunately has side effects when using the deprecated iframe linker
   * (ie. "std" linker). Make sure you are using the cross site iframe linker when using
   * this method in your code.
   *
   * <p>Calling this method will cause
   * {@code ValueChangeHandler.onValueChange(com.google.gwt.event.logical.shared.ValueChangeEvent)}
   * to be called as well.
   *
   * @param historyToken history token to replace current top entry
   */
  public static void replaceItem(String historyToken) {
    replaceItem(historyToken, true);
  }

  /**
   * Replace the current history token on top of the browsers history stack.
   *
   * <p>Note: This method has problems. The URL is updated with window.location.replace,
   * this unfortunately has side effects when using the deprecated iframe linker
   * (ie. "std" linker). Make sure you are using the cross site iframe linker when using
   * this method in your code.
   *
   * <p>Calling this method will cause
   * {@code ValueChangeHandler.onValueChange(com.google.gwt.event.logical.shared.ValueChangeEvent)}
   * to be called as well if and only if issueEvent is true.
   *
   * @param historyToken history token to replace current top entry
   * @param issueEvent issueEvent true if a
   *      {@code ValueChangeHandler.onValueChange(com.google.gwt.event.logical.shared.ValueChangeEvent)}
   *      event should be issued
   */
  public static void replaceItem(String historyToken, boolean issueEvent) {
    if (historyToken == null) {
      historyToken = "";
    }
    if (!historyToken.equals(token)) {
      token = historyToken;
      historyAgent.replaceToken(historyToken);
      if (issueEvent) {
        fireCurrentHistoryState();
      }
    }
  }

  static void onHistoryChanged(HistoryAgent historyAgent, String historyToken) {
    if (History.historyAgent != historyAgent) {
      return;
    }
    if (historyToken == null) {
      historyToken = "";
    }
    if (!historyToken.equals(token)) {
      token = historyToken;
      fireCurrentHistoryState();
    }
  }
}