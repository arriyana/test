package com.test.project.factory.impl;

import com.test.project.factory.ElementCharacteristicFactory;
import com.test.project.model.ElementCharacteristic;
import com.test.project.model.HtmlElement;

import java.util.List;
import java.util.Map;

/**
 * Created on 09.11.2018 20:59 with love.
 */
public class ElementCharacteristicFactoryImpl implements ElementCharacteristicFactory {

    public ElementCharacteristic createElementCharacteristic(List<HtmlElement> parents,
                                                             Map<String, String> attributes,
                                                             HtmlElement htmlElement,
                                                             String text) {
        ElementCharacteristic elementCharacteristic = new ElementCharacteristic();
        elementCharacteristic.setAttributes(attributes);
        elementCharacteristic.setParents(parents);
        elementCharacteristic.setHtmlElement(htmlElement);
        elementCharacteristic.setText(text);
        return elementCharacteristic;
    }
}
