# Views
We'll create a simple view that will pop-up a value entered into an `<input>` field.

Create a `HelloView.html` that will hold the plain HTML content for our view:
```html
<div>
  <a href="javascript:void(0)" data-mvp-id="greetLink">Greet</a>
  <input type="text" value="John Doe" data-mvp-id="nameInput"/>
</div>
```

Noticed the `data-mvp-id` attributes? That's how we tell the compiler how to bind this HTML onto the following `View` interface:
```java
public interface HelloView extends View<DivElement> {
  @HtmlElement InputElement getNameInput();
  @HtmlHandler("greetLink") void setGreetHandler(ClickHandler handler);
}
```

In order to automatically map HTML elements onto the generated methods, we need to keep the `data-mvp-id` and the method name the same, e.g. `nameInput` element will be mapped onto `getNameInput()` method. If (for some reason) you need to name the method differently, you can set the element ID in the annotation: `@HtmlElement("nameInput") InputElement getName();`. You can also access HTML elements using the `View.getElement(String mvpId)` method without the need to declare them in the interface, although to retain code readability we encourage you to use the mapping as much as possible.

The `@HtmlHandler` annotation allows you to easily bind DOM events onto your handler implementations. In this particular case calling `setGreetHandler()` will allow you to capture all click events on the anchor element, see sample below.

## Event handler binding
Calling many event binding methods may be tiresome and does not help clarity of the code. GWT MVP helps you by automatically binding all annotated methods to respective events. Let's say we have the HTML defined above and want to handle mouse over/out/click events on the anchor link (`greetLink`) and focus events on the input (`nameInput`). The `HelloView` interface will now inherit `HandlerView` and include the class name that defines the handling methods (`HelloPresenter` in this case):
```java
public interface HelloView extends HandlerView<DivElement, HelloPresenter> {
  // We don't need anything here since we're only interested in the events
}
```

The class that wants to bind the events has to define and annotate the handling methods with the `@HtmlHandler` annotation.
```java
public class HelloPresenter {
  public HelloPresenter() {
    HelloView view = (HelloView) GWT.create(HelloView.class));
    view.setHandler(this); // Let the view know that we're handling all the events
  }

  // This method is automatically bound to click events of "greetLink" thanks to HandlerView
  @HtmlHandler("greetLink") // The annotation is saying that the method handles some event for greetLink element
  void onGreetMouseOver(MouseOverEvent event) { // The method name does not matter, the argument defines the kind of event that it handles
    // Put your code here
  }

  @HtmlHandler("greetLink")
  void onGreetMouseOut(MouseOutEvent event) {
    // Put your code here
  }

  @HtmlHandler("greetLink")
  void onGreetClick(ClickEvent event) {
    // Put your code here
  }

  @HtmlHandler("nameInput")
  void onNameFocus(FocusEvent event) {
    // Put your code here
  }
}
```

## Inline view HTML
There may be cases where the view HTML is dead-simple or such that it'd be a waste to create a separate file for it. We've created the @ViewHtml annotation to cover exactly such (be carefull though not to over-use it as it reduces the readability of the HTML):
```java
@ViewHtml("<div><input type=\"text\" value=\"John Doe\" data-mvp-id=\"nameInput\"/></div>")
public interface HelloView extends View<DivElement> {
  @HtmlElement InputElement getNameInput();
}
```

## Async views
By default, the view HTML files are bundled with the compiled javascript code, making the whole package larger, even when not all the views are being used in that particular session. I.e. when a user enters your site he has to wait until all the javascript and all the HTML files are loaded and first then starts seeing some content. GWT MVP allows the view HTML content to be separated from the code and only loading the content when necessary.

You can enable asynchronous views simply by putting the `@AsyncView` annotation to the view interface:
```java
@AsyncView
public interface HelloView extends View<DivElement> {
  ...
}
```

Because it may take a while before the view content is fetched from the server, it may be a nice habit to let the user know that something is going on on the background. The `Mvp` class has methods to set a loader in such cases, e.g.:
```java
  ...
  Loader.register(DOM.findElementById("loaderId"), true);
  new Mvp(Document.get().getBody())
      .setLoader(null) // Tell MVP to use the default loader
      .addPresenter(...)
      ...
      .start();
  ...
```

Should you for some reason need to disable the loader at runtime, use the `Mvp.disableLoader()` method.

## Standalone View usage
Although this library works best as a whole, you can also use the HTML to View binding mechanism separately (without the `Mvp` and `Presenter` parts). Once you have the HTML and View interface set up, here is a sample usage of the view itself:
```java
// Create an instance of our view
final HelloView helloView = GWT.create(HelloView.class);
helloView.loadView(new View.ViewLoadedHandler<DivElement>() {
  @Override
  public void onViewLoaded(DivElement rootElement) {
    // Attach the view into some parent element
    DOM.findElementById("someContainerId").appendChild(rootElement);
    // Work with the view
    helloView.getNameInput().setValue("John Doe");
    helloView.setGreetHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent e) {
        Window.alert("Hello " + hellowView.getNameInput().getValue());
      }
    });
  }
});
```

Note, that you can work with the view methods only after call to `View.loadView()` since that's where the view is loaded and the mapping occurs. Trying to call something on an element (or setting an event handler) before that will most likely result in a `NullPointerException`.
