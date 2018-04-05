/*
 * Copyright 2014 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
(function(){
  // Variables declared in this scope will be seen by both recompile_lib and the property providers.
  var $wnd = window;
  var $doc = $wnd.document;
  var __moduleName = 'showcase';

  // Because GWT linker architecture allows property providers to use global variables
  // we need to make sure that these are defined in the scope of property providers.
  // Initializes the property providers.
  // Note: The parameters of this function are used by property providers so they cannot be renamed
  // and need to be kept in sync with PropertySource.__getProvidersAndValues.
  // TODO(dankurka): refactor linkers and templates to not use global symbols anymore.
  var __initPropertyProviders = function(__gwt_getMetaProperty, __gwt_isKnownPropertyValue){
    var providers = {};
var values = {};
providers['locale'] = function(){
  var locale = null;
  var rtlocale = 'default';
  try {
  if (!locale) {
    var queryParam = location.search;
    var qpStart = queryParam.indexOf("locale=");
    if (qpStart >= 0) {
      var value = queryParam.substring(qpStart + 7);
      var end = queryParam.indexOf("&", qpStart);
      if (end < 0) {
        end = queryParam.length;
      }
      locale = queryParam.substring(qpStart + 7, end);
    }
  }
  if (!locale) {
    locale = __gwt_getMetaProperty("locale");
  }
  if (!locale) {
    locale = $wnd['__gwt_Locale'];
  }
  if (locale) {
    rtlocale = locale;
  }
  while (locale && !__gwt_isKnownPropertyValue("locale", locale)) {
    var lastIndex = locale.lastIndexOf("_");
    if (lastIndex < 0) {
      locale = null;
      break;
    }
    locale = locale.substring(0, lastIndex);
  }
} catch (e) {
  alert("Unexpected exception in locale detection, using default: " + e);

}
$wnd['__gwt_Locale'] = rtlocale;
return locale || "default";
}
;values['locale'] = {'default':0,'sk':1};providers['user.agent'] = function(){
  var ua = navigator.userAgent.toLowerCase();
  var docMode = $doc.documentMode;
  if ((function() { 
    return (ua.indexOf('webkit') != -1);
  })()) return 'safari';
  if ((function() { 
    return (ua.indexOf('msie') != -1 && (docMode >= 10 && docMode < 11));
  })()) return 'ie10';
  if ((function() { 
    return (ua.indexOf('msie') != -1 && (docMode >= 9 && docMode < 11));
  })()) return 'ie9';
  if ((function() { 
    return (ua.indexOf('msie') != -1 && (docMode >= 8 && docMode < 11));
  })()) return 'ie8';
  if ((function() { 
    return (ua.indexOf('gecko') != -1 || docMode >= 11);
  })()) return 'gecko1_8';
  return '';
}
;values['user.agent'] = {'gecko1_8':0,'ie10':1,'ie8':2,'ie9':3,'safari':4};

    return {values: values, providers: providers};
  };

  var executeMain = function() {
    // $namespace is used by the library to publish its symbols
    var $namespace = {};
    /*
 * Copyright 2014 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

// Export into our name space
// We do not consider any of these classes a public API and they will be changed as needed.
$namespace.lib = $namespace.lib || {};

StringHelper = {};

StringHelper.endsWith = function(str, suffix) {
  return str.indexOf(suffix, str.length - suffix.length) !== -1;
};

StringHelper.removeQueryString = function(str) {
  return str.split("#")[0].split("?")[0];
};

$namespace.lib.StringHelper = StringHelper;

/**
 * A PropertySource computes the binding properties to use for calling the GWT compiler.
 *
 * @constructor
 * @param {string} moduleName - the name of the gwt module
 * @param {Function} propertyProvidersHolder - a function returning property providers and
 *                       their values when invoked.
 * @param {MetaTagParser} metaTagParser - an instance of {@link MetaTagParser}
 */
function PropertySource(moduleName, propertyProvidersHolder, metaTagParser) {
  this.__moduleName = moduleName;
  this.__metaTagParser = metaTagParser;
  var props = this.__getProvidersAndValues(propertyProvidersHolder);
  this.__propertyProviders = props.providers;
  this.__propertyValues = props.values;
}

