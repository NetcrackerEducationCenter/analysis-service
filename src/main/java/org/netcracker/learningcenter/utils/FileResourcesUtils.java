package org.netcracker.learningcenter.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileResourcesUtils {
    public static List<String> readListFromFile(String filename) throws IOException {
        List<String> list = new ArrayList();
        ClassLoader classLoader = FileResourcesUtils.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(filename);
             BufferedReader r = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = r.readLine()) != null) {
                list.add(line);
            }
        }
        return list;
    }
}
