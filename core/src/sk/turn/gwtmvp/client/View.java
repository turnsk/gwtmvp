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

import java.util.Collection;
import java.util.Collections;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;

/**
 * Base interface for a view. All views must remain interfaces and extend this interface. The GWT
 * compile-time generator expects a .html file with a same name as the view class, e.g. for
 * HomeView.java there must also be a HomeView.html file in the same package.
 * <p>
 * The .html file must have exactly one root element that must match the element class of this
 * interface, e.g. for <code>interface HomeView extends View&lt;DivElement&gt;</code> the root element
 * must be <code>&lt;div&gt;</code>.
 * <p>
 * Every element in the .html that is either being mapped to a {@link HtmlElement} or
 * {@link HtmlHandler} must be marked with a {@code data-mvp-id} attribute, which must be unique
 * within this view. For the connection between {@code data-mvp-id} attribute and the name of the
 * accessor method, see {@link HtmlElement} and/or {@link HtmlHandler}. Note that the
 * {@code data-mvp-id} attribute is removed after the element has been mapped, i.e. you won't be able
 * to read the attribute later.
 * <p>
 * A sample simple implementation may look like following:
 * <p>
 * HelloView.java
 * <pre><code>public interface HelloView extends View&lt;DivElement&gt; {
 *  {@literal @}HtmlElement InputElement getNameInput();
 *  {@literal @}HtmlElement SpanElement getCounter();
 *  {@literal @}HtmlHandler("greetLink") void setGreetHandler(ClickHandler handler);
 *}</code></pre>
 * <p>
 * HelloView.html
 * <pre><code>&lt;div&gt;
 *  &lt;a href="javascript:void(0)" data-mvp-id="greetLink"&gt;Greet&lt;/a&gt;
 *  &lt;input type="text" data-mvp-id="nameInput"/&gt;
 *  for the &lt;span data-mvp-id="counter"&gt;&lt;/span&gt; time!
 *&lt;/div&gt;</code></pre>
 *
 * @param <E> The element class that matches the root element in the corresponding .html file.
 */
public interface View<E extends Element> {
  interface ViewLoadedHandler<E extends Element> {
    void onViewLoaded(E rootElement);
  }
  /**
   * An empty view made of a single empty {@code div} element.
   */
  View<Element> EMPTY = new View<Element>() {
    private Element root;
    @Override
    public void loadView(ViewLoadedHandler<Element> viewLoadedHandler) {
      root = Document.get().createDivElement();
      viewLoadedHandler.onViewLoaded(root);
    }
    @Override
    public Element getRootElement() { return root; }
    @Override
    public <E2 extends Element> E2 getElement(String mvpId) { return null; }
    @Override
    public <C extends Control<?>> Collection<C> getControls() { return Collections.emptyList(); }
  };

  /**
   * Tells the view to load itself (root element). This may be an asynchronous operation, 
   * in either case the {@code ViewLoadedHandler} must be called when the root element is ready.
   * When used within {@code Mvp} it is guaranteed that this method will be called only once.
   * 
   * @param viewLoadedHandler The handler to receive the call when the view is loaded.
   */
  void loadView(ViewLoadedHandler<E> viewLoadedHandler);

  /**
   * Do not implement or override this method, this is automatically done by the compile-time GWT
   * generator. This method parses the .html file and maps all {@code data-mvp-id} attributed
   * elements.
   * 
   * @return The root element of this view.
   */
  E getRootElement();

  /**
   * Tries to find an element with a specific {@code data-mvp-id} and returns it. We strongly encourage 
   * you to define the element in the View, though for one-time element usage this may help code brevity.
   * @param mvpId The ID of the element to find
   * @param <E2> The element class that matches the returned element in the corresponding .html file.
   * @return The found Element or null if no such element exists in the HTML file
   */
  <E2 extends Element> E2 getElement(String mvpId);

  /**
   * Returns a collection of {@link Control}s that are included in this {@code View}.
   * @return The collection of {@link Control}s that are included in this {@code View}, is never {@code null}.
   */
  <C extends Control<?>> Collection<C> getControls();
}
