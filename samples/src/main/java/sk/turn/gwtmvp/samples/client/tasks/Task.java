package sk.turn.gwtmvp.samples.client.tasks;

import com.google.gwt.core.client.JavaScriptObject;

public class Task extends JavaScriptObject {
  protected Task() { }

  public final native String getName() /*-{ return this.name; }-*/;
  public final native void setName(String name) /*-{ this.name = name; }-*/;
  public final native boolean isFinished() /*-{ return this.finished; }-*/;
  public final native void setFinished(boolean finished) /*-{ this.finished = finished; }-*/;
}
