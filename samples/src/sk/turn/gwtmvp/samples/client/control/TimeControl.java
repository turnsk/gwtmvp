package sk.turn.gwtmvp.samples.client.control;

import java.util.Date;

import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Timer;

import sk.turn.gwtmvp.client.Control;
import sk.turn.gwtmvp.client.View;
import sk.turn.gwtmvp.client.ViewHtml;

public class TimeControl extends Control<TimeControl.TimeView> {
  @ViewHtml("<span></span>")
  interface TimeView extends View<Element> { }

  private DateTimeFormat format = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_MEDIUM);
  private Timer timer = new Timer() {
    @Override
    public void run() {
      updateTime();
    }
  };

  public void setFormat(DateTimeFormat format) {
    this.format = format;
  }

  @Override
  public void onShow() {
    updateTime();
    timer.scheduleRepeating(1000);
  }

  @Override
  public void onHide() {
    timer.cancel();
  }

  private void updateTime() {
    view.getRootElement().setInnerText(format.format(new Date()));
  }
}
