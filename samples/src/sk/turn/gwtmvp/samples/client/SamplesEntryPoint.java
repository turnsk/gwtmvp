package sk.turn.gwtmvp.samples.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Document;

import sk.turn.gwtmvp.client.Mvp;
import sk.turn.gwtmvp.samples.client.presenters.HelloPresenter;
import sk.turn.gwtmvp.samples.client.presenters.LoadersPresenter;
import sk.turn.gwtmvp.samples.client.presenters.AdapterPresenter;
import sk.turn.gwtmvp.samples.client.views.HelloView;
import sk.turn.gwtmvp.samples.client.views.LoadersView;
import sk.turn.gwtmvp.samples.client.views.AdapterView;

public class SamplesEntryPoint implements EntryPoint {

  @Override
  public void onModuleLoad() {
    Mvp mvp = new Mvp(Document.get().getBody());
    mvp.addPresenter(new HelloPresenter((HelloView) GWT.create(HelloView.class)));
    mvp.addPresenter(new LoadersPresenter((LoadersView) GWT.create(LoadersView.class)));
    mvp.addPresenter(new AdapterPresenter((AdapterView) GWT.create(AdapterView.class)));
    mvp.start();
  }

}
