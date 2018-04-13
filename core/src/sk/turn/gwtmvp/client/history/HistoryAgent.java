package sk.turn.gwtmvp.client.history;

public abstract class HistoryAgent {
  void onHistoryChanged(String historyToken) {
    History.onHistoryChanged(this, historyToken);
  }

  protected abstract void start();
  protected abstract void stop();
  protected abstract String encodeHistoryToken(String historyToken);
  protected abstract String getToken();
  protected abstract void setToken(String token);
  protected abstract void replaceToken(String token);
}
