package sk.turn.gwtmvp.samples.client.control;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.client.DateTimeFormat;

import sk.turn.gwtmvp.client.BasePresenter;
import sk.turn.gwtmvp.client.HtmlElement;
import sk.turn.gwtmvp.client.View;

public class ControlPresenter extends BasePresenter<ControlPresenter.ControlView> {
  interface ControlView extends View<Element> {
    @HtmlElement TimeControl getTime1();
    @HtmlElement TimeControl getTime2();
  }

  public ControlPresenter() {
    super("^controls$", (ControlView) GWT.create(ControlView.class));
  }

  @Override
  public void onViewLoaded() {
    view.getTime2().setFormat(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_FULL));
  }
}
