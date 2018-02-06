package sk.turn.gwtmvp.samples.client.hello;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.user.client.Window;

import sk.turn.gwtmvp.client.BasePresenter;
import sk.turn.gwtmvp.client.HandlerView;
import sk.turn.gwtmvp.client.HtmlElement;
import sk.turn.gwtmvp.client.HtmlHandler;

public class HelloPresenter extends BasePresenter<HelloPresenter.HelloView> {

  interface HelloView extends HandlerView<DivElement, HelloPresenter> {
    @HtmlElement InputElement getNameInput();
    @HtmlElement SpanElement getCounter();
  }

  private int counter = 1;

  public HelloPresenter() {
    super("^hello$", GWT.<HelloView>create(HelloView.class));
    view.setHandler(this);
  }

  @Override
  public void onShow(String... groups) {
    view.getCounter().setInnerText(formatCounter());
    view.getNameInput().focus();
  }

  @HtmlHandler("nameInput")
  void onNameKeyPress(KeyPressEvent event) {
    if (event.getCharCode() == KeyCodes.KEY_ENTER) {
      onGreetClick(null);
    }
  }

  @HtmlHandler("greetLink")
  void onGreetClick(ClickEvent event) {
    String name = view.getNameInput().getValue();
    Window.alert("Hello " + (name.length() == 0 ? "Mr. Nobody" : name) + " for the " + formatCounter() + " time!");
    counter++;
    view.getCounter().setInnerText(formatCounter());
  }

  private String formatCounter() {
    return counter + (counter % 10 == 1 && (counter / 10) % 10 != 1 ? "st" : 
      counter % 10 == 2 && (counter / 10) % 10 != 1 ? "nd" : 
        counter % 10 == 3 && (counter / 10) % 10 != 1 ? "rd" : "th");
  }

}
