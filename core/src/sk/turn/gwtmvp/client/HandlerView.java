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

/**
 * A {@link View} sub-interface that allows direct event handler method mapping.
 * <p>
 * In plain {@code View} interface you had to define a setter for event handler and later call that 
 * to bind a handler implementation:
 * <pre><code>{@literal @}HtmlHandler("") void setLinkClickHandler(ClickHandler handler);
 *...
 *view.setLinkClickHandler(new ClickHandler() {
 *  {@literal @}Override
 *  public void onClick(ClickEvent event) {
 *    // Implementation here
 *  }
 *});</code></pre>
 * <p>
 * In {@code HandlerView} you define the class that will implement the handler methods for this view
 * <pre><code>interface MyView extends HandlerView&lt;DivElement, MyEventHandler&gt;</code></pre>
 * Add the handlers directly in the class implementation
 * <pre><code>class MyEventHandler { // This may as well (and often will) be the Presenter itself
 *  ...
 *  {@literal @}HtmlHandler("link") // Here goes the data-gwtid attribute(s) for which to handle the event
 *  void onLinkClicked(ClickEvent event) { // The method name does not matter, the only one parameter must be a DomEvent&lt;?&gt; class.
 *    // Implementation here
 *  }
 *}</code></pre>
 * And let the view know which instance to use
 * <pre><code>myView.setHandler(myEventHandler);</code></pre>
 * <p>
 * Although this View-Presenter coupling is against the strict MVP pattern, it helps code readability and to reduce verbosity.
 * If you plan to have proper unit testing or need to decouple View-Presenter for any other reason, 
 * either use the plain {@link View} interface or pull the handler methods out in a separate interface.
 * 
 * @param <E> The HTML element that represents the root node of the view.
 * @param <H> The class that has the {@literal @}HtmlHandler annotated handler methods.
 */
public interface HandlerView<E extends Element, H> extends View<E> {
  /**
   * Sets the instance of event handler class. Calling this method multiple times only keeps the last instance.
   * This method can be considered "cheap" in terms of processing and may be called even before {@link View#getRootElement()} was called.
   * @param handler The event handler instance.
   */
  void setHandler(H handler);
}
