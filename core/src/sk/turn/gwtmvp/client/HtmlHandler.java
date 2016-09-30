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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for methods that handle HTML DOM events. The annotation value lists one or more
 * {@code data-gwtid} attributes that the method should handle the events for. The actual method
 * name does not matter. The type of the event is generated from the method parameter type, e.g.
 * 
 * <pre><code>// Registers ClickHandler on an element with data-gwtid="inputName"
 *{@literal @}HtmlHandler("inputName") void setClickHandler(ClickHandler handler);
 *{@literal //} Registers BlurHandler on elements with data-gwtid="inputName" and data-gwtid="inputEmail"
 *{@literal @}HtmlHandler({ "inputName", "inputEmail" }) void setBlurHandlerOnInputs(BlurHandler handler);</code></pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HtmlHandler {
  /**
   * Lists element IDs that the annotated method should handle events for.
   * 
   * @return A list of {@code data-gwtid}s of elements that should register for the event.
   */
  String[] value();
}
