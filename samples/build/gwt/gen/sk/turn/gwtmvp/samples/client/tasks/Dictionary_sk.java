package sk.turn.gwtmvp.samples.client.tasks;

public class Dictionary_sk implements sk.turn.gwtmvp.samples.client.tasks.Dictionary {
  
  public java.lang.String loginTitle() {
    return "Úlohy - prihlásenie";
  }
  
  public java.lang.String loginUsername() {
    return "Prihlasovacie meno";
  }
  
  public java.lang.String loginUsernameHint() {
    return "Zadajte akékoľvek meno...";
  }
  
  public java.lang.String loginButton() {
    return "Prihlásiť";
  }
  
  public java.lang.String loginFailed(java.lang.String arg0) {
    return "Prihlásenie zlyhalo: " + arg0;
  }
  
  public java.lang.String loginInvalidStatusCode(int arg0) {
    return "Server vrátil neočakávaný HTTP kód: " + arg0;
  }
  
  public java.lang.String listTasks() {
    return "Úlohy";
  }
  
  public java.lang.String listLogout() {
    return "odhlásiť";
  }
  
  public java.lang.String listLogoutConfirmation() {
    return "Odhlásenie vymaže Váš zoznam úloh, pokračovať?";
  }
}
