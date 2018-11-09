package com.test.project;

import com.test.project.service.SameElementService;
import com.test.project.service.impl.SameElementServiceImpl;

import java.io.File;

/**
 * Created on 09.11.2018 20:08 with love.
 */
public class Test {

    private static SameElementService sameElementService = new SameElementServiceImpl();
    private static final String TARGET_ELEMENT_ID = "make-everything-ok-button";

    public static void main(String[] args) {
        String originalFileName = args[0];
        String sampleFileName = args[1];
        File originalFile = new File(originalFileName);
        File sampleFile = new File(sampleFileName);
        String findPathToSameElement = null;
        try {
            findPathToSameElement = sameElementService.findPathToSameElement(originalFile, sampleFile, TARGET_ELEMENT_ID);
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
        System.out.println(findPathToSameElement);
    }

}
