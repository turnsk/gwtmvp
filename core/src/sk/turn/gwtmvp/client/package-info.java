/**
 * <p>
 * MVP library for GWT, adding a light-weighted, easy-to-use MVP framework, plain HTML-to-Java
 * binding and at the same time taking the complexity off of the built-in Widgets library.
 * </p>
 * 
 * <p>
 * Following assumptions were considered when designing this library:
 * <ul>
 * <li>GWT is a great tool with amazing potential, but at the same time the learning curve is
 * anything but steep</li>
 * <li>We find that one of the most difficult part to comprehend and use effectively is the Widget
 * library</li>
 * <li>Out-of-the box GWT has no MVC/MVP/anything alike framework that would "force" the developers
 * to write clean and manageable code</li>
 * <li>Most people that start with GWT already have HTML/CSS (or even JS?) background and are not
 * scared to death by it</li>
 * </ul>
 * </p>
 * 
 * <p>
 * What this library is trying to provide:
 * <ul>
 * <li>Plain bind-able HTML support within GWT (so that you can write and have full control of the
 * HTML directly and not worry what overhead will a widget generate, see
 * {@link sk.turn.gwtmvp.client.View}, {@link sk.turn.gwtmvp.client.HtmlElement} and
 * {@link sk.turn.gwtmvp.client.HtmlHandler})</li>
 * <li>Light-weight (read no huge code-base and few classes to learn) and easy-to-use (read as
 * little code overhead as possible) MVP framework (see {@link sk.turn.gwtmvp.client.Mvp},
 * {@link sk.turn.gwtmvp.client.Presenter})</li>
 * <li>HTML binding can be used standalone without the MVP part</li>
 * </ul>
 * </p>
 */
package sk.turn.gwtmvp.client;
