package sk.turn.gwtmvp.samples.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Document;

import sk.turn.gwtmvp.client.Mvp;
import sk.turn.gwtmvp.samples.client.presenters.SimplePresenter;
import sk.turn.gwtmvp.samples.client.views.SimpleView;

public class SimpleEntryPoint implements EntryPoint {

  public void onModuleLoad() {
    Mvp mvp = new Mvp(Document.get().getBody());
    mvp.addPresenter(new SimplePresenter((SimpleView) GWT.create(SimpleView.class)));
    mvp.start();
  }

}
