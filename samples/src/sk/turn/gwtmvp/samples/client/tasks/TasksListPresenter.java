package sk.turn.gwtmvp.samples.client.tasks;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.Window;

import sk.turn.gwtmvp.client.BasePresenter;
import sk.turn.gwtmvp.client.HandlerView;
import sk.turn.gwtmvp.client.HtmlElement;
import sk.turn.gwtmvp.client.HtmlHandler;

public class TasksListPresenter extends BasePresenter<TasksListPresenter.TasksListView> implements Dao.OnUserChangedHandler {

  interface TasksListView extends HandlerView<Element, TasksListPresenter> {
    @HtmlElement SpanElement getUsername();
    @HtmlElement Element getTasksContainer();
  }

  private static final Dictionary dict = GWT.create(Dictionary.class);

  public TasksListPresenter() {
    super("^tasks/list$", (TasksListView) GWT.create(TasksListView.class));
  }

  private TaskAdapter taskAdapter;

  @Override
  public void onViewLoaded() {
    view.setHandler(this);
    Dao.addOnUserChangedHandler(this);
    onUserChanged(Dao.getUsername());
    taskAdapter = new TaskAdapter(view.getTasksContainer());
  }

  @Override
  public void onShow(String... groups) {
    taskAdapter.setItems(Dao.getTasks());
  }

  @Override
  public void onUserChanged(String username) {
    view.getUsername().setInnerText(username != null ? username + (username.endsWith("s") ? "'" : "'s") : "");
  }

  @HtmlHandler("addButton")
  void onAddTask(ClickEvent event) {
    taskAdapter.addTask();
  }

  @HtmlHandler("logout")
  void onLogout(ClickEvent event) {
    if (Window.confirm(dict.listLogoutConfirmation())) {
      Dao.setTasks(null);
      Dao.setUsername(null);
    }
  }

}
