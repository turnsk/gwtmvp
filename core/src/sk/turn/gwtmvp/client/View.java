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

/**
 * Base interface for a view. All views must remain interfaces and extend this interface. The GWT
 * compile-time generator expects a .html file with a same name as the view class, e.g. for
 * HomeView.java there must also be a HomeView.html file in the same package.
 * 
 * <p>
 * The .html file must have exactly one root element that must match the element class of this
 * interface, e.g. for <code>interface HomeView extends View&lt;DivElement></code> the root element
 * must be <code>&lt;div></code>.
 * </p>
 * 
 * <p>
 * Every element in the .html that is either being mapped to a {@link HtmlElement} or
 * {@link HtmlHandler} must be marked with a {@code data-gwtid} attribute, which must be unique
 * within this view. For the connection between {@code data-gwtid} attribute and the name of the
 * accessor method, see {@link HtmlElement} and/or {@link HtmlHandler}. Note that the
 * {@code data-gwtid} attribute is removed after the element has been mapped, i.e. you won't be able
 * to read the attribute later.
 * </p>
 * 
 * <p>
 * A sample simple implementation may look like following:
 * </p>
 * 
 * <p>
 * HelloView.java
 * <code><pre>public interface HelloView extends View&lt;DivElement> {
 *  {@literal @}HtmlElement InputElement getNameInput();
 *  {@literal @}HtmlElement SpanElement getCounter();
 *  {@literal @}HtmlHandler("greetLink") void setGreetHandler(ClickHandler handler);
 *}</pre></code>
 * </p>
 * 
 * <p>
 * HelloView.html
 * <code><pre>&lt;div>
 *  &lt;a href="javascript:void(0)" data-gwtid="greetLink">Greet&lt;/a>
 *  &lt;input type="text" data-gwtid="nameInput"/>
 *  for the &lt;span data-gwtid="counter">&lt;/span> time!
 *&lt;/div></pre></code>
 * </p>
 * 
 * @author tomas
 *
 * @param <E> The element class that matches the root element in the corresponding .html file.
 */
public interface View<E extends Element> {
  /**
   * Do not implement or override this method, this is automatically done by the compile-time GWT
   * generator. This method parses the .html file and maps all {@code data-gwtid} attributed
   * elements.
   * 
   * @return The root element of this view.
   */
  E getRootElement();
}
