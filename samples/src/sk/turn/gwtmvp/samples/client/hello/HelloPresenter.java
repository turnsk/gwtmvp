package sk.turn.gwtmvp.samples.client.hello;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.user.client.Window;

import sk.turn.gwtmvp.client.BasePresenter;
import sk.turn.gwtmvp.client.HtmlElement;
import sk.turn.gwtmvp.client.HtmlHandler;
import sk.turn.gwtmvp.client.View;

public class HelloPresenter extends BasePresenter<HelloPresenter.HelloView> {

  interface HelloView extends View<DivElement> {
    @HtmlElement InputElement getNameInput();
    @HtmlElement SpanElement getCounter();
    @HtmlHandler("nameInput") void setInputKeyPressHandler(KeyPressHandler handler);
    @HtmlHandler("greetLink") void setGreetHandler(ClickHandler handler);
  }

  private int counter = 1;

  public HelloPresenter() {
    super("^hello$", (HelloView) GWT.create(HelloView.class));
  }

  @Override
  public void onViewLoaded() {
    getView().setInputKeyPressHandler(new KeyPressHandler() {
      @Override
      public void onKeyPress(KeyPressEvent event) {
        if (event.getCharCode() == KeyCodes.KEY_ENTER) {
          greet();
        }
      }
    });
    getView().setGreetHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent e) {
        greet();
      }
    });
  }

  @Override
  public void onShow(MatchResult matchResult) {
    getView().getCounter().setInnerText(formatCounter());
    getView().getNameInput().focus();
  }

  private void greet() {
    String name = getView().getNameInput().getValue();
    Window.alert("Hello " + (name.length() == 0 ? "Mr. Nobody" : name) + " for the " + formatCounter() + " time!");
    counter++;
    getView().getCounter().setInnerText(formatCounter());
  }

  private String formatCounter() {
    return counter + (counter % 10 == 1 && (counter / 10) % 10 != 1 ? "st" : 
      counter % 10 == 2 && (counter / 10) % 10 != 1 ? "nd" : 
        counter % 10 == 3 && (counter / 10) % 10 != 1 ? "rd" : "th");
  }

}
