package sk.turn.gwtmvp.gen;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.JParameterizedType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.event.shared.EventHandler;

import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import sk.turn.gwtmvp.client.HtmlElement;
import sk.turn.gwtmvp.client.HtmlHandler;
import sk.turn.gwtmvp.client.View;

/**
 * Created by tomas on 20/09/16.
 */

public class ViewGenerator extends Generator {

  @Override
  public String generate(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException {
    TypeOracle typeOracle = context.getTypeOracle();
    try {
      JClassType viewType = typeOracle.getType(typeName);
      String packageName = viewType.getPackage().getName();
      String generatedClassName = viewType.getName().replace('.', '_') + "Gen";
      PrintWriter writer = context.tryCreate(logger, packageName, generatedClassName);
      if (writer == null) {
        return null;
      }
      JParameterizedType superType = null;
      for (JClassType intfc : viewType.getImplementedInterfaces()) {
        if (intfc.getQualifiedSourceName().equals(View.class.getName())) {
          superType = intfc.isParameterized();
          break;
        }
      }
      if (superType == null) {
        throw new Exception(typeName + " does not inherit from " + View.class.getName());
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
            Map<String, JMethod> methods = handlersMap.get(val);
            if (methods == null) {
              methods = new LinkedHashMap<>();
              handlersMap.put(val, methods);
            }
            if (methods.containsKey(paramType.getQualifiedSourceName())) {
              throw new Exception("Element \"" + val + "\" already has a " + paramType.getName() + " defined.");
            }
            methods.put(paramType.getQualifiedSourceName(), method);
          }
        }
      }
      writer.println("package " + packageName + ";");
      writer.println();
      writer.println("import com.google.gwt.core.client.GWT;");
      writer.println("import com.google.gwt.dom.client.Element;");
      writer.println("import com.google.gwt.dom.client.DivElement;");
      writer.println("import com.google.gwt.dom.client.Document;");
      writer.println("import com.google.gwt.dom.client.NodeList;");
      writer.println("import com.google.gwt.event.dom.client.DomEvent;");
      writer.println("import com.google.gwt.event.shared.HandlerManager;");
      writer.println("import com.google.gwt.resources.client.ClientBundle;");
      writer.println("import com.google.gwt.resources.client.TextResource;");
      writer.println("import com.google.gwt.user.client.Event;");
      writer.println("import com.google.gwt.user.client.EventListener;");
      writer.println("import java.util.HashMap;");
      writer.println("import java.util.Map;");
      writer.println("import java.util.logging.Logger;");
      writer.println();
      writer.println("public class " + generatedClassName + " implements " + viewType.getQualifiedSourceName() + " {");
      writer.println("  interface Resources extends ClientBundle {");
      writer.println("    Resources INSTANCE = GWT.create(Resources.class);");
      writer.println("    @Source(\"" + viewType.getSimpleSourceName() + ".html\") TextResource htmlContent();");
      writer.println("  }");
      writer.println();
      writer.println("  private static final Logger LOG = Logger.getLogger(\"" + packageName + "." + generatedClassName + "\");");
      writer.println();
      writer.println("  private " + rootElementType + " rootElement = null;");
      for (Map.Entry<String, JMethod> entry : fieldsMap.entrySet()) {
        writer.println("  private " + entry.getValue().getReturnType().getQualifiedSourceName() + " generated_" + entry.getKey() + " = null;");
      }
      writer.println("  private final Map<String, HandlerManager> handlerManagers = new HashMap<>();");
      writer.println();
      writer.println("  @Override");
      writer.println("  public " + rootElementType + " getRootElement() {");
      writer.println("    if (rootElement != null) {");
      writer.println("      return rootElement;");
      writer.println("    }");
      writer.println("    DivElement tempDiv = Document.get().createDivElement();");
      writer.println("    tempDiv.setInnerHTML(Resources.INSTANCE.htmlContent().getText());");
      writer.println("    rootElement = (" + rootElementType + ") tempDiv.getFirstChild();");
      writer.println("    Map<String, Element> elementsMap = new HashMap<>();");
      writer.println("    addElementToMap(rootElement, elementsMap);");
      writer.println("    NodeList<Element> elements = rootElement.getElementsByTagName(\"*\");");
      writer.println("    for (int i = 0; i < elements.getLength(); i++) {");
      writer.println("      addElementToMap(elements.getItem(i), elementsMap);");
      writer.println("    }");
      for (Map.Entry<String, JMethod> entry : fieldsMap.entrySet()) {
        writer.println("    generated_" + entry.getKey() + " = (" + entry.getValue().getReturnType().getQualifiedSourceName() + ") elementsMap.get(\"" + entry
            .getKey() + "\");");
        writer.println("    if (generated_" + entry.getKey() + " == null) {");
        writer.println("      LOG.severe(\"Could not find element with data-gwtid=\\\"" + entry.getKey() + "\\\" in " + viewType.getSimpleSourceName()
            + ".html.\");");
        writer.println("    }");
      }
      for (Map.Entry<String, Map<String, JMethod>> entry : handlersMap.entrySet()) {
        writer.println("    final Element element_" + entry.getKey() + " = elementsMap.get(\"" + entry.getKey() + "\");");
        writer.println("    if (element_" + entry.getKey() + " == null) {");
        writer.println("      LOG.severe(\"Could not find element with data-gwtid=\\\"" + entry.getKey() + "\\\" in " + viewType.getSimpleSourceName()
            + ".html.\");");
        writer.println("    } else {");
        writer.println("      handlerManagers.put(\"" + entry.getKey() + "\", new HandlerManager(element_" + entry.getKey() + "));");
        for (Map.Entry<String, JMethod> entry2 : entry.getValue().entrySet()) {
          JClassType paramType = typeOracle.getType(entry2.getKey());
          if (!paramType.getName().endsWith("Handler")) {
            throw new Exception("Unexpected type (" + paramType.getQualifiedSourceName() + "), was expecting \"...Handler\".");
          }
          writer.println("      Event.sinkEvents(element_" + entry.getKey() + ", Event.ON" + paramType.getName().substring(0, paramType.getName().length() - 7)
              .toUpperCase() + ");");
        }
        writer.println("      Event.setEventListener(element_" + entry.getKey() + ", new EventListener() {");
        writer.println("        @Override");
        writer.println("        public void onBrowserEvent(Event event) {");
        writer.println("          DomEvent.fireNativeEvent(event, handlerManagers.get(\"" + entry.getKey() + "\"), element_" + entry.getKey() + ");");
        writer.println("        }");
        writer.println("      });");
        writer.println("    }");
      }
      writer.println("    return rootElement;");
      writer.println("  }");
      for (Map.Entry<String, JMethod> entry : fieldsMap.entrySet()) {
        writer.println();
        writer.println("  @Override");
        writer.println("  public " + entry.getValue().getReturnType().getQualifiedSourceName() + " " + entry.getValue().getName() + "() {");
        writer.println("    return generated_" + entry.getKey() + ";");
        writer.println("  }");
      }
      for (JMethod method : viewType.getMethods()) {
        HtmlHandler handlerAnn = method.getAnnotation(HtmlHandler.class);
        if (handlerAnn == null) {
          continue;
        }
        String paramType = method.getParameters()[0].getType().getQualifiedSourceName();
        writer.println();
        writer.println("  @Override");
        writer.println("  public void " + method.getName() + "(" + paramType + " handler) {");
        writer.println("    HandlerManager hm;");
        for (String id : handlerAnn.value()) {
          writer.println("    hm = handlerManagers.get(\"" + id + "\");");
          writer.println("    if (hm != null) {");
          writer.println("      hm.addHandler(" + paramType.substring(0, paramType.length() - 7) + "Event.getType(), handler);");
          writer.println("    }");
        }
        writer.println("  }");
      }
      writer.println();
      writer.println("  private void addElementToMap(Element element, Map<String, Element> elementsMap) {");
      writer.println("    String gwtid = element.getAttribute(\"data-gwtid\");");
      writer.println("    if (gwtid != null && !gwtid.equals(\"\")) {");
      writer.println("      element.removeAttribute(\"data-gwtid\");");
      writer.println("      elementsMap.put(gwtid, element);");
      writer.println("    }");
      writer.println("  }");
      writer.println("}");
      context.commit(logger, writer);
      return packageName + "." + generatedClassName;
    } catch (Exception e) {
      logger.log(TreeLogger.Type.ERROR, "Failed generating wrapper for class " + typeName, e);
      throw new UnableToCompleteException();
    }
  }

}
