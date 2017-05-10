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

import com.google.gwt.dom.client.Element;
import com.google.gwt.regexp.shared.RegExp;

/**
 * Basic implementation of {@link Presenter} interface. This class is a convenient and preferred way to use {@code Presenter}s. 
 * All you need to do is call the super constructor with proper parameters and override one or more of the event methods: 
 * {@code onViewLoaded()}, {@code onShow(String...)} and/or {@code onHide()}.
 * <p>
 * An implementation may look like following:
 * <pre><code>public class HelloPresenter extends BasePresenter&lt;HelloView&gt; {
 *  private int counter = 1;
 *
 *  public HelloPresenter(HelloView view) {
 *    super("^hello$", view);
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
 *  public void onShow(String... groups) {
 *    view.getCounter().setInnerText(formatCounter());
 *  }
 *
 *  private String formatCounter() {
 *    return counter + (counter % 10 == 1 &amp;&amp; (counter / 10) % 10 != 1 ? "st" : 
 *        counter % 10 == 2 &amp;&amp; (counter / 10) % 10 != 1 ? "nd" : "th");
 *  }
 *}</code></pre>
 * 
 * @param <V> The {@link View} class that this presenter works with.
 */
public class BasePresenter<V extends View<? extends Element>> implements Presenter<V> {

  protected final V view;
  private RegExp regExp;
  private String[] currentGroups;

  public BasePresenter(String regExp, V view) {
    setTokenRegExp(regExp);
    this.view = view;
  }

  @Override
  public RegExp getTokenRegExp() {
    return regExp;
  }

  @Override
  public V getView() {
    return view;
  }

  @Override
  public void onViewLoaded() {
    // Empty implementation
  }

  @Override
  public void onShow(String... groups) {
    // Empty implementation
  }

  @Override
  public void onHide() {
    // Empty implementation
  }

  /**
   * Returns the current visibility state of this presenter.
   * @return True if this presenter is currently visible (active), false otherwise.
   */
  public boolean isVisible() {
    return currentGroups != null;
  }

  /**
   * Returns the current history token groups that this presenter is showing or null if the presenter is hidden (inactive).
   * @return Current history token matched groups or null if presenter is hidden (inactive).
   */
  public String[] getCurrentGroups() {
    return currentGroups;
  }

  /**
   * Allows to change the history token regular expression at runtime.
   * @param regExp The new regular expression to use with this Presenter.
   */
  public void setTokenRegExp(String regExp) {
    this.regExp = (regExp != null ? RegExp.compile(regExp) : null);
  }

  void onPresenterShown(String... groups) {
    currentGroups = groups;
    onShow(groups);
  }

  void onPresenterHidden() {
    currentGroups = null;
    onHide();
  }

}
