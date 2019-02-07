/*
 * Copyright 2016 Turn s.r.o.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package sk.turn.gwtmvp.gen;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.IncrementalGenerator;
import com.google.gwt.core.ext.RebindMode;
import com.google.gwt.core.ext.RebindResult;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.JParameterizedType;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.dev.resource.Resource;
import com.google.gwt.dev.util.Util;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import sk.turn.gwtmvp.client.AsyncView;
import sk.turn.gwtmvp.client.Control;
import sk.turn.gwtmvp.client.HandlerView;
import sk.turn.gwtmvp.client.HtmlElement;
import sk.turn.gwtmvp.client.HtmlHandler;
import sk.turn.gwtmvp.client.View;
import sk.turn.gwtmvp.client.ViewHtml;

public class ViewGenerator extends IncrementalGenerator {

  @Override
  public long getVersionId() {
    return 7;
  }

  @Override
  public RebindResult generateIncrementally(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException {
    try {
      TypeOracle typeOracle = context.getTypeOracle();
      JClassType controlClassType = typeOracle.findType(Control.class.getName());
      JClassType viewType = typeOracle.getType(typeName);
      boolean asyncView = (viewType.getAnnotation(AsyncView.class) != null);
      ViewHtml viewHtml = viewType.getAnnotation(ViewHtml.class);
      String packageName = viewType.getPackage().getName();
      String generatedClassName = viewType.getName().replace('.', '_') + "Impl";
      PrintWriter w = context.tryCreate(logger, packageName, generatedClassName);
      if (w == null) {
        return new RebindResult(RebindMode.USE_ALL_CACHED, packageName + "." + generatedClassName);
      }
      JParameterizedType superType = null;
      String handlerType = null;
      for (JClassType intfc : viewType.getImplementedInterfaces()) {
        if (intfc.getQualifiedSourceName().equals(View.class.getName()) || intfc.getQualifiedSourceName().equals(HandlerView.class.getName())) {
          superType = intfc.isParameterized();
          if (intfc.getQualifiedSourceName().equals(HandlerView.class.getName())) {
            handlerType = superType.getTypeArgs()[1].getQualifiedSourceName();
          }
          break;
        }
      }
      if (superType == null) {
        throw new Exception(typeName + " does not inherit from " + View.class.getName());
      }
      // If the view HTML is not inline, make sure the view HTML file exists
      Resource htmlResource = null;
      if (viewHtml == null) {
        htmlResource = context.getResourcesOracle().getResource(packageName.replace('.', '/') + "/" + viewType.getSimpleSourceName() + ".html");
        if (htmlResource == null) {
          throw new Exception("Cannot find " + viewType.getSimpleSourceName() + ".html");
        }
      }
      String rootElementType = superType.getTypeArgs()[0].getQualifiedSourceName();
      // Map all annotated methods
      Map<String, JMethod> fieldsMap = new LinkedHashMap<>();
      // elemId -> { com.google.gwt.event.dom.client.???Handler -> JMethod, ... }, ...
      Map<String, Map<String, JMethod>> handlersMap = new LinkedHashMap<>();
      for (JMethod method : viewType.getMethods()) {
        // Check element mapping
        HtmlElement elemAnn = method.getAnnotation(HtmlElement.class);
        if (elemAnn != null) {
          if (method.getParameters().length > 0) {
            throw new Exception("Method " + typeName + "." + method.getName() + "() must have zero parameters (has " + method.getParameters().length + ").");
          }
          String id = elemAnn.value();
          if (id.equals("")) {
            id = method.getName();
            if (id.startsWith("get") && id.length() > 3) {
              id = id.substring(3, 4).toLowerCase() + id.substring(4);
            }
          }
          fieldsMap.put(id, method);
        }
        // Check handler mapping
        HtmlHandler handlerAnn = method.getAnnotation(HtmlHandler.class);
        if (handlerAnn != null) {
          JParameter param = (method.getParameters().length == 1 ? method.getParameters()[0] : null);
          JClassType paramType = (param != null ? param.getType().isInterface() : null);
          if (paramType == null || !paramType.isAssignableTo(typeOracle.getType(EventHandler.class.getName()))) {
            throw new Exception("Method " + typeName + "." + method.getName() + "() must have exactly one parameter of an EventHandler subinterface (has "
                + (paramType == null ? "none" : paramType.getQualifiedSourceName()) + ").");
          }
          for (String val : handlerAnn.value()) {
            Map<String, JMethod> methods = handlersMap.computeIfAbsent(val, k -> new LinkedHashMap<>());
            if (methods.containsKey(paramType.getQualifiedSourceName())) {
              throw new Exception("Element \"" + val + "\" already has a " + paramType.getName() + " defined.");
            }
            methods.put(paramType.getQualifiedSourceName(), method);
          }
        }
      }
      // Check whether dictionary class is defined
      String html = (viewHtml != null ? viewHtml.value() : Util.readStreamAsString(htmlResource.openContents()));
      Matcher dictMatcher = Pattern.compile("^<[^<]+data-mvp-dict=\"(.+)\"").matcher(html);
      String dictClassName = null;
      JClassType dictClass = null;
      if (dictMatcher.find()) {
        dictClassName = dictMatcher.group(1);
        if ((dictClass = typeOracle.getType(dictClassName)) == null) {
          throw new Exception("Localization classs " + dictClassName + " does not exist");
        }
      }
      // Check whether data-gwtid is still being used
      if (html.contains("data-gwtid=\"")) {
        logger.log(TreeLogger.Type.WARN, "The use of \"data-gwtid\" attribute is deprecated and its support will be removed in future versions, please use \"data-mvp-id\" instead.");
      }
      w.println("package " + packageName + ";");
      w.println();
      w.println("import com.google.gwt.core.client.GWT;");
      w.println("import com.google.gwt.dom.client.Element;");
      w.println("import com.google.gwt.dom.client.Document;");
      w.println("import com.google.gwt.dom.client.Node;");
      w.println("import com.google.gwt.dom.client.NodeList;");
      w.println("import com.google.gwt.event.dom.client.DomEvent;");
      w.println("import com.google.gwt.event.shared.HandlerManager;");
      w.println("import com.google.gwt.resources.client.ClientBundle;");
      w.println("import com.google.gwt.resources.client.ExternalTextResource;");
      w.println("import com.google.gwt.resources.client.ResourceCallback;");
      w.println("import com.google.gwt.resources.client.ResourceException;");
      w.println("import com.google.gwt.resources.client.TextResource;");
      w.println("import com.google.gwt.user.client.Event;");
      w.println("import com.google.gwt.user.client.EventListener;");
      w.println("import java.util.HashMap;");
      w.println("import java.util.Map;");
      w.println("import java.util.logging.Level;");
      w.println("import java.util.logging.Logger;");
      w.println("import sk.turn.gwtmvp.client.Control;");
      w.println();
      w.println("public class " + generatedClassName + " implements " + viewType.getQualifiedSourceName() + " {");
      if (htmlResource != null) {
        w.println("  interface Resources extends ClientBundle {");
        w.println("    Resources INSTANCE = GWT.create(Resources.class);");
        w.println("    @Source(\"" + viewType.getSimpleSourceName() + ".html\") " + (asyncView ? "External" : "") + "TextResource htmlContent();");
        w.println("  }");
        w.println();
      }
      w.println("  private static final Logger LOG = Logger.getLogger(\"" + packageName + "." + generatedClassName + "\");");
      if (asyncView) {
        w.println("  private static String sHtml = null;");
      }
      w.println();
      w.println("  private " + rootElementType + " rootElement = null;");
      w.println("  private final Map<String, Element> elementsMap = new HashMap<>();");
      w.println("  private final Map<String, Control> controlsMap = new HashMap<>();");
      if (handlerType != null) {
        w.println("  private " + handlerType + " handler;");
      }
      w.println();
      w.println("  @Override");
      w.println("  public void loadView(final ViewLoadedHandler<" + rootElementType + "> viewLoadedHandler) {");
      w.println("    if (rootElement != null) {");
      w.println("      viewLoadedHandler.onViewLoaded(rootElement);");
      w.println("    }");
      if (asyncView) {
        w.println("    if (sHtml != null) {");
        w.println("      loadView(new String(sHtml), viewLoadedHandler);");
        w.println("    } else {");
        w.println("      try {");
        w.println("        Resources.INSTANCE.htmlContent().getText(new ResourceCallback<TextResource>() {");
        w.println("          public void onSuccess(TextResource r) {");
        w.println("            sHtml = r.getText();");
        w.println("            loadView(new String(sHtml), viewLoadedHandler);");
        w.println("          }");
        w.println("          public void onError(ResourceException e) {");
        w.println("            LOG.log(Level.SEVERE, \"Failed to load " + viewType.getSimpleSourceName() + ".html\", e);");
        w.println("            viewLoadedHandler.onViewLoaded(null);");
        w.println("          }");
        w.println("        });");
        w.println("      } catch (ResourceException e) {");
        w.println("        LOG.log(Level.SEVERE, \"Failed to load " + viewType.getSimpleSourceName() + ".html\", e);");
        w.println("        viewLoadedHandler.onViewLoaded(null);");
        w.println("      }");
        w.println("    }");
      } else if (viewHtml != null) {
        w.println("    loadView(\"" + escapeJavaString(viewHtml.value()) + "\", viewLoadedHandler);");
      } else {
        w.println("    loadView(Resources.INSTANCE.htmlContent().getText(), viewLoadedHandler);");
      }
      w.println("  }");
      w.println();
      w.println("  private void loadView(String html, ViewLoadedHandler<" + rootElementType + "> viewLoadedHandler) {");
      // Replace any dictionary entries
      new SafeHtmlBuilder();
      if (dictClassName != null) {
        w.println("    " + dictClassName + " dict = GWT.create(" + dictClassName + ".class);");
        w.println("    Object dictEntry;");
        dictMatcher = Pattern.compile("\\{mvpDict\\.([^}]+)\\}").matcher(html);
        Set<String> replacedEntries = new HashSet<>();
        while (dictMatcher.find()) {
          String dictEntry = dictMatcher.group(1);
          if (!replacedEntries.contains(dictEntry)) {
            try {
              dictClass.getMethod(dictEntry, new JType[] { });
            } catch (NotFoundException e) {
              throw new Exception("Localization method " + dictClassName + "." + dictEntry + "() does not exist.");
            }
            w.println("    dictEntry = dict." + dictEntry + "();");
            w.println("    html = html.replace(\"" + dictMatcher.group(0) + "\", dictEntry instanceof com.google.gwt.safehtml.shared.SafeHtml ? ((com.google.gwt.safehtml.shared.SafeHtml)dictEntry).asString() : dictEntry.toString());");
            replacedEntries.add(dictEntry);
          }
        }
      }
      // Create all the controls here
      boolean controlDefined = false;
      for (Map.Entry<String, JMethod> entry : fieldsMap.entrySet()) {
        if (isMethodReturningType(entry.getValue(), controlClassType)) {
          if (!controlDefined) {
            w.println("    Control ctrl;");
            controlDefined = true;
          }
          JClassType controlViewClass = entry.getValue().getReturnType().isClass().getSuperclass().isParameterized().getTypeArgs()[0];
          // Show compiler warning when controlViewClass is annotated with @AsyncView
          if (controlViewClass.isAnnotationPresent(AsyncView.class)) {
            logger.log(TreeLogger.Type.WARN, entry.getValue().getReturnType().getQualifiedSourceName() + ".onShow() or .onHide() may be called before .onViewLoaded() for async control views.");
          }
          w.println("    ctrl = new " + entry.getValue().getReturnType().getQualifiedSourceName() + "();");
          w.println("    if (ctrl.getView() == null) {");
          w.println("      ctrl.setView((" + controlViewClass.getQualifiedSourceName() + ") GWT.create(" + controlViewClass.getQualifiedSourceName() + ".class));");
          w.println("    }");
          w.println("    controlsMap.put(\"" + entry.getKey() + "\", ctrl);");
        }
      }
      // Load the view HTML
      w.println("    Element tempElem = Document.get().create" + (
          rootElementType.equals("com.google.gwt.dom.client.TableRowElement") ? "TBody" :
            rootElementType.equals("com.google.gwt.dom.client.TableCellElement") ? "TR" : "Div") + "Element();");
      w.println("    tempElem.setInnerHTML(html);");
      w.println("    rootElement = (" + rootElementType + ") tempElem.getFirstChild();");
      if (dictClassName != null) {
        w.println("    rootElement.removeAttribute(\"data-mvp-dict\");");
      }
      // Loop through all the sub-elements and add the attributed elements to map or replace control elements with view HTML
      w.println("    addElementToMap(rootElement);");
      w.println("    NodeList<Element> elements = rootElement.getElementsByTagName(\"*\");");
      w.println("    for (int i = 0; i < elements.getLength(); i++) {");
      w.println("      addElementToMap(elements.getItem(i));");
      w.println("    }");
      // Make sure we have all the declared elements/controls
      for (Map.Entry<String, JMethod> entry : fieldsMap.entrySet()) {
        w.println("    if (elementsMap.get(\"" + entry.getKey() + "\") == null && controlsMap.get(\"" + entry.getKey() + "\") == null) {");
        w.println("      LOG.severe(\"Could not find element with data-mvp-id=\\\"" + entry.getKey() + "\\\" in " + viewType.getSimpleSourceName() + ".html.\");");
        w.println("    }");
      }
      // Make sure we have all the elements for handlers
      for (Map.Entry<String, Map<String, JMethod>> entry : handlersMap.entrySet()) {
        w.println("    if (elementsMap.get(\"" + entry.getKey() + "\") == null) {");
        w.println("      LOG.severe(\"Could not find element with data-mvp-id=\\\"" + entry.getKey() + "\\\" in " + viewType.getSimpleSourceName() + ".html.\");");
        w.println("    }");
      }
      if (handlerType != null) {
        // Map @HtmlHandlers of enclosing class
        JClassType enclosingType = viewType.getEnclosingType();
        JMethod[] methods = (enclosingType != null ? enclosingType.getMethods() : new JMethod[] { });
        JClassType baseEventHandlerType = typeOracle.getType(EventHandler.class.getName());
        for (JMethod method : methods) {
          HtmlHandler handlerAnnotation = method.getAnnotation(HtmlHandler.class);
          if (handlerAnnotation == null) {
            continue;
          }
          JClassType eventType = method.getParameters()[0].getType().isClass();
          // find event handler type
          JParameterizedType eventSuperclassType = eventType.isClass().getSuperclass().isParameterized();
          if (eventSuperclassType == null || eventSuperclassType.getTypeArgs().length != 1
              || !baseEventHandlerType.isAssignableFrom(eventSuperclassType.getTypeArgs()[0])) {
            throw new IllegalArgumentException("Event " + eventType.getName() + " is expected to inherit from class with single type parameter being subclass of EventHandler");
          }
          JClassType eventHandlerType = eventSuperclassType.getTypeArgs()[0];
          // find event handler's "handling" method
          JMethod handlerMethod = Arrays.stream(eventHandlerType.getOverridableMethods())
                  .filter(m -> m.getParameters().length == 1 && eventType == m.getParameterTypes()[0])
                  .findFirst()
                  .orElse(null);
          if (handlerMethod == null) {
            throw new IllegalArgumentException("EventHandler " + eventHandlerType.getName() + " is expected to have overridable method that takes single parameter of type " + eventType.getName());
          }
          for (String elemId : handlerAnnotation.value()) {
            w.println("    sk.turn.gwtmvp.client.EventManager.setEventHandler(getElement(\"" + elemId + "\"), " + eventType.getQualifiedSourceName() + ".getType(), new " + eventHandlerType.getQualifiedSourceName() + "() {");
            w.println("      @Override");
            w.println("      public void " + handlerMethod.getName() + "(" + eventType.getQualifiedSourceName() + " event) {");
            w.println("        if (handler != null) {");
            w.println("          try { handler." + method.getName() + "(event); }");
            w.println("          catch (Exception e) { LOG.log(Level.SEVERE, \"Invoke of " + enclosingType.getName() + "." + method.getName() + " failed\", e); }");
            w.println("        } else {");
            w.println("          LOG.severe(\"Ignoring " + enclosingType.getName() + "." + method.getName() + " - no HandlerView.handler set\");");
            w.println("        }");
            w.println("      }");
            w.println("    });");
          }
        }
      }
      w.println("    viewLoadedHandler.onViewLoaded(rootElement);");
      w.println("  }");
      w.println();
      w.println("  @Override");
      w.println("  public " + rootElementType + " getRootElement() {");
      w.println("    return rootElement;");
      w.println("  }");
      w.println();
      w.println("  @Override");
      w.println("  public <E2 extends Element> E2 getElement(String mvpId) {");
      w.println("    return (E2) elementsMap.get(mvpId);");
      w.println("  }");
      w.println();
      w.println("  @Override");
      w.println("  public <C extends Control<?>> java.util.Collection<C> getControls() {");
      w.println("    return (java.util.Collection<C>) controlsMap.values();");
      w.println("  }");
      if (handlerType != null) {
        w.println();
        w.println("  @Override");
        w.println("  public void setHandler(" + handlerType + " handler) {");
        w.println("    this.handler = handler;");
        w.println("  }");
      }
      for (Map.Entry<String, JMethod> entry : fieldsMap.entrySet()) {
        w.println();
        w.println("  @Override");
        w.println("  public " + entry.getValue().getReturnType().getQualifiedSourceName() + " " + entry.getValue().getName() + "() {");
        if (isMethodReturningType(entry.getValue(), controlClassType)) {
          w.println("    return (" + entry.getValue().getReturnType().getQualifiedSourceName() + ") controlsMap.get(\"" + entry.getKey() + "\");");
        } else {
          w.println("    return elementsMap.get(\"" + entry.getKey() + "\").<" + entry.getValue().getReturnType().getQualifiedSourceName() + ">cast();");
        }
        w.println("  }");
      }
      for (JMethod method : viewType.getMethods()) {
        HtmlHandler handlerAnn = method.getAnnotation(HtmlHandler.class);
        if (handlerAnn == null) {
          continue;
        }
        String paramType = method.getParameters()[0].getType().getQualifiedSourceName();
        String eventType = paramType.substring(0, paramType.length() - 7) + "Event.getType()";
        w.println();
        w.println("  @Override");
        w.println("  public void " + method.getName() + "(" + paramType + " handler) {");
        for (String id : handlerAnn.value()) {
          w.println("    sk.turn.gwtmvp.client.EventManager.setEventHandler(getElement(\"" + id + "\"), " + eventType + ", handler);");
        }
        w.println("  }");
      }
      w.println();
      w.println("  private void addElementToMap(Element element) {");
      w.println("    String attrName = (element.hasAttribute(\"data-mvp-id\") ? \"data-mvp-id\" : element.hasAttribute(\"data-gwtid\") ? \"data-gwtid\" : null);");
      w.println("    if (attrName == null) {");
      w.println("      return;");
      w.println("    }");
      w.println("    String mvpId = element.getAttribute(attrName);");
      w.println("    if (mvpId.equals(\"\")) {");
      w.println("      return;");
      w.println("    }");
      w.println("    element.removeAttribute(attrName);");
      w.println("    if (element.getTagName().equalsIgnoreCase(\"object\")) {");
      w.println("      Control control = controlsMap.get(mvpId);");
      w.println("      if (control == null) {");
      w.println("        LOG.severe(\"Control \" + mvpId + \" is not declared in " + typeName + "\");");
      w.println("        return;");
      w.println("      }");
      w.println("      control.getView().loadView(new ViewLoadedHandler() {");
      w.println("        @Override");
      w.println("        public void onViewLoaded(Element rootElement) {");
      w.println("          final com.google.gwt.core.client.JsArray<Node> attributes = getAttributes(element);");
      w.println("          for (int i = 0; i < attributes.length(); i ++) {");
      w.println("            rootElement.setAttribute(attributes.get(i).getNodeName(), attributes.get(i).getNodeValue());");
      w.println("          }");
      w.println("          element.getParentElement().replaceChild(rootElement, element);");
      w.println("          control.onViewLoaded();");
      w.println("        }");
      w.println("      });");
      w.println("    } else {");
      w.println("      elementsMap.put(mvpId, element);");
      w.println("    }");
      w.println("  }");
      w.println();
      w.println("  private native com.google.gwt.core.client.JsArray<Node> getAttributes(Element elem) /*-{");
      w.println("    return elem.attributes;");
      w.println("  }-*/;");
      w.println("}");
      context.commit(logger, w);
      return new RebindResult(RebindMode.USE_ALL_NEW, packageName + "." + generatedClassName);
    } catch (Exception e) {
      logger.log(TreeLogger.Type.ERROR, "Failed generating wrapper for class " + typeName + ": " + e.getMessage());
      throw new UnableToCompleteException();
    }
  }

  private String escapeJavaString(String str) {
    return str.replace("\\", "\\\\").replace("\"", "\\\"").replace("\b", "\\b").replace("\n", "\\n")
        .replace("\t", "\\t").replace("\f", "\\f").replace("\r", "\\r");
  }

  private boolean isMethodReturningType(JMethod method, JClassType classType) {
    JClassType returnClassType = method.getReturnType().isClass();
    return returnClassType != null && returnClassType.isAssignableTo(classType);
  }

}