/**
 * Setup the environment that property providers need to run and initialize property providers
 * with that environment.
 *
 * @returns an object containing property providers and possible values.
 */
PropertySource.prototype.__getProvidersAndValues = function(propertyProvidersHolder) {
  // Setup scope for property providers
  var that = this;

  //Used by LocalePropertyGenerator
  var __gwt_getMetaProperty = function(name) {
    return that.__metaTagParser.get()[name];
  };

  // Used by LocalePropertyGenerator
  var __gwt_isKnownPropertyValue = function(propName, propValue) {
    return propValue in that.__propertyValues[propName];
  };

  // This function is defined in recompile_template.js and parameters being passed here need to be
  // kept in sync.
  return propertyProvidersHolder(__gwt_getMetaProperty, __gwt_isKnownPropertyValue);
};

/**
 * Compute the binding property value for a given property name.
 *
 * @param {string} propName - the binding property name
 * @returns {string} the computed value of the binding property
 */
PropertySource.prototype.__computePropValue = function(propName) {
  var val = this.__propertyProviders[propName]();
  // sanity check
  var allowedValuesMap = this.__propertyValues[propName];
  if (val in allowedValuesMap) {
    return val;
  } else {
    // TODO(dankurka): trigger this error in the ui
    // IE8 only has console defined if its dev tools have been opened before
    if ($wnd.console && $wnd.console.log) {
      $wnd.console.log("provider for " + propName
          + " returned unexpected value: '" + val + "'");
    }
    throw "can't compute binding property value for " + propName;
  }
};

/**
 * Compute all binding properties for a given module.
 * @returns {Object} a map of from binding properties to their current values
 */
PropertySource.prototype.computeBindingProperties = function() {
  var result = {};
  for (var key in this.__propertyValues) {
    if (this.__propertyValues.hasOwnProperty(key)) {
      result[key] = this.__computePropValue(key);
    }
  }
  return result;
};

$namespace.lib.PropertySource = PropertySource;

/**
 * Create a dialog.
 * @constructor
 */
function Dialog() {
  var dialog = $doc.createElement('div');
  dialog.style.zIndex = 1000001;
  dialog.style.position = 'fixed';
  dialog.style.top = '20pt';
  dialog.style.left = '20pt';
  dialog.style.right = '20pt';
  dialog.style.color = 'black';
  dialog.style.background = 'white';
  dialog.style.border = '4px solid #ccc';
  dialog.style.padding = '1em';
  dialog.style.borderRadius = '5px';
  dialog.style.wordWrap = 'break-word';
  this.__dialog = dialog;

  var overlay = $doc.createElement('div');
  overlay.style.zIndex = 1000000;
  overlay.style.position = 'absolute';
  overlay.style.top = 0;
  overlay.style.left = 0;
  overlay.style.bottom = 0;
  overlay.style.right = 0;
  overlay.style.background = 'black'; // darken background
  overlay.style.opacity = '0.5';
  this.__overlay = overlay;
}

/**
 * Create a DOM node with a given text.
 * @returns {external:Node} the node with the given text.
 */
Dialog.prototype.createTextElement = function(tagName, fontSize, text) {
  var elt = $doc.createElement(tagName);
  elt.style.color = 'black';
  elt.style.background = 'white';
  elt.style.fontSize = fontSize;
  elt.appendChild($doc.createTextNode(text));
  return elt;
};

/**
 * Add a node to the dialog.
 * @param {external:Node} node - the node to add to the dialog
 */
Dialog.prototype.add = function(node) {
  this.__dialog.appendChild(node);
};

/**
 * Remove all children from the dialog.
 */
Dialog.prototype.clear = function() {
  while (this.__dialog.firstChild) {
    this.__dialog.removeChild(this.__dialog.firstChild);
  }
};

/**
 * Show the dialog (add it to the body).
 */
Dialog.prototype.show = function() {
  $doc.body.appendChild(this.__overlay);
  $doc.body.appendChild(this.__dialog);
};

/**
 * Hide the dialog (remove it from the body).
 */
