package org.netcracker.learningcenter.utils;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class Pipeline {
    private StanfordCoreNLP stanfordCoreNLP;

    public Pipeline(@Value("${annotators}") String annotators) {
        Properties properties = new Properties();
        properties.setProperty("annotators", annotators);
        stanfordCoreNLP = new StanfordCoreNLP(properties);
    }

    public StanfordCoreNLP getStanfordCoreNLP() {
        return stanfordCoreNLP;
    }
}
