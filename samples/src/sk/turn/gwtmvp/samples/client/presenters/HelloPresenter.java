package sk.turn.gwtmvp.samples.client.presenters;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.Window;

import sk.turn.gwtmvp.client.Presenter;
import sk.turn.gwtmvp.samples.client.views.HelloView;

public class HelloPresenter implements Presenter<HelloView> {
  private final RegExp tokenRegExp = RegExp.compile("^hello$");
  private final HelloView view;
  private int counter = 1;

  public HelloPresenter(HelloView view) {
    this.view = view;
  }

  public RegExp getTokenRegExp() {
    return tokenRegExp;
  }

  public HelloView getView() {
    return view;
  }

  public void onViewLoaded() {
    view.setGreetHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent e) {
        Window.alert("Hello " + view.getNameInput().getValue() + " for the " + formatCounter() + " time!");
        counter++;
        view.getCounter().setInnerText(formatCounter());
      }
    });
  }

  public void onShow(MatchResult matchResult) {
    view.getCounter().setInnerText(formatCounter());
  }

  public void onHide() {
    // Nothing to do here in this sample
  }

  private String formatCounter() {
    return counter + (counter % 10 == 1 && (counter / 10) % 10 != 1 ? "st" : counter % 10 == 2 && (counter / 10) % 10 != 1 ? "nd" : "th");
  }
}
