package sk.turn.gwtmvp.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

public interface Presenter<V extends View<? extends Element>> {

    RegExp getTokenRegExp();
    V getView();
    void onViewLoaded();
    void onShow(MatchResult matchResult);
    void onHide();

}
