/*
 * Copyright 2017 Turn s.r.o.
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
package sk.turn.gwtmvp.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

import sk.turn.gwtmvp.client.Presenter;
import sk.turn.gwtmvp.client.View;

public abstract class BasePresenter<V extends View<? extends Element>> implements Presenter<V> {

  private RegExp regExp;
  private final V view;

  public BasePresenter(String regExp, V view) {
    setTokenRegExp(regExp);
    this.view = view;
  }

  @Override
  public RegExp getTokenRegExp() {
    return regExp;
  }

  @Override
  public V getView() {
    return view;
  }

  @Override
  public void onViewLoaded() {
    // Nothing to do here
  }

  @Override
  public void onShow(MatchResult matchResult) {
    // Nothing to do here
  }

  @Override
  public void onHide() {
    // Nothing to do here
  }

  public void setTokenRegExp(String regExp) {
    this.regExp = RegExp.compile(regExp);
  }

}
