# GWT MVP
[MVP](https://en.wikipedia.org/wiki/Model-view-presenter) library for [GWT](http://www.gwtproject.org/), adding a light-weighted, easy-to-use MVP framework, plain HTML-to-Java binding and at the same time taking the complexity off of the built-in widgets library.

* [Assumptions and Goals](#assumptions-and-goals)
* [Build Configuration](#build-configuration)
* [Resources](#resources)
* [Views](#views)
* [Presenters](#presenters)
* [Loaders](#loaders)

## Assumptions and Goals
Following assumptions were considered when designing this library:
* GWT is a great tool with amazing potential, but at the same time the learning curve is anything but steep
* We find that one of the most difficult part to comprehend and use effectively is the Widget library
* Out-of-the box GWT has no MVC/MVP/anything alike framework that would "force" the developers to write clean and manageable code
* Most people that start with GWT already have HTML/CSS (or even JS?) background and are not scared to death by it

What this library is trying to provide:
* Plain bind-able HTML support within GWT (so that you can write and have full control of the HTML directly and not worry what overhead will a widget generate)
* Light-weight (read "no huge code-base and few classes to learn") and easy-to-use (read "as little code overhead as possible") MVP framework
* HTML binding can be used standalone without the MVP part

## Build Configuration

### JAR file
1. Download the latest JAR file, copy it to your project's libs directory and add it to your project's classpath
2. Add the following entry to your module .gwt.xml file:
```xml
<inherits name='sk.turn.gwtmvp.GwtMvp'/>
```

### Gradle
1. In the `build.gradle` add the following repository:
```gradle
repositories {
  ...
  maven { url 'https://jitpack.io/' }
}
```
2. Add the dependency:
```gradle
dependencies {
  ...
  providedCompile 'sk.turn:gwtmvp:1.0-rc2'
}
```

## Resources
* [Read Javadoc](https://jitpack.io/sk/turn/gwtmvp/1.0-rc2/javadoc/)
* [Download JAR](https://jitpack.io/sk/turn/gwtmvp/1.0-rc2/gwtmvp-1.0-rc2.jar)

## Views
We'll create a simple view that will pop-up a value entered into an `<input>` field.

Create a `HelloView.html` that will hold the plain HTML content for our view:
```html
<div>
  <a href="javascript:void(0)" data-gwtid="greetLink">Greet</a>
  <input type="text" value="John Doe" data-gwtid="nameInput"/>
  for the <span data-gwtid="counter"></span> time!
</div>
```

Noticed the `data-gwtid` attributes? That's how we tell the compiler how to bind this HTML onto the following `View` interface:
```java
public interface HelloView extends View<DivElement> {
  @HtmlElement InputElement getNameInput();
  @HtmlElement SpanElement getCounter();
  @HtmlHandler("greetLink") void setGreetHandler(ClickHandler handler);
}
```

In order to automatically map HTML elements onto the generated methods, we need to keep the `data-gwtid` and the method name the same, e.g. `nameInput` element will be mapped onto `getNameInput()` method. If (for some reason) you need to name the method differently, you can set the element ID in the annotation: `@HtmlElement("nameInput") InputElement getName();`.

The `@HtmlHandler` annotation allows you to easily bind DOM events onto your handler implementations. In this particular case calling `setGreetHandler()` will allow you to capture all click events on the anchor element, see sample below.

That's it! That's all that's necessary to use the library HTML to GWT binding mechanism. To use this particular view (without the great MVP that will follow), you may write:
```java
// Create an instance of our view
final HelloView helloView = GWT.create(HelloView.class);
// Attach the view into some parent element
DOM.findElementById("someContainerId").appendChild(helloView.getRootElement());
// Work with the view
helloView.getNameInput().setValue("John Doe");
helloView.setGreetHandler(new ClickHandler() {
  @Override
  public void onClick(ClickEvent e) {
    Window.alert("Hello " + hellowView.getNameInput().getValue());
  }
});
```

Note that you can work with the view method only after first call to `View.getRootElement()` since that's where all the mapping occurs. Trying to call something on a method (or setting an event handler) before that will result in an `NullPointerException`.

## Presenters
Now comes the fun part. We'll create a presenter, that will work with the above view and add it to the MVP framework.

**HelloPresenter.java**
```java
public class HelloPresenter implements Presenter<HelloView> {
  private final RegExp tokenRegExp = RegExp.compile("^(|greet/(.*))$");
  private final HelloView view;

  public HelloPresenter(HelloView view) {
    this.view = view;
  }

  public RegExp getTokenRegExp() {
    return tokenRegExp; // It's recommended to cache the RegExp since this method may be called many times
  }

  public HelloView getView() {
    return view;
  }

  // One-time view initialization shall go here
  public void onViewLoaded() {
    view.setGreetHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent e) {
        Window.alert("Hello " + view.getNameInput().getValue());
      }
    });
  }

  public void onShow(MatchResult matchResult) {
    if (matchResult.getGroupCount() >= 3 && matchResult.getGroup(2) != null) {
      view.getNameInput().setValue(matchResult.getGroup(2));
    }
  }

  public void onHide() {
    // Nothing to do here in this sample
  }
}
```
We have defined a class that is (implements) a presenter that presents the `HelloView`. Every presenter has its regular expression that is validated against the current GWT history token (if you're not familier with how GWT history works [read here](http://www.gwtproject.org/doc/latest/DevGuideCodingBasicsHistory.html)), so that the MVP framework can find the best presenter for the current state.

In this case, the `HelloPresenter` handles either empty tokens (default) or ones that start with `greet/`. Since the `getTokenRegExp()` method may be called at every change of the history token, it's a nice behavior to keep the regex and not to compile it every time.

`getView()` is pretty self-explanatory, the next interresting bit are the `on...()` methods.

`onViewLoaded()` serves as a one-time initialization, in most cases for the view. As stated above, the view fields and methods are only usable once the `View.getRootElement()` method has been called, and since `onShow()` may be called many times, this is the right place for any view initialization. It's also recommended that the presenter itself does one-time initialization here (as opposed to contructor), we might avoid the initialization completely in case the presenter is never shown.

The `onShow(MatchResult)` method is called every time the history token changes and this presenter's token regex matches it. The `MatchResult` argument has the regex matching result of the current history token, so if you define groups in your regex, those are already populated in the argument. In most cases this method will fetch some identifiers from the history token and load/show the corresponding data in its view.

The last `onHide()` method is called by the MVP framework everytime the user is about to be navigated away from this presenter. Note, that this method will not get called if the same presenter (althou different history token) is shown; in this case the `onShow()` method will be called consecutively twice.

**EntryPoint.java**
```java
@Override
public void onModuleLoad() {
  Mvp mvp = new Mvp(Document.get().getBody());
  mvp.addPresenter(new HelloPresenter((HelloView) GWT.create(HelloView.class)));
  mvp.start();
}
```

Here we just create the MVP instance, pass the root element (parent element that will hold all the views) and add as many presenters as we need. As soon we hit the `mvp.start()` method the wheels start turning and from this point the MVP takes over.

## Loaders
This is an optional helper class to assist with a common task -- showing a loading indicator while one or more background tasks are in progress. The `Loader` class allows you to register one or more HTML elements that represent loaders in your application and you can show/hide them anywhere within your app.

There are two kinds of loaders:
1. **Non-counted** loader doesn't care how many times you call `show()` method, the first call to `hide()` hides it
2. whereas **counted** loader will not hide unless you call `hide()` the same number of times as you did call `show()`.

### Sample
```java
// Register a counted loader we will use later
Loader.register(DOM.findElementById("loaderId"), true);
...
// Later, before making a server call
Loader.show();
...
// When the call has finished (either successfully or not)
Loader.hide();
```

You also may use more than one loader, in such case use the methods that get a tag (identifier) as the first parameter.
