package sk.turn.gwtmvp.samples.client.presenters;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

import sk.turn.gwtmvp.client.Loader;
import sk.turn.gwtmvp.client.Presenter;
import sk.turn.gwtmvp.samples.client.views.LoadersView;

public class LoadersPresenter implements Presenter<LoadersView> {
  private final RegExp tokenRegExp = RegExp.compile("^loaders$");
  private final LoadersView view;
  private int loadCount = 0;

  public LoadersPresenter(LoadersView view) {
    this.view = view;
  }

  @Override
  public RegExp getTokenRegExp() {
    return tokenRegExp;
  }

  @Override
  public LoadersView getView() {
    return view;
  }

  @Override
  public void onViewLoaded() {
    Loader.register(view.getLoader(), false);
    Loader.register("counted", view.getCountedLoader(), true);
    view.setShowHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        Loader.show();
      }
    });
    view.setHideHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        Loader.hide();
      }
    });
    view.setShowCountedHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        Loader.show("counted");
        view.getLoadCount().setInnerText(String.valueOf(++loadCount));
      }
    });
    view.setHideCountedHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        Loader.hide("counted");
        if (loadCount > 0) {
          view.getLoadCount().setInnerText(String.valueOf(--loadCount));
        }
      }
    });
  }

  @Override
  public void onShow(MatchResult matchResult) {
  }

  @Override
  public void onHide() {
  }

}
