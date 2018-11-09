package com.test.project.service.impl;

import com.test.project.model.HtmlElement;
import com.test.project.service.HtmlElementConverter;
import org.jsoup.nodes.Element;

/**
 * Created on 09.11.2018 21:18 with love.
 */
public class HtmlElementConverterImpl implements HtmlElementConverter {

    public HtmlElement convert(Element element) {
        HtmlElement htmlElement = new HtmlElement();
        htmlElement.setClassName(element.className());
        htmlElement.setId(element.id());
        htmlElement.setTag(element.tagName());
        return htmlElement;
    }
}
