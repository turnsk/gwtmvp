package sk.turn.gwtmvp.samples.client.tasks;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.storage.client.Storage;

public class Dao {

  interface OnUserChangedHandler {
    void onUserChanged(String username);
  }

  interface OnTasksLoadedCallback {
    void onTasksLoaded(JsArray<Task> tasks);
  }

  private static final Storage localStorage = Storage.getLocalStorageIfSupported();
  private static String username = (localStorage != null ? localStorage.getItem("username") : null);
  private static final List<OnUserChangedHandler> onUserChangedHandlers = new ArrayList<>();
  private static JsArrayIterable<Task> tasks;

  public static String getUsername() {
    return username;
  }

  public static void setUsername(String username) {
    Dao.username = username;
    if (localStorage != null) {
      if (username == null) {
        localStorage.removeItem("username");
        localStorage.removeItem("tasks");
      } else {
        localStorage.setItem("username", username);
      }
    }
    for (OnUserChangedHandler handler : onUserChangedHandlers) {
      handler.onUserChanged(username);
    }
  }

  public static void addOnUserChangedHandler(OnUserChangedHandler handler) {
    onUserChangedHandlers.add(handler);
  }

  public static JsArrayIterable<Task> getTasks() {
    if (tasks == null && localStorage != null) {
      String tasksJson = localStorage.getItem("tasks");
      if (tasksJson != null) {
        tasks = JsonUtils.<JsArrayIterable<Task>>safeEval(tasksJson);
      }
    }
    return tasks;
  }

  public static void setTasks(JsArray<Task> tasks) {
    Dao.tasks = (tasks != null ? tasks.<JsArrayIterable<Task>>cast() : null);
    onTasksChanged();
  }

  public static void onTasksChanged() {
    if (localStorage != null) {
      if (tasks != null) {
        localStorage.setItem("tasks", JsonUtils.stringify(tasks));
      } else {
        localStorage.removeItem("tasks");
      }
    }
  }

}
