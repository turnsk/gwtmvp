# Loaders
This is an optional helper class to assist with a common task -- showing a loading indicator while one or more background tasks are in progress. The `Loader` class allows you to register one or more HTML elements that represent loaders in your application and you can show/hide them anywhere within your app.

There are two kinds of loaders:
1. **Non-counted** loader doesn't care how many times you call `show()` method, the first call to `hide()` hides it
2. whereas **counted** loader will not hide unless you call `hide()` the same number of times as you did call `show()`.

## Sample
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

## LoaderAsyncCallback
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
