package sk.turn.gwtmvp.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;

public class Html5History {

    public enum Mode {
        LEGACY,
        HTML5,
        AUTO_DETECT
    }

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

        public void fireValueChangedEvent(String newToken) {
            ValueChangeEvent.fire(this, newToken);
        }

        public HandlerManager getHandlers() {
            return handlers;
        }
    }

    /**
     * HistoryTokenEncoder is responsible for encoding and decoding history token,
     * thus ensuring that tokens are safe to use in the browsers URL.
     */
    private static class HistoryTokenEncoder {
        public native String encode(String toEncode) /*-{
          // encodeURI() does *not* encode the '#' character.
          return $wnd.encodeURI(toEncode).replace("#", "%23");
        }-*/;

        public native String decode(String toDecode) /*-{
          return $wnd.decodeURI(toDecode.replace("%23", "#"));
        }-*/;
    }

    /**
     * Html5History implementation using html5 history.
     */
    private static class Html5HistoryImpl {

        public Html5HistoryImpl() {
            attachListener();
        }

        protected native void attachListener() /*-{
            $doc.body.addEventListener("click",function(e){
                var element = e.target || e.srcElement;
                if (typeof element === 'undefined' || element.tagName.toLowerCase() !== 'a') {
                    return false;
                }
                var href = element.getAttribute("href");
                var externalLink = href.startsWith("https://") || href.startsWith("http://");
                if (!externalLink && !href.startsWith("javascript") && !href.startsWith("mailto")) {
                    e.preventDefault();
                    $entry(@Html5History::onAnchorClicked(Ljava/lang/String;)(href));
                }
            },false);

            var handler = $entry(@Html5History::onPopStateChanged());
            $wnd.addEventListener('popstate', handler, false);
        }-*/;

        public native void newToken(String historyToken) /*-{
          $wnd.history.pushState({}, '', historyToken);
          $wnd.dispatchEvent(new Event('popstate'));
        }-*/;

        public native void replaceToken(String historyToken)   /*-{
            $wnd.history.replaceState({}, '', historyToken);
        }-*/;
    }

    /**
     * History implementation using hash tokens.
     */
    private static class HashTokenHistoryImpl {

        public HashTokenHistoryImpl() {
            attachListener();
        }

        protected native void attachListener() /*-{
          // We explicitly use the third parameter for capture, since Firefox before version 6
          // throws an exception if the parameter is missing.
          // See: https://developer.mozilla.org/es/docs/DOM/elemento.addEventListener#Gecko_notes
          var handler = $entry(@Html5History::onHashChanged());
          $wnd.addEventListener('hashchange', handler, false);
        }-*/;

        public native void newToken(String historyToken) /*-{
          $wnd.location.hash = historyToken;
        }-*/;

        public void replaceToken(String historyToken) {
            Window.Location.replace("#" + historyToken);
        }
    }

    private static Html5HistoryImpl html5HistoryImpl = GWT.create(Html5HistoryImpl.class);
    private static HashTokenHistoryImpl hashTokenHistoryImpl = GWT.create(HashTokenHistoryImpl.class);
    private static Html5History.HistoryEventSource historyEventSource = new HistoryEventSource();
    private static Html5History.HistoryTokenEncoder tokenEncoder = GWT.create(HistoryTokenEncoder.class);

    private static String token = getDecodedHash();
    private static Mode mode = Mode.LEGACY;

    /**
     * <p>
     * Choose your history mode. You have 3 options:
     * <ul><li>for history based on #hastag content please use {@code LEGACY}</li><li>for history based on classical approach with "/" convention use  {@code HTML5}</li><li>for auto-detect mode use  {@code AUTO_DETECT}</li></ul>
     * </p>
     *
     * @param historyMode
     */
    public static void setMode(Mode historyMode) {
        if (historyMode == Mode.AUTO_DETECT) {
            mode = isHtml5HistorySupported() ? Mode.HTML5 : Mode.LEGACY;
        } else {
            mode = historyMode;
        }
        token = (mode ==  Mode.HTML5 ? getDecodedToken() : getDecodedHash());
    }

    public static Mode getMode() {
        return mode;
    }
    /**
     * Adds a {@link com.google.gwt.event.logical.shared.ValueChangeEvent} handler
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
        return tokenEncoder.encode(historyToken);
    }

    /**
     * Fire
     * {@link ValueChangeHandler#onValueChange(com.google.gwt.event.logical.shared.ValueChangeEvent)}
     * events with the current history state. This is most often called at the end
     * of an application's
     * {@link com.google.gwt.core.client.EntryPoint#onModuleLoad()} to inform
     * history handlers of the initial application state.
     */
    public static void fireCurrentHistoryState() {
        String currentToken = getToken();
        historyEventSource.fireValueChangedEvent(currentToken);
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
     * {@link ValueChangeHandler#onValueChange(com.google.gwt.event.logical.shared.ValueChangeEvent)}
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
     * {@link ValueChangeHandler#onValueChange(com.google.gwt.event.logical.shared.ValueChangeEvent)}
     * to be called as well.
     *
     * @param historyToken the token to associate with the new history item
     */
    public static void newItem(String historyToken) {
        newItem(historyToken, true);
    }

    /**
     * Adds a new browser history entry. Calling this method will cause
     * {@link ValueChangeHandler#onValueChange(com.google.gwt.event.logical.shared.ValueChangeEvent)}
     * to be called as well if and only if issueEvent is true.
     *
     * @param historyToken the token to associate with the new history item
     * @param issueEvent true if a
     *          {@link ValueChangeHandler#onValueChange(com.google.gwt.event.logical.shared.ValueChangeEvent)}
     *          event should be issued
     */
    public static void newItem(String historyToken, boolean issueEvent) {
        historyToken = (historyToken == null) ? "" : historyToken;
        if (!historyToken.equals(getToken())) {
            token = historyToken;
            String updateToken = encodeHistoryToken(historyToken);
            if (getMode() == Mode.LEGACY) {
                hashTokenHistoryImpl.newToken(updateToken);
            } else {
                html5HistoryImpl.newToken(updateToken);
            }
            if (issueEvent) {
                historyEventSource.fireValueChangedEvent(historyToken);
            }
        }
    }

    /**
     * Call all history handlers with the specified token. Note that this does not
     * change the history system's idea of the current state and is only kept for
     * backward compatibility. To fire history events for the initial state of the
     * application, instead call {@link #fireCurrentHistoryState()} from the
     * application {@link com.google.gwt.core.client.EntryPoint#onModuleLoad()}
     * method.
     *
     * @param historyToken history token to fire events for
     * @deprecated Use {@link #fireCurrentHistoryState()} instead.
     */
    @Deprecated
    public static void onHistoryChanged(String historyToken) {
        historyEventSource.fireValueChangedEvent(historyToken);
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
     * {@link ValueChangeHandler#onValueChange(com.google.gwt.event.logical.shared.ValueChangeEvent)}
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
     * {@link ValueChangeHandler#onValueChange(com.google.gwt.event.logical.shared.ValueChangeEvent)}
     * to be called as well if and only if issueEvent is true.
     *
     * @param historyToken history token to replace current top entry
     * @param issueEvent issueEvent true if a
     *          {@link ValueChangeHandler#onValueChange(com.google.gwt.event.logical.shared.ValueChangeEvent)}
     *          event should be issued
     */
    public static void replaceItem(String historyToken, boolean issueEvent) {
        token = historyToken;
        if (getMode() == Mode.LEGACY) {
            hashTokenHistoryImpl.replaceToken(encodeHistoryToken(historyToken));
        } else {
            html5HistoryImpl.replaceToken(encodeHistoryToken(historyToken));
        }
        if (issueEvent) {
            fireCurrentHistoryState();
        }
    }

    private static String getDecodedToken() {
        String url = Window.Location.getHref();
        String newToken = url.substring(GWT.getModuleBaseURL().length());
        if (newToken.isEmpty()) {
            return "";
        }
        return tokenEncoder.decode(newToken);
    }

    private static native boolean isHtml5HistorySupported() /*-{
        return $wnd.history && $wnd.history.pushState;
    }-*/;

    // this is called from JS when the native click on a element occurs
    private static void onAnchorClicked(String newToken) {
        newToken = removeHashTag(newToken);
        newItem(newToken);
    }

    private static String removeHashTag(String token) {
        if (token == null || token.equals("")) {
            return "";
        }
        if (token.substring(0, 1).equals("#")) {
            return token.substring(1);
        }
        return token;
    }

    // this is called from JS when the native popstate occurs
    private static void onPopStateChanged() {
        if (getMode() != Mode.HTML5) {
            return;
        }
        String urlToken = getDecodedToken();
        if (!urlToken.equals(getToken())) {
            token = urlToken;
            historyEventSource.fireValueChangedEvent(urlToken);
        }
    }

    private static String getDecodedHash() {
        String hashToken = Window.Location.getHash();
        if (hashToken == null || hashToken.isEmpty()) {
            return "";
        }
        return tokenEncoder.decode(hashToken.substring(1));
    }

    // this is called from JS when the native onhashchange occurs
    private static void onHashChanged() {
        /*
         * We guard against firing events twice, some browser (e.g. safari) tend to
         * fire events on startup if HTML5 pushstate is used.
         */
        if (getMode() != Mode.LEGACY) {
            return;
        }
        String hashToken = getDecodedHash();
        if (!hashToken.equals(getToken())) {
            token = hashToken;
            historyEventSource.fireValueChangedEvent(hashToken);
        }
    }
}