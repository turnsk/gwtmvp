package sk.turn.gwtmvp.samples.client.adapter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class PhonebookPresenter_PhonebookViewImpl_Resources_default_InlineClientBundleGenerator implements sk.turn.gwtmvp.samples.client.adapter.PhonebookPresenter_PhonebookViewImpl.Resources {
  private static PhonebookPresenter_PhonebookViewImpl_Resources_default_InlineClientBundleGenerator _instance0 = new PhonebookPresenter_PhonebookViewImpl_Resources_default_InlineClientBundleGenerator();
  private void htmlContentInitializer() {
    htmlContent = new com.google.gwt.resources.client.TextResource() {
      // file:/Users/Zajko/Documents/GitHub/gwtmvp/samples/src/main/java/sk/turn/gwtmvp/samples/client/adapter/PhonebookView.html
      public String getText() {
        return "<div class=\"row\">\n  <div class=\"col-md-8\"><h1>Phonebook</h1></div>\n  <div class=\"col-md-4\"><input class=\"form-control\" placeholder=\"Search...\" style=\"margin-top: 20px;\" data-mvp-id=\"searchInput\"/></div>\n  <div class=\"col-md-12\">\n    <table class=\"table table-striped\" data-mvp-id=\"table\">\n      <tr>\n        <th><a href=\"javascript:;\" data-mvp-id=\"nameHeader\">Name</a> <span data-mvp-id=\"nameSortIndicator\">&#9650;</span></th>\n        <th><a href=\"javascript:;\" data-mvp-id=\"cityHeader\">City</a> <span data-mvp-id=\"citySortIndicator\"></span></th>\n        <th>Phone</th>\n      </tr>\n    </table>\n    <div class=\"text-center\">\n      <ul class=\"pagination\">\n        <li><a href=\"javascript:;\" data-mvp-id=\"prevPage\">Previous</a></li>\n        <li class=\"disabled\"><a href=\"javascript:;\" data-mvp-id=\"currentPage\"></a></li>\n        <li><a href=\"javascript:;\" data-mvp-id=\"nextPage\">Next</a></li>\n      </ul>\n    </div>\n  </div>\n  <!-- Source code -->\n  <div class=\"col-md-12\">\n    <h2><small><a href=\"https://github.com/turnsk/gwtmvp/blob/master/samples/src/sk/turn/gwtmvp/samples/client/adapter/PhonebookPresenter.java\" target=\"_blank\">PhonebookPresenter.java</a></small></h2>\n    <pre class=\"pre-scrollable\" style=\"max-height: 500px;\">\npublic class PhonebookPresenter extends BasePresenter&lt;PhonebookPresenter.PhonebookView&gt; {\n\n  interface PhonebookView extends HandlerView&lt;Element, PhonebookPresenter&gt; {\n    @HtmlElement InputElement getSearchInput();\n    @HtmlElement TableElement getTable();\n    @HtmlElement Element getNameSortIndicator();\n    @HtmlElement Element getCitySortIndicator();\n    @HtmlElement Element getPrevPage();\n    @HtmlElement Element getCurrentPage();\n    @HtmlElement Element getNextPage();\n  }\n\n  private TableRowAdapter&lt;DataProvider.Person&gt; adapter;\n  private SearchFilter&lt;DataProvider.Person&gt; searchFilter;\n  private ColumnSortingFilter&lt;DataProvider.Person&gt; sortFilter;\n  private PagingFilter&lt;DataProvider.Person&gt; pagingFilter;\n\n  public PhonebookPresenter() {\n    super(\"^phonebook$\", GWT.&lt;PhonebookView&gt;create(PhonebookView.class));\n    view.setHandler(this);\n  }\n\n  @Override\n  public void onViewLoaded() {\n    // Initialize the table row adapter\n    adapter = new TableRowAdapter&lt;DataProvider.Person&gt;(view.getTable(), 3) {\n      @Override\n      protected Object getCellContent(int column, Person item) {\n        switch (column) {\n          case 0:\n            return item.name;\n          case 1:\n            return item.city;\n          case 2:\n            return item.phone;\n        }\n        return null;\n      }\n    };\n    // Initialize search filter\n    adapter.addFilter(searchFilter = new SearchFilter&lt;DataProvider.Person&gt;() {\n      @Override\n      protected boolean matches(Person item, String query) {\n        return (item.name.toLowerCase().contains(query.toLowerCase()) || \n            item.city.toLowerCase().contains(query.toLowerCase()) || \n            item.phone.contains(query));\n      }\n    });\n    // Initialize the column sorting filter\n    adapter.addFilter(sortFilter = new ColumnSortingFilter&lt;Person&gt;()\n        .setColumnComparator(0, new Comparator&lt;Person&gt;() {\n          @Override\n          public int compare(Person o1, Person o2) {\n            return o1.name.compareToIgnoreCase(o2.name);\n          }\n        })\n        .setColumnComparator(1, new Comparator&lt;Person&gt;() {\n          @Override\n          public int compare(Person o1, Person o2) {\n            return o1.city.compareToIgnoreCase(o2.city);\n          }\n        }));\n    // Initialize the paging filter\n    adapter.addFilter(pagingFilter = new PagingFilter&lt;&gt;(10));\n    pagingFilter.addPagingChangeHandler(new PagingFilter.PagingChangeHandler() {\n      @Override\n      public void onPagingChange(PagingChangeEvent event) {\n        view.getPrevPage().getParentElement().setClassName(event.getPageCount() &gt; 0 &amp;&amp; event.getPage() &gt; 0 ? \"\" : \"disabled\");\n        view.getCurrentPage().setInnerText(String.valueOf(event.getPage() + 1) + \" / \" + String.valueOf(event.getPageCount()));\n        view.getNextPage().getParentElement().setClassName(event.getPageCount() &gt; 0 &amp;&amp; event.getPage() &lt; event.getPageCount() - 1 ? \"\" : \"disabled\");\n      }\n    });\n    // Load some random crowd\n    adapter.setNotifyOnChange(false);\n    for (int i = 0; i &lt; 100; i++) {\n      adapter.addItem(DataProvider.getRandomPerson());\n    }\n    adapter.notifyDataSetChanged();\n  }\n\n  @HtmlHandler(\"searchInput\")\n  void onSearchKeyUp(KeyUpEvent event) {\n    searchFilter.setQuery(view.getSearchInput().getValue());\n  }\n\n  @HtmlHandler({\"nameHeader\", \"cityHeader\"})\n  void onTableHeaderClick(ClickEvent event) {\n    // Find the column we're to sort the table by\n    int column = getElementIndexInParent(event.getNativeEvent().getCurrentEventTarget().&lt;Element&gt;cast().getParentElement());\n    // Set the sort filter\n    sortFilter.setSortColumn(column, sortFilter.getColumn() != column || !sortFilter.isAscending());\n    // Update sorting indicators in the table header\n    view.getNameSortIndicator().setInnerHTML(column != 0 ? \"\" : sortFilter.isAscending() ? \"&amp;#9650;\" : \"&amp;#9660;\");\n    view.getCitySortIndicator().setInnerHTML(column != 1 ? \"\" : sortFilter.isAscending() ? \"&amp;#9650;\" : \"&amp;#9660;\");\n  }\n\n  @HtmlHandler(\"prevPage\")\n  void onPrevPageClick(ClickEvent event) {\n    pagingFilter.setPage(pagingFilter.getPage() - 1);\n  }\n\n  @HtmlHandler(\"nextPage\")\n  void onPageNumberClick(ClickEvent event) {\n    pagingFilter.setPage(pagingFilter.getPage() + 1);\n  }\n\n  private int getElementIndexInParent(Element elem) {\n    int index = 0;\n    while ((elem = elem.getPreviousSiblingElement()) != null) {\n      index++;\n    }\n    return index;\n  }\n\n}</pre>\n  </div>\n</div>";
      }
      public String getName() {
        return "htmlContent";
      }
    }
    ;
  }
  private static class htmlContentInitializer {
    static {
      _instance0.htmlContentInitializer();
    }
    static com.google.gwt.resources.client.TextResource get() {
      return htmlContent;
    }
  }
  public com.google.gwt.resources.client.TextResource htmlContent() {
    return htmlContentInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static com.google.gwt.resources.client.TextResource htmlContent;
  
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      htmlContent(), 
    };
  }
  public ResourcePrototype getResource(String name) {
    if (GWT.isScript()) {
      return getResourceNative(name);
    } else {
      if (resourceMap == null) {
        resourceMap = new java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype>();
        resourceMap.put("htmlContent", htmlContent());
      }
      return resourceMap.get(name);
    }
  }
  private native ResourcePrototype getResourceNative(String name) /*-{
    switch (name) {
      case 'htmlContent': return this.@sk.turn.gwtmvp.samples.client.adapter.PhonebookPresenter_PhonebookViewImpl.Resources::htmlContent()();
    }
    return null;
  }-*/;
}
