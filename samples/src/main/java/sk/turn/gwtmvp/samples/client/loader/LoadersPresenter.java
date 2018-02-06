package sk.turn.gwtmvp.samples.client.loader;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;

import sk.turn.gwtmvp.client.BasePresenter;
import sk.turn.gwtmvp.client.HandlerView;
import sk.turn.gwtmvp.client.HtmlElement;
import sk.turn.gwtmvp.client.HtmlHandler;
import sk.turn.gwtmvp.client.Loader;

public class LoadersPresenter extends BasePresenter<LoadersPresenter.LoadersView> {

  interface LoadersView extends HandlerView<DivElement, LoadersPresenter> {
    @HtmlElement SpanElement getLoader();
    @HtmlElement SpanElement getCountedLoader();
    @HtmlElement SpanElement getLoadCount();
  }

  private int loadCount = 0;

  public LoadersPresenter() {
    super("^loaders$", (LoadersView) GWT.create(LoadersView.class));
  }

  @Override
  public void onViewLoaded() {
    view.setHandler(this);
    // Register global (non-counted) loader
    Loader.register(view.getLoader(), false);
    // Register named (counted) loader
    Loader.register("counted", view.getCountedLoader(), true);
  }

  @HtmlHandler("showButton")
  void onShowClicked(ClickEvent event) {
    Loader.show();
  }

  @HtmlHandler("hideButton")
  void onHideClicked(ClickEvent event) {
    Loader.hide();
  }

  @HtmlHandler("showCountedButton")
  void onShowCountedClicked(ClickEvent event) {
    Loader.show("counted");
    view.getLoadCount().setInnerText(String.valueOf(++loadCount));
  }

  @HtmlHandler("hideCountedButton")
  void onHideCountedClicked(ClickEvent event) {
    Loader.hide("counted");
    if (loadCount > 0) {
      view.getLoadCount().setInnerText(String.valueOf(--loadCount));
    }
  }

}
