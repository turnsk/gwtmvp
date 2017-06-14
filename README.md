# GWT MVP
[MVP](https://en.wikipedia.org/wiki/Model-view-presenter) library for [GWT](http://www.gwtproject.org/), adding a light-weighted, easy-to-use MVP framework, plain HTML-to-Java binding and at the same time taking the complexity off of the built-in widgets library.

* [GWT MVP Showcase](https://turnsk.github.io/gwtmvp/)
* [Read Javadoc](https://jitpack.io/sk/turn/gwtmvp/1.4/javadoc/)
* [Download JAR](https://jitpack.io/sk/turn/gwtmvp/1.4/gwtmvp-1.4.jar)

## Contents
* [Assumptions and Goals](#assumptions-and-goals)
* [Build Configuration](#build-configuration)
* [Views](#views)
* [Presenters](#presenters)
* [Standalone View usage](#standalone-view-usage)
* [Loaders](#loaders)
* [View adapters](#view-adapters)
* [Table adapters](#table-adapters)
* [Internationalization](#internationalization)
* [Async views](#async-views)

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
  providedCompile 'sk.turn:gwtmvp:1.4'
}
```

## Views
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

We'll get on with creating a Presenter for this view.

## Presenters
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

## Loaders
This is an optional helper class to assist with a common task -- showing a loading indicator while one or more background tasks are in progress. The `Loader` class allows you to register one or more HTML elements that represent loaders in your application and you can show/hide them anywhere within your app.

There are two kinds of loaders:
1. **Non-counted** loader doesn't care how many times you call `show()` method, the first call to `hide()` hides it
2. whereas **counted** loader will not hide unless you call `hide()` the same number of times as you did call `show()`.

### Sample
```java
// Register a counted loader we will use later
Loader.register(DOM.findElementById("loaderId"), true /* false for non-counted loader */);
...
// Later, before making a server call
Loader.show();
...
// When the call has finished (either successfully or not)
Loader.hide();
```

You also may use more than one loader, in such case use the methods that get a tag (identifier) as the first parameter.

### LoaderAsyncCallback
`LoaderAsyncCallback` is a helper `AsyncCallback` implementation that will display a loader prior making the async request and then hide it when a response (either successful or an exception) is received. The constructor can optionally take an identifier of a registered loader to use, otherwise it'll use the global loader. A sample usage of this class:
```java
customServiceAsync.getSomeData(param1, param2, new LoaderAsyncCallback<String>() {
  @Override
  public void onSuccess(String result) {
    super.onSuccess(result);
    // Handle the result
  }
  @Override
  public void onFailure(Throwable caught) {
    super.onFailure(result);
    // Handle the excpetion
  }
});
```

## View adapters
View adapters come in handy when you need to display repetitive data, most of the time lists or tables. It also had the advantage that is reuses old views so it's also effective and doesn't waste resources. You define which view and how will be used for showing the data.

**PersonView.html**
```html
<div>
  <a href="javascript:void(0)" style="float:right;" data-mvp-id="delete">&times;</a>
  <a data-mvp-id="name"></a>
</div>
```

**PersonAdapter.java**
```java
class PersonAdapter extends ViewAdapter<PersonAdapter.PersonView, Person> {
  interface PersonView extends HandlerView<DivElement, PersonAdapter> {
    @HtmlElement AnchorElement getName();
  }
  public PersonAdapter(Element parentElement) {
    super(parentElement);
  }
  @Override
  protected PersonView createView() {
    PersonView view = GWT.create(PersonView.class);
    view.setHandler(this);
    return view;
  }
  @Override
  protected void setViewData(PersonView view, Person item) {
    view.getName().setInnerText(item.getName());
    view.getName().setHref("person/" + item.getId());
  }
  @HtmlHandler("delete")
  void onRemoveClicked(ClickEvent event) {
    int index = getItemIndexFromEvent(event);
    Person person = getItemFromEvent(event);
    // Remove either based on index or person object directly
    // Update the items in the adapter
  }
}
```

We first define the interface of view representing each of the adapter items. A constructor that defines the parent of all item views has to be retained.

Then we implement methods that
1. create an instance of the view (only called when a previously created view cannot be reused)
2. update the view to match the data object (this method may be called for a single view many times with different objects)

The last event handler shows how to get either an item index (`ViewAdapter.getItemIndexFromEvent`) or the item object itself (`ViewAdapter.getItemFromEvent`) from any event object. There's also method `ViewAdapter.getItemViewFromEvent` that'll return the view associated with the event.

To use a view adapter in a Presenter, you'd create an instance with a parent element and set the data as needed:
```java
public class SomePresenter extends BasePresenter<SomeView> {
  ...
  private PersonAdapter adapter;
  ...
  @Override
  public void onViewLoaded() {
    adapter = new PersonAdapter(getView().getPersonContainer());
    ...
  }
  @Override
  public void onShow(String... groups) {
    List<Person> people;
    // Load the person list
    adapter.setItems(people);
  }
}
```

## Table adapters
Since a very common scenario is to list data in a table and creating a separate view for every type of object may be tedious and flood the project with similar java/HTML files, we've created a `TableRowAdapter` class that takes the load off. It is a `ViewAdapter` in the end, though it creates the table rows at runtime and allows you to format the data anyway you need.
```java
personAdapter = new TableRowAdapter<Person>(getView().getTable(), 2) {
  @Override
  protected Object getCellContent(int column, Person item) {
    switch (column) {
      case 0:
        // Can return either SafeHtml
        return new SafeHtmlBuilder()
            .appendHtmlConstant("<a href=\"#person/")
            .append(item.id)
            .appendHtmlConstant("\">")
            .appendEscaped(item.getName())
            .appendHtmlConstant("</a>")
            .toSafeHtml();
      case 1:
        // ...or plain String
        return item.getEmail();
    }
    return null;
  }
};
```

Here we've created a table-row adapter with a parent table element and each row will have two columns. `TableRowAdapter.getCellContent` is called for every column and every object letting you to choose the appropriate text or SafeHtml values.

There are cases where you may need to customize the cell even further, in that case override `TableRowAdapter.setTableCell` method:
```java
  ...
  @Override
  protected void setTableCell(int column, TableCellElement elem, Person item) {
    // You may decide not to call the super-method, in which case you have full control of the cell yourself
    super.setTableCell(column, elem, item);
    if (column == 1) {
      // Right-align content in the second column
      elem.getStyle().setTextAlign(Style.TextAlign.RIGHT);
    }
  }
  ...
```

## Internationalization
GWT MVP supports GWT's built-in internationalization tools. Suppose you have the following dictionary interface:
```java
package com.sample.project;

public interface Dictionary extends com.google.gwt.i18n.client.Constants {
  @DefaultStringValue("Hello world!")
  String helloWorld();
  @DefaultStringValue("Hello <b>HTML</b> world!")
  SafeHtml helloHtmlWorld();
}
```

In order to use the dictionary values in a view HTML file, first define the dictionary class in the root element's attribute `data-mvp-dict` and then use placeholders in the form `{mvpDict.identifier}` anywhere in the HTML file, e.g.
```html
<div data-mvp-dict="com.sample.project.Dictionary">
  <p title="{mvpDict.helloWorld}">{mvpDict.helloHtmlWorld}</p>
</div>
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