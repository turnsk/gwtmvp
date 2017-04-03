package sk.turn.gwtmvp.samples.client.tasks;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;

import sk.turn.gwtmvp.client.HandlerView;
import sk.turn.gwtmvp.client.HtmlElement;
import sk.turn.gwtmvp.client.HtmlHandler;
import sk.turn.gwtmvp.client.ViewAdapter;

public class TaskAdapter extends ViewAdapter<Task, TaskAdapter.TaskAdapterView> {

  interface TaskAdapterView extends HandlerView<Element, TaskAdapter> {
    @HtmlElement DivElement getRoot();
    @HtmlElement InputElement getCheck();
    @HtmlElement DivElement getName();
    @HtmlElement InputElement getNameInput();
    @HtmlElement AnchorElement getEditButton();
    @HtmlElement AnchorElement getRemoveButton();
  }

  public TaskAdapter(Element parentElement) {
    super(parentElement);
  }

  public void addTask() {
    JsArrayIterable<Task> tasks = Dao.getTasks();
    tasks.push(Task.createObject().<Task>cast());
    Dao.setTasks(tasks);
    setItems(tasks);
    startEdit(tasks.length() - 1);
  }

  @Override
  protected TaskAdapterView createView() {
    TaskAdapterView view = GWT.create(TaskAdapterView.class);
    view.setHandler(this);
    return view;
  }

  @Override
  protected void setViewData(TaskAdapterView view, Task item) {
    view.getCheck().setChecked(item.isFinished());
    view.getName().setInnerText(item.getName());
    if (item.isFinished()) {
      view.getName().addClassName("finished");
      view.getEditButton().addClassName("hidden");
    } else {
      view.getName().removeClassName("finished");
      view.getEditButton().removeClassName("hidden");
    }
  }

  @HtmlHandler("root")
  void onMouseOver(MouseOverEvent event) {
    TaskAdapterView view = getItemView(getItemIndexFromEvent(event));
    view.getEditButton().getStyle().setVisibility(Visibility.VISIBLE);
    view.getRemoveButton().getStyle().setVisibility(Visibility.VISIBLE);
  }

  @HtmlHandler("root")
  void onMouseOut(MouseOutEvent event) {
    TaskAdapterView view = getItemView(getItemIndexFromEvent(event));
    view.getEditButton().getStyle().setVisibility(Visibility.HIDDEN);
    view.getRemoveButton().getStyle().setVisibility(Visibility.HIDDEN);
  }

  @HtmlHandler("check")
  void onCheckChange(ChangeEvent event) {
    int index = getItemIndexFromEvent(event);
    getItem(index).setFinished(getItemView(index).getCheck().isChecked());
    rearrangeTasks();
  }

  @HtmlHandler("nameInput")
  void onNameKeyUp(KeyUpEvent event) {
    if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER || event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) {
      finishEdit(event, event.getNativeKeyCode() == KeyCodes.KEY_ENTER);
    }
  }

  @HtmlHandler("nameInput")
  void onNameBlur(BlurEvent event) {
    finishEdit(event, true);
  }

  private void finishEdit(DomEvent<?> event, boolean save) {
    int index = getItemIndexFromEvent(event);
    Task task = getItem(index);
    TaskAdapterView view = getItemView(index);
    if (save) {
      if (view.getNameInput().getValue().trim().length() == 0) {
        removeTask(index);
      } else {
        task.setName(view.getNameInput().getValue());
        view.getName().setInnerText(task.getName());
        Dao.onTasksChanged();
      }
    }
    view.getEditButton().removeClassName("hidden");
    view.getRemoveButton().removeClassName("hidden");
    view.getName().removeClassName("hidden");
    view.getNameInput().addClassName("hidden");
  }

  @HtmlHandler("editButton")
  void onEditClick(ClickEvent event) {
    startEdit(getItemIndexFromEvent(event));
  }

  private void startEdit(int index) {
    Task task = getItem(index);
    TaskAdapterView view = getItemView(index);
    view.getEditButton().addClassName("hidden");
    view.getRemoveButton().addClassName("hidden");
    view.getName().addClassName("hidden");
    view.getNameInput().removeClassName("hidden");
    view.getNameInput().setValue(task.getName());
    view.getNameInput().focus();
  }

  @HtmlHandler("removeButton")
  void onClick(ClickEvent event) {
    removeTask(getItemIndexFromEvent(event));
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
    setItems(newTasks);
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
    setItems(newTasks);
  }

}