Dialog.prototype.hide = function() {
  $doc.body.removeChild(this.__overlay);
  $doc.body.removeChild(this.__dialog);
};

//Export Dialog to namespace
$namespace.lib.Dialog = Dialog;

/**
 * Construct a Recompiler object.
 * @constructor
 * @param {string} moduleName - the name of the gwt module
 * @param {Object} permutationProperties - A map of binding property names (string) to their values (string).
 * @returns
 */
function Recompiler(moduleName, permutationProperties) {
  if ($wnd.__gwt_sdm_globals) {
    this.__globals = $wnd.__gwt_sdm_globals;
  } else {
    this.__globals = {
      callbackCounter: new Date().getTime(), // avoid cache hits
      callbacks: {}
    };
    $wnd.__gwt_sdm_globals = this.__globals;
  }
  this.__moduleName = moduleName;
  this.__permutationProperties = permutationProperties;
  this.__compiling = false;
}

/**
 * Build the url that triggers a compile on the code server.
 * @returns {string} the url
 */
Recompiler.prototype.__buildCompileUrl = function() {
  var url = this.getCodeServerBaseUrl() + 'recompile/' + this.__moduleName + '?';
  var props = [];
  for (var key in this.__permutationProperties) {
    props.push($wnd.encodeURIComponent(key) + '=' +
        $wnd.encodeURIComponent(this.__permutationProperties[key]));
  }
  return url + props.join('&');
};

/**
 * Issue a compile request for the module of this Recompiler class.
 * @param {Function} finishCallback - the callback to invoke once compile finishes
 */
Recompiler.prototype.compile = function(finishCallback) {
  var that = this;
  this.__compiling = true;
  var compileUrl = this.__buildCompileUrl();
  this.__jsonp(compileUrl, function(data) {
    that.__compiling = false;
    finishCallback(data);
  });
};

/**
 * Issue a jsonp request.
 * @param {string} url - the url to use
 * @param {Function} callback - the callback to invoke
 */
Recompiler.prototype.__jsonp = function(url, callback) {
  var that = this;
  var callback_id = 'c' + this.__globals.callbackCounter++;
  this.__globals.callbacks[callback_id] = function(json) {
    delete that.__globals.callbacks[callback_id];
    callback(json);
  };

  var url = url + '&_callback=__gwt_sdm_globals.callbacks.' + callback_id;
  this.__injectScriptTag(url);
};

Recompiler.prototype.__injectScriptTag = function(url) {
  var script = $doc.createElement('script');
  script.src = url;
  var $head = $doc.head || $doc.getElementsByTagName('head')[0];
  $head.appendChild(script);
};

/**
 * Get protocol and host from a url.
 * @param {string} url
 */
Recompiler.prototype.__getBaseUrl = function(url) {
  var a = $doc.createElement('a');
  a.href = url;
  return a.protocol + '//' + a.host + '/';
};

/**
 * Get the base url of the code server.
 * @returns {string} the url of the code server
 */
Recompiler.prototype.getCodeServerBaseUrl = function() {
  var scriptTagsToSearch = $doc.getElementsByTagName('script');
  var expectedSuffix = '/recompile-requester/' + this.__moduleName;
  for (var i = 0; i < scriptTagsToSearch.length; i++) {
    var candidate = StringHelper.removeQueryString(scriptTagsToSearch[i].src);
    if (StringHelper.endsWith(candidate, expectedSuffix)) {
      return this.__getBaseUrl(candidate);
    }
  }
  throw "unable to find the script tag that includes /recompile-requester/" + moduleName ;
};

/**
 * Load the compiled application
 */
Recompiler.prototype.loadApp = function() {
  var url = this.getCodeServerBaseUrl() + this.__moduleName + '/' + this.__moduleName + '.nocache.js';
  var scriptTag = $doc.createElement('script');
  scriptTag.src = url;
  var $head = $doc.head || $doc.getElementsByTagName('head')[0];
  $head.insertBefore(scriptTag, $head.firstElementChild || $head.children[0]);
};

Recompiler.prototype.getLogUrl = function() {
  return this.getCodeServerBaseUrl() + 'log/' + this.__moduleName;
};

