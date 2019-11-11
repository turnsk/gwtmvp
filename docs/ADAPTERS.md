# View adapters
View adapters come in handy when you need to display repetitive data, most of the time lists or tables. It also has the advantage that is reuses old views so it's also effective and doesn't waste resources. You define which view and how will be used for showing the data.

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
  protected void setViewData(PersonView view, Person item, int position) {
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

## View adapter filters
But just shoving the data into the adapter may not be enough. Real-life situation will expect either (column?) sorting, filtering, paging the data or any other data manipulation for that matter. `ViewAdapter` has a built-in support for filters - customised data manipulation before the items are populated into the views. Some basic filter implementations are provided out-of-the-box such as `PagingFilter`, `SearchFilter`, `SortingFilter` or `ColumnSortingFilter`. Others can be easily written to fit any of your needs by providing an implementation of `ViewAdapter.Filter` interface.

Filters within an adapter can be chained where the result of first filter is passed into the second, etc. See the [Phonebook sample](https://turnsk.github.io/gwtmvp/#phonebook) for a sample implementation.

## View holder adapters
View adapters may be insuffucient in cases where you need to keep data related to the view (e.g. nested view adapter, event registration handlers, etc.). In those cases `ViewHolderAdapter` is the way to go, where you create a `ViewHolder` subclass instance instead of a simple `View` and handle binding and/or keep view-related data in there.

Below is the PersonAdapter sample done using ViewHolderAdapter:

**PersonAdapter.java**
```java
class PersonAdapter extends ViewHolderAdapter<Person, PersonAdapter.PersonViewHolder> {
  interface PersonView extends View<DivElement> {
    @HtmlElement AnchorElement getName();
  }
  public static class PersonViewHolder extends ViewHolderAdapter.ViewHolder<Person, PersonView> {
    public PersonViewHolder() {
      super(GWT.create(PersonView.class));
    }
    @Override
    protected void bind(Person item, int position) {
      view.getName().setInnerText(item.getName());
      view.getName().setHref("person/" + item.getId());
    }
  }
  public PersonAdapter(Element parentElement) {
    super(parentElement);
  }
  @Override
  protected PersonViewHolder createViewHolder() {
    return new PersonViewHolder();
  }
}
```

## Table adapters
Since a very common scenario is to list data in a table and creating a separate view for every type of object may be tedious and flood the project with similar java/HTML files, we've created a `TableRowAdapter` class that takes the load off. It is a `ViewAdapter` in the end, though it creates the table rows at runtime and allows you to format the data anyway you need.
```java
personAdapter = new TableRowAdapter<Person>(getView().getTable(), 2) {
  @Override
  protected Object getCellContent(int row, int column, Person item) {
    switch (column) {
      case 0:
        // Can return either SafeHtml
        return new SafeHtmlBuilder()
            .appendHtmlConstant("<a href=\"person/")
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
  protected void setTableCell(int row, int column, TableCellElement elem, Person item) {
    // You may decide not to call the super-method, in which case you have full control of the cell yourself
    super.setTableCell(row, column, elem, item);
    if (column == 1) {
      // Right-align content in the second column
      elem.getStyle().setTextAlign(Style.TextAlign.RIGHT);
    }
  }
  ...
```
