# Controls
Views are fine and reusable as they are. But unfortunately they're just dummy UI elements and lack any logic whatsoever -- that has to be in the Presenter or whoever handles the view. `Control`s are a way to implement reusable components with own logic contained within.

In this sample we'll create a `TimeControl` which shows the current time in customisable format. The control itself handles properly starting/stopping the timer responsible for updating the time.

```java
public class TimeControl extends Control<TimeControl.TimeView> {
  @ViewHtml("<span></span>")
  interface TimeView extends View<Element> { }

  private DateTimeFormat format = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_MEDIUM);
  private Timer timer = new Timer() {
    @Override
    public void run() {
      updateTime();
    }
  };

  public void setFormat(DateTimeFormat format) {
    this.format = format;
  }

  @Override
  public void onShow() {
    updateTime();
    timer.scheduleRepeating(1000);
  }

  @Override
  public void onHide() {
    timer.cancel();
  }

  private void updateTime() {
    view.getRootElement().setInnerText(format.format(new Date()));
  }
}
```

The class extends `Control` and defines which `View` is responsible for showing the content (this part is the same as with Presenters). The view itself is just an empty span. As with presenters, controls also have lifecycle callback methods: `onViewLoaded`, `onShow` and `onHide`. **Note** that the onShow and onHide callbacks are invoked after and before the presenter onShow and onHide methods respectively. It's not invoked when the control is hidden/shown in the HTML DOM.

If you want to include a control in a view HTML use the `<object>` element with the usual `data-mvp-id` attribute, e.g.:
```html
<div class="col-md-6">
  <object data-mvp-id="time1"></object>
</div>
<div class="col-md-6">
  <object data-mvp-id="time2"></object>
</div>
```

The `<object>` element will be fully replaced with the root element of the control. However the attributes of the object will be applied to the root control element. Not only that, but the attributes of the object take precedence over the attributes in the root element.

You *have* to declare all controls in your `View` interface, because that's how the UI binding library knows what the actual type (class) of the control is.
```java
interface ControlView extends View<Element> {
  @HtmlElement TimeControl getTime1();
  @HtmlElement TimeControl getTime2();
}
```
