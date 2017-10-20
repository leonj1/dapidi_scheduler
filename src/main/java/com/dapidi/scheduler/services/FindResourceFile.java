package com.dapidi.scheduler.services;

import java.io.File;
import java.net.URL;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class FindResourceFile {
    private URL url;

    public FindResourceFile(String filename) throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        this.url = classLoader.getResource(filename);
        if (this.url == null) {
            throw new Exception(String.format("Unable to find %s", filename));
        }
    }

    public FindResourceFile(URL url) {
        this.url = url;
    }

    public File file() {
        return new File(url.getFile());
    }
}
