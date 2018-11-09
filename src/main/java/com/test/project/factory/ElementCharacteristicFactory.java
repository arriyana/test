package com.test.project.factory;

import com.test.project.model.ElementCharacteristic;
import com.test.project.model.HtmlElement;

import java.util.List;
import java.util.Map;

/**
 * Created on 09.11.2018 20:56 with love.
 */
public interface ElementCharacteristicFactory {
    ElementCharacteristic createElementCharacteristic(List<HtmlElement> parents,
                                                      Map<String, String> attributes,
                                                      HtmlElement htmlElement,
                                                      String text);
}
