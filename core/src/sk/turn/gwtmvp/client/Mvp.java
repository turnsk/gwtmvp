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

/**
 * The centerpiece of the MVP framework, it manages the {@link Presenter}s and handles GWT history
 * changes. Mvp has one root element which is used as the parent for all the {@link View}s from the
 * {@link Presenter}s.
 * 
 * <p>
 * A typical use-case of this class is
 * <code><pre>{@literal @}Override
 *public void onModuleLoad() {
 *  Mvp mvp = new Mvp(DOM.getElementById("mainContainer"));
 *  mvp.addPresenter(new HelloPresenter((HelloView) GWT.create(HelloView.class)));
 *  mvp.start();
 *}</pre></code>
 * </p>
 * 
 * @author tomas
 */
public class Mvp {

  private static final Logger LOG = Logger.getLogger(Mvp.class.getName());

  private class HistoryHandler implements ValueChangeHandler<String> {
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

  }

  private final Element rootElement;
  private HandlerRegistration historyRegistration;
  private final List<Presenter<? extends View<? extends Element>>> presenters = new ArrayList<>();
  private Presenter<? extends View<? extends Element>> currentPresenter;
  private final Set<Presenter<? extends View<? extends Element>>> initializedPresenters = new HashSet<>();

  /**
   * Constructs a new MVP object with a root container element.
   * 
   * @param rootElement The element that all the views will be attached under.
   */
  public Mvp(Element rootElement) {
    this.rootElement = rootElement;
  }

  /**
   * Returns a list of currently registered {@link Presenter}s. Note that the returned values is a
   * copy of the actual list, i.e. changes made to this list will not be reflected in the Mvp
   * object.
   * 
   * @return A copy of the current {@link Presenter} list.
   */
  public List<Presenter<? extends View<? extends Element>>> getPresenters() {
    return new ArrayList<>(presenters);
  }

  /**
   * Registers a new {@link Presenter}.
   * 
   * @param presenter {@link Presenter} to be managed by this Mvp.
   */
  public void addPresenter(Presenter<? extends View<? extends Element>> presenter) {
    presenters.add(presenter);
  }

  /**
   * Removes a {@link Presenter} from the list of registered {@link Presenter}s. When you remove a
   * currently active {@link Presenter} the {@link View} will remain active until the user navigates
   * away from it.
   * 
   * @param presenter The {@link Presenter} instance to remove.
   * @return True if the {@link Presenter} was removed, false if it wasn't registered.
   */
  public boolean removePresenter(Presenter<? extends View<? extends Element>> presenter) {
    initializedPresenters.remove(presenter);
    return presenters.remove(presenter);
  }

  /**
   * Starts the main handling loop by registering a GWT {@code History} handler. This method will
   * also re-fire the current history state so that the initial presenter is immediately shown.
   */
  public void start() {
    historyRegistration = History.addValueChangeHandler(new HistoryHandler());
    History.fireCurrentHistoryState();
  }

  /**
   * Stops the main handling loop and removes the current {@link Presenter}, if any. The currently
   * registered {@link Presenter} list will remain untouched.
   */
  public void stop() {
    if (historyRegistration != null) {
      historyRegistration.removeHandler();
      historyRegistration = null;
    }
    hideCurrentPresenter();
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
