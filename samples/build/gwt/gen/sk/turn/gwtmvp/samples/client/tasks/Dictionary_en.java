package sk.turn.gwtmvp.samples.client.tasks;

public class Dictionary_en implements sk.turn.gwtmvp.samples.client.tasks.Dictionary {
  
  public java.lang.String loginTitle() {
    return "Tasks Login";
  }
  
  public java.lang.String loginUsername() {
    return "Username";
  }
  
  public java.lang.String loginUsernameHint() {
    return "Type any username you want...";
  }
  
  public java.lang.String loginButton() {
    return "Login";
  }
  
  public java.lang.String loginFailed(java.lang.String arg0) {
    return "Failed logging in: " + arg0;
  }
  
  public java.lang.String loginInvalidStatusCode(int arg0) {
    return "Invalid response status code: " + arg0;
  }
  
  public java.lang.String listTasks() {
    return "Tasks";
  }
  
  public java.lang.String listLogout() {
    return "logout";
  }
  
  public java.lang.String listLogoutConfirmation() {
    return "Logging out also clears your task list, proceed?";
  }
}
