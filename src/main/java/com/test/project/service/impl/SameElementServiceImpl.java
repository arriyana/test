package com.test.project.service.impl;

import com.test.project.factory.ElementCharacteristicFactory;
import com.test.project.factory.impl.ElementCharacteristicFactoryImpl;
import com.test.project.model.ElementCharacteristic;
import com.test.project.model.HtmlElement;
import com.test.project.service.HtmlElementConverter;
import com.test.project.service.SameElementService;
import javafx.util.Pair;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created on 09.11.2018 20:54 with love.
 */
public class SameElementServiceImpl implements SameElementService {

    private static String CHARSET_NAME = "utf8";

    private HtmlElementConverter htmlElementConverter = new HtmlElementConverterImpl();
    private ElementCharacteristicFactory elementCharFactory = new ElementCharacteristicFactoryImpl();

    public String findPathToSameElement(File originalFile, File sampleFile, String elementId) throws Exception {
        ElementCharacteristic elementCharacteristic = getElementCharacteristic(originalFile, elementId);
        ElementCharacteristic sameElement = findSameElement(sampleFile, elementCharacteristic);
        return findPathToElement(sameElement);
    }

    private String findPathToElement(ElementCharacteristic element) {
        List<HtmlElement> parents = element.getParents();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = parents.size() - 1; i >= 0; i--) {
            HtmlElement parent = parents.get(i);
            writeElement(stringBuilder, parent);
            if (i > 0) {
                stringBuilder.append(" > ");
            }
        }
//        writeElement(stringBuilder, element.getHtmlElement());
        return stringBuilder.toString();
    }


    private void writeElement(StringBuilder stringBuilder, HtmlElement element) {
        stringBuilder.append(element.getTag());
        if (StringUtils.isNotEmpty(element.getId())) {
            stringBuilder.append(" id=").append(element.getId());
        }
        if (StringUtils.isNotEmpty(element.getClassName())) {
            stringBuilder.append(" class=").append(element.getClassName());
        }
    }

    private ElementCharacteristic findSameElement(File sampleFile, ElementCharacteristic originalCharacteristic) throws Exception {
        Document doc = getHtmlDocument(sampleFile);
        Elements elements = doc.getElementsByTag(originalCharacteristic.getHtmlElement().getTag());
        Integer prevScore = 0;
        ElementCharacteristic sameElementCharacteristic = null;
        for (Element element : elements) {
            ElementCharacteristic characteristic = getElementCharacteristic(element);
            Integer score = getSimilarityScore(characteristic, originalCharacteristic);
            if (prevScore < score) {
                sameElementCharacteristic = characteristic;
                prevScore = score;
            }
        }
        return sameElementCharacteristic;
    }

    private Integer getSimilarityScore(ElementCharacteristic characteristic,
                                       ElementCharacteristic originalCharacteristic) {
        Integer sameParentsScore = countSameParents(characteristic, originalCharacteristic);
        Integer sameAttributes = countSameAttributes(characteristic, originalCharacteristic);
        Integer textSimilar = compareInnerText(characteristic, originalCharacteristic);
        return sameParentsScore * 2 + sameAttributes * 3 + textSimilar;
    }

    private Integer compareInnerText(ElementCharacteristic characteristic,
                                     ElementCharacteristic originalCharacteristic) {
        Integer matches = 0;
        if (StringUtils.isNotEmpty(characteristic.getText()) && characteristic.getText().length() > 3) {
            for (int i = 0; i < characteristic.getText().length() - 3; i += 3) {
                matches = StringUtils.countMatches(originalCharacteristic.getText(), characteristic.getText().substring(i, i + 2));
            }
        }
        return matches;
    }

    private Integer countSameAttributes(ElementCharacteristic characteristic,
                                        ElementCharacteristic originalCharacteristic) {
        Integer score = 0;
        Map<String, String> sameAttrs = characteristic.getAttributes();
        for (Map.Entry<String, String> attribute : originalCharacteristic.getAttributes().entrySet()) {
            String value = sameAttrs.get(attribute.getKey());
            if (value != null && value.equals(attribute.getValue())) {
                score++;
            }
        }
        return score;
    }

    private Integer countSameParents(ElementCharacteristic characteristic,
                                     ElementCharacteristic originalCharacteristic) {
        Integer score = 0;
        List<HtmlElement> parents = new ArrayList<>(characteristic.getParents());
        for (HtmlElement originalParent : originalCharacteristic.getParents()) {
            Pair<Integer, HtmlElement> sameParent = findSameParent(originalParent, parents);
            if (sameParent != null) {
                score += sameParent.getKey();
                parents.remove(sameParent.getValue());
            }
        }
        return score;
    }

    private Pair<Integer, HtmlElement> findSameParent(HtmlElement originalParent, List<HtmlElement> parents) {
        Integer maxScore = 0;
        HtmlElement sameElement = null;
        for (HtmlElement element : parents) {
            Integer score = 0;
            if (!element.getTag().equals(originalParent.getTag())) {
                continue;
            }
            score++;
            if (isEquals(element.getClassName(), originalParent.getClassName())) {
                score++;
            }
            if (isEquals(element.getId(), originalParent.getId())) {
                score++;
            }
            if (score > maxScore) {
                sameElement = element;
            }
        }
        if (sameElement == null) {
            return null;
        }
        return new Pair<>(maxScore, sameElement);
    }

    private boolean isEquals(String s1, String s2) {
        return s1 == null ? s2 == null : s1.equals(s2);
    }

    private ElementCharacteristic getElementCharacteristic(File originalFile, String elementId) throws Exception {
        Element element = getElementById(originalFile, elementId);
        return getElementCharacteristic(element);
    }

    private ElementCharacteristic getElementCharacteristic(Element element) {
        List<HtmlElement> parents = collectParentElements(element);
        HtmlElement htmlElement = htmlElementConverter.convert(element);
        Map<String, String> attributes = collectAttributes(element);
        String text = element.text();
        return elementCharFactory.createElementCharacteristic(parents, attributes, htmlElement, text);
    }

    private Element getElementById(File originalFile, String elementId) throws Exception {
        Document doc = getHtmlDocument(originalFile);
        return doc.getElementById(elementId);
    }

    private Document getHtmlDocument(File file) throws Exception {
        try {
            return Jsoup.parse(file, CHARSET_NAME, file.getAbsolutePath());
        } catch (IOException e) {
            throw new Exception("Can not open *.html file " + file.getAbsolutePath());
        }
    }

    private Map<String, String> collectAttributes(Element element) {
        if (element == null
                || element.attributes() == null
                || CollectionUtils.isEmpty(element.attributes().asList())) {
            return new HashMap<>();
        }
        return element.attributes().asList().stream()
                .collect(Collectors.toMap(Attribute::getKey, Attribute::getValue));
    }

    private List<HtmlElement> collectParentElements(Element element) {
        List<HtmlElement> elements = new ArrayList<HtmlElement>();
        while (element.parent() != null) {
            elements.add(htmlElementConverter.convert(element));
            element = element.parent();
        }
        return elements;
    }
}
