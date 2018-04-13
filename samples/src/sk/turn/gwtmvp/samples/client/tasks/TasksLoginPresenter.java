package sk.turn.gwtmvp.samples.client.tasks;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;

import sk.turn.gwtmvp.client.*;
import sk.turn.gwtmvp.client.history.History;

public class TasksLoginPresenter extends BasePresenter<TasksLoginPresenter.TasksLoginView> {

  interface TasksLoginView extends HandlerView<Element, TasksLoginPresenter> {
    @HtmlElement InputElement getUsername();
  }

  private static final String REGEXP = "^tasks/.*$";
  private static final Dictionary dict = GWT.create(Dictionary.class);

  public TasksLoginPresenter() {
    super(Dao.getUsername() != null ? null : REGEXP, GWT.<TasksLoginView>create(TasksLoginView.class));
    view.setHandler(this);
    // Add a handler for user login/logout
    Dao.addOnUserChangedHandler(new Dao.OnUserChangedHandler() {
      @Override
      public void onUserChanged(String username) {
        // Update this Presenter's RegExp to either handle all history tokens or none
        setTokenRegExp(username != null ? null : REGEXP);
        // Force MVP to update current presenter
        History.fireCurrentHistoryState();
      }
    });
  }

  @Override
  public void onShow(String... groups) {
    // Give the username input focus
    view.getUsername().focus();
  }

  @HtmlHandler("login")
  void onLogin(ClickEvent event) {
    final String username = view.getUsername().getValue();
    if (username.length() == 0) {
      return;
    }
    // You may want to validate the username (and password) here, we'll just grab the list of tasks from server
    // See if we have the tasks already loaded or stored
    if (Dao.getTasks() != null) {
      Dao.setUsername(username);
      return;
    }
    try {
      showProgess(true);
      new RequestBuilder(RequestBuilder.GET, GWT.getModuleBaseForStaticFiles() + "tasks.json").sendRequest(null, new RequestCallback() {
        @Override
        public void onResponseReceived(Request request, Response response) {
          showProgess(false);
          if (response.getStatusCode() == 200) {
            Dao.setTasks(JsonUtils.<JsArray<Task>>safeEval(response.getText()));
            Dao.setUsername(username);
          } else {
            onError(request, new Exception(dict.loginInvalidStatusCode(response.getStatusCode())));
          }
        }
        @Override
        public void onError(Request request, Throwable exception) {
          showProgess(false);
          Window.alert(dict.loginFailed(exception.getMessage()));
        }
      });
    } catch (RequestException e) {
      showProgess(false);
      Window.alert(dict.loginFailed(e.getMessage()));
    }
  }

  private void showProgess(boolean show) {
    view.getElement(show ? "login" : "progress").addClassName("hidden");
    view.getElement(show ? "progress" : "login").removeClassName("hidden");
  }

}
