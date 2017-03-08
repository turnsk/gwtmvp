package sk.turn.gwtmvp.samples.client.presenters;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.user.client.Window;

import sk.turn.gwtmvp.client.BasePresenter;
import sk.turn.gwtmvp.samples.client.views.HelloView;

public class HelloPresenter extends BasePresenter<HelloView> {

  private int counter = 1;

  public HelloPresenter(HelloView view) {
    super("^hello$", view);
  }

  @Override
  public void onViewLoaded() {
    getView().setGreetHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent e) {
        Window.alert("Hello " + getView().getNameInput().getValue() + " for the " + formatCounter() + " time!");
        counter++;
        getView().getCounter().setInnerText(formatCounter());
      }
    });
  }

  public void onShow(MatchResult matchResult) {
    getView().getCounter().setInnerText(formatCounter());
  }

  private String formatCounter() {
    return counter + (counter % 10 == 1 && (counter / 10) % 10 != 1 ? "st" : counter % 10 == 2 && (counter / 10) % 10 != 1 ? "nd" : "th");
  }

}
