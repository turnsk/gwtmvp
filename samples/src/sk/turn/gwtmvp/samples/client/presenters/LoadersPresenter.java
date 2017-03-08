package sk.turn.gwtmvp.samples.client.presenters;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import sk.turn.gwtmvp.client.BasePresenter;
import sk.turn.gwtmvp.client.Loader;
import sk.turn.gwtmvp.samples.client.views.LoadersView;

public class LoadersPresenter extends BasePresenter<LoadersView> {

  private int loadCount = 0;

  public LoadersPresenter(LoadersView view) {
    super("^loaders$", view);
  }

  @Override
  public void onViewLoaded() {
    Loader.register(getView().getLoader(), false);
    Loader.register("counted", getView().getCountedLoader(), true);
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
