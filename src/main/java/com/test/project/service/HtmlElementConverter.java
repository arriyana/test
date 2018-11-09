package com.test.project.service;

import com.test.project.model.HtmlElement;
import org.jsoup.nodes.Element;

/**
 * Created on 09.11.2018 21:17 with love.
 */
public interface HtmlElementConverter {
    HtmlElement convert(Element element);
}
