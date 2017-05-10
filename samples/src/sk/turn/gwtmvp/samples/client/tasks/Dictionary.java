package sk.turn.gwtmvp.samples.client.tasks;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;

@DefaultLocale("en")
public interface Dictionary extends Messages {
  @DefaultMessage("Tasks Login")
  String loginTitle();
  @DefaultMessage("Username")
  String loginUsername();
  @DefaultMessage("Type any username you want...")
  String loginUsernameHint();
  @DefaultMessage("Login")
  String loginButton();
  @DefaultMessage("Failed logging in: {0}")
  String loginFailed(String reason);
  @DefaultMessage("Invalid response status code: {0}")
  String loginInvalidStatusCode(int code);
  @DefaultMessage("Tasks")
  String listTasks();
  @DefaultMessage("logout")
  String listLogout();
  @DefaultMessage("Logging out also clears your task list, proceed?")
  String listLogoutConfirmation();
}
