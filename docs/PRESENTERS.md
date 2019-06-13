# Presenters
Now comes the fun part. We'll create a presenter, that will work with the above view and add it to the MVP framework.

**HelloPresenter.java**
```java
public class HelloPresenter extends BasePresenter<HelloPresenter.HelloView> {
  interface HelloView extends HandlerView<DivElement, HelloPresenter> {
    @HtmlElement InputElement getNameInput();
    // Event handler is directly in HelloPresenter now
  }

  public HelloPresenter() {
    super("^greet(/(.+))?$", (HelloView) GWT.create(HelloView.class));
    getView().setHandler(this); // Let the view know that we're handling all the events
  }

  // This method is called everytime the history token changes and this presenter's regex matches it
  @Override
  public void onShow(String... groups) {
    if (groups[2] != null) {
      getView().getNameInput().setValue(groups[2]);
    }
  }

  // This method is automatically bound to click events of "greetLink" thanks to HandlerView
  @HtmlHandler("greetLink")
  void onGreetClick(ClickEvent event) {
    Window.alert("Hello " + getView().getNameInput().getValue());
  }
}
```
We have defined a class that is (extends) a presenter that presents the `HelloView`. For code brewity we've included the view as an internal interface of the Presenter. We've extended the `BasePresenter` which already has some basic implementation and logic, though if you need more control over you can implement the `Presenter` interface directly.

The view interface extends from `HandlerView` with a generic reference back our `Presenter` as opposed from plain View as it was in the previous section. Although coupling View with a Presenter is a deviation from the MVP principles, in this case it removes the necessity to bind the event handlers manually in `Presenter.onViewLoaded()`. Should you need strict separation of Views (for unit testing or any other reason) extend your view from `View` interface and bind the handlers manually.

Every presenter has its regular expression that is validated against the current GWT history token (if you're not familiar with how GWT history works [read here](http://www.gwtproject.org/doc/latest/DevGuideCodingBasicsHistory.html)), so that the MVP framework can find the best presenter for the current state. In this case, the `HelloPresenter` handles `greet` tokens or ones matching the pattern `greet/(name)`.

The `onShow(String...)` method is called every time the history token changes and this presenter's token regex matches it. The `MatchResult` argument has the regex matching result of the current history token, so if you define groups in your regex, those are already populated in the argument. In most cases this method will fetch some identifiers from the history token and load/show the corresponding data in its view. Here we check whether a name was included in the token and if so populate the input field.

The method `onGreetClick` annotated with `@HtmlHandler` gets called everytime the element with `data-mvp-id="greetLink"` is clicked. Should you need to handle other events, just create annotated methods with appropriate event classes as the parameter, `HandlerView` generator will take care of the rest. Though, don't forget to call `HandlerView.setHandler` method to wire it all up.

**EntryPoint.java**
```java
@Override
public void onModuleLoad() {
  Mvp mvp = new Mvp(Document.get().getBody());
  mvp.addPresenter(new HelloPresenter());
  // Add more presenters here...
  mvp.start();
}
```

Here we just create the MVP instance, pass the root element (parent element that will hold all the views) and add as many presenters as we need. As soon we hit the `mvp.start()` method the wheels start turning and from this point the MVP takes over.

See [SamplesEntryPoint.java](https://github.com/turnsk/gwtmvp/blob/master/samples/src/sk/turn/gwtmvp/samples/client/SamplesEntryPoint.java) and [HelloPresenter.java](https://github.com/turnsk/gwtmvp/blob/master/samples/src/sk/turn/gwtmvp/samples/client/hello/HelloPresenter.java) for the sample source files.
