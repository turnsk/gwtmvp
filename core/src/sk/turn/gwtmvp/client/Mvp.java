package sk.turn.gwtmvp.client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.user.client.History;

public class Mvp implements ValueChangeHandler<String> {

  private static final Logger LOG = Logger.getLogger(Mvp.class.getName());

  private final Element rootElement;
  private HandlerRegistration historyRegistration;
  private final List<Presenter<? extends View<? extends Element>>> presenters = new ArrayList<>();
  private Presenter<? extends View<? extends Element>> currentPresenter;
  private final Set<Presenter<? extends View<? extends Element>>> initializedPresenters = new HashSet<>();

  public Mvp(Element rootElement) {
    this.rootElement = rootElement;
  }

  public List<Presenter<? extends View<? extends Element>>> getPresenters() {
    return new ArrayList<>(presenters);
  }

  public void addPresenter(Presenter<? extends View<? extends Element>> presenter) {
    presenters.add(presenter);
  }

  public boolean removePresenter(Presenter<? extends View<? extends Element>> presenter) {
    for (Presenter<? extends View<? extends Element>> p : presenters) {
      if (p == presenter) {
        presenters.remove(presenter);
        return true;
      }
    }
    return false;
  }

  public void start() {
    historyRegistration = History.addValueChangeHandler(this);
    History.fireCurrentHistoryState();
  }

  public void stop() {
    if (historyRegistration != null) {
      historyRegistration.removeHandler();
      historyRegistration = null;
    }
    hideCurrentPresenter();
  }

  @Override
  public void onValueChange(ValueChangeEvent<String> event) {
    Presenter<? extends View<? extends Element>> matchingPresenter = null;
    MatchResult matchResult = null;
    for (Presenter<? extends View<? extends Element>> presenter : presenters) {
      matchResult = presenter.getTokenRegExp().exec(event.getValue());
      if (matchResult != null) {
        matchingPresenter = presenter;
        break;
      }
    }
    if (matchingPresenter == null) {
      LOG.warning("Not presenter found for token \"" + event.getValue() + "\".");
      return;
    }
    try {
      if (currentPresenter != matchingPresenter) {
        hideCurrentPresenter();
        currentPresenter = matchingPresenter;
        rootElement.appendChild(currentPresenter.getView().getRootElement());
        if (!initializedPresenters.contains(currentPresenter)) {
          try {
            currentPresenter.onViewLoaded();
          } catch (Exception e) {
            LOG.log(Level.SEVERE, "Call to Presenter.onViewLoaded() failed.", e);
          }
          initializedPresenters.add(currentPresenter);
        }
      }
      currentPresenter.onShow(matchResult);
    } catch (Exception e) {
      LOG.log(Level.SEVERE, "Call to Presenter.onShow(MatchResult) failed.", e);
    }
  }

  private void hideCurrentPresenter() {
    if (currentPresenter == null) {
      return;
    }
    try {
      currentPresenter.onHide();
    } catch (Exception e) {
      LOG.log(Level.SEVERE, "Call to Presenter.onHide() failed.", e);
    }
    currentPresenter.getView().getRootElement().removeFromParent();
    currentPresenter = null;
  }

}
