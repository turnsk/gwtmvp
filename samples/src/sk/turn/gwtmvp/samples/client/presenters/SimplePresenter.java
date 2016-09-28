package sk.turn.gwtmvp.samples.client.presenters;

import java.util.logging.Logger;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.Window;

import sk.turn.gwtmvp.client.Presenter;
import sk.turn.gwtmvp.samples.client.views.SimpleView;

public class SimplePresenter implements Presenter<SimpleView> {

  private static final Logger LOG = Logger.getLogger(SimplePresenter.class.getName());

  private SimpleView view;
  private final RegExp tokenRegExp = RegExp.compile("^(test/([0-9]+))|.*$");

  public SimplePresenter(SimpleView view) {
    this.view = view;
  }

  @Override
  public RegExp getTokenRegExp() {
    return tokenRegExp;
  }

  @Override
  public SimpleView getView() {
    return view;
  }

  @Override
  public void onViewLoaded() {
    LOG.severe("Initializing view");
    view.getLink().setHref("#test/16");
    view.setClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        LOG.severe("Clicked " + Element.as(event.getNativeEvent().getEventTarget()).getTagName() + ", fired via " + event.getRelativeElement().getTagName() + "!");
        event.stopPropagation();
      }
    });
  }

  @Override
  public void onShow(MatchResult matchResult) {
    LOG.severe("Showing " + matchResult);
    if (matchResult.getGroupCount() >= 3 && matchResult.getGroup(2) != null) {
      Window.alert("Showing " + matchResult.getGroup(2));
    }
  }

  @Override
  public void onHide() {
  }

}
