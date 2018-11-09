package com.test.project.service;

import java.io.File;

/**
 * Created on 09.11.2018 20:52 with love.
 */
public interface SameElementService {
    String findPathToSameElement(File originalFile, File sampleFile, String elementId) throws Exception;
}
