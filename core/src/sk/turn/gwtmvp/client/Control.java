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
package sk.turn.gwtmvp.client;

import com.google.gwt.dom.client.Element;

/**
 * Base class for all controls - reusable UI components with own login that can be included in other views.
 * A control is automatically created and injected into the view HTML as part of the view-loading.
 * <p>
 * In HTML you add a control with a {@code &lt;object&gt;} element with {@code data-mvp-id} attribute as follows:
 * <pre><code>&lt;div&gt;
 *  ...
 *    &lt;object data-mvp-id="randomControl"&gt;&lt;/object&gt;
 *  ...
 *&lt;/div&gt;</code></pre>
 * 
 * Then in the {@code View} interface annotate the control in the same way as you would an element:
 * <pre><code>{@literal @}Override RandomControl getRandomControl();</code></pre>
 * <p>
 * A same implementation of the {@code RandomControl} may be following:
 * <pre><code>class RandomControl extends Control&lt;RandomControl.RandomView&gt; {
 *  {@literal @}ViewHtml("&lt;span&gt;&lt;/span&gt;")
 *  interface RandomView { }
 *  
 *  {@literal @}Override
 *  public void onShow() {
 *    view.getRootElement().setInnerText(String.valueOf((int) (Math.random() * 100)));
 *  }
 *}</code></pre>
 * 
 * @see <a href="https://turnsk.github.io/gwtmvp/controls" target="_blank">Sample Control showcase</a>
 * 
 * @param <V> The {@link View} class that this control is represented with.
 */
public class Control<V extends View<? extends Element>> {
  protected V view;

  /**
   * Every control has to have an empty constructor or no constructor defined.
   */
  public Control() {
  }

  /**
   * Alternative constructor that also initializes the view. Otherwise the controls view will be automatically created by 
   * the containing view base on the generic {@code V} declaration.
   * @param view The view instance to use with this control
   */
  protected Control(V view) {
    this.view = view;
  }

  /**
   * The controls view
   * @return The controls view
   */
  public V getView() {
    return view;
  }

  /**
   * This method is internal and should not be used.
   * @param view View to set
   */
  public void setView(V view) {
    this.view = view;
  }

  /**
   * Callback method that is called when the view is loaded and ready to be used. This will only be called once.
   */
  public void onViewLoaded() {
    // Empty implementation
  }

  /**
   * Callback method that is called after the presenter containing this control is shown.
   */
  public void onShow() {
    // Empty implementation
  }

  /**
   * Callback method that is called right before the presenter containing this control is about to be hidden.
   */
  public void onHide() {
    // Empty implementation
  }
}
