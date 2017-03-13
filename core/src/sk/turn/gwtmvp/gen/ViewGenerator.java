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
import java.util.LinkedHashMap;
import java.util.Map;

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
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.event.shared.EventHandler;

import sk.turn.gwtmvp.client.HtmlElement;
import sk.turn.gwtmvp.client.HtmlHandler;
import sk.turn.gwtmvp.client.View;

public class ViewGenerator extends IncrementalGenerator {
  
  @Override
  public long getVersionId() {
    return 2;
  }

  @Override
  public RebindResult generateIncrementally(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException {
    try {
      TypeOracle typeOracle = context.getTypeOracle();
      JClassType viewType = typeOracle.getType(typeName);
      String packageName = viewType.getPackage().getName();
      String generatedClassName = viewType.getName().replace('.', '_') + "Impl";
      PrintWriter w = context.tryCreate(logger, packageName, generatedClassName);
      if (w == null) {
        return new RebindResult(RebindMode.USE_ALL_CACHED, packageName + "." + generatedClassName);
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
      w.println("package " + packageName + ";");
      w.println();
      w.println("import com.google.gwt.core.client.GWT;");
      w.println("import com.google.gwt.dom.client.Element;");
      w.println("import com.google.gwt.dom.client.Document;");
      w.println("import com.google.gwt.dom.client.NodeList;");
      w.println("import com.google.gwt.event.dom.client.DomEvent;");
      w.println("import com.google.gwt.event.shared.HandlerManager;");
      w.println("import com.google.gwt.resources.client.ClientBundle;");
      w.println("import com.google.gwt.resources.client.TextResource;");
      w.println("import com.google.gwt.user.client.Event;");
      w.println("import com.google.gwt.user.client.EventListener;");
      w.println("import java.util.HashMap;");
      w.println("import java.util.Map;");
      w.println("import java.util.logging.Logger;");
      w.println();
      w.println("public class " + generatedClassName + " implements " + viewType.getQualifiedSourceName() + " {");
      w.println("  interface Resources extends ClientBundle {");
      w.println("    Resources INSTANCE = GWT.create(Resources.class);");
      w.println("    @Source(\"" + viewType.getSimpleSourceName() + ".html\") TextResource htmlContent();");
      w.println("  }");
      w.println();
      w.println("  private static final Logger LOG = Logger.getLogger(\"" + packageName + "." + generatedClassName + "\");");
      w.println();
      w.println("  private " + rootElementType + " rootElement = null;");
      for (Map.Entry<String, JMethod> entry : fieldsMap.entrySet()) {
        w.println("  private " + entry.getValue().getReturnType().getQualifiedSourceName() + " generated_" + entry.getKey() + " = null;");
      }
      if (fieldsMap.size() > 0 || handlersMap.size() > 0) {
        w.println("  private final Map<String, Element> elementsMap = new HashMap<>();");
      }
      w.println();
      w.println("  @Override");
      w.println("  public " + rootElementType + " getRootElement() {");
      w.println("    if (rootElement != null) {");
      w.println("      return rootElement;");
      w.println("    }");
      w.println("    Element tempElem = Document.get().create" + (
          rootElementType.equals("com.google.gwt.dom.client.TableRowElement") ? "TBody" :
            rootElementType.equals("com.google.gwt.dom.client.TableCellElement") ? "TR" : "Div") + "Element();");
      w.println("    tempElem.setInnerHTML(Resources.INSTANCE.htmlContent().getText());");
      w.println("    rootElement = (" + rootElementType + ") tempElem.getFirstChild();");
      if (fieldsMap.size() > 0 || handlersMap.size() > 0) {
        w.println("    addElementToMap(rootElement, elementsMap);");
        w.println("    NodeList<Element> elements = rootElement.getElementsByTagName(\"*\");");
        w.println("    for (int i = 0; i < elements.getLength(); i++) {");
        w.println("      addElementToMap(elements.getItem(i), elementsMap);");
        w.println("    }");
        for (Map.Entry<String, JMethod> entry : fieldsMap.entrySet()) {
          w.println("    generated_" + entry.getKey() + " = (" + entry.getValue().getReturnType().getQualifiedSourceName() + ") elementsMap.get(\"" + entry
              .getKey() + "\");");
          w.println("    if (generated_" + entry.getKey() + " == null) {");
          w.println("      LOG.severe(\"Could not find element with data-gwtid=\\\"" + entry.getKey() + "\\\" in " + viewType.getSimpleSourceName()
              + ".html.\");");
          w.println("    }");
        }
        for (Map.Entry<String, Map<String, JMethod>> entry : handlersMap.entrySet()) {
          w.println("    final Element element_" + entry.getKey() + " = elementsMap.get(\"" + entry.getKey() + "\");");
          w.println("    if (element_" + entry.getKey() + " == null) {");
          w.println("      LOG.severe(\"Could not find element with data-gwtid=\\\"" + entry.getKey() + "\\\" in " + viewType.getSimpleSourceName() + ".html.\");");
          w.println("    }");
        }
      }
      w.println("    return rootElement;");
      w.println("  }");
      for (Map.Entry<String, JMethod> entry : fieldsMap.entrySet()) {
        w.println();
        w.println("  @Override");
        w.println("  public " + entry.getValue().getReturnType().getQualifiedSourceName() + " " + entry.getValue().getName() + "() {");
        w.println("    return generated_" + entry.getKey() + ";");
        w.println("  }");
      }
      for (JMethod method : viewType.getMethods()) {
        HtmlHandler handlerAnn = method.getAnnotation(HtmlHandler.class);
        if (handlerAnn == null) {
          continue;
        }
        JClassType paramClassType = (JClassType) method.getParameters()[0].getType();
        String paramType = paramClassType.getQualifiedSourceName();
        w.println();
        w.println("  @Override");
        w.println("  public void " + method.getName() + "(" + paramType + " handler) {");
        w.println("    Element elem;");
        for (String id : handlerAnn.value()) {
          w.println("    elem = elementsMap.get(\"" + id + "\");");
          w.println("    if (elem != null) {");
          String eventType = paramType.substring(0, paramType.length() - 7) + "Event.getType()";
          w.println("      sk.turn.gwtmvp.client.EventManager.setEventHandler(elem, " + eventType + ", handler);");
          w.println("    }");
        }
        w.println("  }");
      }
      if (fieldsMap.size() > 0 || handlersMap.size() > 0) {
        w.println();
        w.println("  private void addElementToMap(Element element, Map<String, Element> elementsMap) {");
        w.println("    String gwtid = null;");
        w.println("    try {");
        w.println("      gwtid = element.getAttribute(\"data-gwtid\");");
        w.println("    } catch (Exception e) {");
        w.println("      LOG.warning(\"Unable to call getAttribute on \" + element.getTagName());");
        w.println("    }");
        w.println("    if (gwtid != null && !gwtid.equals(\"\")) {");
        w.println("      element.removeAttribute(\"data-gwtid\");");
        w.println("      elementsMap.put(gwtid, element);");
        w.println("    }");
        w.println("  }");
      }
      w.println("}");
      context.commit(logger, w);
      return new RebindResult(RebindMode.USE_ALL_NEW, packageName + "." + generatedClassName);
    } catch (Exception e) {
      logger.log(TreeLogger.Type.ERROR, "Failed generating wrapper for class " + typeName, e);
      throw new UnableToCompleteException();
    }
  }

}
