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

import com.google.gwt.dom.client.Element;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

/**
 * Base interface for presenters. Every presenter to be registered by {@link Mvp} must implement
 * this interface.
 * <p>
 * A sample simple implementation may look like following:
 * <pre><code>public class HelloPresenter implements Presenter&lt;HelloView&gt; {
 *  private final RegExp tokenRegExp = RegExp.compile("^hello$");
 *  private final HelloView view;
 *  private int counter = 1;
 *
 *  public HelloPresenter(HelloView view) {
 *    this.view = view;
 *  }
 *
 *  {@literal @}Override
 *  public RegExp getTokenRegExp() {
 *    return tokenRegExp;
 *  }
 *
 *  {@literal @}Override
 *  public HelloView getView() {
 *    return view;
 *  }
 *
 *  {@literal @}Override
 *  public void onViewLoaded() {
 *    view.setGreetHandler(new ClickHandler() {
 *      {@literal @}Override
 *      public void onClick(ClickEvent e) {
 *        Window.alert("Hello " + view.getNameInput().getValue() + " for the " + 
 *            formatCounter() + " time!");
 *        counter++;
 *        view.getCounter().setInnerText(formatCounter());
 *      }
 *    });
 *  }
 *
 *  {@literal @}Override
 *  public void onShow(MatchResult matchResult) {
 *    view.getCounter().setInnerText(formatCounter());
 *  }
 *
 *  {@literal @}Override
 *  public void onHide() {
 *    // Nothing to do here in this sample
 *  }
 *
 *  private String formatCounter() {
 *    return counter + (counter % 10 == 1 &amp;&amp; (counter / 10) % 10 != 1 ? "st" : 
 *        counter % 10 == 2 &amp;&amp; (counter / 10) % 10 != 1 ? "nd" : 
 *        counter % 10 == 3 &amp;&amp; (counter / 10) % 10 != 1 ? "rd" : "th");
 *  }
 *}</code></pre>
 *
 * @param <V> The {@link View} class that this presenter works with.
 */
public interface Presenter<V extends View<? extends Element>> {

  /**
   * Regular expression that the {@link Mvp} checks when looking for a suitable presenter that
   * matches the current history token. Since this method may be called with every history token
   * change, it is a good practice to keep the reg-exp in a variable.
   * 
   * <p>
   * The implementation may change the reg-exp in time, thus allowing to respond differently at
   * runtime. The value that is matched against the reg-exp does not contain the leading hash-tag.
   * </p>
   * 
   * @return Regular expression that is used to check the current history token.
   */
  RegExp getTokenRegExp();

  /**
   * Returns the {@link View} that represents this presenter. Unless required by the implementation,
   * it is strongly recommended to keep one instance of the view and not create a new instance every
   * time this method is called. Do not do any {@link View} initialization in this method, as the
   * {@link View} fields are not populated at this point, see {@link #onViewLoaded()} for one-time
   * {@link View} initialization.
   * 
   * @return {@link View} that represents this presenter.
   */
  V getView();

  /**
   * Called for one-time initialization of this presenter, in most cases the related {@link View}.
   * At the time of calling this method the {@link View} is already attached to the browser DOM.
   * This method is called only once in the lifetime of the presenter and immediately before the
   * {@code #onShow(MatchResult)} method is called.
   */
  void onViewLoaded();

  /**
   * Called when this presenter is being shown to the user. At the time this method is called the
   * {@link View} is already attached to the browser DOM. When the history token changes and the
   * same presenter matches the token, this method is called without de- and re-attaching the
   * {@link View}.
   * 
   * @param matchResult The match result of the regular expression validation, see
   *          {@link #getTokenRegExp()}.
   */
  void onShow(MatchResult matchResult);

  /**
   * Called when this presenter is about to be hidden. At the time of calling this method the
   * {@link View} is still attached to the DOM and will be detached by the {@link Mvp} immediately
   * upon returning from this method.
   */
  void onHide();

}
