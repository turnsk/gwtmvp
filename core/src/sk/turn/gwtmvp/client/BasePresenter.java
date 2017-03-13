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
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

import sk.turn.gwtmvp.client.Presenter;
import sk.turn.gwtmvp.client.View;

/**
 * Basic implementation of {@link Presenter} interface. This class is a convenient and preferred way to use {@code Presenter}s. 
 * All you need to do is call the super constructor with proper parameters and override one or more of the event methods: 
 * {@code onViewLoaded()}, {@code onShow(MatchResult)} and/or {@code onHide()}.
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
 *    getView().setGreetHandler(new ClickHandler() {
 *      {@literal @}Override
 *      public void onClick(ClickEvent e) {
 *        Window.alert("Hello " + getView().getNameInput().getValue() + " for the " + 
 *            formatCounter() + " time!");
 *        counter++;
 *        getView().getCounter().setInnerText(formatCounter());
 *      }
 *    });
 *  }
 *
 *  {@literal @}Override
 *  public void onShow(MatchResult matchResult) {
 *    getView().getCounter().setInnerText(formatCounter());
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
public abstract class BasePresenter<V extends View<? extends Element>> implements Presenter<V> {

  private RegExp regExp;
  private final V view;

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
    // Nothing to do here
  }

  @Override
  public void onShow(MatchResult matchResult) {
    // Nothing to do here
  }

  @Override
  public void onHide() {
    // Nothing to do here
  }

  /**
   * Allows to change the history token regular expression at runtime.
   * @param regExp The new regular expression to use with this Presenter.
   */
  public void setTokenRegExp(String regExp) {
    this.regExp = RegExp.compile(regExp);
  }

}