//Export Recompiler to namespace
$namespace.lib.Recompiler = Recompiler;

function MetaTagParser(moduleName) {
  this.__parsed = false;
  this.__metaProperties = null;
  this.__moduleName = moduleName;
}

MetaTagParser.prototype.__getMetaTags = function() {
  return $doc.getElementsByTagName('meta');
};

MetaTagParser.prototype.__parse = function() {
  var metaProps = {};
  var metas = this.__getMetaTags();

  for (var i = 0, n = metas.length; i < n; ++i) {
    var meta = metas[i];
    var name = meta.getAttribute('name');
    var content = meta.getAttribute('content');

    if (name) {
      name = name.replace(this.__moduleName + '::', '');
      if (name.indexOf('::') >= 0) {
        // It's for a different module
        continue;
      }
    }
    if (name == 'gwt:property' && content) {
      var value;
      var eq = content.indexOf('=');
      if (eq >= 0) {
        name = content.substring(0, eq);
        value = content.substring(eq+1);
      } else {
        name = content;
        value = '';
      }
      metaProps[name] = value;
    }
  }
  return metaProps;
};

MetaTagParser.prototype.get = function() {
  if (!this.__parsed) {
    this.__metaProperties = this.__parse();
    this.__parsed = true;
  }
  return this.__metaProperties;
};

//Export MetaTagParser to namespace
$namespace.lib.MetaTagParser = MetaTagParser;

/**
 * BaseUrlProvider provides the url to the original server that the app has been loaded from.
 * This is not the url of super dev mode.
 *
 * @constructor
 * @param {string} moduleName - the module for which we should determine the base url.
 */
function BaseUrlProvider(moduleName, metaTagParser) {
  this.__moduleName = moduleName;
  this.__metaTagParser = metaTagParser;
}

BaseUrlProvider.prototype.__getScriptTags = function() {
  return $doc.getElementsByTagName('script');
};

BaseUrlProvider.prototype.__stripHashQueryAndFileName = function(url) {
  // Truncate starting at the first '?' or '#', whichever comes first.
  var hashIndex = url.lastIndexOf('#');
  if (hashIndex == -1) {
    hashIndex = url.length;
  }

  var queryIndex = url.indexOf('?');
  if (queryIndex == -1) {
    queryIndex = url.length;
  }

  var slashIndex = url.lastIndexOf('/', Math.min(queryIndex, hashIndex));
  if (slashIndex < 0) {
    throw new 'Cannot find slash in: ' + url;
  }

  return url.substring(0, slashIndex + 1);
};

