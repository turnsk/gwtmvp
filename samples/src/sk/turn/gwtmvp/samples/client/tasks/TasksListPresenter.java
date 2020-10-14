package sk.turn.gwtmvp.samples.client.tasks;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.user.client.Window;

import sk.turn.gwtmvp.client.BasePresenter;
import sk.turn.gwtmvp.client.HandlerView;
import sk.turn.gwtmvp.client.HtmlElement;
import sk.turn.gwtmvp.client.HtmlHandler;
import sk.turn.gwtmvp.client.adapters.ViewHolderAdapter;

public class TasksListPresenter extends BasePresenter<TasksListPresenter.TasksListView> implements Dao.OnUserChangedHandler {

  interface TasksListView extends HandlerView<Element, TasksListPresenter> {
    @HtmlElement SpanElement getUsername();
    @HtmlElement Element getTasksContainer();
  }

  interface TasksListItemView extends HandlerView<Element, TaskViewHolder> {
    @HtmlElement DivElement getRoot();
    @HtmlElement InputElement getCheck();
    @HtmlElement DivElement getName();
    @HtmlElement InputElement getNameInput();
    @HtmlElement AnchorElement getEditButton();
    @HtmlElement AnchorElement getRemoveButton();
  }

  class TaskViewHolder extends ViewHolderAdapter.ViewHolder<Task, TasksListItemView> {
    private boolean mouseOver = false;

    public TaskViewHolder() {
      super(GWT.create(TasksListItemView.class));
      view.setHandler(this);
    }

    @Override
    protected void bind(Task item, int position) {
      view.getCheck().setChecked(item.isFinished());
      view.getName().setInnerText(item.getName());
      if (item.isFinished()) {
        view.getName().addClassName("finished");
        view.getEditButton().addClassName("hidden");
      } else {
        view.getName().removeClassName("finished");
        view.getName().removeClassName("hidden");
        view.getNameInput().addClassName("hidden");
        view.getEditButton().addClassName("hidden");
        view.getRemoveButton().addClassName("hidden");
        if (editingIndex == position) {
          view.getName().addClassName("hidden");
          view.getNameInput().removeClassName("hidden");
          view.getNameInput().setValue(item.getName());
          view.getNameInput().focus();
        } else if (mouseOver) {
          view.getEditButton().removeClassName("hidden");
          view.getRemoveButton().removeClassName("hidden");
        }
      }
    }

    @HtmlHandler("root")
    void onMouseOver(MouseOverEvent event) {
      mouseOver = true;
      int index = taskAdapter.getItemIndexFromEvent(event);
      if (editingIndex != index) {
        taskAdapter.reload(index);
      }
    }

    @HtmlHandler("root")
    void onMouseOut(MouseOutEvent event) {
      mouseOver = false;
      int index = taskAdapter.getItemIndexFromEvent(event);
      if (editingIndex != index) {
        taskAdapter.reload(index);
      }
    }

    @HtmlHandler("check")
    void onCheckChange(ChangeEvent event) {
      int index = taskAdapter.getItemIndexFromEvent(event);
      taskAdapter.getItem(index).setFinished(view.getCheck().isChecked());
      rearrangeTasks();
    }

    @HtmlHandler("nameInput")
    void onNameKeyUp(KeyUpEvent event) {
      if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER || event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) {
        finishEdit(taskAdapter.getItemIndexFromEvent(event), event.getNativeKeyCode() == KeyCodes.KEY_ENTER);
      }
    }

    @HtmlHandler("nameInput")
    void onNameBlur(BlurEvent event) {
      finishEdit(taskAdapter.getItemIndexFromEvent(event), true);
    }

    @HtmlHandler("editButton")
    void onEditClick(ClickEvent event) {
      editingIndex = taskAdapter.getItemIndexFromEvent(event);
      taskAdapter.notifyDataSetChanged();
    }

    @HtmlHandler("removeButton")
    void onClick(ClickEvent event) {
      removeTask(taskAdapter.getItemIndexFromEvent(event));
    }
  }

  private static final Dictionary dict = GWT.create(Dictionary.class);

  public TasksListPresenter() {
    super("^tasks/list$", (TasksListView) GWT.create(TasksListView.class));
  }

  private ViewHolderAdapter<Task, TaskViewHolder> taskAdapter;
  private int editingIndex = -1;

  @Override
  public void onViewLoaded() {
    view.setHandler(this);
    Dao.addOnUserChangedHandler(this);
    onUserChanged(Dao.getUsername());
    taskAdapter = new ViewHolderAdapter<Task, TaskViewHolder>(view.getTasksContainer()) {
      @Override
      protected TaskViewHolder createViewHolder() { return new TaskViewHolder(); }
    };
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
    Task task = Task.createObject().<Task>cast();
    task.setName("");
    JsArrayIterable<Task> tasks = Dao.getTasks();
    tasks.push(task);
    Dao.setTasks(tasks);
    editingIndex = taskAdapter.getCount();
    taskAdapter.addItem(task);
  }

  @HtmlHandler("logout")
  void onLogout(ClickEvent event) {
    if (Window.confirm(dict.listLogoutConfirmation())) {
      Dao.setTasks(null);
      Dao.setUsername(null);
    }
  }

  private void finishEdit(int index, boolean save) {
    Task task = taskAdapter.getItem(index);
    TasksListItemView view = taskAdapter.getItemViewHolder(index).getView();
    if (save) {
      if (view.getNameInput().getValue().trim().length() == 0) {
        removeTask(index);
      } else {
        task.setName(view.getNameInput().getValue());
        Dao.onTasksChanged();
      }
    }
    editingIndex = -1;
    taskAdapter.reload(index);
  }

  private void removeTask(int index) {
    JsArray<Task> tasks = Dao.getTasks();
    JsArrayIterable<Task> newTasks = JavaScriptObject.createArray().cast();
    // Copy the tasks but for the current
    for (int i = 0; i < tasks.length(); i++) {
      if (i != index) {
        newTasks.push(tasks.get(i));
      }
    }
    Dao.setTasks(newTasks);
    taskAdapter.setItems(newTasks);
  }

  private void rearrangeTasks() {
    JsArrayIterable<Task> newTasks = JavaScriptObject.createArray().cast();
    JsArrayIterable<Task> oldTasks = Dao.getTasks();
    for (Task task : oldTasks) {
      if (!task.isFinished()) {
        newTasks.push(task);
      }
    }
    for (Task task : oldTasks) {
      if (task.isFinished()) {
        newTasks.push(task);
      }
    }
    Dao.setTasks(newTasks);
    taskAdapter.setItems(newTasks);
  }

}
