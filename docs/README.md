# GWT MVP
[MVP](https://en.wikipedia.org/wiki/Model-view-presenter) library for [GWT](http://www.gwtproject.org/), adding a light-weighted, easy-to-use MVP framework, plain HTML-to-Java binding and at the same time taking the complexity off of the built-in widgets library.

* [GWT MVP Showcase](https://turnsk.github.io/gwtmvp/)
* [Read Javadoc](https://jitpack.io/sk/turn/gwtmvp/1.8.6/javadoc/)
* [Download JAR](https://jitpack.io/sk/turn/gwtmvp/1.8.6/gwtmvp-1.8.6.jar)

## Assumptions and Goals
Following assumptions were considered when designing this library:
* GWT is a great tool with amazing potential, but at the same time the learning curve is anything but steep
* We find that one of the most difficult part to comprehend and use effectively is the Widget library
* Out-of-the box GWT has no MVC/MVP/anything alike framework that would "force" the developers to write clean and manageable code
* Most people that start with GWT already have HTML/CSS (or even JS?) background and are not scared to death by it

What this library is trying to provide:
* Plain bind-able HTML support within GWT (so that you can write and have full control of the HTML directly and not worry what overhead will a widget generate)
* Light-weight (read "no huge code-base and few classes to learn") and easy-to-use (read "as little code overhead as possible") MVP framework
* HTML binding can be used standalone without the MVP part

## Build Configuration

### JAR file
1. Download the latest JAR file, copy it to your project's libs directory and add it to your project's classpath
2. Add the following entry to your module .gwt.xml file:
```xml
<inherits name='sk.turn.gwtmvp.GwtMvp'/>
```

### Gradle
1. In the `build.gradle` add the following repository:
```gradle
repositories {
  ...
  maven { url 'https://jitpack.io/' }
}
```
2. Add the dependency:
```gradle
dependencies {
  ...
  providedCompile 'sk.turn:gwtmvp:1.8.6'
}
```

## Contents
* [Views](./VIEWS.md)
* [Presenters](./PRESENTERS.md)
* [Loaders](./LOADERS.md)
* [View adapters](./ADAPTERS.md)
* [Controls](./CONTROLS.md)
* [Internationalization](./I18N.md)