BaseUrlProvider.prototype.__maybeConvertToAbsoluteUrl = function(url) {
  // test for valid protocol starts
  if ((url.match(/^(?:(?:https)|(?:http)|(?:file)):\/\//))) {
    return url;
  }

  // Probably a relative URL; use magic to make the browser absolutify it.
  // I wish there were a better way to do this, but this seems the only
  // sure way!  (A side benefit is it preloads clear.cache.gif)
  // Note: this trick is harmless if the URL was really already absolute.
  var img = $doc.createElement("img");
  img.src = url + 'clear.cache.gif';
  return this.__stripHashQueryAndFileName(img.src);
};

BaseUrlProvider.prototype.getBaseUrl = function() {
  // This code follows the order in com/google/gwt/core/ext/linker/impl/computeScriptBase.js
  // Try to get the url from a meta property first
  var url = this.__getBaseUrlFromMetaTag();
  if (url) {
    return this.__maybeConvertToAbsoluteUrl(url);
  }

  // try the nocache next
  var expectedSuffix = this.__moduleName + '.nocache.js';
  var scriptTags = this.__getScriptTags();
  for (var i = 0; i < scriptTags.length; i++) {
    var candidate = StringHelper.removeQueryString(scriptTags[i].src);
    if (StringHelper.endsWith(candidate, expectedSuffix)) {
      var stripedUrl = this.__stripHashQueryAndFileName(candidate);
      return this.__maybeConvertToAbsoluteUrl(stripedUrl);
    }
  }

  //try the base tag
  var baseElements = this.__getBaseElements();
  if (baseElements.length > 0) {
    // It's always the last parsed base tag that will apply to this script.
    var baseElementUrl = baseElements[baseElements.length - 1].href;
    return this.__maybeConvertToAbsoluteUrl(baseElementUrl);
  }

  // Now we are getting desperate and as a last resort we try the current doc
  var fallbackUrl = this.__stripHashQueryAndFileName($doc.location.href);
  return this.__maybeConvertToAbsoluteUrl(fallbackUrl);
};

BaseUrlProvider.prototype.__getBaseElements = function() {
  return $doc.getElementsByTagName('base');
};

BaseUrlProvider.prototype.__getBaseUrlFromMetaTag = function() {
  return this.__metaTagParser.get()['baseUrl'];
};

//Export BaseUrlProvider to namespace
$namespace.lib.BaseUrlProvider = BaseUrlProvider;

    /*
 * Copyright 2014 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

var Dialog = $namespace.lib.Dialog;
var PropertySource = $namespace.lib.PropertySource;
var Recompiler = $namespace.lib.Recompiler;
var BaseUrlProvider = $namespace.lib.BaseUrlProvider;
var MetaTagParser = $namespace.lib.MetaTagParser;
//Publish a global variable to let others know that we have been loaded
$wnd.__gwt_sdm = $wnd.__gwt_sdm || {};
$wnd.__gwt_sdm.loaded = true;

/**
 * Construct the main class.
 *
 * @constructor
 * @param {string} moduleName
 * @param {Function} initPropertyProviders
 */
function Main(moduleName, initPropertyProviders){
  var metaTagParser = new MetaTagParser(moduleName);
  var propertySource = new PropertySource(moduleName, initPropertyProviders, metaTagParser);
  this.__moduleName = moduleName;
  this.__dialog = new Dialog();
  this.__recompiler = new Recompiler(moduleName, propertySource.computeBindingProperties());
  this.__baseUrlProvider = new BaseUrlProvider(moduleName, metaTagParser);
}

/**
 * Compile the current gwt module.
 */
Main.prototype.compile = function() {
  var that = this;
  // Export the module base of the server running the backend. (Returned by GWT.getModuleBaseURL.)
  // (If we are using the bookmarklets with an external server, the dev mode hook has already set
  // this up to point somewhere different, so don't override it.)
  var moduleBaseKey = '__gwtDevModeHook:' + this.__moduleName + ':moduleBase';
  if (!$wnd[moduleBaseKey]) {
    $wnd[moduleBaseKey] = this.__baseUrlProvider.getBaseUrl();
  }

  this.__dialog.clear();
  this.__dialog.add(this.__dialog.createTextElement("div", "12pt", "Compiling " + this.__moduleName));
  this.__dialog.show();
  this.__recompiler.compile(function(result) {
    that.__dialog.clear();
    if (result.status != 'ok') {
      that.__renderError(result);
    } else {
      that.__dialog.hide();
      that.__recompiler.loadApp();
    }
  });
};

/**
 * Render an error if compile failed.
 * @param {object} result - the jsonp object from the compile server.
 */
Main.prototype.__renderError = function(result) {
  var that = this;
  var link = this.__dialog.createTextElement('a', '16pt', result.status);
  link.setAttribute('href', this.__recompiler.getLogUrl());
  link.setAttribute('target', 'gwt_dev_mode_log');
  link.style.color = 'red';
  link.style.textDecoration = 'underline';
  this.__dialog.add(link);

  var button = this.__dialog.createTextElement('button', '12pt', 'Try Again');
  button.onclick = function() {
    that.compile();
  };
  button.style.marginLeft = '10px';
  this.__dialog.add(button);
};

new Main(__moduleName, __initPropertyProviders).compile();

  };

  if (/loaded|complete/.test($doc.readyState)) {
    executeMain();
  } else {
    //defer app script insertion until the body is ready
    if($wnd.addEventListener){
      $wnd.addEventListener('load', executeMain, false);
    } else{
      $wnd.attachEvent('onload', executeMain);
    }
  }
})();
