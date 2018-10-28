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

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.http.client.URL;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.Window;

public class Html5HistoryAgent extends HistoryAgent {
  public static native boolean isSupported() /*-{
    return $wnd.history && $wnd.history.pushState;
  }-*/;

  private String pathToRoot;
  private JavaScriptObject popStateHandler;
  private JavaScriptObject globalClickHandler;

  public Html5HistoryAgent() {
    this("/");
  }

  public Html5HistoryAgent(String pathToRoot) {
    this.pathToRoot = (!pathToRoot.startsWith("/") ? "/" : "") + pathToRoot + (!pathToRoot.endsWith("/") ? "/" : "");
    if (!Window.Location.getPath().startsWith(this.pathToRoot)) {
      throw new IllegalArgumentException("pathToRoot=" + this.pathToRoot + " is not valid for current path=" + Window.Location.getPath());
    }
  }

  @Override
  protected void start() {
    if (popStateHandler == null) {
      popStateHandler = addPopStateEventListener();
    }
    if (globalClickHandler == null) {
      globalClickHandler = addGlobalClickEventListener();
    }
  }

  @Override
  protected void stop() {
    if (popStateHandler != null) {
      removePopStateEventListener(popStateHandler);
      popStateHandler = null;
    }
    if (globalClickHandler != null) {
      removeGlobalClickEventListener(globalClickHandler);
      globalClickHandler = null;
    }
  }

  @Override
  protected String encodeHistoryToken(String historyToken) {
    return URL.encode(historyToken);
  }

  @Override
  protected String getToken() {
    String url = Window.Location.getPath();
    String newToken = url.substring(pathToRoot.length());
    newToken = URL.decode(newToken);
    return newToken;
  }

  @Override
  protected void setToken(String token) {
    nativeSetToken(pathToRoot + token);
  }

  @Override
  protected void replaceToken(String token) {
    nativeReplaceToken(pathToRoot + token);
  }

  private void onPopState() {
    onHistoryChanged(getToken());
  }

  private boolean onBodyClick(NativeEvent e) {
    if (!isSupported() || e.getAltKey() || e.getCtrlKey() || e.getShiftKey() || e.getButton() != NativeEvent.BUTTON_LEFT) {
      return false;
    }
    Element target = e.getEventTarget().cast();
    // Try to find first <a> going up the DOM tree
    while (target != null && !target.getTagName().equalsIgnoreCase("a")) {
      target = target.getParentElement();
    }
    if (target == null) {
      return false;
    }
    String enableFlag = target.getAttribute("data-html5history");
    if ("false".equalsIgnoreCase(enableFlag)) {
      return false;
    }
    String href = target.getAttribute("href");
    if (!"true".equalsIgnoreCase(enableFlag)) {
      // Automatic history link discovery
      if (RegExp.compile("^[a-zA-Z0-9_\\-]+:").test(href)) {
        return false;
      }
    }
    e.preventDefault();
    History.newItem(href.startsWith(pathToRoot) ? href.substring(pathToRoot.length()) : href);
    return true;
  }

  private native JavaScriptObject addPopStateEventListener() /*-{
    var that = this;
    var handler = function(e) {
      that.@Html5HistoryAgent::onPopState()();
    };
    $wnd.addEventListener('popstate', handler);
    return handler;
  }-*/;

  private native void removePopStateEventListener(JavaScriptObject handler) /*-{
    $wnd.removeEventListener('popstate', handler);
  }-*/;

  private native void nativeSetToken(String token) /*-{
    $wnd.history.pushState({}, '', token);
  }-*/;

  private native void nativeReplaceToken(String token) /*-{
    $wnd.history.replaceState({}, '', token);
  }-*/;

  protected native JavaScriptObject addGlobalClickEventListener() /*-{
    var that = this;
    var handler = function(e) {
      that.@Html5HistoryAgent::onBodyClick(Lcom/google/gwt/dom/client/NativeEvent;)(e);
    };
    $doc.body.addEventListener("click", handler);
    return handler;
  }-*/;

  protected native JavaScriptObject removeGlobalClickEventListener(JavaScriptObject handler) /*-{
    $doc.body.removeEventListener("click", handler);
  }-*/;
}
