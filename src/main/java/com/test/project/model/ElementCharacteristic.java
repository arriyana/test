package com.test.project.model;

import java.util.List;
import java.util.Map;

/**
 * Created on 09.11.2018 20:55 with love.
 */
public class ElementCharacteristic {

    private List<HtmlElement> parents;
    private Map<String, String> attributes;
    private HtmlElement htmlElement;
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public HtmlElement getHtmlElement() {
        return htmlElement;
    }

    public void setHtmlElement(HtmlElement htmlElement) {
        this.htmlElement = htmlElement;
    }

    public List<HtmlElement> getParents() {
        return parents;
    }

    public void setParents(List<HtmlElement> parents) {
        this.parents = parents;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
}
