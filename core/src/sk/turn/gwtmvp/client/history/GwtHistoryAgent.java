package sk.turn.gwtmvp.client.history;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.History;

public class GwtHistoryAgent extends HistoryAgent {
  private HandlerRegistration historyHandlerRegistration;

  @Override
  protected void start() {
    if (historyHandlerRegistration == null) {
      historyHandlerRegistration = History.addValueChangeHandler(new ValueChangeHandler<String>() {
        @Override
        public void onValueChange(ValueChangeEvent<String> event) {
          onHistoryChanged(event.getValue());
        }
      });
    }
  }

  @Override
  protected void stop() {
    if (historyHandlerRegistration != null) {
      historyHandlerRegistration.removeHandler();
      historyHandlerRegistration = null;
    }
  }

  @Override
  protected String encodeHistoryToken(String historyToken) {
    return History.encodeHistoryToken(historyToken);
  }

  @Override
  protected String getToken() {
    return History.getToken();
  }

  @Override
  protected void setToken(String token) {
    History.newItem(token, false);
  }

  @Override
  protected void replaceToken(String token) {
    History.replaceItem(token, false);
  }
}
