package sk.turn.gwtmvp.samples.client.loader;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import sk.turn.gwtmvp.client.BasePresenter;
import sk.turn.gwtmvp.client.HtmlElement;
import sk.turn.gwtmvp.client.HtmlHandler;
import sk.turn.gwtmvp.client.Loader;
import sk.turn.gwtmvp.client.View;

public class LoadersPresenter extends BasePresenter<LoadersPresenter.LoadersView> {

  interface LoadersView extends View<DivElement> {
    @HtmlElement SpanElement getLoader();
    @HtmlHandler("showButton") void setShowHandler(ClickHandler handler);
    @HtmlHandler("hideButton") void setHideHandler(ClickHandler handler);
    @HtmlElement SpanElement getCountedLoader();
    @HtmlElement SpanElement getLoadCount();
    @HtmlHandler("showCountedButton") void setShowCountedHandler(ClickHandler handler);
    @HtmlHandler("hideCountedButton") void setHideCountedHandler(ClickHandler handler);
  }

  private int loadCount = 0;

  public LoadersPresenter() {
    super("^loaders$", (LoadersView) GWT.create(LoadersView.class));
  }

  @Override
  public void onViewLoaded() {
    // Register global (non-counted) loader
    Loader.register(getView().getLoader(), false);
    // Register named (counted) loader
    Loader.register("counted", getView().getCountedLoader(), true);
    // Configure button handlers
    getView().setShowHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        Loader.show();
      }
    });
    getView().setHideHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        Loader.hide();
      }
    });
    getView().setShowCountedHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        Loader.show("counted");
        getView().getLoadCount().setInnerText(String.valueOf(++loadCount));
      }
    });
    getView().setHideCountedHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        Loader.hide("counted");
        if (loadCount > 0) {
          getView().getLoadCount().setInnerText(String.valueOf(--loadCount));
        }
      }
    });
  }

}
