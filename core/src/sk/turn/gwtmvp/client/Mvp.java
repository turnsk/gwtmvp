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
import com.google.gwt.regexp.shared.RegExp;
import sk.turn.gwtmvp.client.history.History;

/**
 * The centerpiece of the MVP framework, it manages the {@link Presenter}s and handles GWT history
 * changes. Mvp has one root element which is used as the parent for all the {@link View}s from the
 * {@link Presenter}s.
 * <p>
 * A typical use-case of this class is
 * <pre><code>{@literal @}Override
 *public void onModuleLoad() {
 *  Mvp mvp = new Mvp(DOM.getElementById("mainContainer"));
 *  mvp.addPresenter(new HelloPresenter((HelloView) GWT.create(HelloView.class)));
 *  mvp.start();
 *}</code></pre>
 */
public class Mvp {

  private static final Logger LOG = Logger.getLogger(Mvp.class.getName());

  private final Element rootElement;
  private HandlerRegistration historyRegistration;
  private final List<Presenter<? extends View<? extends Element>>> presenters = new ArrayList<>();
  private Presenter<? extends View<? extends Element>> currentPresenter;
  private final Set<Presenter<? extends View<? extends Element>>> initializedPresenters = new HashSet<>();
  private boolean useLoader = false;
  private String loaderId = null;

  /**
   * Constructs a new MVP object with a root container element.
   * 
   * @param rootElement The element that all the views will be attached under.
   */
  public Mvp(Element rootElement) {
    this.rootElement = rootElement;
  }

  /**
   * Enables a specific loader to show when loading views asynchronously. Note, that {@code null} is a valid value.
   * The loader does not need to be initialized at the time of calling this method, but should be before calling {@code start()}.
   * 
   * @param loaderId
   * @return This object for easy method chaining.
   */
  public Mvp setLoader(String loaderId) {
    useLoader = true;
    this.loaderId = loaderId;
    return this;
  }

  /**
   * Disables the loader when loading async views.
   * 
   * @return This object for easy method chaining.
   */
  public Mvp disableLoader() {
    useLoader = false;
    return this;
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
   * @return This object for easy method chaining.
   */
  public Mvp addPresenter(Presenter<? extends View<? extends Element>> presenter) {
    presenters.add(presenter);
    return this;
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
   * 
   * @return This object for easy method chaining.
   */
  public Mvp start() {
    historyRegistration = History.addValueChangeHandler(new ValueChangeHandler<String>() {
      @Override
      public void onValueChange(ValueChangeEvent<String> event) {
        showPresenter(event.getValue());
      }
    });
    History.fireCurrentHistoryState();
    return this;
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
    setCurrentPresenter(null);
  }

  @SuppressWarnings("unchecked")
  protected void showPresenter(String token) {
    Presenter<? extends View<? extends Element>> matchingPresenter = null;
    MatchResult matchResult = null;
    for (Presenter<? extends View<? extends Element>> presenter : presenters) {
      RegExp regExp = presenter.getTokenRegExp();
      if (regExp == null) {
        continue;
      }
      matchResult = regExp.exec(token);
      if (matchResult != null) {
        matchingPresenter = presenter;
        break;
      }
    }
    if (matchingPresenter == null) {
      LOG.warning("No presenter found for token \"" + token + "\".");
      return;
    }
    final String[] groups = new String[matchResult.getGroupCount()];
    for (int i = 0; i < matchResult.getGroupCount(); i++) {
      groups[i] = matchResult.getGroup(i);
    }
    setCurrentPresenter((Presenter<? extends View<Element>>) matchingPresenter, groups);
  }

  protected void attachView(View<? extends Element> view) {
    rootElement.appendChild(view.getRootElement());
  }

  protected void detachView(View<? extends Element> view) {
    view.getRootElement().removeFromParent();
  }

  protected Presenter<? extends View<? extends Element>> getCurrentPresenter() {
    return currentPresenter;
  }

  protected <E extends Element> void setCurrentPresenter(Presenter<? extends View<E>> presenter, final String... groups) {
    if (presenter == null) {
      if (currentPresenter == null) {
        return;
      }
      invokeOnPresenterHide();
      detachView(currentPresenter.getView());
      currentPresenter = null;
    } else {
      if (currentPresenter != presenter) {
        setCurrentPresenter(null);
        currentPresenter = presenter;
        if (!initializedPresenters.contains(currentPresenter)) {
          final Presenter<? extends View<? extends Element>> presenterLoading = currentPresenter;
          if (useLoader) {
            Loader.show(loaderId);
          }
          presenter.getView().loadView(new View.ViewLoadedHandler<E>() {
            @Override
            public void onViewLoaded(E rootElement) {
              if (useLoader) {
                Loader.hide(loaderId);
              }
              // Stop if failed to load the view or presenter has changed in the meantime
              if (rootElement == null || presenterLoading != currentPresenter) {
                return;
              }
              attachView(currentPresenter.getView());
              try {
                currentPresenter.onViewLoaded();
              } catch (Exception e) {
                LOG.log(Level.SEVERE, "Call to Presenter.onViewLoaded() failed.", e);
              }
              invokeOnPresenterShow(groups);
            }
          });
          initializedPresenters.add(currentPresenter);
          return; // We'll continue in the handler callback method
        } else if (currentPresenter.getView().getRootElement() != null) {
          attachView(currentPresenter.getView());
        }
      }
      invokeOnPresenterShow(groups);
    }
  }

  private void invokeOnPresenterShow(String[] groups) {
    if (currentPresenter.getView().getRootElement() == null) {
      // Root element of the view does not exist - probably still being loaded asynchronously
      return;
    }
    try {
      if (currentPresenter instanceof BasePresenter) {
        ((BasePresenter<?>) currentPresenter).onPresenterShown(groups);
      } else {
        currentPresenter.onShow(groups);
      }
    } catch (Exception e) {
      LOG.log(Level.SEVERE, "Call to Presenter.onShow(String...) failed.", e);
    }
    // Call onShow on all controls
    for (Control<? extends View<? extends Element>> ctrl : currentPresenter.getView().getControls()) {
      try {
        ctrl.onShow();
      } catch (Exception e) {
        LOG.log(Level.SEVERE, "Call to Control.onShow() failed.", e);
      }
    }
  }

  private void invokeOnPresenterHide() {
    // Call onHide on all controls
    for (Control<? extends View<? extends Element>> ctrl : currentPresenter.getView().getControls()) {
      try {
        ctrl.onHide();
      } catch (Exception e) {
        LOG.log(Level.SEVERE, "Call to Control.onHide() failed.", e);
      }
    }
    // Call onHide on the current presenter
    try {
      if (currentPresenter instanceof BasePresenter) {
        ((BasePresenter<?>) currentPresenter).onPresenterHidden();
      } else {
        currentPresenter.onHide();
      }
    } catch (Exception e) {
      LOG.log(Level.SEVERE, "Call to Presenter.onHide() failed.", e);
    }
  }

}
