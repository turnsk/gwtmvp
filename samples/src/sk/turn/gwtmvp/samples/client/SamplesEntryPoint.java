package sk.turn.gwtmvp.samples.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;

import sk.turn.gwtmvp.client.BasePresenter;
import sk.turn.gwtmvp.client.Mvp;
import sk.turn.gwtmvp.client.View;
import sk.turn.gwtmvp.client.history.History;
import sk.turn.gwtmvp.client.history.Html5HistoryAgent;
import sk.turn.gwtmvp.samples.client.adapter.PeoplePresenter;
import sk.turn.gwtmvp.samples.client.adapter.PersonPresenter;
import sk.turn.gwtmvp.samples.client.adapter.PhonebookPresenter;
import sk.turn.gwtmvp.samples.client.hello.HelloPresenter;
import sk.turn.gwtmvp.samples.client.loader.LoadersPresenter;
import sk.turn.gwtmvp.samples.client.tasks.TasksListPresenter;
import sk.turn.gwtmvp.samples.client.tasks.TasksLoginPresenter;

public class SamplesEntryPoint implements EntryPoint {

  interface SamplesView extends View<Element> {
  }

  @Override
  public void onModuleLoad() {
    Mvp mvp = new Mvp(Document.get().getElementById("container"));
    mvp.addPresenter(new HelloPresenter());
    mvp.addPresenter(new LoadersPresenter());
    mvp.addPresenter(new PeoplePresenter());
    mvp.addPresenter(new PersonPresenter());
    mvp.addPresenter(new TasksLoginPresenter());
    mvp.addPresenter(new TasksListPresenter());
    mvp.addPresenter(new PhonebookPresenter());
    // This last presenter catches any history token that hasn't been caught by other presenters
    mvp.addPresenter(new BasePresenter<SamplesView>(".*", (SamplesView) GWT.create(SamplesView.class)));
    // Choose mode of history
    History.setHistoryAgent(new Html5HistoryAgent("/gwtmvp/"));
    mvp.start();
  }
}
